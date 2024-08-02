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

package pers.saikel0rado1iu.silk.api.landform;

import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.base.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.impl.SilkLandform;

/**
 * <h2 style="color:FFC800">可升级世界管理器注册提供器</h2>
 * 注册可升级世界管理器，可升级世界管理器可以用于动态的修改世界生成与区块生成规则
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ClientRegistration(registrar = UpgradableWorldManagerRegistrationProvider.ClientRegistrar.class, type = UpgradableWorldManager.class)
interface UpgradableWorldManagerRegistrationProvider extends ClientRegistrationProvider<UpgradableWorldManager<?>> {
	/**
	 * 更新管理器客户端注册器
	 *
	 * @param <T> 更新管理器类型
	 */
	final class ClientRegistrar<T extends UpgradableWorldManager<?>> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar() {
			super(() -> {
			});
		}
		
		@Override
		protected Identifier getIdentifier(T t) {
			return SilkLandform.getInstance().ofId(t.modData().id());
		}
		
		@Override
		public void register(T t) {
			UpgradableWorldManager.run(t);
			super.register(t);
		}
	}
}
