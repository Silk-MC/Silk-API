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

package pers.saikel0rado1iu.silk.spinningjenny.data.gen;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">标准注册提供器</h2>
 * 用于整合标准并注册标准以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = CriterionRegistrationProvider.SERVER_REGISTRAR, type = CriterionRegistrationProvider.TYPE)
interface CriterionRegistrationProvider extends MainRegistrationProvider<Criterion<?>> {
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.data.gen.CriterionRegistrationProvider.MainRegistrar";
	String TYPE = "net.minecraft.advancement.criterion.Criterion";
	
	/**
	 * 标准主注册器
	 *
	 * @param <T> 标准
	 */
	final class MainRegistrar<T extends Criterion<?>> extends Registrar<T, MainRegistrar<T>> {
		MainRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		@Override
		protected Optional<Registry<?>> registry() {
			return Optional.of(Registries.CRITERION);
		}
	}
}
