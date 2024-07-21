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

package pers.saikel0rado1iu.silk.pattern.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * <h2 style="color:FFC800">基础屏幕</h2>
 * 用于实现本该实现但未实现的最最基础的功能屏幕
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class BaseScreen extends Screen {
	protected final Screen parent;
	
	protected BaseScreen(@Nullable Screen parent, Text title) {
		super(title);
		this.parent = parent;
	}
	
	/**
	 * 使用 Esc 关闭时能够回到父屏幕
	 */
	@Override
	public void close() {
		if (client == null) return;
		onCloseScreen();
		client.setScreen(parent);
	}
	
	/**
	 * 在关闭屏幕时的操作
	 */
	protected void onCloseScreen() {
	}
}
