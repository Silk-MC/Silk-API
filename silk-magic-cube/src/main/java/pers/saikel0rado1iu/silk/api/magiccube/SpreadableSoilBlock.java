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

package pers.saikel0rado1iu.silk.api.magiccube;

import net.minecraft.block.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

/**
 * <h2 style="color:FFC800">可传播土块</h2>
 * 可以传播的泥土类变种方块，具有覆雪的方块状态
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class SpreadableSoilBlock extends SpreadableBlock {
	protected SpreadableSoilBlock(Settings settings) {
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
	
	protected Vec3i getSpreadableRange() {
		return new Vec3i(3, 5, 3);
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
		if (world.getLightLevel(pos.up()) > Block.REDRAW_ON_MAIN_THREAD) {
			Vec3i range = getSpreadableRange();
			Vec3i halfRange = new Vec3i(Math.round(range.getX() / 2F), Math.round(range.getY() / 2F), Math.round(range.getZ() / 2F));
			for (int i = 0; i < getSpreadableOdds(); i++) {
				BlockPos blockPos = pos.add(random.nextInt(range.getX()) - halfRange.getX(), random.nextInt(range.getY()) - halfRange.getY(), random.nextInt(range.getZ()) - halfRange.getZ());
				if (!canSpread(getSpreadableBlockState(world, blockPos), world, blockPos)) continue;
				world.setBlockState(getSpreadableOffset(blockPos), getSpreadableBlockState(world, blockPos));
			}
		}
	}
}
