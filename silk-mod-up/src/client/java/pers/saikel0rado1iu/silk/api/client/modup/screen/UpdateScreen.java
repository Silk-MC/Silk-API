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

package pers.saikel0rado1iu.silk.api.client.modup.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.codex.OptionTexts;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.api.modup.UpdateData;
import pers.saikel0rado1iu.silk.api.modup.UpdateMode;
import pers.saikel0rado1iu.silk.api.modup.UpdateSettings;
import pers.saikel0rado1iu.silk.api.client.pattern.screen.BaseScreen;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.api.client.modup.ClientUpdateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.util.Util.OperatingSystem.WINDOWS;

/**
 * <h2 style="color:FFC800">更新屏幕</h2>
 * 基础更新屏幕类，所有更新提示都在此基础上拓展
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class UpdateScreen extends BaseScreen {
	protected static final int ICON_SIZE = 72;
	protected static final int INTERVAL = 6;
	protected static final int BUTTON_SPACING = 2;
	protected final UpdateData updateData;
	protected final ClientUpdateManager updateManager;
	protected int screenWidth;
	protected int screenHeight;
	protected ButtonWidget updateNowButton;
	protected ButtonWidget confirmUpdateButton;
	private int transColor = 0xFF0000;
	
	protected UpdateScreen(@Nullable Screen parent, UpdateData updateData, ClientUpdateManager updateManager, Text title) {
		super(parent, title);
		this.updateData = updateData;
		this.updateManager = updateManager;
	}
	
	private static int colorCycling(int originValue) {
		int[] transColor = {(originValue & 0xFF0000) >> 16, (originValue & 0x00FF00) >> 8, originValue & 0x0000FF};
		if (transColor[0] == 0xFF && transColor[2] == 0 && transColor[1] != 0xFF) transColor[1] += 17;
		else if (transColor[1] == 0xFF && transColor[2] == 0 && transColor[0] != 0) transColor[0] -= 17;
		else if (transColor[0] == 0 && transColor[1] == 0xFF && transColor[2] != 0xFF) transColor[2] += 17;
		else if (transColor[0] == 0 && transColor[2] == 0xFF && transColor[1] != 0) transColor[1] -= 17;
		else if (transColor[1] == 0 && transColor[2] == 0xFF && transColor[0] != 0xFF) transColor[0] += 17;
		else transColor[2] -= 17;
		return transColor[0] << 16 | transColor[1] << 8 | transColor[2];
	}
	
	private static void openDownloadWeb(UpdateData updateData) {
		Util.getOperatingSystem().open(String.format("https://modrinth.com/mod/%s/version/%s", updateData.modData().slug(), updateData.modVersion()));
	}
	
	/**
	 * 配置函数, 设置屏幕大小参数
	 */
	@Override
	protected void init() {
		super.init();
		screenWidth = (int) (width / 2.5);
		screenHeight = (int) (height * 0.9);
		updateNowButton = updateNowButton();
		confirmUpdateButton = confirmUpdateButton();
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0, title, textRenderer).setMaxWidth(screenWidth - INTERVAL).setCentered(true);
		messageText.setPosition((width - messageText.getWidth()) / 2, (height - screenHeight) / 2 + INTERVAL);
		addDrawableChild(messageText);
		addDrawableChild(ButtonWidget.builder(Text.of(""), button -> {
		}).dimensions(width / 2 - ICON_SIZE / 2 - 1, (int) ((height - screenHeight) / 2F + (textRenderer.fontHeight + INTERVAL) * 2) - 1, ICON_SIZE + 2, ICON_SIZE + 2).build());
		IconWidget iconWidget;
		addDrawableChild(iconWidget = IconWidget.create(ICON_SIZE, ICON_SIZE, updateData.modData().icon().orElse(SilkModUp.getInstance().icon().orElseThrow()), ICON_SIZE, ICON_SIZE));
		iconWidget.setPosition(width / 2 - ICON_SIZE / 2, (int) ((height - screenHeight) / 2F + (textRenderer.fontHeight + INTERVAL) * 2));
		initWidgets(new GridWidget());
	}
	
	/**
	 * 提供函数, 注册背景屏幕大小标题与模组图标
	 */
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (updateNowButton != null) updateNowButton.setMessage(updateNowButton.getMessage().copy().setStyle(Style.EMPTY.withBold(true).withColor(transColor)));
		if (confirmUpdateButton != null)
			confirmUpdateButton.setMessage(confirmUpdateButton.getMessage().copy().setStyle(Style.EMPTY.withBold(true).withColor(transColor)));
		if (client != null && client.world != null) context.fillGradient(0, 0, width, height, -1072689136, -804253680);
		ButtonWidget.builder(Text.of(""), (button) -> {
				}).dimensions((width - (screenWidth + INTERVAL)) / 2, (height - (screenHeight + INTERVAL)) / 2, screenWidth + INTERVAL, screenHeight + INTERVAL)
				.build().render(context, mouseX, mouseY, delta);
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
		transColor = colorCycling(transColor);
		super.tick();
	}
	
	@Override
	public void close() {
		updateManager.setCanShowUpdateNotify(true);
		super.close();
	}
	
	protected ButtonWidget.Builder updateModeButton() {
		Supplier<Text> textSupplier = () -> ScreenTexts.composeGenericOptionText(OptionTexts.text(UpdateSettings.UPDATE_MODE), OptionTexts.text(UpdateSettings.UPDATE_MODE, updateData.getValue(UpdateSettings.UPDATE_MODE).toString().toLowerCase()));
		return ButtonWidget.builder(textSupplier.get(), button -> {
			List<UpdateMode> modes = new ArrayList<>(List.of(UpdateMode.values()));
			if (!Util.getOperatingSystem().equals(WINDOWS)) modes.remove(UpdateMode.AUTO_UPDATE);
			int index = modes.indexOf(updateData.getValue(UpdateSettings.UPDATE_MODE));
			updateData.updateSettings().setValue(UpdateSettings.UPDATE_MODE, modes.get(index == modes.size() - 1 ? 0 : index + 1));
			button.setMessage(textSupplier.get());
			updateData.updateSettings().save();
		});
	}
	
	protected ButtonWidget confirmUpdateButton() {
		return ButtonWidget.builder(WidgetTexts.text(SilkModUp.getInstance(), "update"), button -> {
			close();
			openDownloadWeb(updateData);
			MinecraftClient.getInstance().setScreen(new DownloadingScreen(parent, updateData, updateManager, title));
		}).build();
	}
	
	protected ButtonWidget updateNowButton() {
		return ButtonWidget.builder(WidgetTexts.text(SilkModUp.getInstance(), "update_now"), button -> {
			close();
			if (updateData.getValue(UpdateSettings.UPDATE_MODE) == UpdateMode.MANUAL_DOWNLOAD) openDownloadWeb(updateData);
			else MinecraftClient.getInstance().setScreen(new ConfirmDownloadScreen(parent, updateData, updateManager, title));
		}).build();
	}
	
	protected ButtonWidget.Builder notUpdateButton() {
		return ButtonWidget.builder(WidgetTexts.text(SilkModUp.getInstance(), "not_update").copy().formatted(Formatting.GRAY), button -> close());
	}
	
	protected ButtonWidget.Builder returnToGameButton() {
		return ButtonWidget.builder(Text.translatable("menu.returnToGame"), button -> close());
	}
	
	protected abstract void initWidgets(GridWidget gridWidget);
}
