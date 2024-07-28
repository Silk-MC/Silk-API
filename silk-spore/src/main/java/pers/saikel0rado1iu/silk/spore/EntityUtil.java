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

package pers.saikel0rado1iu.silk.spore;

/**
 * <h2 style="color:FFC800">实体实用工具</h2>
 * 跟实体有关的相关方法与静态字段
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface EntityUtil {
	/**
	 * 实体位置偏移
	 */
	float POS_SHIFTING = 0.5F;
	/**
	 * 发射物碰撞箱大小
	 */
	float PROJECTILE_BOX = 0.25F;
	/**
	 * 发射物存在范围
	 */
	int PROJECTILE_RANGE = 4;
	/**
	 * 发射物更新速率
	 */
	int PROJECTILE_UPDATE_RATE = 20;
}
