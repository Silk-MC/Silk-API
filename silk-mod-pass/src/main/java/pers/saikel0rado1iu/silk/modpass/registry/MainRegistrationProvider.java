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

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.impl.SilkModPass;
import pers.saikel0rado1iu.silk.modpass.ModMain;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">主要注册提供器</h2>
 * 用于显式说明是需要同时进行客户端与服务端注册并提供主要方法
 *
 * @param <T> 注册的数据类
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = "root", type = "root")
public interface MainRegistrationProvider<T> extends RegisterableModPass<T> {
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
					Object obj = field.get(new Object());
					Registrar.RegistryData data = Registrar.THREAD_LOCAL_TAGS.get().get(obj);
					Identifier id;
					if (data == null) return;
					else if (data.id.isEmpty()) id = modPass.modData().ofId(field.getName().toLowerCase());
					else if (data.id.contains(":")) id = Identifier.tryParse(data.id);
					else id = modPass.modData().ofId(data.id);
					//noinspection unchecked
					Registry.register((Registry<? super Object>) data.registry, id, obj);
					RegisterableModPass.loggingRegistration(modPass, obj, id, RegistrationType.VANILLA_MAIN);
					Registrar.THREAD_LOCAL_TAGS.get().remove(obj);
					if (Registrar.THREAD_LOCAL_TAGS.get().isEmpty()) Registrar.THREAD_LOCAL_TAGS.remove();
				} catch (IllegalAccessException e) {
					String msg = String.format("Register field '%s' with illegal access modifier.", field.getName());
					SilkModPass.getInstance().logger().error(msg);
					throw new RuntimeException(msg);
				}
			}
			loggingRegistration(modPass, classType, registrationType);
		}
	}
	
	/**
	 * 主要注册器<br>
	 * 提供注册项进行注册，注册后返回注册项
	 *
	 * @param <T> 注册的数据类
	 */
	abstract class Registrar<T, R extends Registrar<T, R>> {
		private static final ThreadLocal<Map<Object, RegistryData>> THREAD_LOCAL_TAGS = ThreadLocal.withInitial(HashMap::new);
		protected final T type;
		
		protected Registrar(T type) {
			this.type = type;
		}
		
		protected abstract R self();
		
		protected abstract Registry<?> registry();
		
		/**
		 * 其他注册内容
		 *
		 * @param consumer 注册方法
		 * @return 主要注册器
		 */
		public R other(Consumer<T> consumer) {
			consumer.accept(type);
			return self();
		}
		
		/**
		 * 进行注册<br>
		 * 注册标识符为 {@code id}
		 *
		 * @param id 注册 ID
		 * @return 注册项
		 */
		public T register(Identifier id) {
			register(id.toString());
			return type;
		}
		
		/**
		 * 进行注册<br>
		 * 注册标识符为 {@link  ModMain#modData()}{@code .id():id}
		 *
		 * @param id 注册 ID
		 * @return 注册项
		 */
		public T register(String id) {
			THREAD_LOCAL_TAGS.get().put(type, new RegistryData(registry(), id));
			return type;
		}
		
		/**
		 * 进行注册
		 * 注册标识符为 {@link  ModMain#modData()}{@code .id():<field_name>}
		 *
		 * @return 注册项
		 */
		public T register() {
			register("");
			return type;
		}
		
		private record RegistryData(Registry<?> registry, String id) {
		}
	}
}
