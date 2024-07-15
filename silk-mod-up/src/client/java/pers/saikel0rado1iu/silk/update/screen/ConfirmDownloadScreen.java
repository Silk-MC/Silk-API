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

package pers.saikel0rado1iu.silk.update.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.modup.UpdateData;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.update.ClientUpdateManager;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">确认下载屏幕</h2>
 * 下载模组时弹出的确认网页跳转屏幕
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class ConfirmDownloadScreen extends UpdateScreen {
	protected ConfirmDownloadScreen(Optional<Screen> parent, UpdateData updateData, ClientUpdateManager updateManager, Text title) {
		super(parent, updateData, updateManager, title);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	protected void initWidgets(GridWidget gridWidget) {
		// 添加按钮
		int fullButtonWidth = screenWidth - INTERVAL;
		gridWidget.getMainPositioner().margin(0, BUTTON_SPACING, BUTTON_SPACING, 0);
		GridWidget.Adder adder = gridWidget.createAdder(2);
		confirmUpdateButton.setWidth(fullButtonWidth);
		adder.add(confirmUpdateButton, 2);
		adder.add(notUpdateButton().width(fullButtonWidth).build(), 2);
		gridWidget.refreshPositions();
		SimplePositioningWidget.setPos(gridWidget, 1, screenHeight - BUTTON_SPACING - gridWidget.getHeight() + (height - screenHeight) / 2, width, height, 0.5F, 0);
		gridWidget.forEachChild(this::addDrawableChild);
		// 添加信息
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0,
				WidgetTexts.text(SilkModUp.getInstance(), "confirm_update"), textRenderer)
				.setMaxWidth(screenWidth - INTERVAL * 2);
		messageText.setPosition((width - messageText.getWidth()) / 2, (height - messageText.getHeight()) / 2 + 31);
		addDrawableChild(messageText);
	}
}
