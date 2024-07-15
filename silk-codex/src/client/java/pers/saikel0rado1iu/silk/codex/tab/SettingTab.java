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

package pers.saikel0rado1iu.silk.codex.tab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import pers.saikel0rado1iu.silk.codex.SettingData;
import pers.saikel0rado1iu.silk.codex.SettingOptionHelper;
import pers.saikel0rado1iu.silk.codex.SettingType;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.pattern.tab.ScreenTab;
import pers.saikel0rado1iu.silk.pattern.widget.OptionMenuWidget;

/**
 * <h2 style="color:FFC800">设置选项卡</h2>
 * 用于模组设置选项卡
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class SettingTab extends ScreenTab {
	private final boolean isDouble;
	private final SettingData settingData;
	private OptionMenuWidget optionMenuWidget;
	
	/**
	 * @param modPass     模组通
	 * @param settingData 设置数据
	 */
	public SettingTab(ModPass modPass, SettingData settingData) {
		this(modPass, settingData, false);
	}
	
	/**
	 * @param modPass     模组通
	 * @param settingData 设置数据
	 * @param isDouble    是否为双按钮
	 */
	public SettingTab(ModPass modPass, SettingData settingData, boolean isDouble) {
		super(modPass, "settings");
		this.settingData = settingData;
		this.isDouble = isDouble;
	}
	
	/**
	 * 初始化方法
	 *
	 * @param client       客户端实例
	 * @param textRenderer 文本渲染器
	 * @param width        屏幕宽度
	 * @param height       屏幕高度
	 */
	@Override
	public void init(MinecraftClient client, TextRenderer textRenderer, int width, int height) {
		optionMenuWidget = new OptionMenuWidget(client, width, 32, height - 40, 25);
		if (settingData.type() == SettingType.DEVELOPMENT) return;
		SettingOptionHelper.initOptions(optionMenuWidget, settingData, parent, isDouble, linkTrusted(), settingData::save);
		addSelectableChild(optionMenuWidget);
	}
	
	@Override
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
		super.render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
		SettingOptionHelper.formatOptionRender(optionMenuWidget, settingData);
		optionMenuWidget.render(context, mouseX, mouseY, delta);
	}
}
