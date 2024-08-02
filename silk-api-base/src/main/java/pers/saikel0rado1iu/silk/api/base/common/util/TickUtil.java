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

import java.util.concurrent.TimeUnit;

/**
 * <h2 style="color:FFC800">刻数实用工具</h2>
 * 有关刻的所有实用方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface TickUtil {
	/**
	 * 默认每秒游戏刻数
	 */
	int TPS = 20;
	/**
	 * Minecraft 与现实时间比例倍数
	 */
	int MC_TIME_PASSES_MAGNIFICATION = 72;
	
	/**
	 * 为 {@link TickUtil#getTick(float, Type, TimeUnit)} 的简化版本，以自然秒作为单位
	 *
	 * @param naturalSec 自然秒
	 * @return 游戏刻
	 */
	static int getTick(float naturalSec) {
		return (int) (naturalSec * TPS);
	}
	
	/**
	 * 为 {@link TickUtil#getTime(float, Type, TimeUnit)} 的简化版本，以自然秒作为返回值
	 *
	 * @param tick 游戏刻
	 * @return 自然秒
	 */
	static float getTime(int tick) {
		return (float) tick / TPS;
	}
	
	/**
	 * 获取游戏刻
	 *
	 * @param time 时间
	 * @param type 时间类型
	 * @param unit 时间单位
	 * @return 游戏刻
	 */
	static int getTick(float time, Type type, TimeUnit unit) {
		return switch (type) {
			case NATURAL -> getNaturalTick(unit, time);
			case GAME -> getGameTick(unit, time);
		};
	}
	
	/**
	 * 获取时间
	 *
	 * @param tick 游戏刻
	 * @param type 时间类型
	 * @param unit 时间单位
	 * @return 时间
	 */
	static float getTime(float tick, Type type, TimeUnit unit) {
		return switch (type) {
			case NATURAL -> getNaturalTime(unit, tick);
			case GAME -> getGameTime(unit, tick);
		};
	}
	
	private static int getNaturalTick(TimeUnit unit, float time) {
		return Math.max(0, (int) (time * getTimeRatio(unit)));
	}
	
	private static int getGameTick(TimeUnit unit, float time) {
		return Math.max(0, getNaturalTick(unit, time) / MC_TIME_PASSES_MAGNIFICATION);
	}
	
	private static float getNaturalTime(TimeUnit unit, float tick) {
		return Math.max(0, (float) (tick / getTimeRatio(unit)));
	}
	
	private static float getGameTime(TimeUnit unit, float tick) {
		return getNaturalTime(unit, tick) / MC_TIME_PASSES_MAGNIFICATION;
	}
	
	private static double getTimeRatio(TimeUnit unit) {
		return switch (unit) {
			case DAYS -> TPS * 60 * 60 * 24;
			case HOURS -> TPS * 60 * 60;
			case MINUTES -> TPS * 60;
			case SECONDS -> TPS;
			case MILLISECONDS -> (double) TPS / 1000;
			case MICROSECONDS -> (double) TPS / 1000 / 1000;
			case NANOSECONDS -> (double) TPS / 1000 / 1000 / 1000;
		};
	}
	
	/**
	 * 时间类型
	 */
	enum Type {
		/**
		 * 自然时间
		 */
		NATURAL,
		/**
		 * 自然游戏
		 */
		GAME
	}
}
