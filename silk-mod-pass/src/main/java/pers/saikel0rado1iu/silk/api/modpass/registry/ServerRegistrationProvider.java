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

import com.google.common.base.Suppliers;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">服务端注册提供器</h2>
 * 用于显式说明是服务端注册并提供服务端方法
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = Class.class, type = Class.class)
public interface ServerRegistrationProvider<T> extends RegisterableModPass<T> {
	/**
	 * 服务端注册器<br>
	 * 提供注册项进行注册，注册后返回注册项
	 *
	 * @param <T> 注册的数据类
	 */
	abstract class Registrar<T, R extends Registrar<T, R>> {
		protected final Supplier<T> type;
		
		protected Registrar(Supplier<T> type) {
			this.type = Suppliers.memoize(type::get);
		}
		
		protected abstract R self();
		
		/**
		 * 其他注册内容
		 *
		 * @param consumer 注册方法
		 * @return 服务端注册器
		 */
		public R other(Consumer<T> consumer) {
			consumer.accept(type.get());
			return self();
		}
		
		/**
		 * 进行注册
		 *
		 * @param modPass 模组通
		 * @param id      注册 ID
		 * @return 注册项
		 */
		protected T register(ModPass modPass, String id) {
			RegisterableModPass.loggingRegistration(modPass, type, modPass.modData().ofId(id), RegistrationType.SERVER_ONLY);
			return type.get();
		}
	}
}
