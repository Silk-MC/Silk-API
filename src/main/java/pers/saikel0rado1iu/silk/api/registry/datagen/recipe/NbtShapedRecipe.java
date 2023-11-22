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

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.collection.DefaultedList;

/**
 * <p><b style="color:FFC800"><font size="+1">用于带 NBT 的有序合成配方</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class NbtShapedRecipe extends ShapedRecipe {
	public NbtShapedRecipe(String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> ingredients, ItemStack result, boolean showNotification) {
		super(group, category, width, height, ingredients, result, showNotification);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SilkRecipeSerializers.NBT_SHAPED;
	}
	
	public static class Serializer extends ShapedRecipe.Serializer {
	}
}
