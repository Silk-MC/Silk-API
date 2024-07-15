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

package pers.saikel0rado1iu.silk.common.util;

/**
 * <h2 style="color:FFC800">噪声实用工具</h2>
 * 有关噪声的所有实用方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface NoiseUtil {
	/**
	 * 噪声字符集
	 */
	char[] NOISE_CHARACTERS = {' ', '░', '▒', '▓', '█'};
	
	/**
	 * 获取噪声字符
	 *
	 * @param noise 噪声值
	 * @return 噪声字符
	 */
	private static char getNoiseCharacter(double noise) {
		final double increment = 1.0 / (NOISE_CHARACTERS.length - 1);
		int index = (int) (noise / increment);
		index = Math.min(Math.max(index, 0), NOISE_CHARACTERS.length - 1);
		return NOISE_CHARACTERS[index];
	}
	
	/**
	 * 将噪声数组以字符图像的形式输出。噪声越高，显示的字符越黑
	 *
	 * @param noiseMap 需要转换的噪声数组
	 * @return 显示噪声图的字符串
	 */
	static String getPrintString(double[][] noiseMap) {
		if (noiseMap == null) return "";
		int width = noiseMap[0].length;
		StringBuilder builder = new StringBuilder();
		
		builder.append("┌").append("─".repeat(Math.max(0, width * 2))).append("┐\n");
		for (double[] row : noiseMap) {
			builder.append("│");
			for (double noise : row) {
				char c = getNoiseCharacter(noise);
				builder.append(c).append(c);
			}
			builder.append("│\n");
		}
		builder.append("└").append("─".repeat(Math.max(0, width * 2))).append("┘\n");
		
		return builder.toString();
	}
}
