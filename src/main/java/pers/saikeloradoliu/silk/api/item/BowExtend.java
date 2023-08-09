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

import java.util.concurrent.TimeUnit;

/**
 * <p><b style="color:FFC800"><font size="+1">用与扩展弓特性的方法接口</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface BowExtend {
	@SilkApi
	float BOW_MAX_PROJECTILE_SPEED = 3;
	@SilkApi
	float DEFAULT_USING_SPEED_RATIO = 0.2F;
	@SilkApi
	float DEFAULT_MAX_PULL_TIME = 1;
	@SilkApi
	float DEFAULT_PROJECTILE_DEVIATION = 1;
	@SilkApi
	int DEFAULT_RANGE = BowItem.RANGE;
	
	@SilkApi
	float getUsingMovementMultiple();
	
	@SilkApi
	float getMaxPullTicks();
	
	@SilkApi
	float getMaxProjectileSpeed();
	
	@SilkApi
	float getBasicDamageMultiple();
	
	@SilkApi
	float getUsingFovScale();
	
	@SilkApi
	float getFiringError();
	
	@SilkApi
	default int getMaxUseTicks() {
		return TickUtil.getTick(TickUtil.TimeType.NATURAL, TimeUnit.HOURS, 1);
	}
	
	@SilkApi
	default float getUsingFovMultiple() {
		return 1 / getUsingFovScale();
	}
	
	@SilkApi
	default float getDamageMultiple() {
		return getBasicDamageMultiple() / (getMaxProjectileSpeed() / BOW_MAX_PROJECTILE_SPEED);
	}
	
	@SilkApi
	default float getBowPullProgress(int useTicks) {
		float progress = useTicks / getMaxPullTicks();
		return Math.min(1, (progress * progress + progress * 2) / 3);
	}
	
	/**
	 * 设置弹丸 ID 的 NBT 以供 JSON 渲染使用
	 */
	@SilkApi
	void setProjectileId(ItemStack stack, ItemStack useProjectile);
	
	/**
	 * 获取 NBT 弹丸 ID 以供 JSON 渲染使用
	 */
	@SilkApi
	float getProjectileId(ItemStack stack);
}
