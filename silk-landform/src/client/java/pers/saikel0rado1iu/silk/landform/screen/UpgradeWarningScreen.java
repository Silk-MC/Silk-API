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

package pers.saikel0rado1iu.silk.landform.screen;

import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import pers.saikel0rado1iu.silk.impl.SilkCodex;
import pers.saikel0rado1iu.silk.impl.SilkLandform;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.pattern.screen.BaseScreen;
import pers.saikel0rado1iu.silk.pattern.widget.ButtonHelper;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">升级警告屏幕</h2>
 * 升级世界警告屏幕
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public class UpgradeWarningScreen extends BaseScreen {
	/**
	 * 警告标题
	 */
	public static final Text WARNING_TITLE = WidgetTexts.title(SilkLandform.getInstance(), "warning");
	/**
	 * 警告文本
	 */
	public static final Text WARNING_TEXT = Text.translatable(WidgetTexts.textKey(SilkLandform.getInstance(), "warning"), Text.translatable("selectWorld.edit"));
	private final LevelStorage.Session session;
	private final Consumer<Boolean> consumer;
	private MultilineText warningText = MultilineText.EMPTY;
	
	/**
	 * @param parent  父屏幕
	 * @param session 关卡存储器会话
	 * @param play    开始游戏方法
	 */
	public UpgradeWarningScreen(Optional<Screen> parent, LevelStorage.Session session, Runnable play) {
		super(parent, WARNING_TITLE);
		this.session = session;
		this.consumer = (backup) -> {
			if (backup) EditWorldScreen.backupLevel(session);
			play.run();
		};
	}
	
	@Override
	protected void init() {
		super.init();
		addDrawableChild(ButtonHelper.link(this, SilkCodex.getInstance(), ModData.LinkType.SUPPORT, true).dimensions(width - 115, 6, 110, 20).build());
		warningText = MultilineText.create(textRenderer, WARNING_TEXT, width - 50);
		int widgetY = warningText.count() * textRenderer.fontHeight;
		addDrawableChild(ButtonWidget.builder(Text.translatable("selectWorld.backupJoinConfirmButton"), button -> consumer.accept(true)).dimensions(width / 2 - 155, 100 + widgetY, 150, 20).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("selectWorld.backupJoinSkipButton"), button -> consumer.accept(false)).dimensions(width / 2 - 155 + 160, 100 + widgetY, 150, 20).build());
		addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> {
			session.tryClose();
			Objects.requireNonNull(client).setScreen(parent.orElse(null));
		}).dimensions(width / 2 - 155 + 80, 124 + widgetY, 150, 20).build());
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 40, 0xFFFFFF);
		warningText.drawCenterWithShadow(context, width / 2, 60);
	}
	
	/**
	 * 混入数据
	 */
	public static final class MixinData {
		/**
		 * level
		 */
		public static LevelSummary level;
		/**
		 * parent
		 */
		public static SelectWorldScreen parent;
	}
}
