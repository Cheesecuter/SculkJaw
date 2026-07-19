package ycpk.sculkandjaw.core.legacy;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.SculkAggregatorBlockEntity;
import ycpk.sculkandjaw.blocks.modblocks.AcidcoilReed;
import ycpk.sculkandjaw.blocks.modblocks.SculkAggregator;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModBlocks;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LegacyDataFixer {
    private static final Queue<ChunkTask> TASK_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Set<Long> QUEUED_CHUNKS = ConcurrentHashMap.newKeySet();
    private static final int CHUNKS_PER_TICK = 2;

    public static void register() {
        SculkAndJaw.LOGGER.info("Registering legacy data fixer");
        ServerChunkEvents.CHUNK_LOAD.register(
                (serverLevel, levelChunk) -> {
                    LegacyMigrationData data = LegacyMigrationData.get(serverLevel);
                    long key = levelChunk.getPos().toLong();
                    if (data.isMigrated(key)) {
                        return;
                    }
                    if (QUEUED_CHUNKS.add(key)) {
                        TASK_QUEUE.add(new ChunkTask(serverLevel, levelChunk));
                    }
                }
        );
        ServerTickEvents.END_SERVER_TICK.register(
                (minecraftServer -> {
                    int count = 0;
                    while (!TASK_QUEUE.isEmpty() && count < CHUNKS_PER_TICK) {
                        ChunkTask task = TASK_QUEUE.poll();
                        if (task == null) {
                            break;
                        }
                        LevelChunk chunk = task.serverLevel().getChunkSource().getChunkNow(
                                task.levelChunk().getPos().x,
                                task.levelChunk().getPos().z
                        );
                        if (chunk != null) {
                            migrateLoadedChunks(task.serverLevel(), chunk);
                            LegacyMigrationData data = LegacyMigrationData.get(task.serverLevel());
                            long key = chunk.getPos().toLong();
                            if (!data.isMigrated(key)) {
                                migrateLoadedChunks(task.serverLevel(), chunk);
                                data.setMigrated(key);
                                task.serverLevel().getDataStorage().scheduleSave();
                            }
                        }
                        QUEUED_CHUNKS.remove(task.levelChunk().getPos().toLong());
                        count++;
                    }
                })
        );
        SculkAndJaw.LOGGER.info("Legacy data fixer registered");
    }

    private static void migrateLoadedChunks(ServerLevel serverLevel, LevelChunk levelChunk) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        ChunkPos chunkPos = levelChunk.getPos();
        int startX = chunkPos.getMinBlockX();
        int startZ = chunkPos.getMinBlockZ();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = serverLevel.getMinY(); y < serverLevel.getMaxY(); y++) {
                    blockPos.set(startX + x, y, startZ + z);
                    BlockState blockState = serverLevel.getBlockState(blockPos);
                    migrateBlocks(serverLevel, blockPos, blockState);
                    BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
                    migrateBlockEntities(serverLevel, blockPos, blockEntity);
                    //migrateItemEntities(serverLevel, levelChunk);
                }
            }
        }
        SculkAndJaw.LOGGER.info("Chunk migrated at {}", chunkPos);
    }

    private static void migrateBlocks(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
        try {
            if (blockState.is(ModBlocks.CONCENTRATED_SCULK)) {
                BlockState newState = ModBlocks.SCULK_AGGREGATOR
                        .defaultBlockState()
                        .setValue(
                                SculkAggregator.COMBINED_WITH_SCULK_JAW,
                                blockState.getValue(ConcentratedSculkBlock.COMBINED_WITH_SCULK_JAW)
                        )
                        .setValue(
                                SculkAggregator.COMBINED_WITH_SCULK_CATALYST,
                                blockState.getValue(ConcentratedSculkBlock.COMBINED_WITH_SCULK_CATALYST)
                        )
                        .setValue(
                                SculkAggregator.ACID_FILLED,
                                blockState.getValue(ConcentratedSculkBlock.ACID_FILLED)
                        );
                serverLevel.setBlock(blockPos, newState, Block.UPDATE_ALL);
                SculkAndJaw.LOGGER.info("Migrated Concentrated Sculk -> Sculk Aggregator {}", blockPos);
                return;
            }
            if (blockState.is(ModBlocks.ACIDOPHILIC_CORDYCEPS)) {
                BlockState newState = ModBlocks.ACIDCOIL_REED
                        .defaultBlockState()
                        .setValue(
                                AcidcoilReed.HALF,
                                blockState.getValue(AcidophilicCordyceps.HALF)
                        )
                        .setValue(
                                AcidcoilReed.FACING,
                                blockState.getValue(AcidophilicCordyceps.FACING)
                        )
                        .setValue(
                                AcidcoilReed.AMOUNT,
                                blockState.getValue(AcidophilicCordyceps.AMOUNT)
                        )
                        .setValue(
                                AcidcoilReed.AGE,
                                blockState.getValue(AcidophilicCordyceps.HALF).equals(DoubleBlockHalf.UPPER) ? blockState.getValue(AcidophilicCordyceps.AMOUNT) : 0
                        );
                serverLevel.setBlock(blockPos, newState, Block.UPDATE_ALL);
                SculkAndJaw.LOGGER.info("Migrated Acidophilic Cordyceps -> Acidcoil Reed {}", blockPos);
            }
        }
        catch (Exception e) {
            SculkAndJaw.LOGGER.error("Block migration failed at {}", blockPos, e);
        }
    }

    private static void migrateBlockEntities(ServerLevel serverLevel, BlockPos blockPos, BlockEntity blockEntity) {
        try {
            if (blockEntity instanceof ConcentratedSculkBlockEntity concentratedSculkBlockEntity) {
                BlockState newState = serverLevel.getBlockState(blockPos);
                serverLevel.removeBlockEntity(blockPos);
                BlockEntity newBlockEntity = ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY.create(blockPos, newState);
                if (newBlockEntity instanceof SculkAggregatorBlockEntity sculkAggregatorBlockEntity) {
                    sculkAggregatorBlockEntity.setExperienceReward(concentratedSculkBlockEntity.getExperienceReward());
                    sculkAggregatorBlockEntity.setHasCombinedWithSculkJaw(concentratedSculkBlockEntity.getHasCombinedWithSculkJaw());
                    serverLevel.setBlockEntity(sculkAggregatorBlockEntity);
                    sculkAggregatorBlockEntity.setChanged();
                    SculkAndJaw.LOGGER.info("Migrated Concentrated Sculk BlockEntity -> Sculk Aggregator BlockEntity {}", blockPos);
                }
            }
        }
        catch (Exception e) {
            SculkAndJaw.LOGGER.error("Block Entity migration failed at {}", blockPos, e);
        }
    }

    private static record ChunkTask(ServerLevel serverLevel, LevelChunk levelChunk) {

    }

    private static class LegacyMigrationData extends SavedData {
        private final LongOpenHashSet migratedChunks = new LongOpenHashSet();
        public static final String NAME = "legacy_chunk_migration";

        public LegacyMigrationData() {
        }

        public static final Codec<LegacyMigrationData> CODEC = Codec.LONG.listOf().xmap(
                list -> {
                    LegacyMigrationData data = new LegacyMigrationData();
                    list.forEach(data.migratedChunks::add);
                    return data;
                },
                data -> data.migratedChunks.longStream().boxed().toList()
        );

        public static final SavedDataType<LegacyMigrationData> TYPE =
                new SavedDataType<>(
                        "legacy_chunk_migration",
                        LegacyMigrationData::new,
                        CODEC,
                        DataFixTypes.LEVEL
                );

        public boolean isMigrated(long chunkPos) {
            return migratedChunks.contains(chunkPos);
        }

        public void setMigrated(long chunkPos) {
            migratedChunks.add(chunkPos);
            setDirty();
        }

        public static LegacyMigrationData get(ServerLevel serverLevel) {
            return serverLevel.getDataStorage().computeIfAbsent(TYPE);
        }
    }
}
