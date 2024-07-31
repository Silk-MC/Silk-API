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

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import pers.saikel0rado1iu.silk.magiccube.PhysicsConnectingBlock;
import pers.saikel0rado1iu.silk.test.magiccube.entity.PhysicsConnectingBlockEntityTest;

/**
 * Test {@link PhysicsConnectingBlock}
 */
public final class PhysicsConnectingBlockTest extends PhysicsConnectingBlock {
	/**
	 * 方块解编码器
	 */
	public static final MapCodec<PhysicsConnectingBlockTest> CODEC = createCodec(PhysicsConnectingBlockTest::new);
	
	/**
	 * @param settings 方块设置
	 */
	public PhysicsConnectingBlockTest(Settings settings) {
		super(0.5F, settings);
	}
	
	/**
	 * 是否可放置
	 *
	 * @param state 检测方块状态
	 * @return 如果为 {@code true} 则此连接块可以放置在这些完整方块上
	 */
	@Override
	public boolean isPlaceable(BlockState state) {
		return true;
	}
	
	/**
	 * 是否可连接
	 *
	 * @param state 检测方块状态
	 * @return 如果为 {@code true} 此方块会将这些方块识别为可连接的一部分
	 */
	@Override
	public boolean isConnectable(BlockState state) {
		return state.isOf(this);
	}
	
	@Override
	public BlockState withConnectionProperties(BlockView world, BlockPos pos, BlockState state) {
		placeBlock(world, pos, (p, s) -> {
		});
		return super.withConnectionProperties(world, pos, state);
	}
	
	@Override
	protected MapCodec<? extends ConnectingBlock> getCodec() {
		return CODEC;
	}
	
	@Override
	public @NotNull BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PhysicsConnectingBlockEntityTest(pos, state);
	}
}
