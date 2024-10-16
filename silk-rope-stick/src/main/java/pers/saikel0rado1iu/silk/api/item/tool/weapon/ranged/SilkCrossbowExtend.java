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

package pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged;

import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.item.tool.HoldingFovZoom;
import pers.saikel0rado1iu.silk.api.item.tool.HoldingMovementMultiplier;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">用于扩展弩特性的方法接口</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface SilkCrossbowExtend extends SilkRangedWeaponExtend, HoldingMovementMultiplier, HoldingFovZoom {
	@SilkApi
	String CHARGED_KEY = "Charged";
	@SilkApi
	String CHARGED_PROJECTILES_KEY = "ChargedProjectiles";
	@SilkApi
	int CROSSBOW_MAX_USE_TICKS = 25;
	@SilkApi
	float CROSSBOW_MAX_PROJECTILE_SPEED = 3.15F;
	
	@Override
	default float getMaxProjectileSpeed() {
		return CROSSBOW_MAX_PROJECTILE_SPEED;
	}
	
	@Override
	default float getMaxDamageMultiple() {
		return 1;
	}
	
	@Override
	default float getFiringError() {
		return DEFAULT_FIRING_ERROR;
	}
	
	@Override
	default int getMaxUseTicks() {
		return CROSSBOW_MAX_USE_TICKS;
	}
	
	@Override
	default float getDamageMultiple() {
		return getMaxDamageMultiple() / (getMaxProjectileSpeed() / CROSSBOW_MAX_PROJECTILE_SPEED);
	}
	
	@SilkApi
	default float getUsingProgress(int useTicks, ItemStack stack) {
		return Math.min(1, useTicks / (float) getMaxPullTicks(stack));
	}
	
	@SilkApi
	default int getMaxPullTicks(ItemStack stack) {
		// 设置“快速装填”效果
		int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		return quickChargeLevel == 0 ? getMaxUseTicks() : getMaxUseTicks() - getMaxUseTicks() / 5 * quickChargeLevel;
	}
	
	@SilkApi
	default float getMaxProjectileSpeed(ChargedProjectilesComponent stack) {
		return stack.contains(Items.FIREWORK_ROCKET) ? getMaxProjectileSpeed() / 2 : getMaxProjectileSpeed();
	}
	
	@Override
	default float getHoldingFovZoom() {
		return 1;
	}
	
	@Override
	default float getHoldingMovementMultiple() {
		return 1;
	}
	
	@Override
	default boolean isConflictItems(Item checkItem) {
		return checkItem instanceof HoldingMovementMultiplier;
	}
	
	@Override
	default Optional<Identifier> getHubOverlay() {
		return Optional.empty();
	}
	
	@Override
	default boolean onlyFirstPerson() {
		return false;
	}
}

