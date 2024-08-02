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

package pers.saikel0rado1iu.silk.api.ropestick.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * <h2 style="color:FFC800">在持有时修改移动</h2>
 * 握持物品时的移动速度修改器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ModifyMoveWhileHold extends ModifyMove {
	/**
	 * 是否能够修改移动速度
	 *
	 * @param stack 物品堆栈
	 * @return 是否可以修改
	 */
	default boolean canModifyMove(ItemStack stack) {
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
