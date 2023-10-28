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

package pers.saikel0rado1iu.silk.api.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <p><b style="color:FFC800"><font size="+1">可传播土块，可以传播的泥土类变种方块，具有覆雪的方块状态</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SpreadableSoilBlock extends SpreadableBlock {
	public SpreadableSoilBlock(Settings settings) {
		super(settings);
	}
	
	protected boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isOf(Blocks.SNOW) && blockState.get(SnowBlock.LAYERS) == 1) {
			return true;
		} else if (blockState.getFluidState().getLevel() == 8) {
			return false;
		} else {
			int i = ChunkLightProvider.getRealisticOpacity(world, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(world, blockPos));
			return i < world.getMaxLightLevel();
		}
	}
	
	protected boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		return canSurvive(state, world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
	}
	
	protected BlockState getSpreadableBlockState() {
		return getDefaultState();
	}
	
	protected int getSpreadableOdds() {
		return 4;
	}
	
	protected int getSpreadableRange() {
		return 3;
	}
	
	protected BlockPos getSpreadableOffset(BlockPos original) {
		return original;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.getBlockState(pos.up()).isIn(BlockTags.DIRT) || !world.getFluidState(pos.up()).isOf(Fluids.EMPTY)) {
			world.setBlockState(pos, Blocks.DIRT.getDefaultState());
		} else {
			int range = getSpreadableRange();
			int halfRange = getSpreadableRange() / 2;
			for (int i = 0; i < getSpreadableOdds(); i++) {
				BlockPos blockPos = pos.add(random.nextInt(range) - halfRange, random.nextInt(range) - halfRange, random.nextInt(range) - halfRange);
				if (blockPos != pos && (world.getBlockState(blockPos).isOf(Blocks.DIRT)
						|| world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK))
						&& canSpread(getSpreadableBlockState(), world, blockPos)) {
					world.setBlockState(getSpreadableOffset(blockPos), getSpreadableBlockState());
				}
			}
		}
	}
}
