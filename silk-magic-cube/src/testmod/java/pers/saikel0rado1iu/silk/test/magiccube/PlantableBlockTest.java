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

package pers.saikel0rado1iu.silk.test.magiccube;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pers.saikel0rado1iu.silk.magiccube.PlantableBlock;

/**
 * Test {@link PlantableBlock}
 */
public final class PlantableBlockTest extends Block implements PlantableBlock {
	/**
	 * @param settings 方块设置
	 */
	public PlantableBlockTest(Settings settings) {
		super(settings);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult actionResult = onUse(world, pos, player, hand, hit, ItemTags.SAPLINGS);
		return actionResult.equals(ActionResult.PASS) ? super.onUse(state, world, pos, player, hand, hit) : actionResult;
	}
}
