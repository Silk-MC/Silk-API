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

package pers.saikel0rado1iu.silk.generate.world.chunk;

import net.minecraft.world.gen.chunk.GenerationShapeConfig;

/**
 * <h2 style="color:FFC800">区块生成形状配置</h2>
 * 访问加宽的 {@link GenerationShapeConfig}
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class ChunkGenerationShapeConfig extends GenerationShapeConfig {
	/**
	 * 加宽的 {@link GenerationShapeConfig#SURFACE}
	 */
	public static final GenerationShapeConfig SURFACE = GenerationShapeConfig.SURFACE;
	/**
	 * 加宽的 {@link GenerationShapeConfig#NETHER}
	 */
	public static final GenerationShapeConfig NETHER = GenerationShapeConfig.NETHER;
	/**
	 * 加宽的 {@link GenerationShapeConfig#END}
	 */
	public static final GenerationShapeConfig END = GenerationShapeConfig.END;
	/**
	 * 加宽的 {@link GenerationShapeConfig#CAVES}
	 */
	public static final GenerationShapeConfig CAVES = GenerationShapeConfig.CAVES;
	/**
	 * 加宽的 {@link GenerationShapeConfig#FLOATING_ISLANDS}
	 */
	public static final GenerationShapeConfig FLOATING_ISLANDS = GenerationShapeConfig.FLOATING_ISLANDS;
	
	/**
	 * @param minimumY       最小 Y 轴
	 * @param height         高度
	 * @param horizontalSize 水平大小
	 * @param verticalSize   垂直大小
	 */
	public ChunkGenerationShapeConfig(int minimumY, int height, int horizontalSize, int verticalSize) {
		super(minimumY, height, horizontalSize, verticalSize);
	}
}
