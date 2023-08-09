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

package pers.saikeloradoliu.silk.api.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import pers.saikeloradoliu.silk.annotation.SilkApi;

/**
 * <p><b style="color:FFC800"><font size="+1">在 {@link BowExtend} 的基础上用与扩展弩特性的方法接口</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface CrossbowExtend extends BowExtend {
	@SilkApi
	String CHARGED_PROJECTILES_KEY = "ChargedProjectiles";
	@SilkApi
	float CROSSBOW_MAX_PROJECTILE_SPEED = 3.15F;
	
	@SilkApi
	float getChargedMovementMultiple();
	
	@SilkApi
	float getChargedFovScale();
	
	@SilkApi
	default float getChargedFovMultiple() {
		return 1 / getChargedFovScale();
	}
	
	@SilkApi
	default float getCrossbowPullProgress(int useTicks, ItemStack stack) {
		return Math.min(1, useTicks / getPullTicks(stack));
	}
	
	@SilkApi
	default int getPullTicks(ItemStack stack) {
		// 设置“快速装填”效果
		int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		return quickChargeLevel == 0 ? getMaxUseTicks() : getMaxUseTicks() - getMaxUseTicks() / 5 * quickChargeLevel;
	}
	
	@SilkApi
	default float getProjectileSpeed(ItemStack stack) {
		return CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? getMaxProjectileSpeed() / 2 : getMaxProjectileSpeed();
	}
}

