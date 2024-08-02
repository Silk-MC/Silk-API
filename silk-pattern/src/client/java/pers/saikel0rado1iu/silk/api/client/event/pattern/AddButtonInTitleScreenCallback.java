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

package pers.saikel0rado1iu.silk.api.client.event.pattern;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">在标题屏幕中添加按钮回调</h2>
 * 用于在标题屏幕中添加一个按钮
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@FunctionalInterface
public interface AddButtonInTitleScreenCallback {
	/**
	 * 事件
	 */
	Event<AddButtonInTitleScreenCallback> EVENT = EventFactory.createArrayBacked(AddButtonInTitleScreenCallback.class, listeners -> new AddButtonInTitleScreenCallback() {
		@Override
		public <T extends Element & Drawable & Selectable> boolean add(MinecraftClient client, Screen parent, Function<T, T> addFunction, int y, int spacingY, boolean hasMainButton) {
			boolean result = false;
			for (AddButtonInTitleScreenCallback event : listeners) {
				if (!result) result = event.add(client, parent, addFunction, y, spacingY, false);
				else event.add(client, parent, addFunction, y, spacingY, true);
			}
			return result;
		}
	});
	
	/**
	 * 添加方法<br>
	 * 在此方法中添加按钮
	 *
	 * @param client        客户端实例
	 * @param parent        父屏幕
	 * @param addFunction   添加按钮方法
	 * @param y             当前坐标 Y
	 * @param spacingY      Y 轴间距
	 * @param hasMainButton 是否已有主按钮，如果为 {@code true} 您就不应该继续添加主按钮以免导致问题
	 * @param <T>           按钮方法参数类型
	 * @return 是否为屏幕正中心的主按钮
	 */
	<T extends Element & Drawable & Selectable> boolean add(MinecraftClient client, Screen parent, Function<T, T> addFunction, int y, int spacingY, boolean hasMainButton);
}
