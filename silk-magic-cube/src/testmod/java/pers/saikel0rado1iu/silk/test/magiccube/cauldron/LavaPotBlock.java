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
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * LavaPotBlock
 */
public final class LavaPotBlock extends AbstractCauldronBlock {
	/**
	 * codec
	 */
	public static final MapCodec<LavaPotBlock> CODEC = createCodec(LavaPotBlock::new);
	
	/**
	 * @param settings 方块设置
	 */
	public LavaPotBlock(Settings settings) {
		super(settings, CauldronLikeBehaviorTest.INSTANCE.lavaBehavior);
	}
	
	@Override
	protected MapCodec<? extends AbstractCauldronBlock> getCodec() {
		return CODEC;
	}
	
	@Override
	protected double getFluidHeight(BlockState state) {
		return 0.9375;
	}
	
	@Override
	public boolean isFull(BlockState state) {
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (isEntityTouchingFluid(state, pos, entity)) entity.setOnFireFromLava();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return LeveledCauldronBlock.MAX_LEVEL;
	}
}
