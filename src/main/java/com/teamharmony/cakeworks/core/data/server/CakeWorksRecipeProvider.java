package com.teamharmony.cakeworks.core.data.server;

import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.neapolitan.core.other.tags.NeapolitanItemTags;
import com.teamabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.teamabnormals.neapolitan.core.registry.NeapolitanItems;
import com.teamharmony.cakeworks.core.CakeWorks;
import com.teamharmony.cakeworks.core.registry.CakeWorksItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CakeWorksRecipeProvider extends BlueprintRecipeProvider {

    public CakeWorksRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(CakeWorks.MOD_ID, output, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer, HolderLookup.Provider provider) {
        Ingredient water = Ingredient.of(Stream.of(
                new ItemStack(Items.WATER_BUCKET),
                PotionContents.createItemStack(Items.POTION, Potions.WATER)
        ));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CakeWorksItems.CAKE_DOUGH.get())
                .requires(Items.WHEAT, 2)
                .requires(water)
                .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                .save(consumer);

        foodCookingRecipes(consumer, CakeWorksItems.CAKE_DOUGH.get(), CakeWorksItems.CAKE_BASE.get(), getHasName(CakeWorksItems.CAKE_DOUGH.get()));

        icingRecipe(consumer, CakeWorksItems.CAKE_ICING.get(), null, null, getHasName(Items.SUGAR), has(Items.SUGAR));

        ICondition neapolitanLoaded = new ModLoadedCondition("neapolitan");

        icingRecipe(consumer, CakeWorksItems.VANILLA_ICING.get(), Ingredient.of(NeapolitanItems.DRIED_VANILLA_PODS.get()), neapolitanLoaded, getHasName(NeapolitanItems.DRIED_VANILLA_PODS.get()), has(NeapolitanItems.DRIED_VANILLA_PODS.get()));
        icingRecipe(consumer, CakeWorksItems.CHOCOLATE_ICING.get(), Ingredient.of(NeapolitanItems.CHOCOLATE_BAR.get()), neapolitanLoaded, getHasName(NeapolitanItems.CHOCOLATE_BAR.get()), has(NeapolitanItems.CHOCOLATE_BAR.get()));
        icingRecipe(consumer, CakeWorksItems.STRAWBERRY_ICING.get(), Ingredient.of(NeapolitanItemTags.FOODS_STRAWBERRY), neapolitanLoaded, "has_strawberry", has(NeapolitanItemTags.FOODS_STRAWBERRY));
        icingRecipe(consumer, CakeWorksItems.BANANA_ICING.get(), Ingredient.of(NeapolitanItemTags.FOODS_BANANA), neapolitanLoaded, "has_banana", has(NeapolitanItemTags.FOODS_BANANA));
        icingRecipe(consumer, CakeWorksItems.MINT_ICING.get(), Ingredient.of(NeapolitanItems.MINT_LEAVES.get()), neapolitanLoaded, getHasName(NeapolitanItems.MINT_LEAVES.get()), has(NeapolitanItems.MINT_LEAVES.get()));
        icingRecipe(consumer, CakeWorksItems.ADZUKI_ICING.get(), Ingredient.of(NeapolitanItems.ROASTED_ADZUKI_BEANS.get()), neapolitanLoaded, getHasName(NeapolitanItems.ROASTED_ADZUKI_BEANS.get()), has(NeapolitanItems.ROASTED_ADZUKI_BEANS.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Blocks.CAKE)
                .requires(CakeWorksItems.CAKE_BASE.get())
                .requires(CakeWorksItems.CAKE_ICING.get())
                .unlockedBy(getHasName(CakeWorksItems.CAKE_ICING.get()), has(CakeWorksItems.CAKE_ICING.get()))
                .save(consumer);

        cakeRecipe(consumer, NeapolitanBlocks.VANILLA_CAKE.get(), CakeWorksItems.VANILLA_ICING.get(), neapolitanLoaded);
        cakeRecipe(consumer, NeapolitanBlocks.CHOCOLATE_CAKE.get(), CakeWorksItems.CHOCOLATE_ICING.get(), neapolitanLoaded);
        cakeRecipe(consumer, NeapolitanBlocks.STRAWBERRY_CAKE.get(), CakeWorksItems.STRAWBERRY_ICING.get(), neapolitanLoaded);
        cakeRecipe(consumer, NeapolitanBlocks.BANANA_CAKE.get(), CakeWorksItems.BANANA_ICING.get(), neapolitanLoaded);
        cakeRecipe(consumer, NeapolitanBlocks.MINT_CAKE.get(), CakeWorksItems.MINT_ICING.get(), neapolitanLoaded);
        cakeRecipe(consumer, NeapolitanBlocks.ADZUKI_CAKE.get(), CakeWorksItems.ADZUKI_ICING.get(), neapolitanLoaded);
    }

    public static void cakeRecipe(RecipeOutput consumer, ItemLike output, ItemLike icing, ICondition condition) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, output)
                .requires(CakeWorksItems.CAKE_BASE.get())
                .requires(icing)
                .unlockedBy(getHasName(icing), has(icing))
                .save(consumer.withConditions(condition), CakeWorks.location(getSimpleRecipeName(output)));
    }

    public static void icingRecipe(RecipeOutput consumer, ItemLike output, Ingredient flavor, ICondition condition, String hasName, Criterion<?> has) {
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, output)
                .requires(Items.BOWL)
                .requires(Tags.Items.EGGS)
                .requires(BlueprintItemTags.MILK)
                .requires(Items.SUGAR);

        if (flavor != null) {
            builder.requires(flavor);
        }

        builder.unlockedBy(hasName, has);

        if (condition != null) {
            builder.save(consumer.withConditions(condition));
        } else {
            builder.save(consumer);
        }
    }

    public static void foodCookingRecipes(RecipeOutput consumer, ItemLike input, ItemLike output, String hasName) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, 0.35F, 200).unlockedBy(hasName, has(input)).save(consumer);
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.FOOD, output, 0.35F, 100).unlockedBy(hasName, has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), RecipeCategory.FOOD, output, 0.35F, 600).unlockedBy(hasName, has(input)).save(consumer, RecipeBuilder.getDefaultRecipeId(output) + "_from_campfire_cooking");
    }

}