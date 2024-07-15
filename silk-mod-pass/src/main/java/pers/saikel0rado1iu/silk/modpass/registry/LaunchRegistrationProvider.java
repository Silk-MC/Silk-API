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

import pers.saikel0rado1iu.silk.impl.SilkModPass;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * <h2 style="color:FFC800">预启动注册提供器</h2>
 * 用于显式说明是预启动注册并提供预启动方法<br>
 * 实现此提供器必须提供一个 {@code static void register(Field);} 方法，此方法用于在预启动时注册字段
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface LaunchRegistrationProvider<T> extends RegisterableModPass<T> {
	/**
	 * 记录注册事件
	 *
	 * @param modPass          发起注册事件的模组的模组通
	 * @param clazz            {@link RegisterableModPass} 的类参数
	 * @param registrationType 注册类型
	 */
	static void loggingRegistration(ModPass modPass, Class<?> clazz, RegistrationType registrationType) {
		// 处理当前接口
		boolean foundTargetType = RegisterableModPass.processInterface(modPass, clazz, registrationType);
		// 处理父接口
		if (foundTargetType) return;
		Type[] interfaces = clazz.getGenericInterfaces();
		for (Type type : interfaces) {
			if (!(type instanceof Class<?> classType && classType.isInterface())) continue;
			for (Field field : clazz.getDeclaredFields()) {
				try {
					Method method = clazz.getInterfaces()[0].getMethod("register", Field.class);
					method.invoke(null, field);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					String msg = String.format("%s interface provides an incorrect [static void register(Field);] method or does not provide this method at all. Please implement this method for registration!", clazz.getInterfaces()[0].getName());
					SilkModPass.getInstance().logger().error(msg);
					throw new RuntimeException(msg);
				}
			}
			loggingRegistration(modPass, classType, registrationType);
		}
	}
	
	/**
	 * 预启动注册器<br>
	 * 提供注册项进行注册，注册后返回注册项
	 *
	 * @param <T> 注册的数据类
	 */
	abstract class Registrar<T> {
		protected final T type;
		
		protected Registrar(T type) {
			this.type = type;
		}
		
		/**
		 * 进行注册
		 *
		 * @param modPass 模组通
		 * @param id      注册 ID
		 * @return 注册项
		 */
		protected T register(ModPass modPass, String id) {
			RegisterableModPass.loggingRegistration(modPass, type, modPass.modData().ofId(id), RegistrationType.PRE_LAUNCH);
			return type;
		}
	}
}
