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

package pers.saikel0rado1iu.silk.mixin.util.world.upgrade.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary;
import pers.saikel0rado1iu.silk.util.world.upgrade.screen.UpgradePromptScreen;

import java.util.Objects;

import static pers.saikel0rado1iu.silk.util.world.upgrade.screen.UpgradePromptScreen.*;

/**
 * <h2 style="color:FFC800">设置升级（世界）提示屏幕混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@Mixin(EditWorldScreen.class)
abstract class UpgradePromptScreenMixin extends Screen {
	@Shadow
	@Final
	private LevelStorage.Session storageSession;
	
	@Shadow
	@Final
	private BooleanConsumer callback;
	
	private UpgradePromptScreenMixin(Text title) {
		super(title);
	}
	
	@Inject(method = "init", at = @At("RETURN"))
	private void init(CallbackInfo ci) {
		LevelSummary levelSummary = storageSession.getLevelSummary();
		if (!(levelSummary instanceof UpgradeLevelSummary upgradeLevelSummary)) return;
		Text title;
		if (upgradeLevelSummary.shouldUpgradeWorld()) title = UPGRADE_BUTTON_TITLE;
		else if (upgradeLevelSummary.shouldDowngradeWorld()) title = DOWNGRADE_BUTTON_TITLE;
		else title = FLUSH_BUTTON_TITLE;
		addDrawableChild(ButtonWidget.builder(title, button -> Objects.requireNonNull(client).setScreen(new UpgradePromptScreen(this, title, upgradeLevelSummary, callback, storageSession)))
				.dimensions(width / 2 - 100, height / 4 + 120 + 5, 200, 20).build());
	}
}
