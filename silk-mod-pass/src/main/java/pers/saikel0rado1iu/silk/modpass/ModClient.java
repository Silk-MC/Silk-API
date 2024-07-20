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

package pers.saikel0rado1iu.silk.modpass;

import net.fabricmc.api.ClientModInitializer;
import pers.saikel0rado1iu.silk.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.modpass.registry.RegisterableModPass;
import pers.saikel0rado1iu.silk.modpass.registry.RegistrationType;

/**
 * <h2 style="color:FFC800">模组客户端主类</h2>
 * 继承自 {@link ClientModInitializer}。所有模组客户端注册或操作由此开始
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ModClient extends ClientModInitializer, ModEntry<ClientRegistrationProvider<?>> {
	@Override
	default void onInitializeClient() {
		main(this);
		for (Class<? extends RegisterableModPass<?>> clazz : registry()) RegisterableModPass.loggingRegistration(this, clazz, RegistrationType.CLIENT_ONLY);
	}
}
