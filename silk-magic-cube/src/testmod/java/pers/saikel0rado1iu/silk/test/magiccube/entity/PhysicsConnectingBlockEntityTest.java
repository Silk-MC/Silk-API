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

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import pers.saikel0rado1iu.silk.api.magiccube.entity.PhysicsConnectingBlockEntity;
import pers.saikel0rado1iu.silk.test.magiccube.BlockEntityTypes;

/**
 * Test {@link PhysicsConnectingBlockEntity}
 */
public class PhysicsConnectingBlockEntityTest extends PhysicsConnectingBlockEntity {
	/**
	 * @param pos   方块位置
	 * @param state 方块状态
	 */
	public PhysicsConnectingBlockEntityTest(BlockPos pos, BlockState state) {
		super(BlockEntityTypes.TEST_BLOCK_ENTITY, pos, state);
	}
}
