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

package pers.saikel0rado1iu.silk.mixin.api.item.weapon;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.item.tool.weapon.BreakingShield;

/**
 * <p><b style="color:FFC800"><font size="+1">设置可以破盾</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@Mixin(LivingEntity.class)
abstract class BreakingShieldMixin {
	@Unique
	private DamageSource damageSource;
	
	@Shadow
	public abstract void damageShield(float amount);
	
	@Shadow
	protected abstract void takeShieldHit(LivingEntity attacker);
	
	@Shadow
	public abstract ItemStack getActiveItem();
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private DamageSource getSource(DamageSource source) {
		return damageSource = source;
	}
	
	@Inject(method = "blockedByShield", at = @At("RETURN"), cancellable = true)
	private void setBreak(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && source.getAttacker() instanceof LivingEntity living
				&& living.getEquippedStack(EquipmentSlot.MAINHAND).getItem() instanceof BreakingShield breakingShield)
			cir.setReturnValue(breakingShield.throughShield());
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float setDamage(float amount) {
		if (!(getActiveItem().isOf(Items.SHIELD) && damageSource.getAttacker() instanceof LivingEntity living
				&& living.getEquippedStack(EquipmentSlot.MAINHAND).getItem() instanceof BreakingShield breakingShield
				&& breakingShield.canBreaking(damageSource))) return amount;
		damageShield(breakingShield.getShieldDamage(amount));
		if (breakingShield.throughShield()) {
			takeShieldHit((LivingEntity) damageSource.getAttacker());
			if (((LivingEntity) (Object) this) instanceof PlayerEntity player) player.disableShield(true);
		}
		
		return breakingShield.getHurtDamage(amount);
	}
}
