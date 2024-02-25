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

package pers.saikel0rado1iu.silk.api.registry.gen.data.recipe;

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
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.dynamic.Codecs;

/**
 * <h2 style="color:FFC800">用于带 NBT 的有序合成配方</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class NbtShapedRecipe extends ShapedRecipe {
	protected final String group;
	protected final RawShapedRecipe raw;
	protected final CraftingRecipeCategory category;
	protected final ItemStack result;
	protected final boolean showNotification;
	
	public NbtShapedRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
		super(group, category, raw, result, showNotification);
		this.group = group;
		this.category = category;
		this.raw = raw;
		this.result = result;
		this.showNotification = showNotification;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SilkRecipeSerializers.NBT_SHAPED;
	}
	
	public static class Serializer extends ShapedRecipe.Serializer {
		public static final Codec<Item> ITEM_CODEC = Codecs.validate(Registries.ITEM.getCodec(), item -> item == Items.AIR ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(item));
		public static final Codec<NbtCompound> NBT_CODEC = Codecs.xor(Codec.STRING, NbtCompound.CODEC).flatXmap(either -> either.map(string -> {
			try {
				return DataResult.success(StringNbtReader.parse(string));
			} catch (CommandSyntaxException e) {
				return DataResult.error(e::getMessage);
			}
		}, DataResult::success), nbtCompound -> DataResult.success(Either.left(nbtCompound.asString())));
		public static final Codec<ItemStack> NBT_RECIPE_RESULT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
						ITEM_CODEC.fieldOf("item").forGetter(ItemStack::getItem),
						Codecs.createStrictOptionalFieldCodec(NBT_CODEC, "nbt", new NbtCompound()).forGetter(ItemStack::getNbt),
						Codecs.createStrictOptionalFieldCodec(Codecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount))
				.apply(instance, (item, nbtCompound, integer) -> {
					ItemStack stack = new ItemStack(item, integer);
					stack.setNbt(nbtCompound);
					return stack;
				}));
		protected static final Codec<ShapedRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter(recipe -> ((NbtShapedRecipe) recipe).group),
						CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(recipe -> ((NbtShapedRecipe) recipe).category),
						RawShapedRecipe.CODEC.forGetter(recipe -> ((NbtShapedRecipe) recipe).raw),
						NBT_RECIPE_RESULT_CODEC.fieldOf("result").forGetter(recipe -> ((NbtShapedRecipe) recipe).result),
						Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "show_notification", true).forGetter(recipe -> ((NbtShapedRecipe) recipe).showNotification))
				.apply(instance, NbtShapedRecipe::new));
		
		@Override
		public Codec<ShapedRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public ShapedRecipe read(PacketByteBuf packetByteBuf) {
			String string = packetByteBuf.readString();
			CraftingRecipeCategory craftingRecipeCategory = packetByteBuf.readEnumConstant(CraftingRecipeCategory.class);
			RawShapedRecipe rawShapedRecipe = RawShapedRecipe.readFromBuf(packetByteBuf);
			ItemStack itemStack = packetByteBuf.readItemStack();
			return new ShapedRecipe(string, craftingRecipeCategory, rawShapedRecipe, itemStack, packetByteBuf.readBoolean());
		}
		
		@Override
		public void write(PacketByteBuf packetByteBuf, ShapedRecipe shapedRecipe) {
			if (!(shapedRecipe instanceof NbtShapedRecipe nbtShapedRecipe)) return;
			packetByteBuf.writeString(nbtShapedRecipe.group);
			packetByteBuf.writeEnumConstant(nbtShapedRecipe.category);
			nbtShapedRecipe.raw.writeToBuf(packetByteBuf);
			packetByteBuf.writeItemStack(nbtShapedRecipe.result);
			packetByteBuf.writeBoolean(nbtShapedRecipe.showNotification);
		}
	}
}
