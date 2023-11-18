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

package pers.saikel0rado1iu.silk.gen.data;

import com.google.common.collect.Sets;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.gen.data.family.EquipFamily;
import pers.saikel0rado1iu.silk.util.Minecraft;

import java.util.*;
import java.util.function.Consumer;

import static net.minecraft.data.server.recipe.RecipeProvider.*;
import static pers.saikel0rado1iu.silk.gen.data.family.EquipFamily.Variant.*;

/**
 * <p><b style="color:FFC800"><font size="+1">用于提供模组中常用但未提供更方便方法的配方生成方法</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface SilkRecipeJsonBuilder {
	@SilkApi
	static void generateEquipRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient ingredient, EquipFamily family) {
		for (EquipFamily.Variant variant : family.getVariants().keySet()) {
			switch (variant) {
				case SHOVEL -> offerShovelRecipe(exporter, ingredient, family.getVariant(SHOVEL));
				case PICKAXE -> offerPickaxeRecipe(exporter, ingredient, family.getVariant(PICKAXE));
				case AXE -> offerAxeRecipe(exporter, ingredient, family.getVariant(AXE));
				case HOE -> offerHoeRecipe(exporter, ingredient, family.getVariant(HOE));
				case SWORD -> offerSwordRecipe(exporter, ingredient, family.getVariant(SWORD));
				case HELMET -> offerHelmetRecipe(exporter, ingredient, family.getVariant(HELMET));
				case CHESTPLATE -> offerChestplateRecipe(exporter, ingredient, family.getVariant(CHESTPLATE));
				case LEGGINGS -> offerLeggingsRecipe(exporter, ingredient, family.getVariant(LEGGINGS));
				case BOOTS -> offerBootsRecipe(exporter, ingredient, family.getVariant(BOOTS));
			}
		}
	}
	
	@SilkApi
	static void offer2x2CompactingRecipeWithRecipeGroup(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonBuilder.create(category, output, 1).group(getItemPath(output)).input('#', input)
				.pattern("##")
				.pattern("##")
				.criterion(hasItem(input), conditionsFromItem(input))
				.offerTo(exporter);
	}
	
	@SilkApi
	static void offer2x2CrossCompactingRecipe(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, Ingredient base, Ingredient add, ItemConvertible output) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(category, output, 1).group(getItemPath(output)).input('#', base).input('X', add)
				.pattern("#X")
				.pattern("X#");
		Set<Item> items = Sets.newHashSetWithExpectedSize(2);
		Arrays.stream(base.getMatchingStacks()).forEach(stack -> items.add(stack.getItem()));
		Arrays.stream(add.getMatchingStacks()).forEach(stack -> items.add(stack.getItem()));
		items.forEach(item -> recipe.criterion(hasItem(item), conditionsFromItem(item)));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerCrossCompactingRecipe(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, Ingredient base, Ingredient add, ItemConvertible output) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(category, output, 1).group(getItemPath(output)).input('#', base).input('X', add)
				.pattern("#X#")
				.pattern("X#X")
				.pattern("#X#");
		Set<Item> items = Sets.newHashSetWithExpectedSize(2);
		Arrays.stream(base.getMatchingStacks()).forEach(stack -> items.add(stack.getItem()));
		Arrays.stream(add.getMatchingStacks()).forEach(stack -> items.add(stack.getItem()));
		items.forEach(item -> recipe.criterion(hasItem(item), conditionsFromItem(item)));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerShovelRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item shovel) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, shovel).group(getItemPath(shovel)).input('X', Items.STICK).input('#', input)
				.pattern("#")
				.pattern("X")
				.pattern("X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerPickaxeRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item pickaxe) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, pickaxe).group(getItemPath(pickaxe)).input('X', Items.STICK).input('#', input)
				.pattern("###")
				.pattern(" X ")
				.pattern(" X ");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerAxeRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item axe) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, axe).group(getItemPath(axe)).input('X', Items.STICK).input('#', input)
				.pattern("##")
				.pattern("#X")
				.pattern(" X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerHoeRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item hoe) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, hoe).group(getItemPath(hoe)).input('X', Items.STICK).input('#', input)
				.pattern("##")
				.pattern(" X")
				.pattern(" X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerSwordRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item sword) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, sword).group(getItemPath(sword)).input('X', Items.STICK).input('#', input)
				.pattern("#")
				.pattern("#")
				.pattern("X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerHelmetRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item helmet) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, helmet).group(getItemPath(helmet)).input('#', input)
				.pattern("###")
				.pattern("# #");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerChestplateRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item chestplate) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, chestplate).group(getItemPath(chestplate)).input('#', input)
				.pattern("# #")
				.pattern("###")
				.pattern("###");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerLeggingsRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item leggings) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, leggings).group(getItemPath(leggings)).input('#', input)
				.pattern("###")
				.pattern("# #")
				.pattern("# #");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerBootsRecipe(Consumer<RecipeJsonProvider> exporter, Ingredient input, Item boots) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, boots).group(getItemPath(boots)).input('#', input)
				.pattern("# #")
				.pattern("# #");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	@SilkApi
	static void offerSmithingIngredient(Consumer<RecipeJsonProvider> exporter, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
		Set<Item> items = Sets.newHashSetWithExpectedSize(2);
		Set<Item> baseItems = Sets.newHashSetWithExpectedSize(2);
		Set<Item> addItems = Sets.newHashSetWithExpectedSize(2);
		Arrays.stream(base.getMatchingStacks()).forEach(stack -> {
			baseItems.add(stack.getItem());
			items.add(stack.getItem());
		});
		Arrays.stream(addition.getMatchingStacks()).forEach(stack -> {
			addItems.add(stack.getItem());
			items.add(stack.getItem());
		});
		SmithingTransformRecipeJsonBuilder main = SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.AIR), base, addition, category, result);
		items.forEach(item -> main.criterion(hasItem(item), conditionsFromItem(item)));
		main.offerTo(exporter, getSmithingItemPath(result));
		if (!baseItems.equals(addItems)) {
			SmithingTransformRecipeJsonBuilder swap = SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.AIR), addition, base, category, result);
			items.forEach(item -> swap.criterion(hasItem(item), conditionsFromItem(item)));
			swap.offerTo(exporter, getSmithingSwapItemPath(result));
		}
	}
	
	@SilkApi
	static void offerSmeltingInOneJson(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
		CookingRecipeJsonBuilder recipe = CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(inputs.toArray(new ItemConvertible[0])), RecipeCategory.MISC, output, experience, cookingTime).group(group);
		inputs.forEach(itemConvertible -> recipe.criterion(hasItem(itemConvertible), conditionsFromItem(itemConvertible)));
		recipe.offerTo(exporter, RecipeProvider.getSmeltingItemPath(output));
	}
	
	@SilkApi
	static void offerBlastingInOneJson(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
		CookingRecipeJsonBuilder recipe = CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(inputs.toArray(new ItemConvertible[0])), RecipeCategory.MISC, output, experience, cookingTime).group(group);
		inputs.forEach(itemConvertible -> recipe.criterion(hasItem(itemConvertible), conditionsFromItem(itemConvertible)));
		recipe.offerTo(exporter, RecipeProvider.getBlastingItemPath(output));
	}
	
	@SilkApi
	static String getSmithingItemPath(ItemConvertible item) {
		return getItemPath(item) + "_from_smithing";
	}
	
	@SilkApi
	static String getSmithingSwapItemPath(ItemConvertible item) {
		return getItemPath(item) + "_from_smithing_swap";
	}
	
	@ApiStatus.Internal
	static String getNamespace(Ingredient input, Item output) {
		String namespace = CraftingRecipeJsonBuilder.getItemId(output).getNamespace();
		if (!Minecraft.DATA.getId().equals(namespace)) return namespace;
		for (ItemStack stack : input.getMatchingStacks()) {
			namespace = CraftingRecipeJsonBuilder.getItemId(stack.getItem()).getNamespace();
			if (!Minecraft.DATA.getId().equals(namespace)) return namespace;
		}
		return namespace;
	}
	
	@ApiStatus.Internal
	static Ingredient getInput(Map<Character, Ingredient> inputs) {
		List<Item> items = new ArrayList<>(List.of());
		inputs.forEach((character, ingredient) -> Arrays.stream(ingredient.getMatchingStacks()).forEach(stack -> items.add(stack.getItem())));
		return Ingredient.ofItems(items.toArray(new Item[0]));
	}
	
	@ApiStatus.Internal
	static Ingredient getInput(List<Ingredient> inputs) {
		List<Item> items = new ArrayList<>(List.of());
		inputs.forEach(ingredient -> Arrays.stream(ingredient.getMatchingStacks()).forEach(stack -> items.add(stack.getItem())));
		return Ingredient.ofItems(items.toArray(new Item[0]));
	}
}
