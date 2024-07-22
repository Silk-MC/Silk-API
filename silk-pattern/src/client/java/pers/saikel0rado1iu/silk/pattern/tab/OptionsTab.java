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

package pers.saikel0rado1iu.silk.pattern.tab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.pattern.widget.OptionMenuWidget;

/**
 * <h2 style="color:FFC800">选项选项卡</h2>
 * 用于创建具有选项列表的选项卡
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class OptionsTab extends ScreenTab {
	protected OptionMenuWidget optionMenuWidget;
	
	/**
	 * @param modPass 模组通
	 * @param key     标签页键
	 */
	public OptionsTab(ModPass modPass, String key) {
		super(modPass, key);
	}
	
	@Override
	public void init(MinecraftClient client, TextRenderer textRenderer, int width, int height) {
		optionMenuWidget = new OptionMenuWidget(client, width, 32, height - 40, 25);
		addOptions(optionMenuWidget);
		addSelectableChild(optionMenuWidget);
	}
	
	@Override
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
		super.render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
		optionMenuWidget.render(context, mouseX, mouseY, delta);
	}
	
	protected abstract void addOptions(OptionMenuWidget optionMenuWidget);
}
