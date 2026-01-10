package com.teamharmony.cakeworks.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import com.teamharmony.cakeworks.core.CakeWorks;
import com.teamharmony.cakeworks.core.registry.CakeWorksBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CakeWorksBlockStateProvider extends BlueprintBlockStateProvider {

    public CakeWorksBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, CakeWorks.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.cakeBlock(CakeWorksBlocks.CAKE_BASE.get());
    }

    private void cakeBlock(Block block) {
        this.getVariantBuilder(block).forAllStates(state -> {
            int bites = state.getValue(BlockStateProperties.BITES);
            String suffix = bites > 0 ? "_slice" + bites : "";
            String name = name(block) + suffix;
            String parent = bites > 0 ? "block/cake_slice" + bites : "block/cake";

            var model = models().withExistingParent(name, ResourceLocation.withDefaultNamespace(parent))
                    .texture("particle", blockTexture(block).withSuffix("_side"))
                    .texture("bottom", ResourceLocation.withDefaultNamespace("block/cake_bottom"))
                    .texture("top", blockTexture(block).withSuffix("_top"))
                    .texture("side", blockTexture(block).withSuffix("_side"));

            if (bites > 0) {
                model.texture("inside", blockTexture(block).withSuffix("_inner"));
            }

            return ConfiguredModel.builder().modelFile(model).build();
        });
    }
}