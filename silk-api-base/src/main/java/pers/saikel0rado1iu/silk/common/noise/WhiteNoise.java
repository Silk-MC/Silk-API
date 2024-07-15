package pers.saikel0rado1iu.silk.common.noise;

import pers.saikel0rado1iu.silk.common.util.NoiseUtil;

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