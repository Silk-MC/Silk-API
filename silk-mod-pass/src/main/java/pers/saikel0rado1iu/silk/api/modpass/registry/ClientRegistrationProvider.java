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

package pers.saikel0rado1iu.silk.api.modpass.registry;

import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.base.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.modpass.ModData;

/**
 * <h2 style="color:FFC800">客户端注册提供器</h2>
 * 用于显式说明是客户端注册并提供客户端方法
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ClientRegistration(registrar = Class.class, type = Class.class)
public interface ClientRegistrationProvider<T> extends RegisterableModPass<T> {
	/**
	 * 客户端注册器<br>
	 *
	 * @param <T> 注册的数据类
	 */
	abstract class Registrar<T> {
		protected final Runnable run;
		
		protected Registrar(Runnable run) {
			this.run = run;
		}
		
		protected abstract Identifier getIdentifier(T t);
		
		/**
		 * 进行注册
		 *
		 * @param t 需要注册的项目
		 */
		public void register(T t) {
			run.run();
			RegisterableModPass.loggingRegistration(((ModData) () -> getIdentifier(t).getNamespace()), t, getIdentifier(t), RegistrationType.CLIENT_ONLY);
		}
	}
}
