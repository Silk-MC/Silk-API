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

package pers.saikel0rado1iu.silk.codex;

import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">设置选项</h2>
 * 用于在设置中添加以及从设置中取出的选项
 *
 * @param <T> 选项类型中存储的参数类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record SettingOption<T>(Identifier id, OptionType<T> type, Function<T, T> getValue) {
	/**
	 * 用于生成设置选项<br>
	 * 使用此方法以避免显式调用构造方法
	 *
	 * @param id   选项的唯一标识符
	 * @param type 选项类型
	 * @param <T>  选项类型中存储的参数类型
	 * @return 设置选项
	 */
	public static <T> SettingOption<T> of(Identifier id, OptionType<T> type) {
		return new SettingOption<>(id, type, value -> value);
	}
	
	/**
	 * 用于生成设置选项<br>
	 * 使用此方法以避免显式调用构造方法
	 *
	 * @param id       选项的唯一标识符
	 * @param type     选项类型
	 * @param getValue 用于获取原值进行显示修改
	 * @param <T>      选项类型中存储的参数类型
	 * @return 设置选项
	 */
	public static <T> SettingOption<T> of(Identifier id, OptionType<T> type, Function<T, T> getValue) {
		return new SettingOption<>(id, type, getValue);
	}
}
