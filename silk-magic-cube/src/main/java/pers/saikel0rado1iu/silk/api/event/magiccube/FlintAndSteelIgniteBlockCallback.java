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

package pers.saikel0rado1iu.silk.api.event.magiccube;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * <h2 style="color:FFC800">打火石点燃方块回调</h2>
 * 用于实现当打火石引燃指定方块时所做的操作
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.0
 */
@FunctionalInterface
public interface FlintAndSteelIgniteBlockCallback {
	/**
	 * 事件
	 */
	Event<FlintAndSteelIgniteBlockCallback> EVENT = EventFactory.createArrayBacked(FlintAndSteelIgniteBlockCallback.class, listeners -> (state, pointer, world, direction, pos, stack) -> {
		for (FlintAndSteelIgniteBlockCallback event : listeners) if (event.igniteBlock(state, pointer, world, direction, pos, stack)) return true;
		return false;
	});
	
	/**
	 * 点燃方块
	 *
	 * @param state     方块状态
	 * @param pointer   块指针
	 * @param world     服务端世界
	 * @param direction 方向
	 * @param pos       块坐标
	 * @param stack     使用物品
	 * @return 是否已经进行了操作
	 */
	boolean igniteBlock(BlockState state, BlockPointer pointer, ServerWorld world, Direction direction, BlockPos pos, ItemStack stack);
}