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
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
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
		public static final Codec<RegistryEntry<Item>> ITEM_CODEC = Registries.ITEM.getEntryCodec().validate(entry -> entry.matches(Items.AIR.getRegistryEntry()) ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(entry));
		public static final Codec<NbtCompound> NBT_CODEC = Codec.xor(Codec.STRING, NbtCompound.CODEC).flatXmap(either -> either.map(string -> {
			try {
				return DataResult.success(StringNbtReader.parse(string));
			} catch (CommandSyntaxException e) {
				return DataResult.error(e::getMessage);
			}
		}, DataResult::success), nbtCompound -> DataResult.success(Either.left(nbtCompound.asString())));
		public static final Codec<ItemStack> NBT_RECIPE_RESULT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
						ITEM_CODEC.fieldOf("item").forGetter(ItemStack::getRegistryEntry),
						NBT_CODEC.optionalFieldOf("nbt", new NbtCompound()).forGetter(stack ->
								(NbtCompound) ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).result().orElse(new NbtCompound())),
						Codecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount))
				.apply(instance, (item, nbtCompound, integer) ->
						ItemStack.CODEC.decode(NbtOps.INSTANCE, nbtCompound).result().orElseThrow().getFirst()));
		protected static final MapCodec<ShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
						Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> ((NbtShapedRecipe) recipe).group),
						CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(recipe -> ((NbtShapedRecipe) recipe).category),
						RawShapedRecipe.CODEC.forGetter(recipe -> ((NbtShapedRecipe) recipe).raw),
						NBT_RECIPE_RESULT_CODEC.fieldOf("result").forGetter(recipe -> ((NbtShapedRecipe) recipe).result),
						Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(recipe -> ((NbtShapedRecipe) recipe).showNotification))
				.apply(instance, NbtShapedRecipe::new));
		public static final PacketCodec<RegistryByteBuf, ShapedRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::write, Serializer::read);
		
		public static ShapedRecipe read(RegistryByteBuf buf) {
			String string = buf.readString();
			CraftingRecipeCategory craftingRecipeCategory = buf.readEnumConstant(CraftingRecipeCategory.class);
			RawShapedRecipe rawShapedRecipe = RawShapedRecipe.PACKET_CODEC.decode(buf);
			ItemStack itemStack = ItemStack.PACKET_CODEC.decode(buf);
			return new ShapedRecipe(string, craftingRecipeCategory, rawShapedRecipe, itemStack, buf.readBoolean());
		}
		
		public static void write(RegistryByteBuf buf, ShapedRecipe shapedRecipe) {
			if (!(shapedRecipe instanceof NbtShapedRecipe nbtShapedRecipe)) return;
			buf.writeString(nbtShapedRecipe.group);
			buf.writeEnumConstant(nbtShapedRecipe.category);
			RawShapedRecipe.PACKET_CODEC.encode(buf, nbtShapedRecipe.raw);
			ItemStack.PACKET_CODEC.encode(buf, nbtShapedRecipe.result);
			buf.writeBoolean(nbtShapedRecipe.showNotification);
		}
		
		@Override
		public MapCodec<ShapedRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public PacketCodec<RegistryByteBuf, ShapedRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
}
