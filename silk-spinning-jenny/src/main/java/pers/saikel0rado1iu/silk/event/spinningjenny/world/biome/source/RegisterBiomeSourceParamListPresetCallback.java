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

package pers.saikel0rado1iu.silk.event.spinningjenny.world.biome.source;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;

import java.util.List;

/**
 * <h2 style="color:FFC800">注册生物群系源参数列表预设回调</h2>
 * 用于注册多重噪声生物群系源参数列表预设的回调
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface RegisterBiomeSourceParamListPresetCallback {
	/**
	 * 事件
	 */
	Event<RegisterBiomeSourceParamListPresetCallback> EVENT = EventFactory.createArrayBacked(RegisterBiomeSourceParamListPresetCallback.class, listeners -> list -> {
		for (RegisterBiomeSourceParamListPresetCallback event : listeners) event.add(list);
	});
	
	/**
	 * 添加方法
	 *
	 * @param list 需要添加到的列表
	 */
	void add(List<MultiNoiseBiomeSourceParameterList.Preset> list);
}
