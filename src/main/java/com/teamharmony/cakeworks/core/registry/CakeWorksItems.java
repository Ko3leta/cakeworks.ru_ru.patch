package com.teamharmony.cakeworks.core.registry;

import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamharmony.cakeworks.core.CakeWorks;
import com.teamabnormals.neapolitan.common.item.HealingItem;
import com.teamabnormals.neapolitan.core.registry.NeapolitanMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredItem;

import static net.minecraft.world.item.CreativeModeTabs.FOOD_AND_DRINKS;
import static net.minecraft.world.item.CreativeModeTabs.INGREDIENTS;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class CakeWorksItems {
    public static final ItemSubRegistryHelper ITEMS = CakeWorks.REGISTRY_HELPER.getItemSubHelper();

    // ICING //
    public static final DeferredItem<Item> CAKE_ICING = ITEMS.createItem("cake_icing", () -> new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING)));

    // NEAPOLITAN ICING //
    public static final DeferredItem<Item> ADZUKI_ICING = ITEMS.createItem("adzuki_icing", () -> ItemSubRegistryHelper.areModsLoaded("neapolitan") ?
            NeapolitanCompat.createAdzukiIcing() : new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING)));

    public static final DeferredItem<Item> BANANA_ICING = ITEMS.createItem("banana_icing", () -> ItemSubRegistryHelper.areModsLoaded("neapolitan") ?
            NeapolitanCompat.createBananaIcing() : new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING)));

    public static final DeferredItem<Item> CHOCOLATE_ICING = ITEMS.createItem("chocolate_icing", () -> ItemSubRegistryHelper.areModsLoaded("neapolitan") ?
            NeapolitanCompat.createChocolateIcing() : new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING)));

    public static final DeferredItem<Item> MINT_ICING = ITEMS.createItem("mint_icing", () -> ItemSubRegistryHelper.areModsLoaded("neapolitan") ?
            NeapolitanCompat.createMintIcing() : new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING)));

    public static final DeferredItem<Item> STRAWBERRY_ICING = ITEMS.createItem("strawberry_icing", () -> ItemSubRegistryHelper.areModsLoaded("neapolitan") ?
            NeapolitanCompat.createStrawberryIcing() : new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING)));

    public static final DeferredItem<Item> VANILLA_ICING = ITEMS.createItem("vanilla_icing", () -> ItemSubRegistryHelper.areModsLoaded("neapolitan") ?
            NeapolitanCompat.createVanillaIcing() : new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING)));

    // CAKE BASE //
    public static final DeferredItem<Item> CAKE_BASE = ITEMS.createItem("cake_base",
            () -> new BlockItem(CakeWorksBlocks.CAKE_BASE.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CAKE_DOUGH = ITEMS.createItem("cake_dough", () -> new Item(new Item.Properties().food(CakeWorksFoods.CAKE_DOUGH)));

    public static void setupTabEditors() {
        var populator = CreativeModeTabContentsPopulator.mod(CakeWorks.MOD_ID);

        populator.tab(INGREDIENTS)
                .addItemsAfter(of(Items.WHEAT), CAKE_DOUGH);

        var foodTab = populator.tab(FOOD_AND_DRINKS);

        foodTab.addItemsBefore(of(Items.MILK_BUCKET), CAKE_ICING);

        if (ItemSubRegistryHelper.areModsLoaded("neapolitan")) {
            foodTab.addItemsAfter(of(CAKE_ICING.get()), VANILLA_ICING, CHOCOLATE_ICING, STRAWBERRY_ICING, BANANA_ICING, MINT_ICING, ADZUKI_ICING);
        }

        foodTab.addItemsBefore(of(Items.CAKE), CAKE_DOUGH, CAKE_BASE);
    }

    public static final class CakeWorksFoods {
        public static FoodProperties.Builder icing() {
            return new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).usingConvertsTo(Items.BOWL);
        }

        public static final FoodProperties ICING = icing().build();
        public static final FoodProperties CAKE_BASE_SLICE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).fast().build();
        public static final FoodProperties CAKE_DOUGH = new FoodProperties.Builder().nutrition(1).saturationModifier(0.1F).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 0), 1.0F).build();
    }

    private static class NeapolitanCompat {
        static Item createAdzukiIcing() {
            return new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.icing().effect(() -> new MobEffectInstance(NeapolitanMobEffects.HARMONY, 200), 1.0F).build()));
        }

        static Item createBananaIcing() {
            return new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.icing().effect(() -> new MobEffectInstance(NeapolitanMobEffects.AGILITY, 200), 1.0F).build()));
        }

        static Item createChocolateIcing() {
            return new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.icing().effect(() -> new MobEffectInstance(NeapolitanMobEffects.SUGAR_RUSH, 200), 1.0F).build()));
        }

        static Item createMintIcing() {
            return new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.icing().effect(() -> new MobEffectInstance(NeapolitanMobEffects.BERSERKING, 200), 1.0F).build()));
        }

        static Item createVanillaIcing() {
            return new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.icing().effect(() -> new MobEffectInstance(NeapolitanMobEffects.VANILLA_SCENT, 200), 1.0F).build()));
        }

        static Item createStrawberryIcing() {
            return new HealingItem(2.0F, new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(CakeWorksFoods.ICING));
        }
    }
}