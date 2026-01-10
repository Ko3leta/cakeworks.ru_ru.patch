package com.teamharmony.cakeworks.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamharmony.cakeworks.common.event.CakeWorksEvents;
import com.teamharmony.cakeworks.core.data.client.CakeWorksBlockStateProvider;
import com.teamharmony.cakeworks.core.data.client.CakeWorksItemModelProvider;
import com.teamharmony.cakeworks.core.data.client.CakeWorksLanguageProvider;
import com.teamharmony.cakeworks.core.data.server.CakeWorksLootTableProvider;
import com.teamharmony.cakeworks.core.data.server.CakeWorksRecipeProvider;
import com.teamharmony.cakeworks.core.registry.CakeWorksBlocks;
import com.teamharmony.cakeworks.core.registry.CakeWorksItems;
import com.teamharmony.cakeworks.core.registry.CakeWorksParticleTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(CakeWorks.MOD_ID)
public class CakeWorks {
    public static final String MOD_ID = "cakeworks";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public CakeWorks(IEventBus modEventBus) {
        CakeWorksBlocks.BLOCKS.register(modEventBus);
        CakeWorksItems.ITEMS.register(modEventBus);
        CakeWorksParticleTypes.PARTICLE_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::dataSetup);

        NeoForge.EVENT_BUS.register(CakeWorksEvents.class);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(CakeWorksItems::setupTabEditors);
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();
        generator.addProvider(server, new CakeWorksLootTableProvider(output, provider));
        generator.addProvider(server, new CakeWorksRecipeProvider(output, provider));

        boolean client = event.includeClient();
        generator.addProvider(client, new CakeWorksBlockStateProvider(output, helper));
        generator.addProvider(client, new CakeWorksItemModelProvider(output, helper));
        generator.addProvider(client, new CakeWorksLanguageProvider(output));
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}