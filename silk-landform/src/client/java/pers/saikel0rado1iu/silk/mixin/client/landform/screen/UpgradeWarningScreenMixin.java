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

package pers.saikel0rado1iu.silk.mixin.client.landform.screen;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.client.landform.UpgradableLevelSummary;
import pers.saikel0rado1iu.silk.api.client.landform.screen.UpgradeWarningScreen;

/**
 * <h2 style="color:FFC800">{@link UpgradeWarningScreen} 混入</h2>
 * 设置升级世界警告屏幕
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
abstract class UpgradeWarningScreenMixin {
	/**
	 * 获取父屏幕
	 */
	@Mixin(WorldListWidget.WorldEntry.class)
	abstract static class GetParentScreen {
		@Shadow
		@Final
		LevelSummary level;
		@Shadow
		@Final
		private SelectWorldScreen screen;
		
		@Inject(method = "play", at = @At("HEAD"))
		private void play(CallbackInfo ci) {
			UpgradeWarningScreen.MixinData.parent = screen;
			UpgradeWarningScreen.MixinData.level = level;
		}
	}
	
	/**
	 * 显示屏幕
	 */
	@Mixin(IntegratedServerLoader.class)
	abstract static class ShowScreen {
		@Shadow
		@Final
		private MinecraftClient client;
		@Unique
		private LevelStorage.Session session;
		
		@Shadow
		protected abstract void start(LevelStorage.Session session, Runnable onCancel);
		
		@Inject(method = "start(L java/lang/String;L java/lang/Runnable;)V",
				at = @At(value = "INVOKE", target = "L net/minecraft/server/integrated/IntegratedServerLoader;start(L net/minecraft/world/level/storage/LevelStorage$Session;L java/lang/Runnable;)V"))
		private void getSession(CallbackInfo ci, @Local LevelStorage.Session session) {
			this.session = session;
		}
		
		@Inject(method = "start(L java/lang/String;L java/lang/Runnable;)V",
				at = @At(value = "INVOKE", target = "L net/minecraft/server/integrated/IntegratedServerLoader;start(L net/minecraft/world/level/storage/LevelStorage$Session;L java/lang/Runnable;)V"), cancellable = true)
		private void start(String levelName, Runnable onCancel, CallbackInfo ci) {
			if (UpgradeWarningScreen.MixinData.level instanceof UpgradableLevelSummary<?> upgradableLevelSummary && upgradableLevelSummary.info().shouldUpgradeWorld()) {
				client.setScreen(new UpgradeWarningScreen(UpgradeWarningScreen.MixinData.parent, session, () -> start(session, onCancel)));
				ci.cancel();
			}
		}
	}
}
