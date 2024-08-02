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

package pers.saikel0rado1iu.silk.api.generate.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2 style="color:FFC800">带 NBT 的有序配方 JSON 构建器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class NbtShapedRecipeJsonBuilder extends ShapedRecipeJsonBuilder {
	protected final RecipeCategory category;
	protected final ItemStack output;
	protected final List<String> pattern = Lists.newArrayList();
	protected final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
	protected final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
	@Nullable
	protected String group;
	protected boolean showNotification = true;
	
	/**
	 * @param category 配方类别
	 * @param output   合成输出
	 */
	public NbtShapedRecipeJsonBuilder(RecipeCategory category, ItemStack output) {
		super(category, output.getItem(), output.getCount());
		this.category = category;
		this.output = output;
	}
	
	/**
	 * 创建 JSON 构建器
	 *
	 * @param category 配方类别
	 * @param output   输出物品堆栈
	 * @return JSON 构建器
	 */
	public static NbtShapedRecipeJsonBuilder create(RecipeCategory category, ItemStack output) {
		return new NbtShapedRecipeJsonBuilder(category, output);
	}
	
	/**
	 * 创建 JSON 构建器
	 *
	 * @param category 配方类别
	 * @param output   输出物品
	 * @param count    物品数量
	 * @return JSON 构建器
	 */
	public static NbtShapedRecipeJsonBuilder create(RecipeCategory category, ItemConvertible output, int count) {
		return create(category, new ItemStack(output, count));
	}
	
	@Override
	public NbtShapedRecipeJsonBuilder input(Character c, TagKey<Item> tag) {
		return input(c, Ingredient.fromTag(tag));
	}
	
	@Override
	public NbtShapedRecipeJsonBuilder input(Character c, ItemConvertible itemProvider) {
		return input(c, Ingredient.ofItems(itemProvider));
	}
	
	@Override
	public NbtShapedRecipeJsonBuilder input(Character c, Ingredient ingredient) {
		if (inputs.containsKey(c)) throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
		else if (c == ' ') throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		else inputs.put(c, ingredient);
		return this;
	}
	
	@Override
	public NbtShapedRecipeJsonBuilder pattern(String patternStr) {
		if (!pattern.isEmpty() && patternStr.length() != pattern.get(0).length())
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		else pattern.add(patternStr);
		return this;
	}
	
	@Override
	public NbtShapedRecipeJsonBuilder criterion(String string, AdvancementCriterion<?> advancementCriterion) {
		criteria.put(string, advancementCriterion);
		return this;
	}
	
	@Override
	public NbtShapedRecipeJsonBuilder group(@Nullable String string) {
		group = string;
		return this;
	}
	
	@Override
	public NbtShapedRecipeJsonBuilder showNotification(boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}
	
	/**
	 * 获取输出的物品堆栈
	 *
	 * @return 输出
	 */
	public ItemStack getOutputItemStack() {
		return output;
	}
	
	@Override
	public void offerTo(RecipeExporter exporter, Identifier recipeId) {
		RawShapedRecipe rawShapedRecipe = validate(recipeId);
		Advancement.Builder builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
		criteria.forEach(builder::criterion);
		exporter.accept(recipeId, new NbtShapedRecipe(group == null ? "" : group, CraftingRecipeJsonBuilder.toCraftingCategory(category), rawShapedRecipe, getOutputItemStack(), showNotification), builder.build(recipeId.withPrefixedPath("recipes/" + category.getName() + "/")));
	}
	
	protected RawShapedRecipe validate(Identifier recipeId) {
		if (this.criteria.isEmpty()) throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		else return RawShapedRecipe.create(this.inputs, this.pattern);
	}
}
