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

import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.registry.gen.data.SilkRecipeSerializer;
import pers.saikel0rado1iu.silk.api.registry.gen.data.criterion.SilkCriteria;

/**
 * <h2 style="color:FFC800">用于补充原版的配方序列化器以便在模组中使用</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface SilkRecipeSerializers {
	@ApiStatus.Internal
	SilkCriteria INSTANCE = new SilkCriteria() {
		@Override
		public String toString() {
			return "SilkRecipeSerializers";
		}
	};
	@SilkApi
	NbtShapedRecipe.Serializer NBT_SHAPED = SilkRecipeSerializer.create(new NbtShapedRecipe.Serializer(), Silk.DATA, "crafting_nbt_shaped");
}
