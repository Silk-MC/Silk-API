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

package pers.saikel0rado1iu.silk.ropestick.ranged;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

/**
 * <h2 style="color:FFC800">射击拓展</h2>
 * 用于扩展远程武器的射击特性的方法接口
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ShootExpansion {
	/**
	 * 已射击 NBT 谓词
	 */
	String SHOT_KEY = "shot";
	
	/**
	 * 设置物品已射击
	 *
	 * @param stack 物品堆栈
	 */
	static void setShot(ItemStack stack) {
		setShot(stack, true);
	}
	
	/**
	 * 重置物品至未射击状态
	 *
	 * @param stack 物品堆栈
	 */
	static void resetShot(ItemStack stack) {
		setShot(stack, false);
	}
	
	/**
	 * 设置物品的射击状态
	 *
	 * @param stack 物品堆栈
	 * @param shot  是否已射击
	 */
	static void setShot(ItemStack stack, boolean shot) {
		NbtCompound stackNbt = stack.getOrCreateNbt();
		stackNbt.putBoolean(SHOT_KEY, shot);
	}
	
	/**
	 * 获取物品的射击状态
	 *
	 * @param stack 物品堆栈
	 * @return 是否已射击
	 */
	static boolean isShot(ItemStack stack) {
		NbtCompound stackNbt = stack.getNbt();
		return stackNbt != null && stackNbt.getBoolean(SHOT_KEY);
	}
	
	/**
	 * 设置已射击状态
	 *
	 * @return 如果为 {@code true} 则每个发射物发射后都会设置已发射状态<br>
	 * 如果为 {@code false} 则只有当所有发射物都被发射后才会设置已发射状态
	 */
	boolean shotState();
	
	/**
	 * 设置射击间隔
	 *
	 * @return 射击间隔，单位为游戏刻
	 */
	int shootingInterval();
	
	/**
	 * 渲染射击时的粒子效果
	 *
	 * @param world   存档世界
	 * @param shooter 射击者
	 */
	void renderShootingParticle(World world, LivingEntity shooter);
}
