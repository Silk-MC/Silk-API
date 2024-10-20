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

package pers.saikel0rado1iu.silk.api.ropestick.tool;

/**
 * <h2 style="color:FFC800">修改移动</h2>
 * 物品的移动速度修改通用数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface ModifyMove {
	/**
	 * 默认移动速度倍速
	 */
	float DEFAULT_SPEED_MULTIPLE = 0.2F;
	
	/**
	 * 移动速度倍率
	 *
	 * @return 相较于行走时的移动速度倍率
	 */
	float moveSpeedMultiple();
}
