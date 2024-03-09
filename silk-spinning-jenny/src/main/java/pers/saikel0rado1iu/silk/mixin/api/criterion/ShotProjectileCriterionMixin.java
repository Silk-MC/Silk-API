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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.registry.gen.data.criterion.SilkCriteria;

/**
 * <h2 style="color:FFC800">设置射击弹药标准在原版中弓弩可触发</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface ShotProjectileCriterionMixin {
	@Mixin(RangedWeaponItem.class)
	abstract class Bow {
		@SuppressWarnings("ConstantValue")
		@Inject(method = "createArrowEntity", at = @At("RETURN"))
		private void createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cir) {
			if (((RangedWeaponItem) (Object) this) instanceof BowItem && shooter instanceof ServerPlayerEntity serverPlayer)
				SilkCriteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, weaponStack, cir.getReturnValue(), 1);
		}
	}
	
	@Mixin(CrossbowItem.class)
	abstract class Crossbow {
		@Inject(method = "createArrowEntity", at = @At("RETURN"))
		private void createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cir) {
			if (shooter instanceof ServerPlayerEntity serverPlayer)
				SilkCriteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, weaponStack, cir.getReturnValue(), 1);
		}
	}
}
