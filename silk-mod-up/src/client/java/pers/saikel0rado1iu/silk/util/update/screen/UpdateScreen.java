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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.config.ConfigScreen;
import pers.saikel0rado1iu.silk.util.screen.BaseScreen;
import pers.saikel0rado1iu.silk.util.update.CheckUpdateThread;
import pers.saikel0rado1iu.silk.util.update.UpdateData;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.Util.OperatingSystem.WINDOWS;
import static pers.saikel0rado1iu.silk.util.update.UpdateData.Mode.MANUAL_DOWNLOAD;
import static pers.saikel0rado1iu.silk.util.update.UpdateData.UPDATE_MODE;

/**
 * <h2 style="color:FFC800">基础更新屏幕类，所有更新提示都在此基础上拓展</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
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
	private int transColor = 0xFF0000;
	private ButtonWidget updateButton;
	private TextWidget updateModeText;
	
	protected UpdateScreen(Screen parent, UpdateShow updateShow, Text title) {
		super(parent, Text.of(""));
		this.updateShow = updateShow;
		this.title = title;
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
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0, title, textRenderer).setMaxWidth(screenWidth - INTERVAL).setCentered(true);
		messageText.setPosition((width - messageText.getWidth()) / 2, (height - screenHeight) / 2 + INTERVAL);
		addDrawableChild(messageText);
		addDrawableChild(ButtonWidget.builder(Text.of(""), button -> {
		}).dimensions(width / 2 - ICON_SIZE / 2 - 1, (int) ((height - screenHeight) / 2 + textRenderer.fontHeight + INTERVAL * 1.5F) - 1, ICON_SIZE + 2, ICON_SIZE + 2).build());
		IconWidget iconWidget;
		addDrawableChild(iconWidget = IconWidget.create(ICON_SIZE, ICON_SIZE, updateShow.getMod().getIcon().orElse(Silk.DATA.getIcon().orElseThrow()), ICON_SIZE, ICON_SIZE));
		iconWidget.setPosition(width / 2 - ICON_SIZE / 2, (int) ((height - screenHeight) / 2 + textRenderer.fontHeight + INTERVAL * 1.5F));
	}
	
	/**
	 * 提供函数, 注册背景屏幕大小标题与模组图标
	 */
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (updateButton != null) updateButton.setMessage(updateButton.getMessage().copy().setStyle(Style.EMPTY.withBold(true).withColor(transColor)));
		if (client != null && client.world != null) context.fillGradient(0, 0, width, height, -1072689136, -804253680);
		ButtonWidget.builder(Text.of(""), (button) -> {
				}).dimensions((width - (screenWidth + INTERVAL)) / 2, (height - (screenHeight + INTERVAL)) / 2, screenWidth + INTERVAL, screenHeight + INTERVAL)
				.build().render(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		if (updateModeText != null) {
			updateModeText.setMessage(Text.translatable(TextUtil.configText(Silk.DATA, UpdateData.KEY + '.' + UPDATE_MODE + "_"),
					Text.translatable(TextUtil.configText(Silk.DATA, UpdateData.KEY + '.' + UPDATE_MODE + '.' + updateShow.getUpdateData().getUpdateMode().toString().toLowerCase()))));
			updateModeText.render(context, mouseX, mouseY, delta);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		transColor = ScreenUtil.colorCycling(transColor);
	}
	
	@SilkApi
	protected ButtonWidget.Builder updateConfigButton() {
		return ButtonWidget.builder(Text.translatable(TextUtil.configText(Silk.DATA, UpdateData.KEY)), (button) -> {
			if (client != null) client.setScreen(new ConfigScreen(this, updateShow.getConfigData(), UpdateData.KEY));
		});
	}
	
	@SilkApi
	protected ButtonWidget updateModeButton(int x, int y, int width, int height) {
		addDrawableChild(updateModeText = new TextWidget(x, y + (BUTTON_HEIGHT - textRenderer.fontHeight) / 2 + 1, width, textRenderer.fontHeight,
				Text.of(""), textRenderer).alignCenter());
		return ButtonWidget.builder(Text.of(""), button -> {
			List<UpdateData.Mode> modes = new ArrayList<>(List.of(UpdateData.Mode.values()));
			if (!Util.getOperatingSystem().equals(WINDOWS) || updateShow.getUpdateThread().getUpdateState() == CheckUpdateThread.State.NEW_MC_VER)
				modes.remove(UpdateData.Mode.AUTO_UPDATE);
			int index = modes.indexOf(updateShow.getUpdateData().getUpdateMode());
			updateShow.getUpdateData().setUpdateMode(modes.get(index == modes.size() - 1 ? 0 : index + 1));
			updateShow.getUpdateData().save();
		}).dimensions(x, y, width, height).build();
	}
	
	@SilkApi
	protected ButtonWidget.Builder returnToGameButton() {
		return ButtonWidget.builder(Text.translatable("menu.returnToGame"), button -> {
			updateShow.getUpdateThread().setCanCheckUpdate(true);
			close();
		});
	}
	
	@SilkApi
	protected ButtonWidget updateNowButton() {
		return updateButton = ButtonWidget.builder(Text.translatable(TextUtil.configText(Silk.DATA, "update_now")), button -> {
			close();
			if (updateShow.getUpdateData().getUpdateMode() != MANUAL_DOWNLOAD)
				MinecraftClient.getInstance().setScreen(new ConfirmDownloadScreen(parent, updateShow, title));
			else
				Util.getOperatingSystem().open("https://modrinth.com/mod/" + updateShow.getMod().getSlug() + "/version/" + updateShow.getUpdateThread().getUpdateModVer());
		}).dimensions(0, 0, 0, BUTTON_HEIGHT).build();
	}
	
	@SilkApi
	protected ButtonWidget.Builder notUpdateButton() {
		return ButtonWidget.builder(Text.translatable(TextUtil.configText(Silk.DATA, "not_update")).copy().formatted(Formatting.GRAY), button -> {
			updateShow.getUpdateThread().setCanCheckUpdate(true);
			close();
		});
	}
}
