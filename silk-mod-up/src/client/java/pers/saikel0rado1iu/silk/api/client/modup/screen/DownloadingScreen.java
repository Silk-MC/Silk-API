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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.api.modup.ModUpdater;
import pers.saikel0rado1iu.silk.api.modup.UpdateData;
import pers.saikel0rado1iu.silk.api.modup.UpdateMode;
import pers.saikel0rado1iu.silk.api.modup.UpdateSettings;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.api.client.modup.ClientUpdateManager;

/**
 * <h2 style="color:FFC800">下载中屏幕</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class DownloadingScreen extends UpdateScreen {
	protected final ModUpdater modUpdater;
	
	protected DownloadingScreen(@Nullable Screen parent, UpdateData updateData, ClientUpdateManager updateManager, Text title) {
		super(parent, updateData, updateManager, title);
		this.modUpdater = ModUpdater.update(this.updateData);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		MultilineText.create(textRenderer, Text.translatable(WidgetTexts.textKey(SilkModUp.getInstance(), "downloading"), updateData.modData().i18nName(), modUpdater.getProgress()), screenWidth - INTERVAL * 2)
				.drawCenterWithShadow(context, width / 2, height / 2 + 35);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (Double.parseDouble(modUpdater.getProgress()) == 100) {
			if (updateData.getValue(UpdateSettings.UPDATE_MODE) == UpdateMode.AUTO_DOWNLOAD) {
				MinecraftClient.getInstance().setScreen(new DownloadedScreen(parent, updateData, updateManager, title));
			} else {
				MinecraftClient.getInstance().setScreen(new UpdatedScreen(parent, updateData, updateManager, title));
			}
		}
	}
	
	@Override
	protected void initWidgets(GridWidget gridWidget) {
		// 添加按钮
		int fullButtonWidth = screenWidth - INTERVAL;
		gridWidget.getMainPositioner().margin(0, BUTTON_SPACING, BUTTON_SPACING, 0);
		GridWidget.Adder adder = gridWidget.createAdder(2);
		adder.add(returnToGameButton().width(fullButtonWidth).build(), 2);
		gridWidget.refreshPositions();
		SimplePositioningWidget.setPos(gridWidget, 1, screenHeight - BUTTON_SPACING - gridWidget.getHeight() + (height - screenHeight) / 2, width, height, 0.5F, 0);
		gridWidget.forEachChild(this::addDrawableChild);
	}
}
