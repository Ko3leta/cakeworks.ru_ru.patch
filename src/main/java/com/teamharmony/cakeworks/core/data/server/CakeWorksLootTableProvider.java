package com.teamharmony.cakeworks.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamharmony.cakeworks.core.CakeWorks;
import com.teamharmony.cakeworks.core.registry.CakeWorksBlocks;
import com.teamharmony.cakeworks.core.registry.CakeWorksItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CakeWorksLootTableProvider extends LootTableProvider {

    public CakeWorksLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Collections.emptySet(), ImmutableList.of(
                new LootTableProvider.SubProviderEntry(CakeWorksBlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(CakeWorksChestLoot::new, LootContextParamSets.CHEST)
        ), provider);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> registry, ValidationContext context, ProblemReporter.Collector collector) {
    }

    private static class CakeWorksBlockLoot extends BlockLootSubProvider {

        protected CakeWorksBlockLoot(HolderLookup.Provider provider) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        public void generate() {
            this.add(CakeWorksBlocks.CAKE_BASE.get(), noDrop());
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return BuiltInRegistries.BLOCK.stream()
                    .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(CakeWorks.MOD_ID))
                    .collect(Collectors.toSet());
        }
    }

    private record CakeWorksChestLoot(HolderLookup.Provider provider) implements LootTableSubProvider {

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(CakeWorks.MOD_ID, "chests/village_house_icing")),
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(CakeWorksItems.CAKE_ICING.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                            .add(LootItem.lootTableItem(CakeWorksItems.VANILLA_ICING.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                            .add(LootItem.lootTableItem(CakeWorksItems.CHOCOLATE_ICING.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                            .add(LootItem.lootTableItem(CakeWorksItems.STRAWBERRY_ICING.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                            .add(LootItem.lootTableItem(CakeWorksItems.BANANA_ICING.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                            .add(LootItem.lootTableItem(CakeWorksItems.MINT_ICING.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                            .add(LootItem.lootTableItem(CakeWorksItems.ADZUKI_ICING.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                    ));
        }
    }
}