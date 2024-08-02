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

package pers.saikel0rado1iu.silk.api.ropestick.armor;

import com.google.common.collect.ImmutableSet;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.ropestick.property.ItemProperties;
import pers.saikel0rado1iu.silk.api.ropestick.property.ItemProperty;

import java.util.Set;

/**
 * <h2 style="color:FFC800">盔甲</h2>
 * 辅助盔甲的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface Armor extends ArmorMaterial {
	/**
	 * 基础耐久基数
	 */
	int[] DURABILITY_BASE = {11, 16, 15, 13};
	/**
	 * 击退抗性比例
	 */
	int KR_RATIO = 10;
	
	/**
	 * 获得带属性的盔甲物品
	 *
	 * @param material   盔甲材料
	 * @param type       盔甲类型
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 带属性的盔甲物品
	 */
	static ArmorItem getArmorItem(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings, ItemProperty... properties) {
		ImmutableSet<ItemProperty> immutableSet = ImmutableSet.copyOf(properties);
		class ArmorItemEx extends ArmorItem implements ItemProperties {
			public ArmorItemEx(ArmorMaterial material, Type type, Settings settings) {
				super(material, type, settings);
			}
			
			@Override
			public Set<ItemProperty> properties() {
				return immutableSet;
			}
		}
		return new ArmorItemEx(material, type, settings);
	}
	
	/**
	 * 获取盔甲 ID
	 *
	 * @return 盔甲 ID
	 */
	Identifier getId();
	
	/**
	 * 获取耐久度
	 *
	 * @return 耐久度
	 */
	int getDurability();
	
	/**
	 * 获取护甲值
	 *
	 * @return 护甲值
	 */
	int[] getProtections();
	
	/**
	 * 获取击退抗性
	 *
	 * @return 击退抗性
	 */
	float getKnockBackResistance();
	
	/**
	 * 创建头盔
	 *
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 头盔物品
	 */
	default ArmorItem createHelmet(Item.Settings settings, ItemProperty... properties) {
		return getArmorItem(this, ArmorItem.Type.HELMET, settings, properties);
	}
	
	/**
	 * 创建胸甲
	 *
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 头盔胸甲
	 */
	default ArmorItem createChestplate(Item.Settings settings, ItemProperty... properties) {
		return getArmorItem(this, ArmorItem.Type.CHESTPLATE, settings, properties);
	}
	
	/**
	 * 创建护腿
	 *
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 头盔护腿
	 */
	default ArmorItem createLeggings(Item.Settings settings, ItemProperty... properties) {
		return getArmorItem(this, ArmorItem.Type.LEGGINGS, settings, properties);
	}
	
	/**
	 * 创建靴子
	 *
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 头盔靴子
	 */
	default ArmorItem createBoots(Item.Settings settings, ItemProperty... properties) {
		return getArmorItem(this, ArmorItem.Type.BOOTS, settings, properties);
	}
	
	/**
	 * 获取装备耐久值
	 *
	 * @param type        装备槽
	 * @param coefficient 耐久系数
	 * @return 实际耐久值
	 */
	private int getDurability(ArmorItem.Type type, int coefficient) {
		return DURABILITY_BASE[type.getEquipmentSlot().getEntitySlotId()] * coefficient;
	}
	
	/**
	 * 获取装备护甲值
	 *
	 * @param type 装备槽
	 * @param head 头盔
	 * @param body 胸甲
	 * @param legs 护腿
	 * @param feet 靴子
	 * @return 实际护甲值
	 */
	private int getProtections(ArmorItem.Type type, int head, int body, int legs, int feet) {
		return new int[]{feet, legs, body, head}[type.getEquipmentSlot().getEntitySlotId()];
	}
	
	/**
	 * 获取击退抗性
	 *
	 * @param kr 击退抗性
	 * @return 击退抗性
	 */
	private float getKnockBackResistance(float kr) {
		return kr / KR_RATIO;
	}
	
	@Override
	default String getName() {
		return getId().toString();
	}
	
	@Override
	default int getDurability(ArmorItem.Type type) {
		return getDurability(type, getDurability());
	}
	
	@Override
	default int getProtection(ArmorItem.Type type) {
		int[] protection = getProtections();
		return getProtections(type, protection[0], protection[1], protection[2], protection[3]);
	}
	
	@Override
	default float getKnockbackResistance() {
		return getKnockBackResistance(getKnockBackResistance());
	}
}
