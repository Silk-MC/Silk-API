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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import static pers.saikel0rado1iu.silk.util.update.CheckUpdateThread.State.THIS_MC_VER;

/**
 * <p><b style="color:FFC800"><font size="+1">此 MC 版本模组更新提示界面</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class ThisMcVerNotifyScreen extends UpdateScreen {
	private static final String KEY = THIS_MC_VER.toString().toLowerCase();
	protected final boolean canTrust;
	
	public ThisMcVerNotifyScreen(Screen parent, UpdateShow show, boolean canTrust) {
		super(parent, show, show.getTitle(KEY));
		this.canTrust = canTrust;
	}
	
	@Override
	protected void init() {
		super.init();
		if (updateShow.getUpdateThread().getUpdatingFail()) MinecraftClient.getInstance().setScreen(new UpdateFailWarningScreen(parent, updateShow, canTrust));
		// 添加信息
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0, updateShow.getVerText(KEY), textRenderer).setMaxWidth(screenWidth - INTERVAL).setCentered(true);
		int height1 = height - (height - screenHeight) - BUTTON_SPACING * 4;
		int height2 = (height - screenHeight) / 2 + textRenderer.fontHeight + ICON_SIZE + INTERVAL;
		int height3 = height1 - height2;
		messageText.setPosition((width - messageText.getWidth()) / 2, height2 + (height3) / 2);
		addDrawableChild(messageText);
		// 按钮
		int fullButtonWidth = screenWidth - 6;
		int fullButtonX = (width - (screenWidth - 6)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		int halfButtonWidth = fullButtonWidth / 2 - 1;
		int halfButtonX = fullButtonX + halfButtonWidth + 2;
		addDrawableChild(ScreenUtil.linkButton(parent, updateShow.getMod(), ModBasicData.LinkType.SUPPORT, canTrust)
				.dimensions(fullButtonX, buttonY - BUTTON_SPACING * 4, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(ScreenUtil.linkButton(parent, updateShow.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
				.dimensions(halfButtonX, buttonY - BUTTON_SPACING * 4, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(updateConfigButton().dimensions(fullButtonX, buttonY - BUTTON_SPACING * 3, fullButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(updateModeButton().createWidget(null, fullButtonX, buttonY - BUTTON_SPACING * 2, fullButtonWidth));
		ButtonWidget updateNowButton = updateNowButton();
		updateNowButton.setPosition(fullButtonX, buttonY - BUTTON_SPACING);
		updateNowButton.setWidth(halfButtonWidth);
		addDrawableChild(updateNowButton);
		addDrawableChild(notUpdateButton().dimensions(halfButtonX, buttonY - BUTTON_SPACING, halfButtonWidth, BUTTON_HEIGHT).build());
	}
	
	/*	@Override
	protected void init() {
		// 赞助按钮、官网按钮、关闭更新按钮、关闭新MC更新按钮、立即更新按钮、暂时不用按钮
		super.init();
		// 添加提示消息文本
		String updateModVersion = updateShow.getUpdateThread().getUpdateModVer().substring(updateShow.getUpdateThread().getUpdateModVer().indexOf("-") + 1);
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
		if (updateShow.getUpdateThread().getUpdatingFail()) {
			clearChildren();
			addDrawableChild(ButtonWidget.builder(Text.translatable("menu.returnToGame"), (button) -> {
						updateShow.getUpdateThread().setCanCheckUpdate(true);
						close();
					})
					.dimensions(fullButtonX, buttonY - buttonSpacing, fullButtonWidth, buttonHeight).build());
		} else {
			if (!updating) {
				addDrawableChild(ScreenUtil.linkButton(parent, updateShow.getMod(), ModBasicData.LinkType.SUPPORT, canTrust)
						.dimensions(fullButtonX, buttonY - buttonSpacing * 4, halfButtonWidth, buttonHeight).build());
				addDrawableChild(ScreenUtil.linkButton(parent, updateShow.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
						.dimensions(halfButtonX, buttonY - buttonSpacing * 4, halfButtonWidth, buttonHeight).build());
				addDrawableChild(ButtonWidget.builder(Text.of(""), (button) -> {
							// 使用断言消除 setScreen NullPointerException警告
							assert client != null;
							client.setScreen(new ConfigScreen(this, updateShow.getConfigData()));
						})
						.dimensions(fullButtonX, buttonY - buttonSpacing * 3, fullButtonWidth, buttonHeight).build());
				addDrawableChild(new SimpleOption<>("", value -> Tooltip.of(Text.translatable("")),
						(optionText, value) -> Text.translatable(""),
						new SimpleOption.MaxSuppliableIntCallbacks(0, () -> Util.getOperatingSystem().equals(WINDOWS) ? 2 : 1, Util.getOperatingSystem().equals(WINDOWS) ? 2 : 1),
						Arrays.stream(UpdateData.Mode.values()).toList().indexOf(updateShow.getUpdateData().getUpdateMode()),
						value -> {
							updateShow.getUpdateData().setUpdateMode(UpdateData.Mode.values()[value]);
							updateShow.getUpdateData().save();
						}).createWidget(null, fullButtonX, buttonY - buttonSpacing * 2, fullButtonWidth));
				addDrawableChild(updateNowButton = ButtonWidget.builder(updateText.copy().setStyle(Style.EMPTY.withBold(true)), (button) -> {
							clearChildren();
							updating = updateShow.updateMod();
							if (updateShow.getUpdateData().getUpdateMode() == MANUAL_DOWNLOAD) close();
						})
						.dimensions(fullButtonX, buttonY - buttonSpacing, halfButtonWidth, buttonHeight).build());
				addDrawableChild(ButtonWidget.builder(returnText.copy().formatted(Formatting.GRAY), (button) -> {
							updateShow.getUpdateThread().setCanCheckUpdate(true);
							close();
						})
						.dimensions(halfButtonX, buttonY - buttonSpacing, halfButtonWidth, buttonHeight).build());
			} else if (updateShow.getUpdateProgress() == 100) {
				if (updateShow.getUpdateData().getUpdateMode() == AUTO_DOWNLOAD) {
					addDrawableChild(ButtonWidget.builder(autoDownloadDoneText, (button) -> {
								Util.getOperatingSystem().open(new File(updateShow.getMod().getPath().toString()));
								Objects.requireNonNull(client).scheduleStop();
							})
							.dimensions(fullButtonX, buttonY - buttonSpacing * 2, fullButtonWidth, buttonHeight).build());
					addDrawableChild(ButtonWidget.builder(returnText.copy().formatted(Formatting.GRAY), (button) -> {
								updateShow.getUpdateThread().setCanCheckUpdate(true);
								close();
							})
							.dimensions(fullButtonX, buttonY - buttonSpacing, fullButtonWidth, buttonHeight).build());
				} else
					addDrawableChild(ButtonWidget.builder(autoUpdateDoneText, (button) -> {
								// 使用断言消除 setScreen NullPointerException警告
								try {
									Runtime.getRuntime().exec("cmd /A /C start \"\" /D \""
											+ updateShow.getMod().getPath() + "\" \"" + updateShow.getUpdateData().getBatName() + "\" CHCP 65001 ");
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
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		int buttonHeight = 20;
		if (updateShow.getUpdateThread().getUpdatingFail()) {
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
				updateNowButton.setMessage(updateText.copy().setStyle(Style.EMPTY.withBold(true).withColor(
						transColor = ScreenUtil.colorCycling(transColor))));
				// 更新配置按钮文本
				int buttonSpacing = buttonHeight + 3;
				int buttonY = (height - (height - screenHeight) / 2);
				Text updateConfigButtonText = composeGenericOptionText(Text.translatable(ScreenUtil.widgetText(updateShow.getMod(), UPDATE_CONFIG)), Text.of(""));
				context.drawTextWithShadow(textRenderer, updateConfigButtonText,
						(width - (textRenderer.getWidth(updateConfigButtonText))) / 2,
						buttonY - buttonSpacing * 3 + 6, 0xFFFFFF);
				// 更新方式按钮文本
				Text updateModeOptionText;
				switch (updateShow.getUpdateData().getUpdateMode()) {
					case MANUAL_DOWNLOAD -> updateModeOptionText = Text.translatable(("update_mode.manual_download"));
					case AUTO_DOWNLOAD -> updateModeOptionText = Text.translatable(("update_mode.auto_download"));
					case AUTO_UPDATE -> updateModeOptionText = Text.translatable(("update_mode.auto_update"));
					default -> updateModeOptionText = Text.literal(updateShow.getUpdateData().getUpdateMode().toString());
				}
				Text updateModeText = composeGenericOptionText(Text.translatable(("update_mode")), updateModeOptionText);
				context.drawTextWithShadow(textRenderer, updateModeText,
						(width - (textRenderer.getWidth(updateModeText))) / 2,
						buttonY - buttonSpacing * 2 + 6, 0xFFFFFF);
			} else {
				if (updateShow.getUpdateProgress() < 100) {
					context.drawCenteredTextWithShadow(textRenderer,
							Text.translatable(ScreenUtil.widgetText(updateShow.getMod(), "updating"), String.format("%.2f", updateShow.getUpdateProgress())),
							width / 2,
							(height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72)) / 2 - 6,
							0xFFFFFF);
				} else {
					init();
					if (updateShow.getUpdateData().getUpdateMode() == MANUAL_DOWNLOAD)
						autoDownloadMessage.drawCenterWithShadow(context, width / 2,
								(height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72) - buttonHeight * 2 - 3) / 2 - 12, 10, 0xFFFFFF);
					else autoUpdateMessage.drawCenterWithShadow(context, width / 2,
							(int) ((height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72) - buttonHeight * 1.5) / 2 - 12), 10, 0xFFFFFF);
					
				}
			}
		}
	}*/
}
