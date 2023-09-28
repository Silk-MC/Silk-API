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

package pers.saikel0rado1iu.silk.mixin.api.item;

import com.google.common.collect.Maps;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.item.WithProjectileProtection;

import java.util.Map;

import static net.minecraft.entity.player.PlayerInventory.MAIN_SIZE;

/**
 * <p><b style="color:FFC800"><font size="+1">设置自带弹射物保护的物品的伤害减免</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
interface WithProjectileProtectionMixin {
	/**
	 * 设置 {@link PlayerEntity} 的效果设置
	 */
	@Mixin(PlayerEntity.class)
	abstract class SetPlayer extends LivingEntity {
		@Unique
		private static DamageSource damageSource = null;
		
		protected SetPlayer(EntityType<? extends LivingEntity> entityType, World world) {
			super(entityType, world);
		}
		
		@Shadow
		public abstract PlayerInventory getInventory();
		
		@Override
		@Shadow
		public abstract ItemStack getEquippedStack(EquipmentSlot slot);
		
		@Inject(method = "applyDamage", at = @At("HEAD"))
		private void getSource(DamageSource source, float amount, CallbackInfo ci) {
			damageSource = source;
		}
		
		@ModifyVariable(method = "applyDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
		private float setProtect(float amount) {
			if (!damageSource.isIn(DamageTypeTags.IS_PROJECTILE)) return amount;
			boolean notAllSlot = false;
			Map<Class<?>, Integer> map = Maps.newHashMapWithExpectedSize(8);
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (!(getEquippedStack(slot).getItem() instanceof WithProjectileProtection item)) continue;
				if ((notAllSlot = item.getEffectiveEquipmentSlot().isEmpty()) || item.getEffectiveEquipmentSlot()
						.get().stream().allMatch(equipmentSlot -> equipmentSlot != slot)) continue;
				if (item.getPrPrStackingCount().isPresent()) {
					int stackingCount = item.getPrPrStackingCount().get();
					int mapCount = map.get(item.getClass()) == null ? 0 : map.get(item.getClass());
					if (mapCount > stackingCount) continue;
					map.put(item.getClass(), mapCount + 1);
				}
				amount = item.getPrPrAmount(amount);
			}
			if (notAllSlot) return amount;
			for (int count = 0; count < MAIN_SIZE; count++) {
				if (!(getInventory().getStack(count).getItem() instanceof WithProjectileProtection item)) continue;
				if (item.getEffectiveEquipmentSlot().isPresent()) continue;
				if (item.getPrPrStackingCount().isPresent()) {
					int stackingCount = item.getPrPrStackingCount().get();
					int mapCount = map.get(item.getClass()) == null ? 0 : map.get(item.getClass());
					if (mapCount > stackingCount) continue;
					map.put(item.getClass(), mapCount + 1);
				}
				amount = item.getPrPrAmount(amount);
			}
			return amount;
		}
	}
	
	/**
	 * 设置 {@link MobEntity} 的效果设置
	 */
	@Mixin(LivingEntity.class)
	abstract class SetMob extends Entity implements Attackable {
		@Unique
		private static DamageSource damageSource = null;
		
		protected SetMob(EntityType<? extends LivingEntity> entityType, World world) {
			super(entityType, world);
		}
		
		@Inject(method = "applyDamage", at = @At("HEAD"))
		private void getSource(DamageSource source, float amount, CallbackInfo ci) {
			damageSource = source;
		}
		
		@ModifyVariable(method = "applyDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
		private float setProtect(float amount) {
			if (!damageSource.isIn(DamageTypeTags.IS_PROJECTILE)) return amount;
			if (!(((LivingEntity) (Object) this) instanceof MobEntity mob)) return amount;
			Map<Class<?>, Integer> map = Maps.newHashMapWithExpectedSize(8);
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (!(mob.getEquippedStack(slot).getItem() instanceof WithProjectileProtection item)) continue;
				if (item.getEffectiveEquipmentSlot().isEmpty() || item.getEffectiveEquipmentSlot()
						.get().stream().allMatch(equipmentSlot -> equipmentSlot != slot)) continue;
				if (item.getPrPrStackingCount().isPresent()) {
					int stackingCount = item.getPrPrStackingCount().get();
					int mapCount = map.get(item.getClass()) == null ? 0 : map.get(item.getClass());
					if (mapCount > stackingCount) continue;
					map.put(item.getClass(), mapCount + 1);
				}
				amount = item.getPrPrAmount(amount);
			}
			return amount;
		}
	}
}
