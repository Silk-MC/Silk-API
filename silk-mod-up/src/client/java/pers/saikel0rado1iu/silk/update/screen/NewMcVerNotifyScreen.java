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
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modup.UpdateData;
import pers.saikel0rado1iu.silk.api.modup.UpdateState;
import pers.saikel0rado1iu.silk.pattern.widget.ButtonHelper;
import pers.saikel0rado1iu.silk.update.ClientUpdateManager;

/**
 * <h2 style="color:FFC800">新版本 MC 模组更新屏幕</h2>
 * 新版本 MC 模组更新提示界面
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class NewMcVerNotifyScreen extends UpdateScreen {
	protected final boolean canTrust;
	
	/**
	 * @param parent        父屏幕
	 * @param updateData    更新数据
	 * @param updateManager 更新管理器
	 * @param canTrust      是否可信任链接
	 */
	public NewMcVerNotifyScreen(@Nullable Screen parent, UpdateData updateData, ClientUpdateManager updateManager, boolean canTrust) {
		super(parent, updateData, updateManager, Text.translatable(UpdateState.NEW_MC_VER.title(), updateData.modData().i18nName(), updateData.minecraftVersion()));
		this.canTrust = canTrust;
	}
	
	@Override
	protected void initWidgets(GridWidget gridWidget) {
		// 添加按钮
		int fullButtonWidth = screenWidth - INTERVAL;
		int halfButtonWidth = (fullButtonWidth - BUTTON_SPACING) / 2;
		gridWidget.getMainPositioner().margin(0, BUTTON_SPACING, BUTTON_SPACING, 0);
		GridWidget.Adder adder = gridWidget.createAdder(2);
		adder.add(ButtonHelper.link(this, updateData, ModData.LinkType.SUPPORT).width(halfButtonWidth).build());
		adder.add(ButtonHelper.link(this, updateData, ModData.LinkType.COMMUNITY).width(halfButtonWidth).build());
		adder.add(updateModeButton().width(fullButtonWidth).build(), 2);
		updateNowButton.setWidth(halfButtonWidth);
		adder.add(updateNowButton);
		adder.add(notUpdateButton().width(halfButtonWidth).build());
		gridWidget.refreshPositions();
		SimplePositioningWidget.setPos(gridWidget, 1, screenHeight - BUTTON_SPACING - gridWidget.getHeight() + (height - screenHeight) / 2, width, height, 0.5F, 0);
		gridWidget.forEachChild(this::addDrawableChild);
		// 添加信息
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0,
				Text.translatable(UpdateState.NEW_MC_VER.text(), updateData.modData().i18nName(), updateData.minecraftVersion()), textRenderer)
				.setMaxWidth(screenWidth - INTERVAL * 2);
		messageText.setPosition((width - messageText.getWidth()) / 2, (height - messageText.getHeight()) / 2 + 20);
		addDrawableChild(messageText);
	}
}
