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
import com.google.common.collect.Sets;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p><b style="color:FFC800"><font size="+1">用于带 NBT 的有序合成配方</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class NbtShapedRecipe extends ShapedRecipe {
	protected final String group;
	protected final CraftingRecipeCategory category;
	protected final int width;
	protected final int height;
	protected final DefaultedList<Ingredient> ingredients;
	protected final ItemStack result;
	protected final boolean showNotification;
	
	public NbtShapedRecipe(String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> ingredients, ItemStack result, boolean showNotification) {
		super(group, category, width, height, ingredients, result, showNotification);
		this.group = group;
		this.category = category;
		this.width = width;
		this.height = height;
		this.ingredients = ingredients;
		this.result = result;
		this.showNotification = showNotification;
	}
	
	protected static int findFirstSymbol(String line) {
		int i = 0;
		while (i < line.length() && line.charAt(i) == ' ') ++i;
		return i;
	}
	
	protected static int findLastSymbol(String pattern) {
		int i = pattern.length() - 1;
		while (i >= 0 && pattern.charAt(i) == ' ') --i;
		return i;
	}
	
	@VisibleForTesting
	protected static String[] removePadding(List<String> pattern) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;
		for (int m = 0; m < pattern.size(); ++m) {
			String string = pattern.get(m);
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
		if (pattern.size() == l) return new String[0];
		String[] strings = new String[pattern.size() - l - k];
		for (int o = 0; o < strings.length; ++o) strings[o] = pattern.get(o + k).substring(i, j + 1);
		return strings;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SilkRecipeSerializers.NBT_SHAPED;
	}
	
	public static class Serializer extends ShapedRecipe.Serializer {
		protected static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().flatXmap(rows -> {
			if (rows.size() > 3) return DataResult.error(() -> "Invalid pattern: too many rows, 3 is maximum");
			if (rows.isEmpty()) return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
			int i = rows.get(0).length();
			for (String string : rows) {
				if (string.length() > 3) return DataResult.error(() -> "Invalid pattern: too many columns, 3 is maximum");
				if (i == string.length()) continue;
				return DataResult.error(() -> "Invalid pattern: each row must be the same width");
			}
			return DataResult.success(rows);
		}, DataResult::success);
		protected static final Codec<String> KEY_ENTRY_CODEC = Codec.STRING.flatXmap(keyEntry -> {
			if (keyEntry.length() != 1) return DataResult.error(() -> "Invalid key entry: '" + keyEntry + "' is an invalid symbol (must be 1 character only).");
			if (" ".equals(keyEntry)) return DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.");
			return DataResult.success(keyEntry);
		}, DataResult::success);
		protected static final Codec<ShapedRecipe> CODEC = RawShapedRecipe.CODEC.flatXmap(recipe -> {
			String[] strings = NbtShapedRecipe.removePadding(recipe.pattern);
			int i = strings[0].length();
			int j = strings.length;
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);
			HashSet<String> set = Sets.newHashSet(recipe.key.keySet());
			for (int k = 0; k < strings.length; ++k) {
				String string = strings[k];
				for (int l = 0; l < string.length(); ++l) {
					Ingredient ingredient;
					String string2 = string.substring(l, l + 1);
					ingredient = " ".equals(string2) ? Ingredient.EMPTY : recipe.key.get(string2);
					if (ingredient == null) {
						return DataResult.error(() -> "Pattern references symbol '" + string2 + "' but it'string not defined in the key");
					}
					set.remove(string2);
					defaultedList.set(l + i * k, ingredient);
				}
			}
			if (!set.isEmpty()) return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + set);
			ShapedRecipe shapedRecipe = new ShapedRecipe(recipe.group, recipe.category, i, j, defaultedList, recipe.result, recipe.showNotification);
			return DataResult.success(shapedRecipe);
		}, recipe -> {
			throw new NotImplementedException("Serializing ShapedRecipe is not implemented yet.");
		});
		
		@Override
		public Codec<ShapedRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public ShapedRecipe read(PacketByteBuf packetByteBuf) {
			int i = packetByteBuf.readVarInt();
			int j = packetByteBuf.readVarInt();
			String string = packetByteBuf.readString();
			CraftingRecipeCategory craftingRecipeCategory = packetByteBuf.readEnumConstant(CraftingRecipeCategory.class);
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);
			defaultedList.replaceAll(ignored -> Ingredient.fromPacket(packetByteBuf));
			ItemStack itemStack = packetByteBuf.readItemStack();
			boolean bl = packetByteBuf.readBoolean();
			return new ShapedRecipe(string, craftingRecipeCategory, i, j, defaultedList, itemStack, bl);
		}
		
		@Override
		public void write(PacketByteBuf packetByteBuf, ShapedRecipe shapedRecipe) {
			if (!(shapedRecipe instanceof NbtShapedRecipe nbtShapedRecipe)) return;
			packetByteBuf.writeVarInt(nbtShapedRecipe.width);
			packetByteBuf.writeVarInt(nbtShapedRecipe.height);
			packetByteBuf.writeString(nbtShapedRecipe.group);
			packetByteBuf.writeEnumConstant(nbtShapedRecipe.category);
			for (Ingredient ingredient : nbtShapedRecipe.ingredients) ingredient.write(packetByteBuf);
			packetByteBuf.writeItemStack(nbtShapedRecipe.result);
			packetByteBuf.writeBoolean(nbtShapedRecipe.showNotification);
		}
		
		protected record RawShapedRecipe(
				String group,
				CraftingRecipeCategory category,
				Map<String, Ingredient> key,
				List<String> pattern,
				ItemStack result,
				boolean showNotification) {
			public static final Codec<Item> CRAFTING_RESULT_ITEM = Codecs.validate(Registries.ITEM.getCodec(), item -> item == Items.AIR ? DataResult.error(() -> "Crafting result must not be minecraft:air") : DataResult.success(item));
			public static final Codec<NbtCompound> NBT_CODEC = Codecs.xor(Codec.STRING, NbtCompound.CODEC).flatXmap(either -> either.map(string -> {
				try {
					return DataResult.success(StringNbtReader.parse(string));
				} catch (CommandSyntaxException e) {
					return DataResult.error(e::getMessage);
				}
			}, DataResult::success), nbtCompound -> DataResult.success(Either.left(nbtCompound.asString())));
			public static final Codec<ItemStack> CRAFTING_RESULT = RecordCodecBuilder.create(instance -> instance.group(
					(CRAFTING_RESULT_ITEM.fieldOf("item")).forGetter(ItemStack::getItem),
					Codecs.createStrictOptionalFieldCodec(NBT_CODEC, "nbt", new NbtCompound()).forGetter(ItemStack::getNbt),
					Codecs.createStrictOptionalFieldCodec(Codecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount)).apply(instance, (item, nbtCompound, integer) -> {
				ItemStack stack = new ItemStack(item, integer);
				stack.setNbt(nbtCompound);
				return stack;
			}));
			public static final Codec<RawShapedRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
					(CraftingRecipeCategory.CODEC.fieldOf("category")).orElse(CraftingRecipeCategory.MISC).forGetter(recipe -> recipe.category),
					(Codecs.strictUnboundedMap(KEY_ENTRY_CODEC, Ingredient.DISALLOW_EMPTY_CODEC).fieldOf("key")).forGetter(recipe -> recipe.key),
					(PATTERN_CODEC.fieldOf("pattern")).forGetter(recipe -> recipe.pattern),
					(CRAFTING_RESULT.fieldOf("result")).forGetter(recipe -> recipe.result),
					Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "show_notification", true).forGetter(recipe -> recipe.showNotification)).apply(instance, RawShapedRecipe::new));
		}
	}
}
