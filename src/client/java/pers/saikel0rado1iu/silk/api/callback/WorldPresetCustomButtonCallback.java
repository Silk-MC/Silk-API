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
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.HashMap;
import java.util.Map;

/**
 * <p><b style="color:FFC800"><font size="+1">世界预设自定义按钮回调</font></b></p>
 * <p style="color:FFC800">设置在创建世界屏幕中给世界类型添加自定义按钮按钮</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface WorldPresetCustomButtonCallback {
	@SilkApi
	Event<WorldPresetCustomButtonCallback> EVENT = EventFactory.createArrayBacked(WorldPresetCustomButtonCallback.class, listeners -> (worldType, client, parent) -> {
		ButtonWidget.PressAction onPress;
		for (WorldPresetCustomButtonCallback event : listeners) if ((onPress = event.canAdd(worldType, client, parent)) != null) return onPress;
		return null;
	});
	
	@Nullable
	ButtonWidget.PressAction canAdd(WorldCreator.WorldType worldType, MinecraftClient client, Screen parent);
	
	@ApiStatus.Internal
	class Data {
		@ApiStatus.Internal
		public static final Map<WorldCreator.WorldType, ButtonWidget> WORLD_CUSTOMS = new HashMap<>(8);
		@ApiStatus.Internal
		public static WorldCreator worldCreator;
		
		@ApiStatus.Internal
		public static MinecraftClient client;
		
		@ApiStatus.Internal
		public static Screen parent;
	}
}
