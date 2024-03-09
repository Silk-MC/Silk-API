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

package pers.saikel0rado1iu.silk.api.registry;

import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.function.UnaryOperator;

/**
 * <h2 style="color:FFC800">用于模组所有数据组件类型与注册</h2>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#dataComponentTypes()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkDataComponentType {
	protected static <T> Builder<T> builder(Class<T> cls, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return new Builder<>(builderOperator);
	}
	
	@SilkApi
	public static final class Builder<T> {
		private final UnaryOperator<DataComponentType.Builder<T>> builderOperator;
		
		@SilkApi
		private Builder(UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
			this.builderOperator = builderOperator;
		}
		
		@SilkApi
		public DataComponentType<T> build(ModBasicData mod, String id) {
			return Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(mod.getId(), id), builderOperator.apply(DataComponentType.builder()).build());
		}
	}
}
