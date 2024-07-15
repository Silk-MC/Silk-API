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

package pers.saikel0rado1iu.silk.spinningjenny;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import pers.saikel0rado1iu.silk.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.ClientRegistrationProvider;

import java.util.List;
import java.util.Map;

/**
 * <h2 style="color:FFC800">实体模型图层注册提供器</h2>
 * 用于整合实体模型图层并注册实体模型图层以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ClientRegistration(registrar = EntityModelLayerRegistrationProvider.CLIENT_REGISTRAR, type = EntityModelLayerRegistrationProvider.TYPE)
interface EntityModelLayerRegistrationProvider extends ClientRegistrationProvider<EntityModelLayer> {
	String CLIENT_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.EntityModelLayerRegistrationProvider.ClientRegistrar";
	String TYPE = "net.minecraft.client.render.entity.model.EntityModelLayer";
	
	/**
	 * 实体模型图层客户端注册器
	 *
	 * @param <T> 实体模型图层类型
	 */
	final class ClientRegistrar<T extends EntityModelLayer> extends ClientRegistrationProvider.Registrar<T> {
		private final Map<T, EntityModelLayerRegistry.TexturedModelDataProvider> map;
		
		ClientRegistrar(Map<T, EntityModelLayerRegistry.TexturedModelDataProvider> map) {
			super(() -> ImmutableList.copyOf(map.keySet()));
			this.map = map;
		}
		
		public List<T> register(ModPass modPass) {
			map.forEach(EntityModelLayerRegistry::registerModelLayer);
			return register(modPass, EntityModelLayer::getId);
		}
	}
}
