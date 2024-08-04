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

package pers.saikel0rado1iu.silk.api.modpass;

import net.fabricmc.api.ModInitializer;
import pers.saikel0rado1iu.silk.api.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.api.modpass.pack.ResourcePack;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.RegisterableModPass;
import pers.saikel0rado1iu.silk.api.modpass.registry.RegistrationType;

/**
 * <h2 style="color:FFC800">模组主类</h2>
 * 继承自 {@link ModInitializer}。所有模组注册或操作由此开始
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ModMain extends ModInitializer, ModEntry<MainRegistrationProvider<?>> {
	@Override
	default void onInitialize() {
		main(this);
		if (modData() instanceof ModDataExpansion modDataExpansion) {
			modDataExpansion.dataPack().ifPresent(DataPack::registry);
			modDataExpansion.resourcePack().ifPresent(ResourcePack::registry);
		}
		for (Class<? extends RegisterableModPass<?>> clazz : registry()) {
			MainRegistrationProvider.loggingRegistration(registrationNamespace(), clazz, RegistrationType.VANILLA_MAIN);
		}
	}
}
