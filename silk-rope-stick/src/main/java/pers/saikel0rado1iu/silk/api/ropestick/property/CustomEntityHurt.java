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

package pers.saikel0rado1iu.silk.api.ropestick.property;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.function.IOQuadFunction;

/**
 * <h2 style="color:FFC800">自定义实体受伤</h2>
 * 用于需要自定义盔甲物品所阻挡的伤害<br>
 * 注！此属性仅能用于在装备栏中的物品，否则不会生效
 *
 * @param setDamage 用于设置伤害的处理函数
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record CustomEntityHurt(IOQuadFunction<ItemStack, DamageSource, LivingEntity, Float, Float> setDamage) implements ItemProperty {
}
