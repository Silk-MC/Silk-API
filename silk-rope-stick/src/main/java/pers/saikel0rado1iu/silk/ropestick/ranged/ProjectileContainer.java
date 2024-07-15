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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.item.CrossbowItem.CHARGED_PROJECTILES_KEY;

/**
 * <h2 style="color:FFC800">发射物容器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ProjectileContainer {
	/**
	 * 获取已装填发射物
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @return 已装填发射物列表
	 */
	static List<ItemStack> getChargedProjectiles(ItemStack stack) {
		NbtCompound stackNbt = stack.getNbt();
		if (stackNbt == null) return ImmutableList.of();
		if (!stackNbt.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE)) return ImmutableList.of();
		ArrayList<ItemStack> list = Lists.newArrayList();
		for (NbtElement element : stackNbt.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE)) {
			if (!(element instanceof NbtCompound nbtCompound)) continue;
			list.add(ItemStack.fromNbt(nbtCompound));
		}
		return list;
	}
	
	/**
	 * *随机*取出一个已装填发射物<br>
	 * 因 NBT 技术原因，无法顺序取出放入的发射物，因为放入就不是顺序的
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @return 发射物
	 */
	static ItemStack popChargedProjectiles(ItemStack stack) {
		NbtCompound stackNbt = stack.getNbt();
		if (stackNbt == null) return ItemStack.EMPTY;
		if (!stackNbt.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE)) return ItemStack.EMPTY;
		NbtList nbtList = stackNbt.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE);
		if (nbtList.isEmpty()) return ItemStack.EMPTY;
		ItemStack popStack = ItemStack.fromNbt(nbtList.getCompound(0));
		nbtList.remove(0);
		return popStack;
	}
	
	/**
	 * 获取已装填发射物数量
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @return 已装填发射物数量
	 */
	static int getChargedAmount(ItemStack stack) {
		return getChargedProjectiles(stack).size();
	}
	
	/**
	 * 放入已装填发射物<br>
	 * 如果需装填的发射物数量大于可装填的数量则会舍弃部分发射物
	 *
	 * @param stack       装填发射物的物品堆栈
	 * @param projectiles 需装填发射物堆栈列表
	 */
	default void putChargedProjectiles(ItemStack stack, List<ItemStack> projectiles) {
		NbtCompound stackNbt = stack.getOrCreateNbt();
		NbtList nbtList = stackNbt.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE);
		for (int count = 0; count < Math.min(projectiles.size(), getLoadableAmount(stack, Optional.empty())); count++) {
			nbtList.add(projectiles.get(count).writeNbt(new NbtCompound()));
		}
		stackNbt.put(CHARGED_PROJECTILES_KEY, nbtList);
	}
	
	/**
	 * 获取能装填发射物数量
	 *
	 * @param stack 装填发射物的物品堆栈
	 * @param user  装填的玩家实体，用于判断是否为创造
	 * @return 能装填发射物数量
	 */
	default int getLoadableAmount(ItemStack stack, Optional<LivingEntity> user) {
		return (user.isPresent() && user.get() instanceof PlayerEntity player && !player.isCreative())
				? Math.min(maxCapacity(), player.getInventory().count(player.getProjectileType(stack).getItem()))
				: maxCapacity() - getChargedAmount(stack);
	}
	
	/**
	 * 最大发射物容量
	 *
	 * @return 最大发射物容量
	 */
	int maxCapacity();
}
