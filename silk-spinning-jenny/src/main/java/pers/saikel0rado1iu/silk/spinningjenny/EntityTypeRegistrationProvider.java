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

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">实体类型注册提供器</h2>
 * 用于整合实体类型并注册实体类型以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = EntityTypeRegistrationProvider.SERVER_REGISTRAR, type = EntityTypeRegistrationProvider.TYPE)
@ClientRegistration(registrar = EntityTypeRegistrationProvider.CLIENT_REGISTRAR, type = EntityTypeRegistrationProvider.TYPE)
interface EntityTypeRegistrationProvider extends MainRegistrationProvider<EntityType<?>>, ClientRegistrationProvider<EntityType<?>> {
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.EntityTypeRegistrationProvider.MainRegistrar";
	String CLIENT_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.EntityTypeRegistrationProvider.ClientRegistrar";
	String TYPE = "net.minecraft.entity.EntityType";
	
	/**
	 * 实体类型主注册器
	 *
	 * @param <T> 实体类型
	 */
	final class MainRegistrar<T extends EntityType<?>> extends MainRegistrationProvider.Registrar<T, MainRegistrar<T>> {
		MainRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		@Override
		protected Optional<Registry<?>> registry() {
			return Optional.of(Registries.ENTITY_TYPE);
		}
		
	}
	
	/**
	 * 实体类型客户端注册器
	 *
	 * @param <T> 实体类型
	 */
	final class ClientRegistrar<T extends EntityType<?>> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar(Runnable run) {
			super(run);
		}
		
		@Override
		protected Identifier getIdentifier(T t) {
			return Registries.ENTITY_TYPE.getId(t);
		}
	}
}
