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

package pers.saikel0rado1iu.silk.api.base.common.noise;

import pers.saikel0rado1iu.silk.api.base.common.util.NoiseUtil;

import java.util.Random;

/**
 * <h2 style="color:FFC800">白噪声</h2>
 * 用于生成二维白噪声
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record WhiteNoise(int width, int height, double intensity, int seed, double[][] noiseMap) {
	/**
	 * 构造一个 WhiteNoise 实例。
	 *
	 * @param seed      随机数种子
	 * @param intensity 噪声强度
	 * @param width     噪声图像的宽度
	 * @param height    噪声图像的高度
	 */
	public WhiteNoise(int width, int height, double intensity, int seed) {
		this(width, height, intensity, seed, new double[height][width]);
		generate();
	}
	
	/**
	 * 生成二维白噪声数组。
	 */
	private void generate() {
		Random random = new Random(seed);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				noiseMap[i][j] = random.nextDouble() * intensity;
			}
		}
	}
	
	/**
	 * 将噪声数组转换为字符串，用于显示噪声图。
	 *
	 * @return 字符图像表示的白噪声
	 */
	@Override
	public String toString() {
		return NoiseUtil.getPrintString(noiseMap);
	}
}