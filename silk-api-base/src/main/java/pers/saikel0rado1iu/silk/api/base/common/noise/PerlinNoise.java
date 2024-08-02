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
 * <h2 style="color:FFC800">柏林噪声</h2>
 * 用于生成二维柏林噪声
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record PerlinNoise(int width, int height, double frequency, double amplitude, int octaves, double persistence, long seed, double[][] noiseMap) {
	/**
	 * 初始化 {@link PerlinNoise} 对象
	 *
	 * @param width       噪声数组的宽度
	 * @param height      噪声数组的高度
	 * @param frequency   频率
	 * @param amplitude   振幅
	 * @param octaves     八度
	 * @param persistence 持续度
	 * @param seed        随机种子
	 */
	public PerlinNoise(int width, int height, double frequency, double amplitude, int octaves, double persistence, long seed) {
		this(width, height, frequency, amplitude, octaves, persistence, seed, new double[height][width]);
		generate();
	}
	
	/**
	 * 生成平滑噪声。
	 *
	 * @param random 随机数生成器对象，用于生成随机数。
	 * @return 返回生成的平滑噪声值。
	 */
	private static double smoothNoise(Random random) {
		double corners = (random.nextDouble() - 0.5) * 2;
		double sides = (random.nextDouble() - 0.5) * 2;
		double center = (random.nextDouble() - 0.5) * 2;
		double edges = (corners + sides) / 2;
		return (center + edges) / 2;
	}
	
	/**
	 * 生成插值噪声
	 *
	 * @param x      水平方向的位置
	 * @param y      垂直方向的位置
	 * @param random 随机数生成器
	 * @return 插值噪声值
	 */
	private static double interpolatedNoise(double x, double y, Random random) {
		int intX = (int) x;
		int intY = (int) y;
		double fracX = x - intX;
		double fracY = y - intY;
		
		double v1 = smoothNoise(random);
		double v2 = smoothNoise(random);
		double v3 = smoothNoise(random);
		double v4 = smoothNoise(random);
		
		double i1 = interpolate(v1, v2, fracX);
		double i2 = interpolate(v3, v4, fracX);
		
		return interpolate(i1, i2, fracY);
	}
	
	/**
	 * 执行线性插值以获取给定值v在a和b之间的插值。
	 *
	 * @param a 插值范围的起始值
	 * @param b 插值范围的结束值
	 * @param v 要插值的值，应该在 0 到 1 之间
	 * @return 在 a 和 b 之间进行插值后得到的结果
	 */
	private static double interpolate(double a, double b, double v) {
		double ft = v * Math.PI;
		double f = (1 - Math.cos(ft)) * 0.5;
		return a * (1 - f) + b * f;
	}
	
	/**
	 * 获取原始噪声数组中指定样本位置的插值值。
	 *
	 * @param originalNoise 原始噪声
	 * @param sampleX       样本的水平位置
	 * @param sampleY       样本的垂直位置
	 * @return 插值得到的样本值
	 */
	private static double getInterpolatedValue(double[][] originalNoise, double sampleX, double sampleY) {
		int x0 = (int) sampleX;
		int x1 = Math.min(x0 + 1, originalNoise[0].length - 1);
		int y0 = (int) sampleY;
		int y1 = Math.min(y0 + 1, originalNoise.length - 1);
		
		double dx = sampleX - x0;
		double dy = sampleY - y0;
		
		double topInterpolation = linearInterpolate(originalNoise[y0][x0], originalNoise[y0][x1], dx);
		double bottomInterpolation = linearInterpolate(originalNoise[y1][x0], originalNoise[y1][x1], dx);
		return linearInterpolate(topInterpolation, bottomInterpolation, dy);
	}
	
	/**
	 * 双线性插值函数。
	 *
	 * @param a 第一个样本值
	 * @param b 第二个样本值
	 * @param t 插值系数
	 * @return 插值结果
	 */
	private static double linearInterpolate(double a, double b, double t) {
		return a * (1 - t) + b * t;
	}
	
	/**
	 * 生成二维 Perlin 噪声数组
	 */
	private void generate() {
		Random random = new Random(seed);
		
		// Generate noise
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double total = 0;
				double frequency = this.frequency;
				double amplitude = this.amplitude;
				
				for (int i = 0; i < octaves; i++) {
					total += interpolatedNoise(x * frequency, y * frequency, random) * amplitude;
					frequency *= 2;
					amplitude *= persistence;
				}
				
				noiseMap[y][x] = total;
			}
		}
	}
	
	/**
	 * 将原始噪声数组按照指定的宽度和高度放大，并使用双线性插值进行平滑处理。
	 * 返回一个新的 {@link PerlinNoise} 对象。
	 *
	 * @param newWidth  新的宽度
	 * @param newHeight 新的高度
	 * @return 放大后的 {@link PerlinNoise} 对象
	 */
	public PerlinNoise scale(int newWidth, int newHeight) {
		double[][] scaledNoise = new double[newHeight][newWidth];
		
		double scaleX = (double) noiseMap[0].length / newWidth;
		double scaleY = (double) noiseMap.length / newHeight;
		
		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				double sampleX = x * scaleX;
				double sampleY = y * scaleY;
				double interpolatedValue = getInterpolatedValue(noiseMap, sampleX, sampleY);
				scaledNoise[y][x] = interpolatedValue;
			}
		}
		
		return new PerlinNoise(newWidth, newHeight, frequency, amplitude, octaves, persistence, seed, scaledNoise);
	}
	
	/**
	 * 将噪声数组转换为字符串，用于显示噪声图。
	 *
	 * @return 显示噪声图的字符串
	 */
	public String toString() {
		return NoiseUtil.getPrintString(noiseMap);
	}
}
