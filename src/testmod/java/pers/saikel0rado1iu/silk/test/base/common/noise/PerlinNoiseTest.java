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

package pers.saikel0rado1iu.silk.test.base.common.noise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.common.noise.PerlinNoise;

import java.time.Duration;
import java.time.Instant;

/**
 * Test {@link PerlinNoise}
 */
public interface PerlinNoiseTest {
	/**
	 * 日志
	 */
	Logger LOGGER = LoggerFactory.getLogger("SilkApiBase/TestingPerlinNoise");
	
	/**
	 * 测试
	 */
	static void test() {
		Instant start = Instant.now();
		PerlinNoise perlinNoise1 = new PerlinNoise(10, 10, 0, 2, 1, 1, 0)
				.scale(50, 50);
		LOGGER.info("噪声生成时间：{} 毫秒", Duration.between(start, Instant.now()).toMillis());
		LOGGER.info("更为平滑的噪声\n{}", perlinNoise1);
		start = Instant.now();
		PerlinNoise perlinNoise2 = new PerlinNoise(50, 50, 1, 10, 8, 0, 0);
		LOGGER.info("方法执行时间：{} 毫秒", Duration.between(start, Instant.now()).toMillis());
		LOGGER.info("更为嘈杂的噪声\n{}", perlinNoise2);
	}
}
