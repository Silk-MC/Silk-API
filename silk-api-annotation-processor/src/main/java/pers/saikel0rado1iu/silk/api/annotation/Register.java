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

package pers.saikel0rado1iu.silk.api.annotation;

/**
 * <h2 style="color:FFC800">注册方法</h2>
 * 用于提供注册方法给 {@link MainRegistryOverwrite} 使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public @interface Register {
	/**
	 * 提供目标方法的名称
	 *
	 * @return 目标方法的名称
	 */
	String method();
	
	/**
	 * 提供需注册的覆盖项的字段名称<br>
	 * 此字段必须和 {@link MainRegistryOverwrite} 声明的覆盖项在同一个类或接口中<br>
	 * 同时，此字段的类型必须与要修改的方法中参数的类型一致，否则会生成的 Mixin 则会无法使用
	 *
	 * @return 注册的覆盖项的字段名称
	 */
	String overwrite();
	
	/**
	 * 是否应该对方法启用重映射<br>
	 * 通常来说，{@code MainRegistrationProvider} 中提供的方法并没有直接使用 MC 中的类。<br>
	 * 但是，不排除有特殊方法使用了 MC 原版的类。届时，您需要手动启用重映射
	 *
	 * @return 是否启用重映射
	 */
	boolean remap() default false;
}
