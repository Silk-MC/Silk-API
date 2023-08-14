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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * <p><b style="color:FFC800"><font size="+1">用于自带状态效果的物品</font></b></p>
 * <p style="color:FFC800">{@link WithStatusEffects} 中所有方法返回值中的 {@link StatusEffect} 都必须一致，不然后果自负</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface WithStatusEffects extends EffectiveEquipmentSlot {
	/**
	 * @return {@link Map} 中的 {@link StatusEffect} 为添加的状态效果，{@link Integer} 为状态效果的最大等级
	 */
	@SilkApi
	@NotNull Map<StatusEffect, Integer> getStatusEffects();
	
	/**
	 * 获取每个状态效果每次的叠加等级
	 *
	 * @return {@link Map} 中的 {@link StatusEffect} 为添加的状态效果，
	 * {@link Float} 为状态效果每次的递增等级，小于 0 则递减，等于 0 则不变
	 */
	@SilkApi
	@NotNull Map<StatusEffect, Float> getStatusEffectsStackingLevel();
	
	/**
	 * 获取效果套装，效果套装中的物品组被识别为相同的 {@link Item}，
	 * 但只有 {@link WithStatusEffects} 才能进行效果判断
	 *
	 * @return {@link Map} 中的 {@link StatusEffect} 为添加的状态效果；
	 * {@link Optional} 为 {@link Optional#empty()} 则说明没有套装效果，
	 * {@link Map} 为当前效果的套装物品组，包括自身或不包括自身都会将自身包括到此物品组。
	 * {@link Item} 为套装物品，如果 {@link Item} 为 {@link WithStatusEffects} 则忽略 {@link Set} 内容，
	 * 否则 {@link Set} 含义请参阅 {@link EffectiveEquipmentSlot#getEffectiveEquipmentSlot()}
	 */
	@SilkApi
	@NotNull Map<StatusEffect, Optional<Map<Item, Optional<Set<EquipmentSlot>>>>> getStatusEffectsKit();
	
	/**
	 * 获取套装触发阈值，套装效果意味着玩家需要集齐一套物品才能触发状态效果
	 *
	 * @return {@link Map} 中的 {@link StatusEffect} 为添加的状态效果，
	 * 如果 {@link WithStatusEffects#getStatusEffectsKit()} 中不存在效果套装则为需要多少个自己；
	 * {@link Optional} 为 {@link Optional#empty()} 则说明需要集齐套装中所有物品，
	 * {@link Integer} 为最少需要的套装中物品数量，如果大于套装数量则为需要一套套装，小于 1 则视为需要 1 个物品
	 */
	@SilkApi
	@NotNull Map<StatusEffect, Optional<Integer>> getKitTriggerThreshold();
}
