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

package pers.saikel0rado1iu.silk.mixin.generate.advancement.criterion;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.generate.advancement.criterion.Criteria;
import pers.saikel0rado1iu.silk.api.generate.advancement.criterion.RangedKilledEntityCriterion;

/**
 * <h2 style="color:FFC800">{@link RangedKilledEntityCriterion} 混入</h2>
 * 设置远程武器击杀实体标准触发
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface RangedKilledEntityCriterionMixin {
	/**
	 * 触发混入
	 */
	@Mixin(ServerPlayerEntity.class)
	abstract class Trigger extends PlayerEntity {
		private Trigger(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
			super(world, pos, yaw, gameProfile);
		}
		
		@Inject(method = "updateKilledAdvancementCriterion", at = @At("TAIL"))
		private void updateKilledAdvancementCriterion(Entity entityKilled, int score, DamageSource damageSource, CallbackInfo ci) {
			Criteria.RANGED_KILLED_ENTITY_CRITERION.trigger((ServerPlayerEntity) (Object) this, entityKilled, damageSource);
		}
	}
	
	/**
	 * 弓物品混入
	 */
	@Mixin(net.minecraft.item.BowItem.class)
	abstract class BowItem {
		@Unique
		private ItemStack stack;
		
		@Inject(method = "onStoppedUsing", at = @At("HEAD"))
		private void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
			this.stack = stack;
		}
		
		@ModifyArg(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z"))
		private Entity getEntity(Entity entity) {
			RangedKilledEntityCriterion.setRangedWeapon(entity, stack);
			return entity;
		}
	}
	
	/**
	 * 弩物品混入
	 */
	@Mixin(net.minecraft.item.CrossbowItem.class)
	abstract class CrossbowItem {
		@Unique
		private static ItemStack stack;
		
		@Inject(method = "shoot", at = @At("HEAD"))
		private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {
			stack = crossbow;
		}
		
		@ModifyArg(method = "shoot", at = @At(value = "INVOKE", target = "L net/minecraft/world/World;spawnEntity(L net/minecraft/entity/Entity;)Z"))
		private static Entity getEntity(Entity entity) {
			RangedKilledEntityCriterion.setRangedWeapon(entity, stack);
			return entity;
		}
	}
}
