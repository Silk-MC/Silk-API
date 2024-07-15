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

package pers.saikel0rado1iu.silk.mixin.landform;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.landform.UpgradableLevelSummary;

import java.util.List;

/**
 * <h2 style="color:FFC800">{@link UpgradableLevelSummary} 混入</h2>
 * 设置可升级关卡摘要
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
interface UpgradeLevelSummaryMixin {
	/**
	 * 创建详细信息
	 */
	@Mixin(LevelSummary.class)
	abstract class CreateDetails {
		@Inject(method = "createDetails", at = @At("RETURN"), cancellable = true)
		private void createDetails(CallbackInfoReturnable<Text> cir) {
			if ((Object) this instanceof UpgradableLevelSummary<?> upgradableLevelSummary)
				cir.setReturnValue(upgradableLevelSummary.addDetails((MutableText) cir.getReturnValue()));
		}
	}
	
	/**
	 * 修复摘要显示
	 */
	@Mixin(WorldListWidget.class)
	abstract class FixSummaryShow extends AlwaysSelectedEntryListWidget<WorldListWidget.Entry> {
		@Shadow
		private @Nullable List<LevelSummary> levels;
		
		private FixSummaryShow(MinecraftClient minecraftClient, int i, int j, int k, int l) {
			super(minecraftClient, i, j, k, l);
		}
		
		@Inject(method = "getRowWidth", at = @At("RETURN"), cancellable = true)
		private void getRowWidth(CallbackInfoReturnable<Integer> cir) {
			if (levels == null) return;
			int rowWidth = cir.getReturnValue();
			for (LevelSummary summary : levels) rowWidth = Math.max(rowWidth, client.textRenderer.getWidth(summary.getDetails()) + 40);
			cir.setReturnValue(rowWidth);
		}
		
		@Inject(method = "getScrollbarPositionX", at = @At("RETURN"), cancellable = true)
		private void getScrollbarPositionX(CallbackInfoReturnable<Integer> cir) {
			if (levels == null) return;
			cir.setReturnValue(Math.max(cir.getReturnValue(), (width + getRowWidth()) / 2));
		}
	}
	
	/**
	 * 添加世界进入警告
	 */
	@Mixin(WorldListWidget.WorldEntry.class)
	abstract class AddWorldEntryWarning extends WorldListWidget.Entry {
		@Unique
		private static final Identifier ERROR_HIGHLIGHTED_TEXTURE = new Identifier("world_list/error_highlighted");
		@Unique
		private static final Identifier WARNING_HIGHLIGHTED_TEXTURE = new Identifier("world_list/warning_highlighted");
		@Unique
		private static final Identifier WARNING_TEXTURE = new Identifier("world_list/warning");
		@Unique
		private static final Identifier ERROR_TEXTURE = new Identifier("world_list/error");
		@Unique
		private static final Identifier MARKED_JOIN_HIGHLIGHTED_TEXTURE = new Identifier("world_list/marked_join_highlighted");
		@Unique
		private static final Identifier MARKED_JOIN_TEXTURE = new Identifier("world_list/marked_join");
		@Shadow
		@Final
		LevelSummary level;
		@Shadow
		@Final
		private MinecraftClient client;
		@Shadow
		@Final
		private SelectWorldScreen screen;
		
		@Inject(method = "render", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/DrawContext;drawGuiTexture(L net/minecraft/util/Identifier;I I I I)V", ordinal = 8), cancellable = true)
		private void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
			if (level instanceof UpgradableLevelSummary<?> upgradableLevelSummary) {
				boolean focus = mouseX - x < 32;
				Identifier warningTexture = focus ? WARNING_HIGHLIGHTED_TEXTURE : WARNING_TEXTURE;
				Identifier errorTexture = focus ? ERROR_HIGHLIGHTED_TEXTURE : ERROR_TEXTURE;
				Identifier joinTexture = focus ? MARKED_JOIN_HIGHLIGHTED_TEXTURE : MARKED_JOIN_TEXTURE;
				if (upgradableLevelSummary.info().shouldUpgradeWorld()) {
					context.drawGuiTexture(joinTexture, x, y, 32, 32);
					context.drawGuiTexture(warningTexture, x, y, 32, 32);
					if (focus) screen.setTooltip(client.textRenderer.wrapLines(upgradableLevelSummary.getUpgradeTooltip(), 175));
					ci.cancel();
				} else if (upgradableLevelSummary.info().shouldDowngradeWorld()) {
					context.drawGuiTexture(errorTexture, x, y, 32, 32);
					if (focus) screen.setTooltip(client.textRenderer.wrapLines(upgradableLevelSummary.getDowngradeTooltip(), 175));
					ci.cancel();
				}
			}
		}
	}
}
