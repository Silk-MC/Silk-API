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

package pers.saikel0rado1iu.silk.ropestick.property;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;

import java.util.Set;

/**
 * <h2 style="color:FFC800">有效物品槽</h2>
 * 用于有特殊属性的物品能够生效的物品槽
 *
 * @param slots 有效的物品槽集合，如果集合为空 {@link Set#of()}，则意味着能在任意物品栏内生效
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public record EffectiveItemSlot(Set<EquipmentSlot> slots) implements ItemProperty {
	/**
	 * 盔甲物品槽
	 */
	public final static EffectiveItemSlot ARMOR = new EffectiveItemSlot(ImmutableSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
	/**
	 * 手部物品槽
	 */
	public final static EffectiveItemSlot HAND = new EffectiveItemSlot(ImmutableSet.of(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
	/**
	 * 所有物品槽
	 */
	public final static EffectiveItemSlot ALL = new EffectiveItemSlot(ImmutableSet.of());
	
	/**
	 * @param slots 装备槽列表
	 */
	public EffectiveItemSlot(EquipmentSlot... slots) {
		this(ImmutableSet.copyOf(slots));
	}
	
	/**
	 * 判断是否有效
	 *
	 * @param entity 判断实体
	 * @param item   判断物品
	 * @return 是否有效
	 */
	public boolean isEffective(LivingEntity entity, ItemConvertible item) {
		if (slots.isEmpty()) return true;
		return slots.stream().anyMatch(slot -> entity.getEquippedStack(slot).isOf(item.asItem()));
	}
}
