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

package pers.saikeloradoliu.silk.api.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;
import pers.saikeloradoliu.silk.annotation.SilkApi;

/**
 * <p><b style="color:FFC800"><font size="+1">辅助盔甲的创建的数据直观和清晰</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface Armor extends ArmorMaterial {
	/**
	 * 基础耐久基数
	 */
	@SilkApi
	int[] DURABILITY_BASE = {11, 16, 15, 13};
	/**
	 * 击退抗性比例
	 */
	@SilkApi
	int KR_RATIO = 10;
	
	@SilkApi
	@NotNull String getId();
	
	@SilkApi
	int getDurability();
	
	@SilkApi
	int[] getProtection();
	
	@SilkApi
	float getKnockBackResistance();
	
	@SilkApi
	default ArmorItem createHelmet(Item.Settings settings) {
		return new ArmorItem(this, ArmorItem.Type.HELMET, settings);
	}
	
	@SilkApi
	default ArmorItem createChestPlate(Item.Settings settings) {
		return new ArmorItem(this, ArmorItem.Type.CHESTPLATE, settings);
	}
	
	@SilkApi
	default ArmorItem createLeggings(Item.Settings settings) {
		return new ArmorItem(this, ArmorItem.Type.LEGGINGS, settings);
	}
	
	@SilkApi
	default ArmorItem createBoots(Item.Settings settings) {
		return new ArmorItem(this, ArmorItem.Type.BOOTS, settings);
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
	private int getProtection(ArmorItem.Type type, int head, int body, int legs, int feet) {
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
		return getId();
	}
	
	@Override
	default int getDurability(ArmorItem.Type type) {
		return getDurability(type, getDurability());
	}
	
	@Override
	default int getProtection(ArmorItem.Type type) {
		int[] protection = getProtection();
		return getProtection(type, protection[0], protection[1], protection[2], protection[3]);
	}
	
	@Override
	default float getKnockbackResistance() {
		return getKnockBackResistance(getKnockBackResistance());
	}
}
