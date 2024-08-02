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

import net.minecraft.item.ItemStack;

/**
 * <h2 style="color:FFC800">在使用时调整视场角</h2>
 * 使用物品时的视场角缩放
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface AdjustFovWhileUse extends AdjustFov {
	/**
	 * 获取使用进度
	 *
	 * @param useTicks 使用刻数
	 * @param stack    物品堆栈
	 * @return 使用进度，在 0 和 1 之间的浮点数
	 */
	float getUsingProgress(int useTicks, ItemStack stack);
}
