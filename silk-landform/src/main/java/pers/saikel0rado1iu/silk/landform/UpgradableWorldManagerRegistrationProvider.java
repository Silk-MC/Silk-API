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

package pers.saikel0rado1iu.silk.landform;

import pers.saikel0rado1iu.silk.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.impl.SilkLandform;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.ClientRegistrationProvider;

import java.util.List;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">可升级世界管理器注册提供器</h2>
 * 注册可升级世界管理器，可升级世界管理器可以用于动态的修改世界生成与区块生成规则
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ClientRegistration(registrar = UpgradableWorldManagerRegistrationProvider.CLIENT_REGISTRAR, type = UpgradableWorldManagerRegistrationProvider.TYPE)
interface UpgradableWorldManagerRegistrationProvider extends ClientRegistrationProvider<UpgradableWorldManager<?>> {
	String CLIENT_REGISTRAR = "pers.saikel0rado1iu.silk.landform.UpgradableWorldManagerRegistrationProvider.ClientRegistrar";
	String TYPE = "pers.saikel0rado1iu.silk.landform.UpgradableWorldManager";
	
	/**
	 * 更新管理器客户端注册器
	 *
	 * @param <T> 更新管理器类型
	 */
	@SuppressWarnings("rawtypes")
	final class ClientRegistrar<T extends UpgradableWorldManager> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar(Supplier<List<T>> types) {
			super(types);
		}
		
		public List<T> register(ModPass modPass) {
			types.get().forEach(type -> UpgradableWorldManager.run(type));
			return register(modPass, type -> SilkLandform.getInstance().ofId(type.modData().id()));
		}
	}
}
