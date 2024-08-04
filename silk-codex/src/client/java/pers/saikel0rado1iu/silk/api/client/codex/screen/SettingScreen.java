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

package pers.saikel0rado1iu.silk.api.client.codex.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.client.codex.SettingOptionHelper;
import pers.saikel0rado1iu.silk.api.client.pattern.screen.BaseScreen;
import pers.saikel0rado1iu.silk.api.client.pattern.screen.LinkTrusted;
import pers.saikel0rado1iu.silk.api.client.pattern.screen.ModScreen;
import pers.saikel0rado1iu.silk.api.client.pattern.widget.ButtonHelper;
import pers.saikel0rado1iu.silk.api.codex.*;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.impl.SilkCodex;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">设置屏幕</h2>
 * 用于构建设置屏幕
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class SettingScreen extends BaseScreen implements LinkTrusted {
	private final SettingData settingData;
	private final boolean isDouble;
	private final Runnable save;
	private OptionListWidget optionListWidget;
	
	/**
	 * @param parent      父屏幕
	 * @param settingData 设置数据
	 */
	public SettingScreen(@Nullable Screen parent, SettingData settingData) {
		this(parent, settingData, false);
	}
	
	/**
	 * @param parent      父屏幕
	 * @param settingData 设置数据
	 * @param isDouble    是否为双按钮
	 */
	public SettingScreen(@Nullable Screen parent, SettingData settingData, boolean isDouble) {
		this(parent, settingData, Optional.empty(), isDouble, settingData::save);
	}
	
	/**
	 * @param parent      父屏幕
	 * @param settingData 设置数据
	 * @param superOption 父设置选项
	 */
	public SettingScreen(@Nullable Screen parent, SettingData settingData, Optional<SettingOption<?>> superOption) {
		this(parent, settingData, superOption, false, settingData::save);
	}
	
	/**
	 * @param parent      父屏幕
	 * @param settingData 设置数据
	 * @param superOption 父设置选项
	 * @param isDouble    是否为双按钮
	 * @param save        保存方法，用于保存数据设置
	 */
	public SettingScreen(@Nullable Screen parent, SettingData settingData, Optional<SettingOption<?>> superOption, boolean isDouble, Runnable save) {
		this(parent, OptionTexts.text(superOption.orElse(SettingOption.of(settingData.modData().ofId("root"), OptionType.SWITCH))), settingData, isDouble, save);
	}
	
	/**
	 * @param parent      父屏幕
	 * @param title       显示标题
	 * @param settingData 设置数据
	 * @param isDouble    是否为双按钮
	 * @param save        保存方法，用于保存数据设置
	 */
	public SettingScreen(@Nullable Screen parent, Text title, SettingData settingData, boolean isDouble, Runnable save) {
		super(parent, settingData.type().getFormatText(title));
		this.settingData = settingData;
		this.isDouble = isDouble;
		this.save = save;
	}
	
	@Override
	protected void init() {
		super.init();
		// 添加"支持我们"按钮
		addDrawableChild(ButtonHelper.link(this, SilkCodex.getInstance(), ModData.LinkType.SUPPORT, linkTrusted()).position(width - 115, 6).width(110).build());
		// 添加完成/返回按钮
		if (parent != null && (parent instanceof SettingScreen || parent instanceof ModScreen)) {
			addDrawableChild(ButtonHelper.back(this).position(width / 2 - 100, height - 26).width(200).build());
		} else {
			addDrawableChild(ButtonHelper.done(this).position(width / 2 - 100, height - 26).width(200).build());
		}
		// 添加黑色透明窗口
		optionListWidget = new OptionListWidget(client, width, height - 32 - 32, 32, 25);
		if (settingData.type() == SettingType.DEVELOPMENT) return;
		SettingOptionHelper.initOptions(optionListWidget, settingData, this, isDouble, linkTrusted(), save);
		addSelectableChild(optionListWidget);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFF);
		SettingOptionHelper.formatOptionRender(optionListWidget, settingData);
		optionListWidget.render(context, mouseX, mouseY, delta);
	}
}
