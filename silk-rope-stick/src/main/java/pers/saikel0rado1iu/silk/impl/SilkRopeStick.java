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

package pers.saikel0rado1iu.silk.impl;

import org.jetbrains.annotations.NotNull;

/**
 * <h2 style="color:FFC800">Silk API: Rope, Stick</h2>
 * Silk API: Rope, Stick 的模组数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface SilkRopeStick extends SilkApi {
	/**
	 * 提供实例
	 *
	 * @return 模组数据实例
	 */
	static SilkRopeStick getInstance() {
		return new SilkRopeStick() {
		};
	}
	
	@Override
	default @NotNull String id() {
		return "silk-rope-stick";
	}
}
