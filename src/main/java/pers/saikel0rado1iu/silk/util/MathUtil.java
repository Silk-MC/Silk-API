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

package pers.saikel0rado1iu.silk.util;

import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.math.BigDecimal;

/**
 * <p><b style="color:FFC800"><font size="+1">有关数学的部分实用方法，主要用于解决部分复杂的赋值、判断问题</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface MathUtil {
	/**
	 * 比较两个浮点数的值
	 *
	 * @return -1 为小于；0 为等于；1 为大于
	 */
	@SilkApi
	static int compareFloat(double f1, double f2) {
		return new BigDecimal(String.valueOf(f1)).compareTo(new BigDecimal(String.valueOf(f2)));
	}
}
