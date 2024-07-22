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

package pers.saikel0rado1iu.silk.mixin.ropestick.property;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pers.saikel0rado1iu.silk.ropestick.property.CustomEntityHurt;
import pers.saikel0rado1iu.silk.ropestick.property.ItemProperty;

import java.io.IOException;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">{@link CustomEntityHurt} 混入</h2>
 * 设置需要自定义盔甲物品所阻挡的伤害
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@Mixin(LivingEntity.class)
abstract class CustomEntityHurtMixin {
	@Unique
	private DamageSource damageSource;
	
	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private DamageSource getSource(DamageSource source) {
		return damageSource = source;
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float setDamage(float amount) throws IOException {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = getEquippedStack(slot);
			Optional<CustomEntityHurt> property = ItemProperty.get(stack.getItem(), CustomEntityHurt.class);
			if (property.isEmpty()) continue;
			return property.get().setDamage().apply(stack, damageSource, ((LivingEntity) (Object) this), amount);
		}
		return amount;
	}
}
