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

package pers.saikel0rado1iu.silk.gen.data.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.visitor.StringNbtWriter;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.registry.gen.data.recipe.SilkRecipeSerializers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">NBT 有序配方 JSON 构建器</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class NbtShapedRecipeJsonBuilder extends ShapedRecipeJsonBuilder {
	protected final RecipeCategory category;
	protected final ItemStack output;
	protected final List<String> pattern = Lists.newArrayList();
	protected final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
	protected final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();
	@Nullable
	protected String group;
	protected boolean showNotification = true;
	
	public NbtShapedRecipeJsonBuilder(RecipeCategory category, ItemStack output) {
		super(category, output.getItem(), output.getCount());
		this.category = category;
		this.output = output;
	}
	
	public static NbtShapedRecipeJsonBuilder create(RecipeCategory category, ItemStack output) {
		return new NbtShapedRecipeJsonBuilder(category, output);
	}
	
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
	public NbtShapedRecipeJsonBuilder criterion(String string, CriterionConditions criterionConditions) {
		advancementBuilder.criterion(string, criterionConditions);
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
	
	@SilkApi
	public ItemStack getOutputItemStack() {
		return output;
	}
	
	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		validate(recipeId);
		advancementBuilder.parent(ROOT).criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(CriterionMerger.OR);
		exporter.accept(new NbtShapedRecipeJsonProvider(recipeId, output, group == null ? "" : group, getCraftingCategory(category), pattern, inputs, advancementBuilder, recipeId.withPrefixedPath("recipes/" + category.getName() + "/"), showNotification));
	}
	
	private void validate(Identifier recipeId) {
		if (pattern.isEmpty()) {
			throw new IllegalStateException("No pattern is defined for shaped recipe " + recipeId + "!");
		} else {
			Set<Character> set = Sets.newHashSet(inputs.keySet());
			set.remove(' ');
			
			for (String string : pattern) {
				for (int i = 0; i < string.length(); ++i) {
					char c = string.charAt(i);
					if (!this.inputs.containsKey(c) && c != ' ')
						throw new IllegalStateException("Pattern in recipe " + recipeId + " uses undefined symbol '" + c + "'");
					
					set.remove(c);
				}
			}
			
			if (!set.isEmpty()) throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + recipeId);
			else if (pattern.size() == 1 && pattern.get(0).length() == 1)
				throw new IllegalStateException("Shaped recipe " + recipeId + " only takes in a single item - should it be a shapeless recipe instead?");
			else if (advancementBuilder.getCriteria().isEmpty()) throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		}
	}
	
	public static class NbtShapedRecipeJsonProvider extends RecipeJsonBuilder.CraftingRecipeJsonProvider {
		private final Identifier recipeId;
		private final ItemStack output;
		private final String group;
		private final List<String> pattern;
		private final Map<Character, Ingredient> inputs;
		private final Advancement.Builder advancementBuilder;
		private final Identifier advancementId;
		private final boolean showNotification;
		
		public NbtShapedRecipeJsonProvider(Identifier recipeId, ItemStack output, String group, CraftingRecipeCategory craftingCategory, List<String> pattern, Map<Character, Ingredient> inputs, Advancement.Builder advancementBuilder, Identifier advancementId, boolean showNotification) {
			super(craftingCategory);
			this.recipeId = recipeId;
			this.output = output;
			this.group = group;
			this.pattern = pattern;
			this.inputs = inputs;
			this.advancementBuilder = advancementBuilder;
			this.advancementId = advancementId;
			this.showNotification = showNotification;
		}
		
		@Override
		public void serialize(JsonObject json) {
			super.serialize(json);
			if (!group.isEmpty()) json.addProperty("group", group);
			
			JsonArray jsonArray = new JsonArray();
			
			for (String string : pattern) jsonArray.add(string);
			
			json.add("pattern", jsonArray);
			JsonObject key = new JsonObject();
			
			for (Map.Entry<Character, Ingredient> characterIngredientEntry : inputs.entrySet()) {
				key.add(String.valueOf(characterIngredientEntry.getKey()), characterIngredientEntry.getValue().toJson());
			}
			
			json.add("key", key);
			JsonObject result = new JsonObject();
			result.addProperty("item", Registries.ITEM.getId(output.getItem()).toString());
			result.addProperty("nbt", new StringNbtWriter().apply(output.getNbt()));
			if (output.getCount() > 1) result.addProperty("count", output.getCount());
			
			json.add("result", result);
			json.addProperty("show_notification", showNotification);
		}
		
		@Override
		public RecipeSerializer<?> getSerializer() {
			return SilkRecipeSerializers.NBT_SHAPED;
		}
		
		@Override
		public Identifier getRecipeId() {
			return this.recipeId;
		}
		
		@Override
		@Nullable
		public JsonObject toAdvancementJson() {
			return this.advancementBuilder.toJson();
		}
		
		@Override
		@Nullable
		public Identifier getAdvancementId() {
			return this.advancementId;
		}
	}
}
