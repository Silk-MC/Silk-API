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

import java.util.ArrayList;
import java.util.List;

/**
 * <h2 style="color:FFC800">多路树节点</h2>
 * 多节点树形结构节点
 *
 * @param <V> 键类型
 * @param <K> 值类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class MultiTreeNode<K, V> {
	private final K key;
	private final V value;
	private final List<MultiTreeNode<K, V>> children;
	
	/**
	 * @param key   键
	 * @param value 值
	 */
	public MultiTreeNode(K key, V value) {
		this.key = key;
		this.value = value;
		this.children = new ArrayList<>();
	}
	
	/**
	 * 获取节点的键
	 *
	 * @return 当前节点的键
	 */
	public K getKey() {
		return key;
	}
	
	/**
	 * 获取节点的值
	 *
	 * @return 当前节点的值
	 */
	public V getValue() {
		return value;
	}
	
	/**
	 * 获取节点的所以子节点
	 *
	 * @return 当前节点下的子节点列表
	 */
	public List<MultiTreeNode<K, V>> getChildren() {
		return children;
	}
	
	/**
	 * 获取该节点下的一个子节点
	 *
	 * @param key 此节点的键
	 * @return 子节点
	 */
	public MultiTreeNode<K, V> getChild(K key) {
		for (MultiTreeNode<K, V> child : children) {
			if (child.getKey().equals(key)) return child;
		}
		return null;
	}
	
	/**
	 * 添加子节点
	 *
	 * @param childKey   子节点的键
	 * @param childValue 子节点的值
	 * @return 添加的子节点
	 */
	public MultiTreeNode<K, V> addChild(K childKey, V childValue) {
		MultiTreeNode<K, V> childNode = new MultiTreeNode<>(childKey, childValue);
		children.add(childNode);
		return childNode;
	}
	
	/**
	 * 移除子节点
	 *
	 * @param childNode 需要移除的子节点
	 * @return 是否已移除
	 */
	public boolean removeChild(MultiTreeNode<K, V> childNode) {
		return children.remove(childNode);
	}
	
	/**
	 * 移除子节点
	 *
	 * @param childKey 需要移除子节点的键
	 * @return 是否已移除
	 */
	public boolean removeChild(K childKey) {
		return removeChild(getChild(childKey));
	}
}
