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

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import pers.saikeloradoliu.silk.annotation.SilkApi;
import pers.saikeloradoliu.silk.util.TickUtil;

/**
 * <p><b style="color:FFC800"><font size="+1">用与扩展远程武器特性的方法接口</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface RangedWeaponExtend extends CustomEnchantment {
	@SilkApi
	float DEFAULT_USING_SPEED_RADIO = 0.2F;
	@SilkApi
	float DEFAULT_MAX_USE_TIME = 60 * 60;
	@SilkApi
	float DEFAULT_MAX_PULL_TIME = 1;
	@SilkApi
	float DEFAULT_VELOCITY_MULTIPLE = 3;
	@SilkApi
	int DEFAULT_RANGE = BowItem.RANGE;
	
	@SilkApi
	float getUsingMovementMultiple();
	
	@SilkApi
	int getMaxUseTicks();
	
	@SilkApi
	float getMaxPullTime();
	
	@SilkApi
	float getBulletSpeed();
	
	@SilkApi
	float getBasicDamageMultiple();
	
	@SilkApi
	float getPullingFovScale();
	
	@SilkApi
	default float getPullingFovMultiple() {
		return 1 / getPullingFovScale();
	}
	
	@SilkApi
	default float getDamageMultiple() {
		return getBasicDamageMultiple() / (getBulletSpeed() / DEFAULT_VELOCITY_MULTIPLE);
	}
	
	/**
	 * 获取拉弓进度
	 */
	@SilkApi
	default float getPullProgress(int useTicks) {
		float maxPullTicks = TickUtil.getTick(getMaxPullTime());
		return (useTicks > maxPullTicks) ? 1 : (float) useTicks / maxPullTicks;
	}
	
	/**
	 * 设置弹丸 ID 的 NBT 以供 JSON 渲染使用
	 */
	@SilkApi
	void setBulletId(ItemStack stack, ItemStack useBullet);
	
	/**
	 * 获取 NBT 弹丸 ID 以供 JSON 渲染使用
	 */
	@SilkApi
	float getBulletId(ItemStack stack);
}
