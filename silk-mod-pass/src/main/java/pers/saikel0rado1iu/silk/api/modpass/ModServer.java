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

import net.fabricmc.api.DedicatedServerModInitializer;
import pers.saikel0rado1iu.silk.api.modpass.registry.RegisterableModPass;
import pers.saikel0rado1iu.silk.api.modpass.registry.RegistrationType;
import pers.saikel0rado1iu.silk.api.modpass.registry.ServerRegistrationProvider;

/**
 * <h2 style="color:FFC800">模组服务端主类</h2>
 * 继承自 {@link DedicatedServerModInitializer}。提供服务端主函数
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ModServer extends DedicatedServerModInitializer, ModEntry<ServerRegistrationProvider<?>> {
	@Override
	default void onInitializeServer() {
		main(this);
		for (Class<? extends RegisterableModPass<?>> clazz : registry()) {
			RegisterableModPass.loggingRegistration(registrationNamespace(), clazz, RegistrationType.SERVER_ONLY);
		}
	}
}
