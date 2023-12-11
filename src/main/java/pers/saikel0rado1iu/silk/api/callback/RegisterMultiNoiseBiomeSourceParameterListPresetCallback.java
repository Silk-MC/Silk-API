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
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.List;

/**
 * <h2 style="color:FFC800">用于注册多重噪声生物群系源参数列表预设的回调</h2>
 * <p style="color:FFC800">模组作者需要在 {@link PreLaunchEntrypoint} 中注册才能生效</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface RegisterMultiNoiseBiomeSourceParameterListPresetCallback {
	@SilkApi
	Event<RegisterMultiNoiseBiomeSourceParameterListPresetCallback> EVENT = EventFactory.createArrayBacked(RegisterMultiNoiseBiomeSourceParameterListPresetCallback.class, listeners -> list -> {
		for (RegisterMultiNoiseBiomeSourceParameterListPresetCallback event : listeners) event.register(list);
	});
	
	void register(List<MultiNoiseBiomeSourceParameterList.Preset> list);
}
