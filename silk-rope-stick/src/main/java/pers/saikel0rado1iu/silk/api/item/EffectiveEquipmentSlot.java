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

package pers.saikel0rado1iu.silk.api.item;

import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Optional;
import java.util.Set;

/**
 * <h2 style="color:FFC800">用于有特殊属性的物品能够生效的装备槽</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@ApiStatus.OverrideOnly
interface EffectiveEquipmentSlot {
	@SilkApi
	Set<EquipmentSlot> ARMOR_SLOT = Set.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
	@SilkApi
	Set<EquipmentSlot> HAND_SLOT = Set.of(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	
	/**
	 * 返回有效的装备槽位集合
	 *
	 * @return 如果值为 {@link Optional#empty()}，则意味着在任意物品栏内皆能生效
	 */
	Optional<Set<EquipmentSlot>> getEffectiveEquipmentSlot();
}
