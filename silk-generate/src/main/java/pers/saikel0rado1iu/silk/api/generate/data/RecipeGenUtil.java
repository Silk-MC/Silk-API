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

package pers.saikel0rado1iu.silk.api.generate.data;

import com.google.common.collect.Sets;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.api.generate.data.family.EquipFamily;
import pers.saikel0rado1iu.silk.impl.Minecraft;

import java.util.*;

import static net.minecraft.data.server.recipe.RecipeProvider.*;

/**
 * <h2 style="color:FFC800">配方生成实用工具</h2>
 * 用于提供模组中常用但未提供更方便方法的配方生成方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface RecipeGenUtil {
	/**
	 * 生成装备配方
	 *
	 * @param exporter   配方导出器
	 * @param ingredient 成分
	 * @param family     装备家族
	 */
	static void generateFamily(RecipeExporter exporter, Ingredient ingredient, EquipFamily family) {
		for (EquipFamily.Variant variant : family.getVariants().keySet()) {
			switch (variant) {
				case SHOVEL -> offerShovelRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.SHOVEL));
				case PICKAXE -> offerPickaxeRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.PICKAXE));
				case AXE -> offerAxeRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.AXE));
				case HOE -> offerHoeRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.HOE));
				case SWORD -> offerSwordRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.SWORD));
				case HELMET -> offerHelmetRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.HELMET));
				case CHESTPLATE -> offerChestplateRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.CHESTPLATE));
				case LEGGINGS -> offerLeggingsRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.LEGGINGS));
				case BOOTS -> offerBootsRecipe(exporter, ingredient, family.getVariant(EquipFamily.Variant.BOOTS));
			}
		}
	}
	
	/**
	 * 提供 2x2 压缩配方和配方组
	 *
	 * @param exporter 配方导出器
	 * @param category 配方类别
	 * @param input    输入物品
	 * @param output   输出物品
	 */
	static void offer2x2CompactingRecipeWithRecipeGroup(RecipeExporter exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonBuilder.create(category, output, 1).group(getItemPath(output)).input('#', input)
				.pattern("##")
				.pattern("##")
				.criterion(hasItem(input), conditionsFromItem(input))
				.offerTo(exporter);
	}
	
	/**
	 * 提供 2x2 交叉压缩配方
	 *
	 * @param exporter 配方导出器
	 * @param category 配方类别
	 * @param base     基础成分
	 * @param add      添加成分
	 * @param output   输出物品
	 */
	static void offer2x2CrossCompactingRecipe(RecipeExporter exporter, RecipeCategory category, Ingredient base, Ingredient add, ItemConvertible output) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(category, output, 1).group(getItemPath(output)).input('#', base).input('X', add)
				.pattern("#X")
				.pattern("X#");
		Set<Item> items = Sets.newHashSetWithExpectedSize(2);
		Arrays.stream(base.getMatchingStacks()).forEach(stack -> items.add(stack.getItem()));
		Arrays.stream(add.getMatchingStacks()).forEach(stack -> items.add(stack.getItem()));
		items.forEach(item -> recipe.criterion(hasItem(item), conditionsFromItem(item)));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供交叉压缩配方
	 *
	 * @param exporter 配方导出器
	 * @param category 配方类别
	 * @param base     基础成分
	 * @param add      添加成分
	 * @param output   输出物品
	 */
	static void offerCrossCompactingRecipe(RecipeExporter exporter, RecipeCategory category, Ingredient base, Ingredient add, ItemConvertible output) {
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
	
	/**
	 * 提供锹配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param shovel   锹
	 */
	static void offerShovelRecipe(RecipeExporter exporter, Ingredient input, Item shovel) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, shovel).group(getItemPath(shovel)).input('X', Items.STICK).input('#', input)
				.pattern("#")
				.pattern("X")
				.pattern("X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供镐配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param pickaxe  镐
	 */
	static void offerPickaxeRecipe(RecipeExporter exporter, Ingredient input, Item pickaxe) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, pickaxe).group(getItemPath(pickaxe)).input('X', Items.STICK).input('#', input)
				.pattern("###")
				.pattern(" X ")
				.pattern(" X ");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供斧配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param axe      斧
	 */
	static void offerAxeRecipe(RecipeExporter exporter, Ingredient input, Item axe) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, axe).group(getItemPath(axe)).input('X', Items.STICK).input('#', input)
				.pattern("##")
				.pattern("#X")
				.pattern(" X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供锄配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param hoe      锄
	 */
	static void offerHoeRecipe(RecipeExporter exporter, Ingredient input, Item hoe) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, hoe).group(getItemPath(hoe)).input('X', Items.STICK).input('#', input)
				.pattern("##")
				.pattern(" X")
				.pattern(" X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供剑配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param sword    剑
	 */
	static void offerSwordRecipe(RecipeExporter exporter, Ingredient input, Item sword) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, sword).group(getItemPath(sword)).input('X', Items.STICK).input('#', input)
				.pattern("#")
				.pattern("#")
				.pattern("X");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供头盔配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param helmet   头盔
	 */
	static void offerHelmetRecipe(RecipeExporter exporter, Ingredient input, Item helmet) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, helmet).group(getItemPath(helmet)).input('#', input)
				.pattern("###")
				.pattern("# #");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供胸甲配方
	 *
	 * @param exporter   配方导出器
	 * @param input      输入成分
	 * @param chestplate 胸甲
	 */
	static void offerChestplateRecipe(RecipeExporter exporter, Ingredient input, Item chestplate) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, chestplate).group(getItemPath(chestplate)).input('#', input)
				.pattern("# #")
				.pattern("###")
				.pattern("###");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供护腿配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param leggings 护腿
	 */
	static void offerLeggingsRecipe(RecipeExporter exporter, Ingredient input, Item leggings) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, leggings).group(getItemPath(leggings)).input('#', input)
				.pattern("###")
				.pattern("# #")
				.pattern("# #");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供靴子配方
	 *
	 * @param exporter 配方导出器
	 * @param input    输入成分
	 * @param boots    靴子
	 */
	static void offerBootsRecipe(RecipeExporter exporter, Ingredient input, Item boots) {
		ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, boots).group(getItemPath(boots)).input('#', input)
				.pattern("# #")
				.pattern("# #");
		Arrays.stream(input.getMatchingStacks()).forEach(stack -> recipe.criterion(hasItem(stack.getItem()), conditionsFromItem(stack.getItem())));
		recipe.offerTo(exporter);
	}
	
	/**
	 * 提供可逆锻造配方
	 *
	 * @param exporter 配方导出器
	 * @param category 配方类别
	 * @param base     基础成分
	 * @param addition 添加成分
	 * @param result   结果物品
	 */
	static void offerReversibleSmithingRecipe(RecipeExporter exporter, RecipeCategory category, Ingredient base, Ingredient addition, Item result) {
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
	
	/**
	 * 提供熔炉配方<br>
	 * 但是生成的配方只在一个 JSON 中
	 *
	 * @param exporter    配方导出器
	 * @param inputs      输入物品集合
	 * @param output      输出物品
	 * @param experience  经验
	 * @param cookingTime 熔炼时间
	 * @param group       配方组
	 */
	static void offerSmelting(RecipeExporter exporter, Set<ItemConvertible> inputs, ItemConvertible output, float experience, int cookingTime, String group) {
		CookingRecipeJsonBuilder recipe = CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(inputs.toArray(new ItemConvertible[0])), RecipeCategory.MISC, output, experience, cookingTime).group(group);
		inputs.forEach(itemConvertible -> recipe.criterion(hasItem(itemConvertible), conditionsFromItem(itemConvertible)));
		recipe.offerTo(exporter, RecipeProvider.getSmeltingItemPath(output));
	}
	
	/**
	 * 提供熔炉配方<br>
	 * 但是生成的配方只在一个 JSON 中
	 *
	 * @param exporter    配方导出器
	 * @param inputs      输入物品集合
	 * @param output      输出物品
	 * @param experience  经验
	 * @param cookingTime 熔炼时间
	 * @param group       配方组
	 */
	static void offerBlasting(RecipeExporter exporter, Set<ItemConvertible> inputs, ItemConvertible output, float experience, int cookingTime, String group) {
		CookingRecipeJsonBuilder recipe = CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(inputs.toArray(new ItemConvertible[0])), RecipeCategory.MISC, output, experience, cookingTime).group(group);
		inputs.forEach(itemConvertible -> recipe.criterion(hasItem(itemConvertible), conditionsFromItem(itemConvertible)));
		recipe.offerTo(exporter, RecipeProvider.getBlastingItemPath(output));
	}
	
	/**
	 * 获取锻造物品路径
	 *
	 * @param item 物品
	 * @return 路径
	 */
	static String getSmithingItemPath(ItemConvertible item) {
		return getItemPath(item) + "_from_smithing";
	}
	
	/**
	 * 获取反转的锻造物品路径
	 *
	 * @param item 物品
	 * @return 路径
	 */
	static String getSmithingSwapItemPath(ItemConvertible item) {
		return getItemPath(item) + "_from_smithing_swap";
	}
	
	/**
	 * 获取命名空间
	 *
	 * @param input  输入成分
	 * @param output 输出物品
	 * @return 命名空间字符串
	 */
	@ApiStatus.Internal
	static String getNamespace(Ingredient input, Item output) {
		String namespace = CraftingRecipeJsonBuilder.getItemId(output).getNamespace();
		if (!Minecraft.getInstance().id().equals(namespace)) return namespace;
		for (ItemStack stack : input.getMatchingStacks()) {
			namespace = CraftingRecipeJsonBuilder.getItemId(stack.getItem()).getNamespace();
			if (!Minecraft.getInstance().id().equals(namespace)) return namespace;
		}
		return namespace;
	}
	
	/**
	 * 获取输入
	 *
	 * @param inputs 输入图表
	 * @return 成分
	 */
	@ApiStatus.Internal
	static Ingredient getInput(Map<Character, Ingredient> inputs) {
		List<Item> items = new ArrayList<>(List.of());
		inputs.forEach((character, ingredient) -> Arrays.stream(ingredient.getMatchingStacks()).forEach(stack -> items.add(stack.getItem())));
		return Ingredient.ofItems(items.toArray(new Item[0]));
	}
	
	/**
	 * 获取输入
	 *
	 * @param inputs 输入列表
	 * @return 成分
	 */
	@ApiStatus.Internal
	static Ingredient getInput(List<Ingredient> inputs) {
		List<Item> items = new ArrayList<>(List.of());
		inputs.forEach(ingredient -> Arrays.stream(ingredient.getMatchingStacks()).forEach(stack -> items.add(stack.getItem())));
		return Ingredient.ofItems(items.toArray(new Item[0]));
	}
}
