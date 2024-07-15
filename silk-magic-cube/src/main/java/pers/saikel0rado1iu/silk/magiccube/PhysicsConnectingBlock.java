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

package pers.saikel0rado1iu.silk.magiccube;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.common.collect.MultiTreeNode;
import pers.saikel0rado1iu.silk.common.collect.MultiWayTree;
import pers.saikel0rado1iu.silk.magiccube.entity.PhysicsConnectingBlockEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <h2 style="color:FFC800">物理连接块</h2>
 * 需要同时注册 {@link PhysicsConnectingBlockEntity}<br>
 * 具有基础的连接与破坏效果，但是具有更符合物理特性的判断算法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class PhysicsConnectingBlock extends ConnectingBlock implements BlockEntityProvider {
	/**
	 * 方块的所有连接冲突偏移距离
	 */
	public static final Vec3i[][] CONFLICT_OFFSETS = {
			{new Vec3i(1, 0, 0), new Vec3i(0, 1, 0), new Vec3i(1, 1, 0)},
			{new Vec3i(-1, 0, 0), new Vec3i(0, 1, 0), new Vec3i(-1, 1, 0)},
			{new Vec3i(1, 0, 0), new Vec3i(0, -1, 0), new Vec3i(1, -1, 0)},
			{new Vec3i(-1, 0, 0), new Vec3i(0, -1, 0), new Vec3i(-1, -1, 0)},
			{new Vec3i(0, 0, 1), new Vec3i(0, 1, 0), new Vec3i(0, 1, 1)},
			{new Vec3i(0, 0, -1), new Vec3i(0, 1, 0), new Vec3i(0, 1, -1)},
			{new Vec3i(0, 0, 1), new Vec3i(0, -1, 0), new Vec3i(0, -1, 1)},
			{new Vec3i(0, 0, -1), new Vec3i(0, -1, 0), new Vec3i(0, -1, -1)},
			{new Vec3i(1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 1)},
			{new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(-1, 0, 1)},
			{new Vec3i(1, 0, 0), new Vec3i(0, 0, -1), new Vec3i(1, 0, -1)},
			{new Vec3i(-1, 0, 0), new Vec3i(0, 0, -1), new Vec3i(-1, 0, -1)}
	};
	
	protected PhysicsConnectingBlock(float radius, Settings settings) {
		super(radius, settings);
		setDefaultState(getDefaultState()
				.with(NORTH, false)
				.with(EAST, false)
				.with(SOUTH, false)
				.with(WEST, false)
				.with(UP, false)
				.with(DOWN, false));
	}
	
	/**
	 * 可放置方块，此方块可以放在这些方法的上方
	 *
	 * @return 如果为 {@link Set#of()} 则此连接块可以放置在任何完整方块上
	 */
	public abstract Set<Block> placeableBlocks();
	
	/**
	 * 可连接方块，此方块会将这些方块识别为可连接的一部分
	 *
	 * @return 如果为 {@link Set#of()} 则此连接块只能连接自身
	 */
	public abstract Set<Block> connectedBlocks();
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos downPos = pos.down();
		BlockState downBlock = world.getBlockState(downPos);
		boolean havePlaceableBlock = downBlock.isSideSolidFullSquare(world, downPos, Direction.UP) && (placeableBlocks().isEmpty() || placeableBlocks().contains(downBlock.getBlock()));
		boolean canConnect = false;
		for (Direction direction : Direction.values()) {
			Block block = world.getBlockState(pos.offset(direction)).getBlock();
			HashSet<Block> connectedBlocks = Sets.newHashSet(connectedBlocks());
			connectedBlocks.add(this);
			if (connectedBlocks.contains(block)) {
				if (block instanceof PhysicsConnectingBlock) {
					canConnect = true;
				}
			}
		}
		for (Vec3i[] conflictOffsets : PhysicsConnectingBlock.CONFLICT_OFFSETS) {
			if (Arrays.stream(conflictOffsets).allMatch(vec3i -> world.getBlockState(pos.add(vec3i)).isOf(this))) {
				havePlaceableBlock = canConnect = false;
			}
		}
		return havePlaceableBlock || canConnect;
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		ImmutableSet.Builder<Direction> directionsBuilder = ImmutableSet.builder();
		BlockPos downPos = pos.down();
		BlockState downBlock = world.getBlockState(downPos);
		boolean havePlaceableBlock = downBlock.isSideSolidFullSquare(world, downPos, Direction.UP) && (placeableBlocks().isEmpty() || placeableBlocks().contains(downBlock.getBlock()));
		if (havePlaceableBlock) directionsBuilder.add(Direction.DOWN);
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = pos.offset(direction);
			Block block = world.getBlockState(offsetPos).getBlock();
			HashSet<Block> connectedBlocks = Sets.newHashSet(connectedBlocks());
			connectedBlocks.add(this);
			if (connectedBlocks.contains(block)) directionsBuilder.add(direction);
		}
		BlockState state = super.getPlacementState(ctx);
		for (Direction direction : directionsBuilder.build()) if (state != null) state = state.with(FACING_PROPERTIES.get(direction), true);
		return state;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		BlockState blockState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		boolean isPlaceableBlock = neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite()) && (placeableBlocks().isEmpty() || placeableBlocks().contains(neighborState.getBlock()));
		Block block = world.getBlockState(pos.offset(direction)).getBlock();
		HashSet<Block> connectedBlocks = Sets.newHashSet(connectedBlocks());
		connectedBlocks.add(this);
		if (neighborState.isAir()) blockState = blockState.with(FACING_PROPERTIES.get(direction), false);
		else if (isPlaceableBlock || connectedBlocks.contains(block)) blockState = blockState.with(FACING_PROPERTIES.get(direction), true);
		return blockState;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (world.isClient) return;
		PhysicsConnectingBlockEntity blockEntity = (PhysicsConnectingBlockEntity) world.getBlockEntity(pos);
		if (blockEntity == null) return;
		BlockPos downPos = pos.down();
		BlockState downBlock = world.getBlockState(downPos);
		boolean havePlaceableBlock = downBlock.isSideSolidFullSquare(world, downPos, Direction.UP) && (placeableBlocks().isEmpty() || placeableBlocks().contains(downBlock.getBlock()));
		HashSet<Block> connectedBlocks = Sets.newHashSet(connectedBlocks());
		connectedBlocks.add(this);
		if (havePlaceableBlock && !connectedBlocks.contains(downBlock.getBlock())) {
			blockEntity.setRootTree(new MultiWayTree<>(pos, ""));
			blockEntity.setTreeNode(blockEntity.getRootTree().getRoot());
			return;
		}
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = pos.offset(direction);
			Block block = world.getBlockState(offsetPos).getBlock();
			if (connectedBlocks.contains(block)) {
				if (world.getBlockEntity(offsetPos) instanceof PhysicsConnectingBlockEntity e) {
					blockEntity.setRootTree(e.getRootTree());
					blockEntity.setTreeNode(e.getTreeNode().addChild(pos, ""));
					return;
				}
			}
		}
	}
	
	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockState superBlockState = super.onBreak(world, pos, state, player);
		if (world.isClient) return superBlockState;
		PhysicsConnectingBlockEntity blockEntity = (PhysicsConnectingBlockEntity) world.getBlockEntity(pos);
		if (blockEntity == null || blockEntity.getTreeNode() == null) return superBlockState;
		ImmutableList<MultiTreeNode<BlockPos, String>> nodeList = ImmutableList.copyOf(blockEntity.getTreeNode().getChildren());
		for (MultiTreeNode<BlockPos, String> node : nodeList) {
			BlockPos nodePos = node.getKey();
			world.getBlockState(nodePos).getBlock().onBreak(world, nodePos, world.getBlockState(nodePos), player);
			world.breakBlock(nodePos, true);
			blockEntity.getRootTree().removeChild(blockEntity.getTreeNode(), node);
		}
		return superBlockState;
	}
}
