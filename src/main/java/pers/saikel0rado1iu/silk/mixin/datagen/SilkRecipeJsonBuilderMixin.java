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

package pers.saikel0rado1iu.silk.mixin.datagen;

import net.minecraft.data.server.recipe.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">设置配方生成的 offer() 方法混入</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
interface SilkRecipeJsonBuilderMixin {
	@SilkApi
	@Mixin(CookingRecipeJsonBuilder.class)
	abstract class CookingRecipeJsonBuilderMixin implements CraftingRecipeJsonBuilder {
		@Override
		public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
			Identifier main = new Identifier(CraftingRecipeJsonBuilder.getItemId(getOutputItem()).getNamespace(), recipePath);
			Identifier test = CraftingRecipeJsonBuilder.getItemId(getOutputItem());
			if (main.equals(test)) throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
			offerTo(exporter, main);
		}
	}
	
	@SilkApi
	@Mixin(ShapedRecipeJsonBuilder.class)
	abstract class ShapedRecipeJsonBuilderMixin implements CraftingRecipeJsonBuilder {
		@Override
		public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
			Identifier main = new Identifier(CraftingRecipeJsonBuilder.getItemId(getOutputItem()).getNamespace(), recipePath);
			Identifier test = CraftingRecipeJsonBuilder.getItemId(getOutputItem());
			if (main.equals(test)) throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
			offerTo(exporter, main);
		}
	}
	
	@SilkApi
	@Mixin(ShapelessRecipeJsonBuilder.class)
	abstract class ShapelessRecipeJsonBuilderMixin implements CraftingRecipeJsonBuilder {
		@Override
		public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
			Identifier main = new Identifier(CraftingRecipeJsonBuilder.getItemId(getOutputItem()).getNamespace(), recipePath);
			Identifier test = CraftingRecipeJsonBuilder.getItemId(getOutputItem());
			if (main.equals(test)) throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
			offerTo(exporter, main);
		}
	}
	
	@SilkApi
	@Mixin(SingleItemRecipeJsonBuilder.class)
	abstract class SingleItemRecipeJsonBuilderMixin implements CraftingRecipeJsonBuilder {
		@Override
		public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
			Identifier main = new Identifier(CraftingRecipeJsonBuilder.getItemId(getOutputItem()).getNamespace(), recipePath);
			Identifier test = CraftingRecipeJsonBuilder.getItemId(getOutputItem());
			if (main.equals(test)) throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
			offerTo(exporter, main);
		}
	}
}
