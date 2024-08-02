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

package pers.saikel0rado1iu.silk.api.codex;

import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.impl.SilkCodex;

import java.util.function.BiFunction;

/**
 * <h2 style="color:FFC800">选项类型</h2>
 * 设置选项中所有可设置的选项类型
 *
 * @param <T> 存储的参数类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record OptionType<T>(Class<T> clazz, Class<T[]> arrayClass, BiFunction<String, Class<T>, T> parseFunc, boolean isSlider) {
	/**
	 * 开关选项<br>
	 * 这个类型的选项只有开和关
	 */
	public static final OptionType<Boolean> SWITCH = new OptionType<>(Boolean.class, Boolean[].class, (s, c) -> Boolean.parseBoolean(s), false);
	/**
	 * 整数类型滑块<br>
	 * 这个类型提供了一个整数类型的滑块
	 */
	public static final OptionType<Integer> INT_SLIDER = new OptionType<>(Integer.class, Integer[].class, (s, c) -> Math.round((float) Double.parseDouble(s)), true);
	/**
	 * 双浮点类型滑块<br>
	 * 这个类型提供了一个双浮点数类型的滑块
	 */
	public static final OptionType<Double> DOUBLE_SLIDER = new OptionType<>(Double.class, Double[].class, (s, c) -> Double.parseDouble(s), true);
	/**
	 * 链接选项<br>
	 * 这个类型的选项是一个按钮，按下后会跳转到一个网页
	 */
	public static final OptionType<String> LINK = new OptionType<>(String.class, String[].class, (s, c) -> s, false);
	/**
	 * 设置选项<br>
	 * 这个类型的选项是一个按钮，按下后会跳转到另一个设置屏幕
	 */
	public static final OptionType<SettingData> SETTINGS = new OptionType<>(SettingData.class, SettingData[].class, (s, c) -> null, false);
	
	/**
	 * @param clazz      clazz
	 * @param arrayClass arrayClass
	 * @param parseFunc  parseFunc
	 * @param isSlider   isSlider
	 */
	@ApiStatus.Internal
	public OptionType {
	}
	
	/**
	 * 创建一个选项类型选项<br>
	 * 这个类型的选项可在多种状态中切换
	 *
	 * @param clazz      枚举类型
	 * @param arrayClass 枚举数组类型
	 * @param <T>        枚举的类型泛型
	 * @return 选项类型
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T extends Enum> OptionType<T> ofOption(Class<T> clazz, Class<T[]> arrayClass) {
		return new OptionType<>(clazz, arrayClass, (s, c) -> (T) Enum.valueOf(c, s), false);
	}
	
	/**
	 * 解析方法，用于解析值的字符串
	 *
	 * @param value 值的字符串
	 * @return 值
	 */
	public T parse(String value) {
		if (this == SETTINGS) {
			String msg = "Unresolvable type: The option type 'SETTING' cannot be directly parsed.";
			SilkCodex.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
		return parseFunc.apply(value, clazz);
	}
}
