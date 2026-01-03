package ycpk.sculkjaw.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.blocks.custom.ConcentratedSculkBlock;
import ycpk.sculkjaw.blocks.custom.SculkJawBlock;

import java.util.function.Function;

public class ModBlocks {

    public static void registerModBlocks() {
        Sculkjaw.LOGGER.info("Registering Blocks for Mod " + Sculkjaw.MOD_ID);
    }

    public static final Block SCULK_JAW;
    public static final Block CONCENTRATED_SCULK;

    static{
        SCULK_JAW = Blocks.register(ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculk_jaw")),
                SculkJawBlock::new,
                BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK).strength(3.0F, 3.0F).forceSolidOn().noOcclusion());
        CONCENTRATED_SCULK = Blocks.register(ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "concentrated_sculk")),
                ConcentratedSculkBlock::new,
                BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK).sound(SoundType.SCULK_CATALYST).strength(3.0F, 3.0F));
    }


    /*public static final Block SCULK_JAW = register(
            "sculk_jaw",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.SCULK).strength(3.0F, 3.0F).forceSolidOn(),
            false
    );*/

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));
        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, name));
    }
}
