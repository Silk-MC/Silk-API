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

package pers.saikel0rado1iu.silk.util.screen.update;

import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.config.ConfigScreen;
import pers.saikel0rado1iu.silk.util.update.UpdateData;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static net.minecraft.screen.ScreenTexts.composeGenericOptionText;
import static net.minecraft.util.Util.OperatingSystem.WINDOWS;
import static pers.saikel0rado1iu.silk.util.update.UpdateData.Mode.AUTO_DOWNLOAD;
import static pers.saikel0rado1iu.silk.util.update.UpdateData.Mode.MANUAL_DOWNLOAD;
import static pers.saikel0rado1iu.silk.util.update.UpdateData.UPDATE_CONFIG;
import static pers.saikel0rado1iu.silk.util.update.UpdateData.UPDATE_MODE;

/**
 * <p><b style="color:FFC800"><font size="+1">此 MC 版本模组更新提示界面</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class ThisMcVerNotifyScreen extends UpdateScreen {
	protected final boolean canTrust;
	protected final int[] transColor = {0xFF, 0, 0};
	protected MultilineText autoDownloadMessage;
	protected MultilineText autoUpdateMessage;
	protected boolean updating;
	
	/**
	 * 构造更新屏幕类
	 */
	public ThisMcVerNotifyScreen(Screen parent, UpdateData data, boolean canTrust) {
		super(parent, data, Text.translatable(ScreenUtil.widgetText(data.getMod(), "this_mc_ver_update_notify"))
				.setStyle(Style.EMPTY.withBold(true).withColor(data.getMod().getThemeColor())));
		this.canTrust = canTrust;
	}
	
	/**
	 * 配置函数, 所以要用到的按钮等都需要在此函数中注册
	 */
	@Override
	protected void init() {
		// 赞助按钮、官网按钮、关闭更新按钮、关闭新MC更新按钮、立即更新按钮、暂时不用按钮
		super.init();
		// 添加提示消息文本
		String updateModVersion = data.getUpdateModVersion().substring(data.getUpdateModVersion().indexOf("-") + 1);
		messageText = MultilineText.create(textRenderer, Text.translatable("text.spontaneous_replace.mod_update_notification", updateModVersion), screenWidth - 6);
		autoDownloadMessage = MultilineText.create(textRenderer, Text.translatable("text.spontaneous_replace.auto_download", updateModVersion), screenWidth - 6);
		autoUpdateMessage = MultilineText.create(textRenderer, Text.translatable("text.spontaneous_replace.auto_update", updateModVersion), screenWidth - 6);
		int fullButtonWidth = screenWidth - 6;
		int buttonHeight = 20;
		int buttonSpacing = buttonHeight + 3;
		int fullButtonX = (width - (screenWidth - 6)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		int halfButtonWidth = fullButtonWidth / 2 - 1;
		int halfButtonX = fullButtonX + halfButtonWidth + 2;
		// 添加按钮
		if (data.getUpdatingFail()) {
			clearChildren();
			addDrawableChild(ButtonWidget.builder(Text.translatable("menu.returnToGame"), (button) -> {
						data.setCanCheckUpdate(true);
						close();
					})
					.dimensions(fullButtonX, buttonY - buttonSpacing, fullButtonWidth, buttonHeight).build());
		} else {
			if (!updating) {
				addDrawableChild(ScreenUtil.linkButton(parent, data.getMod(), ModBasicData.LinkType.SUPPORT, canTrust)
						.dimensions(fullButtonX, buttonY - buttonSpacing * 4, halfButtonWidth, buttonHeight).build());
				addDrawableChild(ScreenUtil.linkButton(parent, data.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
						.dimensions(halfButtonX, buttonY - buttonSpacing * 4, halfButtonWidth, buttonHeight).build());
				addDrawableChild(ButtonWidget.builder(Text.of(""), (button) -> {
							// 使用断言消除 setScreen NullPointerException警告
							assert client != null;
							client.setScreen(new ConfigScreen(this, data.getData()));
						})
						.dimensions(fullButtonX, buttonY - buttonSpacing * 3, fullButtonWidth, buttonHeight).build());
				addDrawableChild(new SimpleOption<>("", value -> Tooltip.of(Text.translatable("")),
						(optionText, value) -> Text.translatable(""),
						new SimpleOption.MaxSuppliableIntCallbacks(0, () -> Util.getOperatingSystem().equals(WINDOWS) ? 2 : 1, Util.getOperatingSystem().equals(WINDOWS) ? 2 : 1),
						Arrays.stream(UpdateData.Mode.values()).toList().indexOf(data.getData().getConfig(UPDATE_MODE, UpdateData.Mode.class)),
						value -> {
							data.getData().setConfig(UPDATE_MODE, UpdateData.Mode.values()[value]);
							data.getData().getMainConfig().writer().save();
						}).createWidget(null, fullButtonX, buttonY - buttonSpacing * 2, fullButtonWidth));
				addDrawableChild(updateButton = ButtonWidget.builder(updateText.copy().setStyle(Style.EMPTY.withBold(true)), (button) -> {
							clearChildren();
							updating = data.updateMod(data.getUpdateLink());
							if (data.getData().getConfig(UPDATE_MODE, UpdateData.Mode.class) == MANUAL_DOWNLOAD) close();
						})
						.dimensions(fullButtonX, buttonY - buttonSpacing, halfButtonWidth, buttonHeight).build());
				addDrawableChild(ButtonWidget.builder(returnText.copy().formatted(Formatting.GRAY), (button) -> {
							data.setCanCheckUpdate(true);
							close();
						})
						.dimensions(halfButtonX, buttonY - buttonSpacing, halfButtonWidth, buttonHeight).build());
			} else if (data.getUpdateProgress() == 100) {
				if (data.getData().getConfig(UPDATE_MODE, UpdateData.Mode.class) == AUTO_DOWNLOAD) {
					addDrawableChild(ButtonWidget.builder(autoDownloadDoneText, (button) -> {
								Util.getOperatingSystem().open(new File(data.getMod().getPath().toString()));
								Objects.requireNonNull(client).scheduleStop();
							})
							.dimensions(fullButtonX, buttonY - buttonSpacing * 2, fullButtonWidth, buttonHeight).build());
					addDrawableChild(ButtonWidget.builder(returnText.copy().formatted(Formatting.GRAY), (button) -> {
								data.setCanCheckUpdate(true);
								close();
							})
							.dimensions(fullButtonX, buttonY - buttonSpacing, fullButtonWidth, buttonHeight).build());
				} else
					addDrawableChild(ButtonWidget.builder(autoUpdateDoneText, (button) -> {
								// 使用断言消除 setScreen NullPointerException警告
								try {
									Runtime.getRuntime().exec("cmd /A /C start \"\" /D \""
											+ data.getMod().getPath() + "\" \"" + data.batName + "\" CHCP 65001 ");
									assert client != null;
									client.scheduleStop();
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
							})
							.dimensions(fullButtonX, (int) (buttonY - buttonSpacing * 1.5), fullButtonWidth, buttonHeight).build());
			}
		}
	}
	
	/**
	 * 每刻修改颜色循环以达到在任何界面的循环速度一致
	 */
	@Override
	public void tick() {
		super.tick();
		// 修改循环按钮颜色
		if (transColor[0] == 0xFF && transColor[2] == 0 && transColor[1] != 0xFF) transColor[1] += 17;
		else if (transColor[1] == 0xFF && transColor[2] == 0 && transColor[0] != 0) transColor[0] -= 17;
		else if (transColor[0] == 0 && transColor[1] == 0xFF && transColor[2] != 0xFF) transColor[2] += 17;
		else if (transColor[0] == 0 && transColor[2] == 0xFF && transColor[1] != 0) transColor[1] -= 17;
		else if (transColor[1] == 0 && transColor[2] == 0xFF && transColor[0] != 0xFF) transColor[0] += 17;
		else transColor[2] -= 17;
	}
	
	/**
	 * 渲染提供函数, 所需要使用的所有特性都要在此函数中注册
	 */
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		int buttonHeight = 20;
		if (data.getUpdatingFail()) {
			init();
			context.drawCenteredTextWithShadow(textRenderer,
					updatingFailText,
					width / 2,
					(height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72) - buttonHeight - 3) / 2 - 6,
					0xFFFFFF);
		} else {
			if (!updating) {
				// 渲染提示消息
				messageText.drawWithShadow(context, (width - screenWidth) / 2 + 3, height / 2 - 9, 10, 0xFFFFFF);
				// 修改循环按钮颜色
				int color = transColor[0] << 16 | transColor[1] << 8 | transColor[2];
				updateButton.setMessage(updateText.copy().setStyle(Style.EMPTY.withBold(true).withColor(color)));
				// 更新配置按钮文本
				int buttonSpacing = buttonHeight + 3;
				int buttonY = (height - (height - screenHeight) / 2);
				Text updateConfigButtonText = composeGenericOptionText(Text.translatable(ScreenUtil.widgetText(data.getMod(), UPDATE_CONFIG)), Text.of(""));
				context.drawTextWithShadow(textRenderer, updateConfigButtonText,
						(width - (textRenderer.getWidth(updateConfigButtonText))) / 2,
						buttonY - buttonSpacing * 3 + 6, 0xFFFFFF);
				// 更新方式按钮文本
				Text updateModeOptionText;
				switch (data.getData().getConfig(UPDATE_MODE, UpdateData.Mode.class)) {
					case MANUAL_DOWNLOAD -> updateModeOptionText = Text.translatable(("update_mode.manual_download"));
					case AUTO_DOWNLOAD -> updateModeOptionText = Text.translatable(("update_mode.auto_download"));
					case AUTO_UPDATE -> updateModeOptionText = Text.translatable(("update_mode.auto_update"));
					default -> updateModeOptionText = Text.literal(data.getData().getConfig(UPDATE_MODE, UpdateData.Mode.class).toString());
				}
				Text updateModeText = composeGenericOptionText(Text.translatable(("update_mode")), updateModeOptionText);
				context.drawTextWithShadow(textRenderer, updateModeText,
						(width - (textRenderer.getWidth(updateModeText))) / 2,
						buttonY - buttonSpacing * 2 + 6, 0xFFFFFF);
			} else {
				if (data.getUpdateProgress() < 100) {
					context.drawCenteredTextWithShadow(textRenderer,
							Text.translatable(ScreenUtil.widgetText(data.getMod(), "updating"), String.format("%.2f", data.getUpdateProgress())),
							width / 2,
							(height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72)) / 2 - 6,
							0xFFFFFF);
				} else {
					init();
					if (data.getData().getConfig(UPDATE_MODE, UpdateData.Mode.class) == MANUAL_DOWNLOAD)
						autoDownloadMessage.drawCenterWithShadow(context, width / 2,
								(height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72) - buttonHeight * 2 - 3) / 2 - 12, 10, 0xFFFFFF);
					else autoUpdateMessage.drawCenterWithShadow(context, width / 2,
							(int) ((height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72) - buttonHeight * 1.5) / 2 - 12), 10, 0xFFFFFF);
					
				}
			}
		}
	}
}
