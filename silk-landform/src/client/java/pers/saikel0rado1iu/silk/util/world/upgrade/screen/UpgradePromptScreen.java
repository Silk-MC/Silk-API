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

package pers.saikel0rado1iu.silk.util.world.upgrade.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.BackupPromptScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.screen.BaseScreen;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary;

import java.util.Objects;

import static pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary.TEXT_PREFIX;

/**
 * <h2 style="color:FFC800">升级（世界）提示屏幕</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public class UpgradePromptScreen extends BaseScreen {
	@ApiStatus.Internal
	public static final Text FLUSH_BUTTON_TITLE = Text.translatable(TextUtil.widgetTitle(Silk.DATA, TEXT_PREFIX + "flush"));
	@ApiStatus.Internal
	public static final Text UPGRADE_BUTTON_TITLE = Text.translatable(TextUtil.widgetTitle(Silk.DATA, TEXT_PREFIX + "upgrade")).formatted(Formatting.BOLD, Formatting.GOLD);
	@ApiStatus.Internal
	public static final Text DOWNGRADE_BUTTON_TITLE = Text.translatable(TextUtil.widgetTitle(Silk.DATA, TEXT_PREFIX + "downgrade")).formatted(Formatting.BOLD, Formatting.RED);
	private final BackupPromptScreen.Callback callback;
	private CheckboxWidget eraseCacheCheckbox;
	private MultilineText promptText = MultilineText.EMPTY;
	
	public UpgradePromptScreen(@Nullable Screen parent, Text title, UpgradeLevelSummary summary, BooleanConsumer callback, LevelStorage.Session session) {
		super(parent, Text.translatable(((TranslatableTextContent) title.getContent()).getKey()));
		this.callback = (backup, eraseCache) -> {
			if (backup) EditWorldScreen.backupLevel(session);
			Objects.requireNonNull(client).setScreen(UpgradeWorldScreen.create(client, this.title, callback, summary, session, eraseCache));
		};
	}
	
	@Override
	protected void init() {
		super.init();
		addDrawableChild(ScreenUtil.linkButton(this, Silk.DATA, ModBasicData.LinkType.SUPPORT, true).dimensions(width - 115, 6, 110, 20).build());
		promptText = MultilineText.create(textRenderer, getPrompt(), width - 50);
		int widgetY = promptText.count() * textRenderer.fontHeight;
		addDrawableChild(ButtonWidget.builder(Text.translatable("selectWorld.backupJoinConfirmButton"), button -> callback.proceed(true, eraseCacheCheckbox.isChecked())).dimensions(width / 2 - 155, 100 + widgetY, 150, 20).build());
		addDrawableChild(ButtonWidget.builder(title, button -> callback.proceed(false, eraseCacheCheckbox.isChecked())).dimensions(width / 2 - 155 + 160, 100 + widgetY, 150, 20).build());
		addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> Objects.requireNonNull(client).setScreen(parent)).dimensions(width / 2 - 155 + 80, 124 + widgetY, 150, 20).build());
		addDrawableChild(eraseCacheCheckbox = new CheckboxWidget(0, 0, 150, 20, Text.translatable("selectWorld.backupEraseCache"), false));
		eraseCacheCheckbox.setPosition((int) ((width - eraseCacheCheckbox.getWidth() * 1.15 / 2) / 2), 76 + widgetY);
	}
	
	private Text getPrompt() {
		return Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "prompt"), title);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 40, 0xFFFFFF);
		promptText.drawCenterWithShadow(context, width / 2, 60);
	}
}
