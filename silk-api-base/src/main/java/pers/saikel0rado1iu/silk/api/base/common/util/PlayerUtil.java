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

package pers.saikel0rado1iu.silk.api.base.common.util;

/**
 * <h2 style="color:FFC800">玩家实用工具</h2>
 * 有关玩家数据的部分数值与实用方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface PlayerUtil {
	/**
	 * 玩家生命值
	 */
	int HEALTH = 20;
	/**
	 * 玩家饥饿值
	 */
	int HUNGER = 20;
	/**
	 * 玩家饱和值
	 */
	int SATURATION = 20;
	
	/**
	 * 获取生命值比例
	 *
	 * @param health 生命值
	 * @return 比例
	 */
	static float getHealthRatio(float health) {
		return HEALTH / health;
	}
	
	/**
	 * 获取饥饿值比例
	 *
	 * @param hunger 饥饿值
	 * @return 比例
	 */
	static int getHungerRatio(float hunger) {
		return (int) (HUNGER / hunger);
	}
	
	/**
	 * 获取饱和值比例
	 *
	 * @param saturation 饱和值
	 * @return 比例
	 */
	static float getSaturationRatio(float saturation) {
		return SATURATION / saturation;
	}
}
