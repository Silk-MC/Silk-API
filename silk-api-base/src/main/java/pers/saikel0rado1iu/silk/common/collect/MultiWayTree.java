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

package pers.saikel0rado1iu.silk.common.collect;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <h2 style="color:FFC800">多路树</h2>
 * 多节点树形结构
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class MultiWayTree<K, V> {
	private final MultiTreeNode<K, V> root;
	
	/**
	 * @param rootKey   根节点键
	 * @param rootValue 根节点值
	 */
	public MultiWayTree(K rootKey, V rootValue) {
		this.root = new MultiTreeNode<>(rootKey, rootValue);
	}
	
	/**
	 * 获取根节点
	 *
	 * @return 根节点
	 */
	public MultiTreeNode<K, V> getRoot() {
		return root;
	}
	
	/**
	 * 添加子节点到根节点
	 *
	 * @param key   节点键
	 * @param value 节点值
	 * @return 添加的子节点
	 */
	public MultiTreeNode<K, V> addChild(K key, V value) {
		return addChild(root, key, value);
	}
	
	/**
	 * 添加子节点
	 *
	 * @param parent 父节点
	 * @param key    节点键
	 * @param value  节点值
	 * @return 添加的子节点
	 */
	public MultiTreeNode<K, V> addChild(MultiTreeNode<K, V> parent, K key, V value) {
		return parent.addChild(key, value);
	}
	
	/**
	 * 删除根节点下的子节点
	 *
	 * @param childNode 删除的子节点
	 * @return 是否成功删除
	 */
	public boolean removeChild(MultiTreeNode<K, V> childNode) {
		return removeChild(root, childNode);
	}
	
	/**
	 * 删除根节点下的子节点
	 *
	 * @param childKey 删除的子节点的键
	 * @return 是否成功删除
	 */
	public boolean removeChild(K childKey) {
		return removeChild(root, childKey);
	}
	
	/**
	 * 删除子节点
	 *
	 * @param parent    父节点
	 * @param childNode 删除的子节点
	 * @return 是否成功删除
	 */
	public boolean removeChild(MultiTreeNode<K, V> parent, MultiTreeNode<K, V> childNode) {
		return parent.removeChild(childNode);
	}
	
	/**
	 * 删除子节点
	 *
	 * @param parent   父节点
	 * @param childKey 删除的子节点的键
	 * @return 是否成功删除
	 */
	public boolean removeChild(MultiTreeNode<K, V> parent, K childKey) {
		return parent.removeChild(childKey);
	}
	
	/**
	 * 获取指定键的子节点
	 *
	 * @param key 键
	 * @return 根节点下该键的节点
	 */
	public MultiTreeNode<K, V> getChild(K key) {
		return getChild(root, key);
	}
	
	/**
	 * 获取指定键的子节点
	 *
	 * @param parent 父节点
	 * @param key    键
	 * @return 父节点下该键的节点
	 */
	public MultiTreeNode<K, V> getChild(MultiTreeNode<K, V> parent, K key) {
		if (parent == null) return null;
		return parent.getChild(key);
	}
	
	/**
	 * 获取树的高度
	 *
	 * @return 树的总高度
	 */
	public int getHeight() {
		return getHeight(root);
	}
	
	/**
	 * 获取此节点下的树的高度
	 *
	 * @param node 根节点
	 * @return 此节点下的树的高度
	 */
	public int getHeight(MultiTreeNode<K, V> node) {
		if (node == null) return 0;
		int height = 0;
		for (MultiTreeNode<K, V> child : node.getChildren()) height = Math.max(height, getHeight(child));
		return height + 1;
	}
	
	/**
	 * 获取树中的节点数量
	 *
	 * @return 树中的节点数量
	 */
	public int getSize() {
		return getSize(root);
	}
	
	/**
	 * 获取此节点下的树中的节点数量
	 *
	 * @param node 根节点
	 * @return 此节点下的树中的节点数量
	 */
	public int getSize(MultiTreeNode<K, V> node) {
		if (node == null) return 0;
		int size = 0;
		Queue<MultiTreeNode<K, V>> queue = new LinkedList<>();
		queue.offer(node);
		while (!queue.isEmpty()) {
			size++;
			MultiTreeNode<K, V> current = queue.poll();
			current.getChildren().forEach(queue::offer);
		}
		return size;
	}
	
	@Override
	public String toString() {
		return toString(root, 0, new StringBuilder()).toString();
	}
	
	private StringBuilder toString(MultiTreeNode<K, V> node, int depth, StringBuilder result) {
		result.append("  ".repeat(Math.max(0, depth))).append(node.getKey()).append(": ").append(node.getValue()).append("\n");
		for (MultiTreeNode<K, V> child : node.getChildren()) toString(child, depth + 1, result);
		return result;
	}
}
