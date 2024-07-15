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

package pers.saikel0rado1iu.silk.spinningjenny.world.gen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

/**
 * <h2 style="color:FFC800">地物注册提供器</h2>
 * 用于整合地物并注册地物以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = FeatureRegistrationProvider.SERVER_REGISTRAR, type = FeatureRegistrationProvider.TYPE)
interface FeatureRegistrationProvider extends MainRegistrationProvider<Feature<?>> {
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.world.gen.FeatureRegistrationProvider.MainRegistrar";
	String TYPE = "net.minecraft.world.gen.feature.Feature";
	
	/**
	 * 地物主注册器
	 *
	 * @param <T> 地物
	 */
	final class MainRegistrar<T extends Feature<?>> extends Registrar<T, MainRegistrar<T>> {
		MainRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		public T register(ModPass modPass, String id) {
			Registry.register(Registries.FEATURE, modPass.modData().ofId(id), type);
			return super.register(modPass, id);
		}
	}
}
