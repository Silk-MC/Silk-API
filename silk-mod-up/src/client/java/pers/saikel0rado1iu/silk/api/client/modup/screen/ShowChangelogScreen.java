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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.client.modup.ClientUpdateManager;
import pers.saikel0rado1iu.silk.api.client.pattern.widget.TextListWidget;
import pers.saikel0rado1iu.silk.api.modpass.log.Changelog;
import pers.saikel0rado1iu.silk.api.modup.UpdateData;
import pers.saikel0rado1iu.silk.api.modup.UpdateState;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">模组日志展示屏幕</h2>
 * 模组日志展示界面
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class ShowChangelogScreen extends UpdateScreen {
	protected final boolean canTrust;
	protected TextListWidget changelogWidget;
	
	/**
	 * @param parent        父屏幕
	 * @param updateData    更新数据
	 * @param updateManager 更新管理器
	 * @param canTrust      是否可信任链接
	 */
	public ShowChangelogScreen(@Nullable Screen parent, UpdateData updateData, ClientUpdateManager updateManager, boolean canTrust) {
		super(parent, updateData, updateManager, Text.translatable(UpdateState.MOD_LOG.title(), updateData.modData().i18nName()));
		this.canTrust = canTrust;
		UpdateData.setCanShowChangelog(this.updateData.updateSettings(), false);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		changelogWidget.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	protected void initWidgets(GridWidget gridWidget) {
		int fullButtonWidth = screenWidth - INTERVAL;
		// 添加按钮
		gridWidget.getMainPositioner().margin(0, BUTTON_SPACING, BUTTON_SPACING, 0);
		GridWidget.Adder adder = gridWidget.createAdder(2);
		adder.add(returnToGameButton().width(fullButtonWidth).build(), 2);
		gridWidget.refreshPositions();
		SimplePositioningWidget.setPos(gridWidget, 1, screenHeight - BUTTON_SPACING - gridWidget.getHeight() + (height - screenHeight) / 2, width, height, 0.5F, 0);
		gridWidget.forEachChild(this::addDrawableChild);
		// 添加提示消息文本
		addSelectableChild(changelogWidget = new TextListWidget(client,
				screenWidth - INTERVAL,
				(int) ((height - screenHeight) / 2F + (textRenderer.fontHeight + INTERVAL) * 2) + ICON_SIZE / 2,
				(height + screenHeight) / 2 - gridWidget.getHeight() - BUTTON_SPACING,
				textRenderer.fontHeight, Changelog.read(updateData.modData(), MinecraftClient.getInstance().getLanguageManager().getLanguage())) {
			@Override
			public Optional<Identifier> background() {
				return Optional.of(OPTIONS_BACKGROUND_TEXTURE);
			}
		});
		changelogWidget.setX((width - changelogWidget.getWidth()) / 2);
	}
}