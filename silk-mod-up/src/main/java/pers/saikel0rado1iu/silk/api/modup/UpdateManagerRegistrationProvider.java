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

package pers.saikel0rado1iu.silk.api.modup;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.base.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.base.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.impl.SilkModUp;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">更新管理器注册提供器</h2>
 * 只能更新上传在 Modrinth 上的模组，并且从 Modrinth API 上查找所需的更新包<br>
 * 用于注册可更新的模组以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = UpdateManagerRegistrationProvider.SERVER_REGISTRAR, type = UpdateManagerRegistrationProvider.TYPE)
@ClientRegistration(registrar = UpdateManagerRegistrationProvider.CLIENT_REGISTRAR, type = UpdateManagerRegistrationProvider.TYPE)
interface UpdateManagerRegistrationProvider extends MainRegistrationProvider<UpdateManager>, ClientRegistrationProvider<UpdateManager> {
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.api.modup.UpdateManagerRegistrationProvider.MainRegistrar";
	String CLIENT_REGISTRAR = "pers.saikel0rado1iu.silk.api.modup.UpdateManagerRegistrationProvider.ClientRegistrar";
	String TYPE = "pers.saikel0rado1iu.silk.api.modup.UpdateManager";
	
	/**
	 * 更新管理器服务端注册器
	 *
	 * @param <T> 更新管理器类型
	 */
	final class MainRegistrar<T extends UpdateManager> extends MainRegistrationProvider.Registrar<T, MainRegistrar<T>> {
		MainRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		@Override
		protected Optional<Registry<?>> registry() {
			return Optional.empty();
		}
		
		public T register() {
			UpdateManager.run(type);
			return super.register(SilkModUp.getInstance().ofId(type.modData().id()));
		}
	}
	
	/**
	 * 更新管理器客户端注册器
	 *
	 * @param <T> 更新管理器类型
	 */
	final class ClientRegistrar<T extends UpdateManager> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar() {
			super(() -> {
			});
		}
		
		@Override
		protected Identifier getIdentifier(T t) {
			return SilkModUp.getInstance().ofId(t.modData().id());
		}
		
		@Override
		public void register(T t) {
			UpdateManager.run(t);
			super.register(t);
		}
	}
}
