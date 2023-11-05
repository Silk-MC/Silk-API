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

package pers.saikel0rado1iu.silk.api.registry.datagen;

import com.google.common.collect.Sets;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.Set;
import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有标准与标准注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#criteria()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkCriterion {
	public static final Set<AbstractCriterion<?>> ALL_MOD_CRITERIA = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static <C extends AbstractCriterionConditions, T extends AbstractCriterion<C>> Builder<C, T> builder(T criterion) {
		return new Builder<>(criterion);
	}
	
	@SilkApi
	public static final class Builder<C extends AbstractCriterionConditions, T extends AbstractCriterion<C>> {
		private final T criterion;
		
		@SilkApi
		private Builder(T criterion) {
			ALL_MOD_CRITERIA.add(this.criterion = criterion);
		}
		
		@SilkApi
		public Builder<C, T> put(Set<AbstractCriterion<?>> criteria) {
			criteria.add(criterion);
			return this;
		}
		
		@SilkApi
		public Builder<C, T> otherRegister(Consumer<T> criterionRegister) {
			criterionRegister.accept(criterion);
			return this;
		}
		
		@SilkApi
		public T build() {
			return criterion;
		}
	}
}
