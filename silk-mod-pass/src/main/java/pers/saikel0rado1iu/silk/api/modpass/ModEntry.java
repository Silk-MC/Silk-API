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

package pers.saikel0rado1iu.silk.api.modpass;

import pers.saikel0rado1iu.silk.api.modpass.registry.RegisterableModPass;

import java.util.Set;

/**
 * <h2 style="color:FFC800">模组入口</h2>
 * 用于实现模组入口
 *
 * @param <T> 可注册模组通类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModEntry<T extends RegisterableModPass<?>> extends ModPass {
	/**
	 * 模组主函数
	 *
	 * @param mod 提供的模组通
	 */
	void main(ModPass mod);
	
	/**
	 * 注册表方法，提供注册表以供注册
	 *
	 * @return 注册表的类型集合
	 */
	Set<Class<? extends T>> registry();
	
	/**
	 * 注册命名空间<br>
	 * 默认的注册命名空间为 {@code modData()} 中提供的模组通
	 *
	 * @return 注册命名空间的模组通
	 */
	default ModPass registrationNamespace() {
		return modData();
	}
}
