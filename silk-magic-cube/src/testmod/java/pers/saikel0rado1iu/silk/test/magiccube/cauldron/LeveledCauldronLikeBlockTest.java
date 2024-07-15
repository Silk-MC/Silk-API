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

package pers.saikel0rado1iu.silk.test.magiccube.cauldron;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import pers.saikel0rado1iu.silk.magiccube.cauldron.LeveledCauldronLikeBlock;

/**
 * Test {@link LeveledCauldronLikeBlock}
 */
public final class LeveledCauldronLikeBlockTest extends LeveledCauldronLikeBlock {
	/**
	 * @param precipitation precipitation
	 * @param behaviorMap   behaviorMap
	 * @param settings      settings
	 */
	public LeveledCauldronLikeBlockTest(Biome.Precipitation precipitation, CauldronBehavior.CauldronBehaviorMap behaviorMap, Settings settings) {
		super(precipitation, behaviorMap, settings);
	}
	
	/**
	 * 液位属性
	 *
	 * @return 整数属性
	 */
	@Override
	public IntProperty level() {
		return LeveledCauldronBlock.LEVEL;
	}
	
	/**
	 * 递减液位方法
	 *
	 * @param world 存档世界
	 * @param state 方块状态
	 * @param pos   方块位置
	 */
	@Override
	public void decrementFluidLevel(World world, BlockState state, BlockPos pos) {
		int level = state.get(LEVEL) - 1;
		BlockState blockState = level == 0 ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, level);
		world.setBlockState(pos, blockState);
		world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
	}
}
