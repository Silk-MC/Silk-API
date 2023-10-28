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

package pers.saikel0rado1iu.silk.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <p><b style="color:FFC800"><font size="+1">树苗成长感染回调</font></b></p>
 * <p style="color:FFC800">将树苗的生长感染成你想要的植物</p>
 * <p style="color:FFC800">在逻辑客户端上，返回值具有以下含义：
 * <ul style="color:FFC800">
 * <li>{@link ActionResult#SUCCESS} 退出后续处理过程</li>
 * <li>{@link ActionResult#PASS} 回落到后续处理过程，如果没有其他的监听器了，则默认为 {@link ActionResult#FAIL}</li>
 * <li>{@link ActionResult#FAIL} 回落到后续处理过程，执行原来的树苗成长方法</li>
 * </ul>
 * </p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface InfectSaplingCallback {
	@SilkApi
	Event<InfectSaplingCallback> EVENT = EventFactory.createArrayBacked(InfectSaplingCallback.class,
			(listeners) -> (world, pos, state, random, sapling) -> {
				for (InfectSaplingCallback listener : listeners) {
					ActionResult result = listener.interact(world, pos, state, random, sapling);
					
					if (result != ActionResult.PASS) return result;
				}
				return ActionResult.FAIL;
			}
	);
	
	ActionResult interact(ServerWorld world, BlockPos pos, BlockState state, Random random, SaplingBlock sapling);
}
