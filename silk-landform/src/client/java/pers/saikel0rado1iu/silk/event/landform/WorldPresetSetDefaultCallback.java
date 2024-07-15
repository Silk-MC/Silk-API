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

package pers.saikel0rado1iu.silk.event.landform;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.world.WorldCreator;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">设置默认世界预设回调</h2>
 * 设置世界预设的默认值
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@FunctionalInterface
public interface WorldPresetSetDefaultCallback extends Function<WorldCreator, WorldCreator.WorldType> {
	/**
	 * 事件
	 */
	Event<WorldPresetSetDefaultCallback> EVENT = EventFactory.createArrayBacked(WorldPresetSetDefaultCallback.class, listeners -> worldCreator -> {
		for (WorldPresetSetDefaultCallback event : listeners) return event.apply(worldCreator);
		return worldCreator.getWorldType();
	});
}
