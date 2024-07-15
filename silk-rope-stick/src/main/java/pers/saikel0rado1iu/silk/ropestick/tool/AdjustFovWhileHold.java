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

package pers.saikel0rado1iu.silk.ropestick.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * <h2 style="color:FFC800">在持有时调整视场角</h2>
 * 持有物品时的视场角缩放
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface AdjustFovWhileHold extends AdjustFov {
	/**
	 * 是否能够调整视场角
	 *
	 * @param stack 物品堆栈
	 * @return 是否可以调整
	 */
	default boolean canAdjustFov(ItemStack stack) {
		return true;
	}
	
	/**
	 * 判断是否为冲突物品
	 *
	 * @param checkItem 检查物品
	 * @return 是否冲突
	 */
	boolean isConflictItem(Item checkItem);
}
