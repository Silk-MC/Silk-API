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

package pers.saikel0rado1iu.silk.modpass.registry;

import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">客户端注册提供器</h2>
 * 用于显式说明是客户端注册并提供客户端方法
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ClientRegistration(registrar = "root", type = "root")
public interface ClientRegistrationProvider<T> extends RegisterableModPass<T> {
	/**
	 * 客户端注册器<br>
	 * 提供 {@link Supplier} 进行注册，使用 {@link List} 作为静态常量使用
	 *
	 * @param <T> 注册的数据类
	 */
	abstract class Registrar<T> {
		protected final Supplier<List<T>> types;
		
		protected Registrar(Supplier<List<T>> types) {
			this.types = types;
		}
		
		/**
		 * 进行注册
		 *
		 * @param modPass  模组通
		 * @param function 注册函数
		 * @return 注册项迭代器
		 */
		protected List<T> register(ModPass modPass, Function<T, Identifier> function) {
			types.get().forEach(type -> RegisterableModPass.loggingRegistration(modPass, type, function.apply(type), RegistrationType.CLIENT_ONLY));
			return types.get();
		}
	}
}
