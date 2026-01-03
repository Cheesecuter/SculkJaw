package ycpk.sculkjaw.worldgen;

import ycpk.sculkjaw.Sculkjaw;

public class ModWorldGen {
    public static void registerModWorldGen(){
        Sculkjaw.LOGGER.info("Registering World Generations for Mod " + Sculkjaw.MOD_ID);
    }

    //public static final SculkJawPatchFeature SCULK_JAW_PATCH_FEATURE = Features
    /*public static final SculkJawPatchFeature SCULK_JAW_PATCH_FEATURE = new SculkJawPatchFeature(SculkPatchConfiguration.CODEC);

    private static Feature<FeatureConfiguration> register(String identifier, Feature feature) {
        return (Feature) Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, identifier), feature);
    }*/
}
