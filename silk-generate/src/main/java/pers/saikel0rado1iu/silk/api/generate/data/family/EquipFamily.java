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

package pers.saikel0rado1iu.silk.api.generate.data.family;

import com.google.common.collect.Maps;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;

import java.util.Map;

/**
 * <h2 style="color:FFC800">装备家族</h2>
 * 与 {@link BlockFamily} 对应。用于创建装备家族以便生成数据包
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class EquipFamily {
	final Map<Variant, Item> variants = Maps.newHashMap();
	
	private EquipFamily() {
	}
	
	/**
	 * 构建器
	 *
	 * @return 装备家族构建器
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * 获取所有变体
	 *
	 * @return 变体图表
	 */
	public Map<Variant, Item> getVariants() {
		return variants;
	}
	
	/**
	 * 获取变体
	 *
	 * @param variant 变体
	 * @return 变体物品
	 */
	public Item getVariant(Variant variant) {
		return variants.get(variant);
	}
	
	/**
	 * 装备家族变体
	 */
	public enum Variant {
		/**
		 * 锹
		 */
		SHOVEL,
		/**
		 * 镐
		 */
		PICKAXE,
		/**
		 * 斧
		 */
		AXE,
		/**
		 * 锄
		 */
		HOE,
		/**
		 * 剑
		 */
		SWORD,
		/**
		 * 头盔
		 */
		HELMET,
		/**
		 * 胸甲
		 */
		CHESTPLATE,
		/**
		 * 护腿
		 */
		LEGGINGS,
		/**
		 * 靴子
		 */
		BOOTS
	}
	
	/**
	 * {@link EquipFamily} 构建器
	 */
	public static final class Builder {
		private final EquipFamily family;
		
		private Builder() {
			family = new EquipFamily();
		}
		
		/**
		 * 锹
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder shovel(Item equip) {
			family.variants.put(Variant.SHOVEL, equip);
			return this;
		}
		
		/**
		 * 镐
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder pickaxe(Item equip) {
			family.variants.put(Variant.PICKAXE, equip);
			return this;
		}
		
		/**
		 * 斧
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder axe(Item equip) {
			family.variants.put(Variant.AXE, equip);
			return this;
		}
		
		/**
		 * 锄
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder hoe(Item equip) {
			family.variants.put(Variant.HOE, equip);
			return this;
		}
		
		/**
		 * 剑
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder sword(Item equip) {
			family.variants.put(Variant.SWORD, equip);
			return this;
		}
		
		/**
		 * 头盔
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder helmet(Item equip) {
			family.variants.put(Variant.HELMET, equip);
			return this;
		}
		
		/**
		 * 胸甲
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder chestplate(Item equip) {
			family.variants.put(Variant.CHESTPLATE, equip);
			return this;
		}
		
		/**
		 * 护腿
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder leggings(Item equip) {
			family.variants.put(Variant.LEGGINGS, equip);
			return this;
		}
		
		/**
		 * 靴子
		 *
		 * @param equip 装备
		 * @return 构建器
		 */
		public Builder boots(Item equip) {
			family.variants.put(Variant.BOOTS, equip);
			return this;
		}
		
		/**
		 * 构建 {@link EquipFamily}
		 *
		 * @return 装备家族
		 */
		public EquipFamily build() {
			return family;
		}
	}
}
