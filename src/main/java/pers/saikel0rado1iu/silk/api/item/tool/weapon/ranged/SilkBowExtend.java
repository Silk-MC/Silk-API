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

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.item.tool.UsingFovZoom;
import pers.saikel0rado1iu.silk.api.item.tool.UsingMovementMultiplier;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">用于扩展弓特性的方法接口</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface SilkBowExtend extends SilkRangedWeaponExtend, UsingMovementMultiplier, UsingFovZoom {
	@SilkApi
	float BOW_MAX_PROJECTILE_SPEED = 3;
	@SilkApi
	int BOW_MAX_USE_TICKS = 72000;
	@SilkApi
	int BOW_MAX_PULL_TICKS = 20;
	
	@SilkApi
	default int getMaxPullTicks() {
		return BOW_MAX_PULL_TICKS;
	}
	
	@Override
	default float getMaxProjectileSpeed() {
		return BOW_MAX_PROJECTILE_SPEED;
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
		return BOW_MAX_USE_TICKS;
	}
	
	@Override
	default float getDamageMultiple() {
		return getMaxDamageMultiple() / (getMaxProjectileSpeed() / BOW_MAX_PROJECTILE_SPEED);
	}
	
	@Override
	default float getUsingProgress(int useTicks, ItemStack stack) {
		float progress = (float) useTicks / getMaxPullTicks();
		return Math.min(1, (progress * (progress + 2)) / 3);
	}
	
	@Override
	default float getUsingFovZoom() {
		return 1;
	}
	
	@Override
	default float getUsingMovementMultiple() {
		return DEFAULT_MOVEMENT_MULTIPLE;
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
