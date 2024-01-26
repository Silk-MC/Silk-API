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

package pers.saikel0rado1iu.silk.api.registry.gen.data;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">用于模组所有标准与标准注册</h2>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#datagen()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface SilkCriterion {
	@SilkApi
	static <C extends AbstractCriterionConditions, T extends AbstractCriterion<C>> T create(T criterion) {
		return criterion;
	}
	
	@SilkApi
	static <C extends AbstractCriterionConditions, T extends AbstractCriterion<C>> T create(T criterion, Consumer<T> criterionRegister) {
		criterionRegister.accept(criterion);
		return criterion;
	}
	
	@SilkApi
	static String getId(ModBasicData mod, String id) {
		return mod.getId() + '/' + id;
	}
}
