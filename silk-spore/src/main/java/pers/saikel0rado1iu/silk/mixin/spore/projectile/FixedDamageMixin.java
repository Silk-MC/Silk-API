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

package pers.saikel0rado1iu.silk.mixin.spore.projectile;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pers.saikel0rado1iu.silk.api.spore.projectile.FixedDamage;

/**
 * <h2 style="color:FFC800">{@link FixedDamage} 混入</h2>
 * 设置发射物的固定伤害
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(LivingEntity.class)
abstract class FixedDamageMixin {
	@Unique
	private DamageSource damageSource;
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private DamageSource getSource(DamageSource source) {
		return damageSource = source;
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float setDamage(float amount) {
		return damageSource.getSource() instanceof FixedDamage fixedDamage ? fixedDamage.fixedDamage() : amount;
	}
}
