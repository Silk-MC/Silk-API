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

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.world.World;

/**
 * <h2 style="color:FFC800">粒子实用工具</h2>
 * 有关粒子的所有实用方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ParticleUtil {
	/**
	 * 添加具有颜色叠加层的状态效果粒子
	 *
	 * @param world  生成世界
	 * @param effect 状态效果
	 * @param x      方块位置 X
	 * @param y      方块位置 Y
	 * @param z      方块位置 Z
	 */
	static void addEffectParticle(World world, StatusEffect effect, double x, double y, double z) {
		world.addParticle(new AreaEffectCloudEntity(world, 0, 0, 0).getParticleType(), x, y, z,
				(effect.getColor() >> 16 & 255) / 255.0,
				(effect.getColor() >> 8 & 255) / 255.0,
				(effect.getColor() & 255) / 255.0);
	}
}
