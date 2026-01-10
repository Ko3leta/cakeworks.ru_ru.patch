package com.teamharmony.cakeworks.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamharmony.cakeworks.common.block.CakeWorksCakeBlock;
import com.teamharmony.cakeworks.core.CakeWorks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

public class CakeWorksBlocks {
    public static final BlockSubRegistryHelper BLOCKS = CakeWorks.REGISTRY_HELPER.getBlockSubHelper();

    public static final DeferredBlock<Block> CAKE_BASE = BLOCKS.createBlockNoItem("cake_base",
            () -> new CakeWorksCakeBlock(CakeWorksItems.CakeWorksFoods.CAKE_BASE_SLICE,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));
}