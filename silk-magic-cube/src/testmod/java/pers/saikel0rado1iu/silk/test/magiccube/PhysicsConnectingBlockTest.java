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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import pers.saikel0rado1iu.silk.magiccube.PhysicsConnectingBlock;
import pers.saikel0rado1iu.silk.test.magiccube.entity.PhysicsConnectingBlockEntityTest;

import java.util.Set;

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
	 * 可放置方块，此方块可以放在这些方法的上方
	 *
	 * @return 如果为 {@link Set#of()} 则此连接块可以放置在任何完整方块上
	 */
	@Override
	public Set<Block> placeableBlocks() {
		return Set.of();
	}
	
	/**
	 * 可连接方块，此方块会将这些方块识别为可连接的一部分
	 *
	 * @return 如果为 {@link Set#of()} 则此连接块只能连接自身
	 */
	@Override
	public Set<Block> connectedBlocks() {
		return Set.of();
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
