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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.base.common.collect.MultiTreeNode;
import pers.saikel0rado1iu.silk.api.base.common.collect.MultiWayTree;
import pers.saikel0rado1iu.silk.api.magiccube.entity.PhysicsConnectingBlockEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;

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
	 * 是否可放置
	 *
	 * @param state 检测方块状态
	 * @return 如果为 {@code true} 则此连接块可以放置在这些完整方块上
	 */
	public abstract boolean isPlaceable(BlockState state);
	
	/**
	 * 是否可连接
	 *
	 * @param state 检测方块状态
	 * @return 如果为 {@code true} 此方块会将这些方块识别为可连接的一部分
	 */
	public abstract boolean isConnectable(BlockState state);
	
	/**
	 * 放置方块方法<br>
	 * 用于在区块生成时使用的放置方法
	 *
	 * @param world  块视图
	 * @param pos    块坐标
	 * @param placer 放置方法
	 */
	public void placeBlock(BlockView world, BlockPos pos, BiConsumer<BlockPos, BlockState> placer) {
		if (!(world instanceof WorldAccess access)) return;
		access.setBlockState(pos, withConnectionProperties(world, pos, getDefaultState()), Block.NOTIFY_LISTENERS);
		setConnectionProperties(world, pos, Optional.ofNullable((PhysicsConnectingBlockEntity) world.getBlockEntity(pos)));
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = pos.offset(direction);
			BlockState offsetState = world.getBlockState(offsetPos);
			if (offsetState.getBlock() instanceof PhysicsConnectingBlock) {
				placer.accept(offsetPos, offsetState.with(FACING_PROPERTIES.get(direction.getOpposite()), true));
			}
		}
	}
	
	/**
	 * 获取具有连接属性的块
	 *
	 * @param world 块视图
	 * @param pos   块坐标
	 * @param state 原始方块状态
	 * @return 带了连接属性的块
	 */
	public BlockState withConnectionProperties(BlockView world, BlockPos pos, BlockState state) {
		ImmutableSet.Builder<Direction> directionsBuilder = ImmutableSet.builder();
		BlockPos downPos = pos.down();
		BlockState downBlock = world.getBlockState(downPos);
		boolean havePlaceableBlock = downBlock.isSideSolidFullSquare(world, downPos, Direction.UP) && isPlaceable(downBlock);
		if (havePlaceableBlock) directionsBuilder.add(Direction.DOWN);
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = pos.offset(direction);
			BlockState offsetState = world.getBlockState(offsetPos);
			if (isConnectable(offsetState)) directionsBuilder.add(direction);
		}
		for (Direction direction : directionsBuilder.build()) if (state != null) state = state.with(FACING_PROPERTIES.get(direction), true);
		return state;
	}
	
	/**
	 * 设置连接属性
	 *
	 * @param world       块视图
	 * @param pos         块坐标
	 * @param blockEntity 方块实体
	 */
	public void setConnectionProperties(BlockView world, BlockPos pos, Optional<PhysicsConnectingBlockEntity> blockEntity) {
		if (blockEntity.isEmpty()) return;
		PhysicsConnectingBlockEntity entity = blockEntity.get();
		if (entity.getRootTree() != null && entity.getTreeNode() != null) return;
		BlockPos downPos = pos.down();
		BlockState downBlock = world.getBlockState(downPos);
		boolean havePlaceableBlock = downBlock.isSideSolidFullSquare(world, downPos, Direction.UP) && isPlaceable(downBlock);
		if (havePlaceableBlock && !isConnectable(downBlock)) {
			entity.setRootTree(new MultiWayTree<>(pos, ""));
			entity.setTreeNode(entity.getRootTree().getRoot());
			return;
		}
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = pos.offset(direction);
			BlockState offsetState = world.getBlockState(offsetPos);
			if (!isConnectable(offsetState)) continue;
			if (!offsetState.isOf(this)) {
				entity.setRootTree(new MultiWayTree<>(offsetPos, ""));
				entity.setTreeNode(entity.getRootTree().addChild(pos, ""));
				return;
			} else if (world.getBlockEntity(offsetPos) instanceof PhysicsConnectingBlockEntity e) {
				entity.setRootTree(e.getRootTree());
				entity.setTreeNode(e.getTreeNode().addChild(pos, ""));
				return;
			}
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return withConnectionProperties(ctx.getWorld(), ctx.getBlockPos(), super.getPlacementState(ctx));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos downPos = pos.down();
		BlockState downBlock = world.getBlockState(downPos);
		boolean havePlaceableBlock = downBlock.isSideSolidFullSquare(world, downPos, Direction.UP) && isPlaceable(downBlock);
		boolean canConnect = false;
		for (Direction direction : Direction.values()) {
			BlockState offsetState = world.getBlockState(pos.offset(direction));
			if (offsetState.isOf(this) || isConnectable(offsetState)) canConnect = true;
		}
		for (Vec3i[] conflictOffsets : PhysicsConnectingBlock.CONFLICT_OFFSETS) {
			if (Arrays.stream(conflictOffsets).allMatch(vec3i -> world.getBlockState(pos.add(vec3i)).isOf(this))) {
				havePlaceableBlock = canConnect = false;
			}
		}
		return havePlaceableBlock || canConnect;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		BlockState blockState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		boolean isPlaceableBlock = neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite()) && isPlaceable(neighborState);
		if (neighborState.isAir() && direction == Direction.DOWN && world.getBlockEntity(pos) instanceof PhysicsConnectingBlockEntity entity
				&& entity.getRootTree() != null && entity.getRootTree().getRoot().equals(entity.getTreeNode())) {
			return onBreak((World) world, pos, Blocks.AIR.getDefaultState(), null);
		}
		if (neighborState.isAir() && world.getBlockEntity(pos) instanceof PhysicsConnectingBlockEntity entity
				&& entity.getRootTree() != null && entity.getRootTree().getRoot().getKey().equals(neighborPos)) {
			return onBreak((World) world, pos, Blocks.AIR.getDefaultState(), null);
		}
		if (neighborState.isAir()) blockState = blockState.with(FACING_PROPERTIES.get(direction), false);
		else if (isPlaceableBlock || isConnectable(neighborState)) blockState = blockState.with(FACING_PROPERTIES.get(direction), true);
		return blockState;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (world.isClient) return;
		setConnectionProperties(world, pos, Optional.ofNullable((PhysicsConnectingBlockEntity) world.getBlockEntity(pos)));
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
