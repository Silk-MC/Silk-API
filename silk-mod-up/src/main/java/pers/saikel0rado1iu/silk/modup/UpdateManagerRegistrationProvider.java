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

package pers.saikel0rado1iu.silk.modup;

import pers.saikel0rado1iu.silk.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

import java.util.List;
import java.util.function.Supplier;

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
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.modup.UpdateManagerRegistrationProvider.ServerRegistrar";
	String CLIENT_REGISTRAR = "pers.saikel0rado1iu.silk.modup.UpdateManagerRegistrationProvider.ClientRegistrar";
	String TYPE = "pers.saikel0rado1iu.silk.modup.UpdateManager";
	
	/**
	 * 更新管理器服务端注册器
	 *
	 * @param <T> 更新管理器类型
	 */
	final class ServerRegistrar<T extends UpdateManager> extends MainRegistrationProvider.Registrar<T, ServerRegistrar<T>> {
		ServerRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected ServerRegistrar<T> self() {
			return this;
		}
		
		public T register() {
			UpdateManager.run(type);
			return super.register(SilkModUp.getInstance(), type.modData().id());
		}
	}
	
	/**
	 * 更新管理器客户端注册器
	 *
	 * @param <T> 更新管理器类型
	 */
	final class ClientRegistrar<T extends UpdateManager> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar(Supplier<List<T>> types) {
			super(types);
		}
		
		public List<T> register(ModPass modPass) {
			types.get().forEach(type -> UpdateManager.run(type));
			return register(modPass, type -> SilkModUp.getInstance().ofId(type.modData().id()));
		}
	}
}
