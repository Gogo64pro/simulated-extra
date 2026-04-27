package net.gogo.simulatedextra.datagen;

import net.gogo.simulatedextra.registers.BlocksReg;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static dev.ryanhcode.offroad.index.OffroadBlocks.WHEEL_MOUNT;

public class Recipe extends RecipeProvider {

    public Recipe(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    void swappable(RecipeOutput output, ItemLike item1, ItemLike item2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item1)
                .requires(item2)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(item1))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item2)
                .requires(item1)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(item2))
                .save(output);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        swappable(output, WHEEL_MOUNT.get(), BlocksReg.CENTERED_WHEEL_MOUNT.asItem());
    }
}