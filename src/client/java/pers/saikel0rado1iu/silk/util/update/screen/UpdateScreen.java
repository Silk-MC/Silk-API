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

package pers.saikel0rado1iu.silk.util.update.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.config.ConfigScreen;
import pers.saikel0rado1iu.silk.util.screen.BaseScreen;
import pers.saikel0rado1iu.silk.util.update.UpdateData;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import java.util.Arrays;

import static pers.saikel0rado1iu.silk.util.update.UpdateData.Mode.MANUAL_DOWNLOAD;

/**
 * <p><b style="color:FFC800"><font size="+1">基础更新屏幕类，所有更新提示都在此基础上拓展</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public abstract class UpdateScreen extends BaseScreen {
	protected static final int ICON_SIZE = 72;
	protected static final int BUTTON_HEIGHT = 20;
	protected static final int INTERVAL = 6;
	protected static final int BUTTON_SPACING = BUTTON_HEIGHT + INTERVAL / 2;
	protected final Text title;
	protected final UpdateShow updateShow;
	protected int screenWidth;
	protected int screenHeight;
	protected MultilineText messageText;
	private ButtonWidget updateButton;
	private int transColor = 0xFF0000;
	
	protected UpdateScreen(Screen parent, UpdateShow updateShow, Text title) {
		super(parent, Text.of(""));
		this.updateShow = updateShow;
		this.title = title;
		//this.autoDownloadDoneText = Text.translatable(ScreenUtil.widgetText(updateShow.getMod(), "auto_download_done")).setStyle(Style.EMPTY.withBold(true));
		//this.autoUpdateDoneText = Text.translatable(ScreenUtil.widgetText(updateShow.getMod(), "auto_update_done")).setStyle(Style.EMPTY.withBold(true));
		//this.updatingFailText = Text.translatable(ScreenUtil.widgetText(updateShow.getMod(), "updating_fail")).setStyle(Style.EMPTY.withBold(true));
		this.updateShow.getUpdateThread().setCanCheckUpdate(false);
		this.updateShow.setCanShowScreen(false);
	}
	
	/**
	 * 配置函数, 设置屏幕大小参数
	 */
	@Override
	protected void init() {
		super.init();
		screenWidth = (int) (width / 2.5);
		screenHeight = (int) (height * 0.9);
		addDrawableChild(new TextWidget((width - textRenderer.getWidth(title)) / 2, (height - screenHeight) / 2 + INTERVAL,
				textRenderer.getWidth(title), textRenderer.fontHeight, title, textRenderer));
		addDrawableChild(ButtonWidget.builder(Text.of(""), button -> {
		}).dimensions(width / 2 - ICON_SIZE / 2 - 1, (int) ((height - screenHeight) / 2 + textRenderer.fontHeight + INTERVAL * 1.5F) - 1, ICON_SIZE + 2, ICON_SIZE + 2).build());
		addDrawableChild(new IconWidget(width / 2 - ICON_SIZE / 2, (int) ((height - screenHeight) / 2 + textRenderer.fontHeight + INTERVAL * 1.5F), ICON_SIZE, ICON_SIZE,
				updateShow.getMod().getIcon().orElse(Silk.DATA.getIcon().orElseThrow())));
	}
	
	/**
	 * 提供函数, 注册背景屏幕大小标题与模组图标
	 */
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		updateButton.setMessage(updateButton.getMessage().copy().setStyle(Style.EMPTY.withBold(true).withColor(transColor)));
		if (client != null && client.world != null) context.fillGradient(0, 0, width, height, -1072689136, -804253680);
		ButtonWidget.builder(Text.of(""), (button) -> {
				}).dimensions((width - (screenWidth + INTERVAL)) / 2, (height - (screenHeight + INTERVAL)) / 2, screenWidth + INTERVAL, screenHeight + INTERVAL)
				.build().render(context, mouseX, mouseY, delta);
		renderBackgroundTexture(context);
		super.render(context, mouseX, mouseY, delta);
	}
	
	/**
	 * 渲染背景纹理
	 */
	@Override
	public void renderBackgroundTexture(DrawContext context) {
		RenderSystem.setShaderTexture(0, OPTIONS_BACKGROUND_TEXTURE);
		RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1);
		context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, (width - screenWidth) / 2, (height - screenHeight) / 2, 0, 0, 0, screenWidth, screenHeight, 32, 32);
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
	
	@Override
	public void tick() {
		super.tick();
		transColor = ScreenUtil.colorCycling(transColor);
	}
	
	protected ButtonWidget.Builder updateConfigButton() {
		return ButtonWidget.builder(Text.translatable(ScreenUtil.configText(updateShow.getMod(), updateShow.getUpdateData().getKey())), (button) -> {
			if (client != null) client.setScreen(new ConfigScreen(this, updateShow.getConfigData(), updateShow.getUpdateData().getKey()));
		});
	}
	
	protected SimpleOption<?> updateModeButton() {
		return new SimpleOption<>("", value -> Tooltip.of(Text.translatable("")),
				(optionText, value) -> Text.translatable(""),
				new SimpleOption.MaxSuppliableIntCallbacks(0, () -> Util.getOperatingSystem().equals(Util.OperatingSystem.WINDOWS) ? 2 : 1, Util.getOperatingSystem().equals(Util.OperatingSystem.WINDOWS) ? 2 : 1),
				Arrays.stream(UpdateData.Mode.values()).toList().indexOf(updateShow.getUpdateData().getUpdateMode()),
				value -> {
					updateShow.getUpdateData().setUpdateMode(UpdateData.Mode.values()[value]);
					updateShow.getUpdateData().save();
				});
	}
	
	protected ButtonWidget.Builder returnToGameButton() {
		return ButtonWidget.builder(Text.translatable("menu.returnToGame"), button -> {
			updateShow.getUpdateThread().setCanCheckUpdate(true);
			close();
		});
	}
	
	protected ButtonWidget updateNowButton() {
		return updateButton = ButtonWidget.builder(Text.translatable(ScreenUtil.widgetText(updateShow.getMod(), "update_now")), button -> {
			if (updateShow.getUpdateData().getUpdateMode() == MANUAL_DOWNLOAD) close();
		}).dimensions(0, 0, 0, BUTTON_HEIGHT).build();
	}
	
	protected ButtonWidget.Builder notUpdateButton() {
		return ButtonWidget.builder(Text.translatable(ScreenUtil.widgetText(updateShow.getMod(), "not_update")).copy().formatted(Formatting.GRAY), button -> {
			updateShow.getUpdateThread().setCanCheckUpdate(true);
			close();
		});
	}
}
