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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <h2 style="color:FFC800">游戏菜单屏幕添加按钮回调</h2>
 * <p style="color:FFC800">设置在游戏菜单屏幕中添加按钮</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface GameMenuScreenAddButtonCallback {
	@SilkApi
	Event<GameMenuScreenAddButtonCallback> EVENT = EventFactory.createArrayBacked(GameMenuScreenAddButtonCallback.class, listeners -> (adder, client, parent) -> {
		for (GameMenuScreenAddButtonCallback event : listeners) event.add(adder, client, parent);
	});
	
	void add(GridWidget.Adder adder, MinecraftClient client, Screen parent);
}
