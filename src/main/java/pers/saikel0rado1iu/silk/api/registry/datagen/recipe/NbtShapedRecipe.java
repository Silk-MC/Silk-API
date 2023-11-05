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

package pers.saikel0rado1iu.silk.api.registry.datagen.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * <p><b style="color:FFC800"><font size="+1">用于带 NBT 的有序合成配方</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class NbtShapedRecipe extends ShapedRecipe {
	public NbtShapedRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output, boolean showNotification) {
		super(id, group, category, width, height, input, output, showNotification);
	}
	
	public NbtShapedRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
		this(id, group, category, width, height, input, output, true);
	}
	
	protected static int findFirstSymbol(String line) {
		int i = 0;
		while (i < line.length() && line.charAt(i) == ' ') i++;
		return i;
	}
	
	protected static int findLastSymbol(String pattern) {
		int i = pattern.length() - 1;
		while (i >= 0 && pattern.charAt(i) == ' ') i--;
		return i;
	}
	
	protected static String[] getPattern(JsonArray json) {
		String[] strings = new String[json.size()];
		if (strings.length > 3) throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
		if (strings.length == 0) throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		for (int i = 0; i < strings.length; ++i) {
			String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
			if (string.length() > 3) throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
			if (i > 0 && strings[0].length() != string.length()) throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
			strings[i] = string;
		}
		return strings;
	}
	
	protected static Map<String, Ingredient> readSymbols(JsonObject json) {
		HashMap<String, Ingredient> map = Maps.newHashMap();
		for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
			if (entry.getKey().length() != 1)
				throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			if (" ".equals(entry.getKey())) throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
		}
		map.put(" ", Ingredient.EMPTY);
		return map;
	}
	
	@VisibleForTesting
	protected static String[] removePadding(String... pattern) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;
		for (int m = 0; m < pattern.length; ++m) {
			String string = pattern[m];
			i = Math.min(i, findFirstSymbol(string));
			int n = findLastSymbol(string);
			j = Math.max(j, n);
			if (n < 0) {
				if (k == m) ++k;
				++l;
				continue;
			}
			l = 0;
		}
		if (pattern.length == l) return new String[0];
		String[] strings = new String[pattern.length - l - k];
		for (int o = 0; o < strings.length; ++o) strings[o] = pattern[o + k].substring(i, j + 1);
		return strings;
	}
	
	protected static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
		DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		HashSet<String> set = Sets.newHashSet(symbols.keySet());
		set.remove(" ");
		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String string = pattern[i].substring(j, j + 1);
				Ingredient ingredient = symbols.get(string);
				if (ingredient == null) throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
				set.remove(string);
				defaultedList.set(j + width * i, ingredient);
			}
		}
		if (!set.isEmpty()) throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		return defaultedList;
	}
	
	public static ItemStack outputFromJson(JsonObject json) {
		ItemStack stack = getItemStack(json);
		if (json.has("data")) throw new JsonParseException("Disallowed data tag found");
		int count = JsonHelper.getInt(json, "count", 1);
		if (count < 1) throw new JsonSyntaxException("Invalid output count: " + count);
		stack.setCount(count);
		return stack;
	}
	
	public static ItemStack getItemStack(JsonObject json) {
		String string = JsonHelper.getString(json, "item");
		NbtCompound nbtCompound;
		try {
			nbtCompound = StringNbtReader.parse(JsonHelper.getString(json, "nbt"));
		} catch (CommandSyntaxException e) {
			nbtCompound = new NbtCompound();
		}
		Item item = Registries.ITEM.getOrEmpty(Identifier.tryParse(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
		if (item == Items.AIR) throw new JsonSyntaxException("Empty ingredient not allowed here");
		ItemStack stack = new ItemStack(item);
		stack.setNbt(nbtCompound);
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SilkRecipeSerializers.NBT_SHAPED;
	}
	
	public static class Serializer extends ShapedRecipe.Serializer {
		@Override
		public ShapedRecipe read(Identifier identifier, JsonObject jsonObject) {
			String string = JsonHelper.getString(jsonObject, "group", "");
			CraftingRecipeCategory craftingRecipeCategory = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(jsonObject, "category", null), CraftingRecipeCategory.MISC);
			Map<String, Ingredient> map = readSymbols(JsonHelper.getObject(jsonObject, "key"));
			String[] strings = removePadding(getPattern(JsonHelper.getArray(jsonObject, "pattern")));
			DefaultedList<Ingredient> defaultedList = createPatternMatrix(strings, map, strings[0].length(), strings.length);
			ItemStack itemStack = outputFromJson(JsonHelper.getObject(jsonObject, "result"));
			return new ShapedRecipe(identifier, string, craftingRecipeCategory, strings[0].length(), strings.length, defaultedList, itemStack, JsonHelper.getBoolean(jsonObject, "show_notification", true));
		}
	}
}
