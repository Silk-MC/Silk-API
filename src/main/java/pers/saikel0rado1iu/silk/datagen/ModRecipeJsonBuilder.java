/*
 * This file is part of Silk API.
 * Copyright (C) 2023 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.datagen;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于提供模组中常用但未提供更方便方法的配方生成方法</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface ModRecipeJsonBuilder {
	private static Identifier smeltingRecipeId(ItemConvertible item) {
		return new Identifier(Registries.ITEM.getId(item.asItem()).getNamespace(), Registries.ITEM.getId(item.asItem()).getPath() + "_from_smelting");
	}
	
	private static Identifier blastingRecipeId(ItemConvertible item) {
		return new Identifier(Registries.ITEM.getId(item.asItem()).getNamespace(), Registries.ITEM.getId(item.asItem()).getPath() + "_from_blasting");
	}
	
	private static Identifier smithingRecipeId(ItemConvertible item) {
		return new Identifier(Registries.ITEM.getId(item.asItem()).getNamespace(), Registries.ITEM.getId(item.asItem()).getPath() + "_from_smithing");
	}
	
	private static Identifier smithingSwapRecipeId(ItemConvertible item) {
		return new Identifier(Registries.ITEM.getId(item.asItem()).getNamespace(), Registries.ITEM.getId(item.asItem()).getPath() + "_from_smithing_swap");
	}
	
	@SilkApi
	static void offerSmithingIngredient(Consumer<RecipeJsonProvider> exporter, Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
		SmithingTransformRecipeJsonBuilder main = SmithingTransformRecipeJsonBuilder.create(template, base, addition, category, result);
		SmithingTransformRecipeJsonBuilder swap = SmithingTransformRecipeJsonBuilder.create(template, addition, base, category, result);
		Arrays.stream(base.getMatchingStacks()).forEach(stack -> {
			main.criterion(FabricRecipeProvider.hasItem(stack.getItem()), FabricRecipeProvider.conditionsFromItem(stack.getItem()));
			swap.criterion(FabricRecipeProvider.hasItem(stack.getItem()), FabricRecipeProvider.conditionsFromItem(stack.getItem()));
		});
		Arrays.stream(addition.getMatchingStacks()).forEach(stack -> {
			main.criterion(FabricRecipeProvider.hasItem(stack.getItem()), FabricRecipeProvider.conditionsFromItem(stack.getItem()));
			swap.criterion(FabricRecipeProvider.hasItem(stack.getItem()), FabricRecipeProvider.conditionsFromItem(stack.getItem()));
		});
		main.offerTo(exporter, smithingRecipeId(result));
		swap.offerTo(exporter, smithingSwapRecipeId(result));
	}
	
	@SilkApi
	static void offerSmeltingInOneJson(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
		CookingRecipeJsonBuilder recipe = CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(inputs.toArray(new ItemConvertible[0])), RecipeCategory.MISC, output, experience, cookingTime).group(group)
				.criterion(FabricRecipeProvider.hasItem(output), FabricRecipeProvider.conditionsFromItem(output));
		inputs.forEach(itemConvertible -> recipe.criterion(FabricRecipeProvider.hasItem(itemConvertible), FabricRecipeProvider.conditionsFromItem(itemConvertible)));
		recipe.offerTo(exporter, smeltingRecipeId(output));
	}
	
	@SilkApi
	static void offerBlastingInOneJson(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
		CookingRecipeJsonBuilder recipe = CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(inputs.toArray(new ItemConvertible[0])), RecipeCategory.MISC, output, experience, cookingTime).group(group)
				.criterion(FabricRecipeProvider.hasItem(output), FabricRecipeProvider.conditionsFromItem(output));
		inputs.forEach(itemConvertible -> recipe.criterion(FabricRecipeProvider.hasItem(itemConvertible), FabricRecipeProvider.conditionsFromItem(itemConvertible)));
		recipe.offerTo(exporter, blastingRecipeId(output));
	}
}
