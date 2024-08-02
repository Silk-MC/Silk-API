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

package pers.saikel0rado1iu.silk.test.magiccube.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import pers.saikel0rado1iu.silk.api.spore.TntLikeEntity;
import pers.saikel0rado1iu.silk.test.magiccube.Blocks;

import java.util.Optional;

/**
 * Test {@link TntLikeEntity}
 */
public final class TntLikeEntityTest extends TntLikeEntity {
	TntLikeEntityTest(EntityType<?> type, World world) {
		super(type, world, Optional.empty());
	}
	
	/**
	 * @param world   world
	 * @param x       x
	 * @param y       y
	 * @param z       z
	 * @param igniter igniter
	 */
	public TntLikeEntityTest(World world, double x, double y, double z, Optional<LivingEntity> igniter) {
		super(EntityTypes.TNT_LIKE_ENTITY, world, x, y, z, igniter);
	}
	
	/**
	 * 引信刻数
	 *
	 * @return 引燃 TNT 所需的刻数
	 */
	@Override
	public int fuseTick() {
		return 80;
	}
	
	/**
	 * TNT 方块
	 *
	 * @return 此实体所依赖的 TNT 块
	 */
	@Override
	public Block tntBlock() {
		return Blocks.TNT_LIKE_BLOCK;
	}
}
