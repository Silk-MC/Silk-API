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

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.BackupPromptScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.impl.SilkCodex;
import pers.saikel0rado1iu.silk.impl.SilkLandform;
import pers.saikel0rado1iu.silk.landform.UpgradableLevelSummary;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.pattern.screen.BaseScreen;
import pers.saikel0rado1iu.silk.pattern.widget.ButtonHelper;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;

import java.util.Objects;

/**
 * <h2 style="color:FFC800">升级提示屏幕</h2>
 * 升级世界提示屏幕
 *
 * @param <T> 区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public class UpgradePromptScreen<T extends ChunkGenerator & ChunkGeneratorUpgradable> extends BaseScreen {
	/**
	 * 刷新按钮标题
	 */
	public static final Text REFRESH_BUTTON_TITLE = WidgetTexts.title(SilkLandform.getInstance(), "refresh");
	/**
	 * 升级按钮标题
	 */
	public static final Text UPGRADE_BUTTON_TITLE = WidgetTexts.title(SilkLandform.getInstance(), "upgrade").formatted(Formatting.BOLD, Formatting.GOLD);
	/**
	 * 降级按钮标题
	 */
	public static final Text DOWNGRADE_BUTTON_TITLE = WidgetTexts.title(SilkLandform.getInstance(), "downgrade").formatted(Formatting.BOLD, Formatting.RED);
	private final BackupPromptScreen.Callback callback;
	private CheckboxWidget eraseCacheCheckbox;
	private MultilineText promptText = MultilineText.EMPTY;
	
	/**
	 * @param parent   父屏幕
	 * @param title    标题
	 * @param summary  升级关卡摘要
	 * @param callback 运行回调
	 * @param session  关卡存储器会话
	 */
	public UpgradePromptScreen(@Nullable Screen parent, Text title, UpgradableLevelSummary<T> summary, BooleanConsumer callback, LevelStorage.Session session) {
		super(parent, Text.translatable(((TranslatableTextContent) title.getContent()).getKey()));
		this.callback = (backup, eraseCache) -> {
			if (backup) EditWorldScreen.backupLevel(session);
			Objects.requireNonNull(client).setScreen(UpgradeWorldScreen.create(client, this.title, callback, summary, session, eraseCache).orElse(null));
		};
	}
	
	private Text getPrompt() {
		return Text.translatable(WidgetTexts.textKey(SilkLandform.getInstance(), "prompt"), title);
	}
	
	@Override
	protected void init() {
		super.init();
		addDrawableChild(ButtonHelper.link(this, SilkCodex.getInstance(), ModData.LinkType.SUPPORT, true).dimensions(width - 115, 6, 110, 20).build());
		promptText = MultilineText.create(textRenderer, getPrompt(), width - 50);
		int widgetY = promptText.count() * textRenderer.fontHeight;
		addDrawableChild(ButtonWidget.builder(Text.translatable("selectWorld.backupJoinConfirmButton"), button -> callback.proceed(true, eraseCacheCheckbox.isChecked())).dimensions(width / 2 - 155, 100 + widgetY, 150, 20).build());
		addDrawableChild(ButtonWidget.builder(title, button -> callback.proceed(false, eraseCacheCheckbox.isChecked())).dimensions(width / 2 - 155 + 160, 100 + widgetY, 150, 20).build());
		addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> Objects.requireNonNull(client).setScreen(parent)).dimensions(width / 2 - 155 + 80, 124 + widgetY, 150, 20).build());
		addDrawableChild(eraseCacheCheckbox = CheckboxWidget.builder(Text.translatable("selectWorld.backupEraseCache"), textRenderer).build());
		eraseCacheCheckbox.setPosition((int) ((width - eraseCacheCheckbox.getWidth() * 1.15) / 2), 76 + widgetY);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 40, 0xFFFFFF);
		promptText.drawCenterWithShadow(context, width / 2, 60);
	}
}
