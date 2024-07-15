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

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

/**
 * EmptyPotBlock
 */
public final class EmptyPotBlock extends AbstractCauldronBlock {
	/**
	 * 方块解编码器
	 */
	public static final MapCodec<EmptyPotBlock> CODEC = createCodec(EmptyPotBlock::new);
	private static final float FILL_WITH_RAIN_CHANCE = 0.05F;
	private static final float FILL_WITH_SNOW_CHANCE = 0.1F;
	
	/**
	 * @param settings 方块设置
	 */
	public EmptyPotBlock(Settings settings) {
		super(settings, CauldronLikeBehaviorTest.INSTANCE.emptyBehavior);
	}
	
	private static boolean canFillWithPrecipitation(World world, Biome.Precipitation precipitation) {
		if (precipitation == Biome.Precipitation.RAIN)
			return world.getRandom().nextFloat() < FILL_WITH_RAIN_CHANCE;
		if (precipitation == Biome.Precipitation.SNOW)
			return world.getRandom().nextFloat() < FILL_WITH_SNOW_CHANCE;
		return false;
	}
	
	@Override
	protected MapCodec<? extends AbstractCauldronBlock> getCodec() {
		return CODEC;
	}
	
	@Override
	public boolean isFull(BlockState state) {
		return false;
	}
	
	@Override
	public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
		if (!canFillWithPrecipitation(world, precipitation)) return;
		if (precipitation == Biome.Precipitation.RAIN) {
			world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState());
			world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
		} else if (precipitation == Biome.Precipitation.SNOW) {
			world.setBlockState(pos, Blocks.POWDER_SNOW_CAULDRON.getDefaultState());
			world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
		}
	}
	
	@Override
	protected boolean canBeFilledByDripstone(Fluid fluid) {
		return true;
	}
	
	@Override
	protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
		if (fluid == Fluids.WATER) {
			BlockState blockState = Blocks.WATER_CAULDRON.getDefaultState();
			world.setBlockState(pos, blockState);
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
			world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS_WATER_INTO_CAULDRON, pos, 0);
		} else if (fluid == Fluids.LAVA) {
			BlockState blockState = Blocks.LAVA_CAULDRON.getDefaultState();
			world.setBlockState(pos, blockState);
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
			world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS_LAVA_INTO_CAULDRON, pos, 0);
		}
	}
}
