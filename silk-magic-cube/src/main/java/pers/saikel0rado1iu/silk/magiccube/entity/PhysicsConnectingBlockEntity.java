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

package pers.saikel0rado1iu.silk.magiccube.entity;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import pers.saikel0rado1iu.silk.common.collect.MultiTreeNode;
import pers.saikel0rado1iu.silk.common.collect.MultiWayTree;

/**
 * <h2 style="color:FFC800">物理连接块实体</h2>
 * 物理连接块中储存的方块数据用于判断连接树
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class PhysicsConnectingBlockEntity extends BlockEntity {
	private MultiWayTree<BlockPos, String> rootTree;
	private MultiTreeNode<BlockPos, String> treeNode;
	
	/**
	 * @param type  方块实体类型
	 * @param pos   方块位置
	 * @param state 方块状态
	 */
	public PhysicsConnectingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	/**
	 * 获取根树
	 *
	 * @return 根树
	 */
	public MultiWayTree<BlockPos, String> getRootTree() {
		return rootTree;
	}
	
	/**
	 * 设置根树
	 *
	 * @param rootTree 根树
	 */
	public void setRootTree(@NotNull MultiWayTree<BlockPos, String> rootTree) {
		this.rootTree = rootTree;
	}
	
	/**
	 * 获取树节点
	 *
	 * @return 树节点
	 */
	public MultiTreeNode<BlockPos, String> getTreeNode() {
		return treeNode;
	}
	
	/**
	 * 设置树节点
	 *
	 * @param treeNode 树节点
	 */
	public void setTreeNode(@NotNull MultiTreeNode<BlockPos, String> treeNode) {
		this.treeNode = treeNode;
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Gson gson = new Gson();
		nbt.putString("rootTree", gson.toJson(rootTree));
		nbt.putString("treeNode", gson.toJson(treeNode));
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Gson gson = new Gson();
		rootTree = gson.fromJson(nbt.getString("rootTree"), new TypeToken<MultiWayTree<BlockPos, String>>() {
		}.getType());
		treeNode = gson.fromJson(nbt.getString("treeNode"), new TypeToken<MultiTreeNode<BlockPos, String>>() {
		}.getType());
	}
}
