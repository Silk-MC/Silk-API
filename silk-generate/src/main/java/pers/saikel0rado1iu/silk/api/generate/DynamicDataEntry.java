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

package pers.saikel0rado1iu.silk.api.generate;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

/**
 * <h2 style="color:FFC800">动态数据条目</h2>
 * 用于注册动态数据生成条目
 *
 * @param <T> 生成数据类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface DynamicDataEntry<T> {
	/**
	 * 动态数据配置方法
	 *
	 * @param registries 注册表
	 * @param entries    注册条目
	 */
	default void configure(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries) {
		entries.addAll(registries.getWrapperOrThrow(registryKey()));
	}
	
	/**
	 * 注册动态数据条目
	 *
	 * @param registryBuilder 注册表构建器
	 */
	default void register(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(registryKey(), bootstrap());
	}
	
	/**
	 * 动态数据注册表键
	 *
	 * @return 动态数据注册表键
	 */
	RegistryKey<? extends Registry<T>> registryKey();
	
	/**
	 * 动态数据构建方法
	 *
	 * @return 动态数据构建方法
	 */
	RegistryBuilder.BootstrapFunction<T> bootstrap();
}
