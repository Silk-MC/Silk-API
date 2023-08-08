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

package pers.saikeloradoliu.silk.util;

import java.util.concurrent.TimeUnit;

/**
 * <p><b style="color:FFC800"><font size="+1">作用</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 */
public interface TickUtil {
	int TPS = 20;
	int MC_TIME_PASSES_MAGNIFICATION = 72;
	
	/**
	 * 为 {@link TickUtil#getTick(TimeType, TimeUnit, float)} 的简化版本，以自然秒作为单位
	 */
	static int getTick(float naturalSec) {
		return (int) (naturalSec * TPS);
	}
	
	static int getTick(TimeType type, TimeUnit unit, float time) {
		return switch (type) {
			case NATURAL -> getNaturalTick(unit, time);
			case GAME -> getGameTick(unit, time);
		};
	}
	
	private static int getNaturalTick(TimeUnit unit, float time) {
		return Math.max(1, (int) (time * getTimeRatio(unit)));
	}
	
	private static int getGameTick(TimeUnit unit, float time) {
		return Math.max(1, getNaturalTick(unit, time) / MC_TIME_PASSES_MAGNIFICATION);
	}
	
	/**
	 * 为 {@link TickUtil#getTime(TimeType, TimeUnit, float)} 的简化版本，以自然秒作为返回值
	 *
	 * @return 自然秒
	 */
	static float getTime(int tick) {
		return (float) tick / TPS;
	}
	
	static float getTime(TimeType type, TimeUnit unit, float tick) {
		return switch (type) {
			case NATURAL -> getNaturalTime(unit, tick);
			case GAME -> getGameTime(unit, tick);
		};
	}
	
	private static float getNaturalTime(TimeUnit unit, float tick) {
		return Math.max(1, (float) (tick / getTimeRatio(unit)));
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
	
	enum TimeType {
		NATURAL, GAME
	}
}
