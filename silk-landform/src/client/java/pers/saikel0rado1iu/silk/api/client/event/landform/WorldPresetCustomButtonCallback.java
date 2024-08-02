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

package pers.saikel0rado1iu.silk.api.client.event.landform;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">世界预设自定义按钮回调</h2>
 * 设置在创建世界屏幕中给世界类型添加自定义按钮按钮
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@FunctionalInterface
public interface WorldPresetCustomButtonCallback {
	/**
	 * 事件
	 */
	Event<WorldPresetCustomButtonCallback> EVENT = EventFactory.createArrayBacked(WorldPresetCustomButtonCallback.class, listeners -> (worldType, client, parent) -> {
		Optional<ButtonWidget.PressAction> onPress;
		for (WorldPresetCustomButtonCallback event : listeners) if ((onPress = event.addPressAction(worldType, client, parent)).isPresent()) return onPress;
		return Optional.empty();
	});
	
	/**
	 * 添加按下行为
	 *
	 * @param worldType 世界类型
	 * @param client    客户端
	 * @param parent    父屏幕
	 * @return 按下行为
	 */
	Optional<ButtonWidget.PressAction> addPressAction(WorldCreator.WorldType worldType, MinecraftClient client, Screen parent);
	
	/**
	 * 混入数据
	 */
	final class MixinData {
		/**
		 * customizeButton
		 */
		public static ButtonWidget customizeButton;
	}
}
