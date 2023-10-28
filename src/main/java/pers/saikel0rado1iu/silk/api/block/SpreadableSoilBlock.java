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

import net.minecraft.block.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
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
		if (blockState.isOf(Blocks.SNOW) && blockState.get(SnowBlock.LAYERS) == Block.NOTIFY_NEIGHBORS) return true;
		if (blockState.getFluidState().getLevel() == Block.REDRAW_ON_MAIN_THREAD) return false;
		int level = ChunkLightProvider.getRealisticOpacity(world, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(world, blockPos));
		return level < world.getMaxLightLevel();
	}
	
	protected boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos).isOf(Blocks.DIRT) && canSurvive(state, world, pos) && !world.getFluidState(pos.up()).isIn(FluidTags.WATER);
	}
	
	protected BlockState getSpreadableBlockState(ServerWorld world, BlockPos pos) {
		return getDefaultState().with(SNOWY, world.getBlockState(pos.up()).isOf(Blocks.SNOW));
	}
	
	/**
	 * @return 退化方块状态，方块无法存活后退化的方块
	 */
	protected BlockState getDegeneratedBlockState() {
		return Blocks.DIRT.getDefaultState();
	}
	
	protected int getSpreadableOdds() {
		return 4;
	}
	
	protected Position getSpreadableRange() {
		return new Position() {
			@Override
			public double getX() {
				return 3;
			}
			
			@Override
			public double getY() {
				return 5;
			}
			
			@Override
			public double getZ() {
				return 3;
			}
		};
	}
	
	protected BlockPos getSpreadableOffset(BlockPos original) {
		return original;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!canSurvive(state, world, pos)) {
			world.setBlockState(pos, getDegeneratedBlockState());
			return;
		}
		if (world.getLightLevel(pos.up()) >= Block.REDRAW_ON_MAIN_THREAD + 1) {
			Position range = getSpreadableRange();
			Position halfRange = new Position() {
				@Override
				public double getX() {
					return Math.floor(range.getX() / 2);
				}
				
				@Override
				public double getY() {
					return Math.floor(range.getY() / 2);
				}
				
				@Override
				public double getZ() {
					return Math.floor(range.getZ() / 2);
				}
			};
			for (int i = 0; i < getSpreadableOdds(); i++) {
				BlockPos blockPos = pos.add((int) (random.nextInt((int) range.getX()) - halfRange.getX()), (int) (random.nextInt((int) range.getY()) - halfRange.getY()), (int) (random.nextInt((int) range.getZ()) - halfRange.getZ()));
				if (!canSpread(getSpreadableBlockState(world, blockPos), world, blockPos)) continue;
				world.setBlockState(getSpreadableOffset(blockPos), getSpreadableBlockState(world, blockPos));
			}
		}
	}
}
