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

package pers.saikel0rado1iu.silk.ropestick.tool;

import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">调整视场角</h2>
 * 物品的视场角缩放通用数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface AdjustFov {
	/**
	 * 默认视场角缩放
	 */
	float DEFAULT_FOV_SCALING = 1.2F;
	/**
	 * 暗角纹理
	 */
	Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
	/**
	 * 南瓜头模糊纹理
	 */
	Identifier PUMPKIN_BLUR = new Identifier("textures/misc/pumpkinblur.png");
	/**
	 * 望远镜镜头纹理
	 */
	Identifier SPYGLASS_SCOPE = new Identifier("textures/misc/spyglass_scope.png");
	/**
	 * 细雪轮廓纹理
	 */
	Identifier POWDER_SNOW_OUTLINE = new Identifier("textures/misc/powder_snow_outline.png");
	
	/**
	 * 是否只在第一人称进行缩放
	 *
	 * @return 是否可以进行缩放
	 */
	boolean onlyFirstPerson();
	
	/**
	 * 抬头显示叠加层
	 *
	 * @return 叠加层的标识符，如果为 {@link Optional#empty()} 则不显示叠加层
	 */
	Optional<Identifier> hudOverlay();
	
	/**
	 * 是否可以拉伸抬头显示
	 *
	 * @return 是否需要拉伸
	 */
	boolean canStretchHud();
	
	/**
	 * 视场角缩放
	 *
	 * @return 视场角缩放倍数，视场角缩放倍数，&gt; 1 则为放大，0 &lt; x &lt; 1 则为缩小
	 */
	float fovScaling();
	
	/**
	 * 视场角缩放倍数
	 *
	 * @return 经过修正的缩放倍数。将倍数的分子分母对调，使得倍数可以正确应用于调整方法
	 */
	default float fovScalingMultiple() {
		return 1 / fovScaling();
	}
}
