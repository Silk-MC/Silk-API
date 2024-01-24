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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary;
import pers.saikel0rado1iu.silk.util.world.upgrade.screen.UpgradeWarningScreen;

/**
 * <h2 style="color:FFC800">设置升级（世界）警告屏幕混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
interface UpgradeWarningScreenMixin {
	@Mixin(WorldListWidget.WorldEntry.class)
	abstract class GetParentScreen {
		@Shadow
		@Final
		public LevelSummary level;
		@Shadow
		@Final
		private SelectWorldScreen screen;
		
		@Inject(method = "play", at = @At("HEAD"))
		private void play(CallbackInfo ci) {
			UpgradeWarningScreen.Mixin.Parent = screen;
			UpgradeWarningScreen.Mixin.level = level;
		}
	}
	
	@Mixin(IntegratedServerLoader.class)
	abstract class ShowScreen {
		@Shadow
		@Final
		private MinecraftClient client;
		@Unique
		private LevelStorage.Session session;
		
		@Shadow
		protected abstract void start(Screen parent, String levelName, boolean safeMode, boolean canShowBackupPrompt);
		
		@ModifyVariable(method = "start(L net/minecraft/client/gui/screen/Screen;L java/lang/String;ZZ)V", at = @At("STORE"), name = "session")
		private LevelStorage.Session getSession(LevelStorage.Session session) {
			return this.session = session;
		}
		
		@Inject(method = "start(L net/minecraft/client/gui/screen/Screen;L java/lang/String;ZZ)V", at = @At("RETURN"), cancellable = true)
		private void start(Screen parent, String levelName, boolean safeMode, boolean canShowBackupPrompt, CallbackInfo ci) {
			if (UpgradeWarningScreen.Mixin.level instanceof UpgradeLevelSummary upgradeLevelSummary && upgradeLevelSummary.shouldUpgradeWorld()) {
				client.setScreen(new UpgradeWarningScreen(UpgradeWarningScreen.Mixin.Parent, session, () -> start(parent, levelName, safeMode, canShowBackupPrompt)));
				ci.cancel();
			}
		}
	}
}
