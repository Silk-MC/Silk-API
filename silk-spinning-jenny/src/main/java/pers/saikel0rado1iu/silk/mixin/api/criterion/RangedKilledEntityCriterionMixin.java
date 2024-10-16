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

package pers.saikel0rado1iu.silk.mixin.api.criterion;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.registry.gen.data.criterion.RangedKilledEntityCriterion;
import pers.saikel0rado1iu.silk.api.registry.gen.data.criterion.SilkCriteria;

/**
 * <h2 style="color:FFC800">设置远程武器击杀实体标准触发</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface RangedKilledEntityCriterionMixin {
	@Mixin(ServerPlayerEntity.class)
	abstract class Trigger extends PlayerEntity {
		private Trigger(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
			super(world, pos, yaw, gameProfile);
		}
		
		@Inject(method = "updateKilledAdvancementCriterion", at = @At("TAIL"))
		private void updateKilledAdvancementCriterion(Entity entityKilled, int score, DamageSource damageSource, CallbackInfo ci) {
			SilkCriteria.RANGED_KILLED_ENTITY_CRITERION.trigger((ServerPlayerEntity) (Object) this, entityKilled, damageSource);
		}
	}
	
	@Mixin(RangedWeaponItem.class)
	abstract class RangedWeapon {
		@Unique
		private ItemStack stack;
		
		@Inject(method = "createArrowEntity", at = @At("HEAD"))
		private void createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cir) {
			stack = weaponStack;
		}
		
		@ModifyArg(method = "shootAll", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z"))
		private Entity getEntity(Entity entity) {
			RangedKilledEntityCriterion.putRangedNbt(entity, stack);
			return entity;
		}
	}
}
