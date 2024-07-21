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

package pers.saikel0rado1iu.silk.mixin.landform.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.landform.UpgradableLevelSummary;
import pers.saikel0rado1iu.silk.landform.screen.UpgradePromptScreen;

import java.io.IOException;
import java.util.Objects;

import static pers.saikel0rado1iu.silk.landform.screen.UpgradePromptScreen.*;

/**
 * <h2 style="color:FFC800">{@link UpgradePromptScreen} 混入</h2>
 * 设置升级世界提示屏幕
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
	
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/widget/DirectionalLayoutWidget;add(L net/minecraft/client/gui/widget/Widget;)L net/minecraft/client/gui/widget/Widget;", ordinal = 10))
	private Widget init(Widget widget) throws IOException {
		LevelSummary levelSummary = storageSession.getLevelSummary(storageSession.readLevelProperties());
		if (!(levelSummary instanceof UpgradableLevelSummary<?> upgradableLevelSummary)) return widget;
		Text title;
		if (upgradableLevelSummary.info().shouldUpgradeWorld()) title = UPGRADE_BUTTON_TITLE;
		else if (upgradableLevelSummary.info().shouldDowngradeWorld()) title = DOWNGRADE_BUTTON_TITLE;
		else title = REFRESH_BUTTON_TITLE;
		return ButtonWidget.builder(title, button -> Objects.requireNonNull(client).setScreen(new UpgradePromptScreen<>(this, title, upgradableLevelSummary, callback, storageSession))).width(200).build();
	}
}
