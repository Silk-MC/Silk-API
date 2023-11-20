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
import net.minecraft.client.gui.screen.world.WorldCreator;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <p><b style="color:FFC800"><font size="+1">设置默认世界预设回调</font></b></p>
 * <p style="color:FFC800">设置世界预设的默认值</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface WorldPresetSetDefaultCallback {
	@SilkApi
	Event<WorldPresetSetDefaultCallback> EVENT = EventFactory.createArrayBacked(WorldPresetSetDefaultCallback.class, listeners -> worldCreator -> {
		for (WorldPresetSetDefaultCallback event : listeners) return event.setDefault(worldCreator);
		return worldCreator.getWorldType();
	});
	
	WorldCreator.WorldType setDefault(WorldCreator worldCreator);
	
	@ApiStatus.Internal
	class Data {
		@ApiStatus.Internal
		public static WorldCreator worldCreator;
	}
}