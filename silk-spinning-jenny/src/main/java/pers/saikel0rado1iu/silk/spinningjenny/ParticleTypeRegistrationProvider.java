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

import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">粒子类型注册提供器</h2>
 * 用于整合粒子类型并注册粒子类型以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = ParticleTypeRegistrationProvider.SERVER_REGISTRAR, type = ParticleTypeRegistrationProvider.TYPE)
@ClientRegistration(registrar = ParticleTypeRegistrationProvider.CLIENT_REGISTRAR, type = ParticleTypeRegistrationProvider.TYPE)
interface ParticleTypeRegistrationProvider extends MainRegistrationProvider<ParticleType<?>>, ClientRegistrationProvider<ParticleType<?>> {
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.ParticleTypeRegistrationProvider.MainRegistrar";
	String CLIENT_REGISTRAR = "pers.saikel0rado1iu.silk.spinningjenny.ParticleTypeRegistrationProvider.ClientRegistrar";
	String TYPE = "net.minecraft.particle.ParticleType";
	
	/**
	 * 粒子类型主注册器
	 *
	 * @param <T> 粒子类型
	 */
	final class MainRegistrar<T extends ParticleType<?>> extends MainRegistrationProvider.Registrar<T, MainRegistrar<T>> {
		MainRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		@Override
		protected Optional<Registry<?>> registry() {
			return Optional.of(Registries.PARTICLE_TYPE);
		}
	}
	
	/**
	 * 粒子类型客户端注册器
	 *
	 * @param <T> 粒子类型
	 */
	final class ClientRegistrar<T extends ParticleType<?>> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar(Runnable run) {
			super(run);
		}
		
		@Override
		protected Identifier getIdentifier(T t) {
			return Registries.PARTICLE_TYPE.getId(t);
		}
	}
}
