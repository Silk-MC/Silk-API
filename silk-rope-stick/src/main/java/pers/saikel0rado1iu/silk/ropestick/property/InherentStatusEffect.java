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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;

import java.util.Set;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">自带状态效果</h2>
 * 用于说明自带状态效果的物品的状态效果属性
 *
 * @param inherentStatusEffects 自带状态效果集合
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record InherentStatusEffect(Set<Property> inherentStatusEffects) implements ItemProperty {
	/**
	 * @param inherentStatusEffects 自带状态效果列表
	 */
	public InherentStatusEffect(Property... inherentStatusEffects) {
		this(ImmutableSet.copyOf(inherentStatusEffects));
	}
	
	/**
	 * 自带状态效果属性
	 *
	 * @param statusEffect        物品的状态效果
	 * @param baseLevel           附魔效果的基础等级，如果 &lt; 1 则按 1 算
	 * @param maxLevel            状态效果的最大等级，如果 &lt; 1 则按 1 算
	 * @param stackingLevel       状态效果的叠加等级，&lt; 0 则递减，== 0 则不变
	 * @param statusEffectKit     状态效果套装，如果图表为空 {@link Set#of()} 则说明没有套装效果。
	 * @param kitTriggerThreshold 套装触发阈值，如果 &lt; 1 则按 1 算，如果为 2 则需要 2 个套装中的物品才能激活效果。
	 *                            如果 {@code statusEffectKit} 为 {@link Set#of()}，则为需要多少个自己
	 * @param effectiveItemSlot   有效物品槽
	 */
	public record Property(StatusEffect statusEffect, int baseLevel, int maxLevel, float stackingLevel, Supplier<Set<Item>> statusEffectKit,
	                       int kitTriggerThreshold, EffectiveItemSlot effectiveItemSlot) implements ItemProperty {
	}
}
