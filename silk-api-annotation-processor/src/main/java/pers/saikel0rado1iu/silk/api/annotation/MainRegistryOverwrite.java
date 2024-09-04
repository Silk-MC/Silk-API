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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2 style="color:FFC800">主要注册表覆写</h2>
 * 此注解只能使用于使用 {@code MainRegistrationProvider} 了进行注册的注册表项，且无法修改注册的标识符<br>
 * 此注解用于重写已被声明的可注册内容，可以快速实现类似于物品或方块的动态修改而不需要混入各种方法<br>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface MainRegistryOverwrite {
	/**
	 * 提供混入包名，生成的混入类会自动放入该包中
	 *
	 * @return 混入包名；
	 */
	String pack();
	
	/**
	 * 提供注册器用于从中修改注册项
	 *
	 * @return 注册项所在的注册器
	 */
	Class<?> registrar();
	
	/**
	 * 提供注册项在注册器中的字段名称
	 *
	 * @return 注册项的字段
	 */
	String field();
	
	/**
	 * 提供注册项的其余注册方法的覆写，如 {@code group()} 方法等
	 *
	 * @return 注册方法
	 */
	Register[] registers() default {};
}
