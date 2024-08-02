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

/**
 * <h2 style="color:FFC800">选项值索引</h2>
 * 设置选项内储存的数据的索引
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public enum OptionValueIndex {
	/**
	 * 当前值索引
	 */
	CURRENT(0),
	/**
	 * 默认值索引
	 */
	DEFAULT(1),
	/**
	 * 最小值索引
	 */
	MIN(2),
	/**
	 * 最大值索引
	 */
	MAX(3);
	
	private final int index;
	
	OptionValueIndex(int index) {
		this.index = index;
	}
	
	/**
	 * 获取索引
	 *
	 * @return 索引
	 */
	public int index() {
		return index;
	}
	
	/**
	 * 获取值的键，值的键为该索引的全小写名称
	 *
	 * @return 值的键
	 */
	public String key() {
		return name().toLowerCase();
	}
}
