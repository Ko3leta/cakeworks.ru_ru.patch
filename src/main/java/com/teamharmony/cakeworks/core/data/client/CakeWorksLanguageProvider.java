package com.teamharmony.cakeworks.core.data.client;

import com.teamharmony.cakeworks.core.CakeWorks;
import com.teamharmony.cakeworks.core.registry.CakeWorksBlocks;
import com.teamharmony.cakeworks.core.registry.CakeWorksItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class CakeWorksLanguageProvider extends LanguageProvider {

    public CakeWorksLanguageProvider(PackOutput output) {
        super(output, CakeWorks.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(CakeWorksBlocks.CAKE_BASE, "Cake Base");
        this.addItem(CakeWorksItems.CAKE_DOUGH, "Cake Dough");
        this.addItem(CakeWorksItems.CAKE_ICING, "Cake Icing");
        this.addItem(CakeWorksItems.ADZUKI_ICING, "Adzuki Icing");
        this.addItem(CakeWorksItems.BANANA_ICING, "Banana Icing");
        this.addItem(CakeWorksItems.CHOCOLATE_ICING, "Chocolate Icing");
        this.addItem(CakeWorksItems.MINT_ICING, "Mint Icing");
        this.addItem(CakeWorksItems.STRAWBERRY_ICING, "Strawberry Icing");
        this.addItem(CakeWorksItems.VANILLA_ICING, "Vanilla Icing");
    }
}