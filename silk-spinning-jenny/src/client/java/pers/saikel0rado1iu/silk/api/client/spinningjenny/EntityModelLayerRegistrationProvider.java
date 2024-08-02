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

package pers.saikel0rado1iu.silk.api.client.spinningjenny;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.base.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;

/**
 * <h2 style="color:FFC800">实体模型图层注册提供器</h2>
 * 用于整合实体模型图层并注册实体模型图层以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ClientRegistration(registrar = EntityModelLayerRegistrationProvider.ClientRegistrar.class, type = EntityModelLayer.class)
interface EntityModelLayerRegistrationProvider extends ClientRegistrationProvider<EntityModelLayer> {
	/**
	 * 实体模型图层客户端注册器
	 *
	 * @param <T> 实体模型图层类型
	 */
	final class ClientRegistrar<T extends EntityModelLayer> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar(Runnable run) {
			super(run);
		}
		
		@Override
		protected Identifier getIdentifier(T t) {
			return t.getId();
		}
	}
}
