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

package pers.saikel0rado1iu.silk.mixin.api.callback;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.callback.WorldPresetCustomButtonCallback;

import java.util.ArrayList;
import java.util.List;

import static pers.saikel0rado1iu.silk.api.callback.WorldPresetCustomButtonCallback.Data.*;

/**
 * <p><b style="color:FFC800"><font size="+1">设置世界预设自定义按钮回调</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
interface WorldPresetCustomButtonCallbackMixin {
	@Mixin(CreateWorldScreen.class)
	abstract class AddButton extends Screen {
		@Final
		@Shadow
		private TabManager tabManager;
		
		private AddButton(Text title) {
			super(title);
		}
		
		/**
		 * 添加自定义世界按钮并获取世界构造器对象
		 *
		 * @param ci 回调信息
		 */
		@Inject(method = "init", at = @At("TAIL"))
		private void init(CallbackInfo ci) {
			WorldPresetCustomButtonCallback.Data.client = this.client;
			parent = this;
			worldCreator = ((CreateWorldScreen) (Object) this).getWorldCreator();
			List<WorldCreator.WorldType> worldTypes = new ArrayList<>();
			worldTypes.addAll(worldCreator.getNormalWorldTypes());
			worldTypes.addAll(worldCreator.getExtendedWorldTypes());
			worldTypes.forEach(worldType -> {
				ButtonWidget.PressAction onPress = button -> {
				};
				if (WorldPresetCustomButtonCallback.EVENT.invoker().canAdd(worldType, client, this, onPress))
					WORLD_CUSTOMS.put(worldType, addDrawableChild(ButtonWidget.builder(Text.translatable("selectWorld.customizeType"), onPress).build()));
			});
			WORLD_CUSTOMS.forEach((worldType, buttonWidget) -> buttonWidget.visible = false);
		}
		
		/**
		 * 如果聚焦不在世界选项卡则取消显示世界自定义按钮
		 *
		 * @param ci 回调信息
		 */
		@Inject(method = "tick", at = @At("HEAD"))
		private void tick(CallbackInfo ci) {
			if (tabManager.getCurrentTab() != null && !tabManager.getCurrentTab().getTitle().equals(Text.translatable("createWorld.tab.world.title")))
				WORLD_CUSTOMS.forEach((worldType, buttonWidget) -> buttonWidget.visible = false);
		}
	}
	
	@Mixin(targets = "net.minecraft.client.gui.screen.world.CreateWorldScreen$WorldTab")
	abstract class ShowCustom extends GridScreenTab {
		@Final
		@Shadow
		private ButtonWidget customizeButton;
		
		private ShowCustom(Text title) {
			super(title);
		}
		
		/**
		 * 处理在合适的时机正确地显示模组自带的自定义按钮
		 *
		 * @param ci 回调信息
		 */
		@Inject(method = "tick", at = @At("HEAD"))
		private void tick(CallbackInfo ci) {
			ButtonWidget.PressAction onPress = button -> {
			};
			if (WorldPresetCustomButtonCallback.EVENT.invoker().canAdd(worldCreator.getWorldType(), client, parent, onPress)) {
				ButtonWidget custom = WORLD_CUSTOMS.get(worldCreator.getWorldType());
				custom.setPosition(customizeButton.getX(), customizeButton.getY());
				custom.visible = true;
				customizeButton.visible = false;
			} else {
				customizeButton.visible = true;
			}
		}
	}
}
