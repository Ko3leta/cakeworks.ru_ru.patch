package com.teamharmony.cakeworks.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamharmony.cakeworks.core.CakeWorks;
import com.teamharmony.cakeworks.core.registry.CakeWorksItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CakeWorksItemModelProvider extends BlueprintItemModelProvider {

    public CakeWorksItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, CakeWorks.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        this.generatedItem(
                CakeWorksItems.CAKE_DOUGH,
                CakeWorksItems.CAKE_BASE,
                CakeWorksItems.CAKE_ICING,
                CakeWorksItems.VANILLA_ICING,
                CakeWorksItems.CHOCOLATE_ICING,
                CakeWorksItems.STRAWBERRY_ICING,
                CakeWorksItems.BANANA_ICING,
                CakeWorksItems.MINT_ICING,
                CakeWorksItems.ADZUKI_ICING
        );
    }
}