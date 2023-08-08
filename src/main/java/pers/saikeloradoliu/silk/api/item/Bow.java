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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import pers.saikeloradoliu.silk.util.TickUtil;

/**
 * <p><b style="color:FFC800"><font size="+1">辅助弓的创建的数据直观和清晰</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 */
public abstract class Bow extends RangedWeaponItem implements RangedWeaponExtend, Vanishable {
	public Bow(Settings settings) {
		super(settings);
	}
	
	/**
	 * 获取使用操作：弓
	 */
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	/**
	 * 获取最大使用时间
	 */
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return getMaxUseTicks();
	}
	
	/**
	 * 获取拉弓进度
	 */
	public float getPullProgress(int useTicks) {
		float maxPullTicks = TickUtil.getTick(getMaxPullTime());
		return (useTicks > maxPullTicks) ? 1 : (float) useTicks / maxPullTicks;
	}
	
	/**
	 * 设置使用前操作
	 */
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		// 默认操作
		ItemStack itemStack = user.getStackInHand(hand);
		if (!user.getAbilities().creativeMode && user.getProjectileType(itemStack).isEmpty())
			return TypedActionResult.fail(itemStack);
		
		user.setCurrentHand(hand);
		return TypedActionResult.consume(itemStack);
	}
}
