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

package pers.saikel0rado1iu.silk.mixin.client.event.landform;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.client.event.landform.WorldPresetCustomButtonCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">{@link WorldPresetCustomButtonCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface WorldPresetCustomButtonCallbackMixin {
	/**
	 * 添加按钮
	 */
	@Mixin(CreateWorldScreen.class)
	abstract class AddButton extends Screen {
		@Unique
		private static final Map<WorldCreator.WorldType, ButtonWidget> WORLD_CUSTOMS = new HashMap<>(8);
		@Final
		@Shadow
		private TabManager tabManager;
		@Unique
		private ButtonWidget custom;
		
		private AddButton(Text title) {
			super(title);
		}
		
		/**
		 * @return WorldCreator
		 */
		@Shadow
		public abstract WorldCreator getWorldCreator();
		
		/**
		 * 添加自定义世界按钮并获取世界构造器对象
		 *
		 * @param ci 回调信息
		 */
		@Inject(method = "init", at = @At("TAIL"))
		private void init(CallbackInfo ci) {
			List<WorldCreator.WorldType> worldTypes = getWorldCreator().getExtendedWorldTypes();
			worldTypes.forEach(worldType -> {
				Optional<ButtonWidget.PressAction> onPress;
				if ((onPress = WorldPresetCustomButtonCallback.EVENT.invoker().addPressAction(worldType, client, this)).isPresent())
					WORLD_CUSTOMS.put(worldType, addDrawableChild(ButtonWidget.builder(Text.translatable("selectWorld.customizeType"), onPress.get()).build()));
			});
			WORLD_CUSTOMS.forEach((worldType, buttonWidget) -> buttonWidget.visible = false);
		}
		
		/**
		 * 如果聚焦不在世界选项卡则取消显示世界自定义按钮
		 */
		@Override
		public void tick() {
			if (WorldPresetCustomButtonCallback.MixinData.customizeButton == null) return;
			if (WorldPresetCustomButtonCallback.EVENT.invoker().addPressAction(getWorldCreator().getWorldType(), client, this).isPresent()) {
				custom = WORLD_CUSTOMS.get(getWorldCreator().getWorldType());
				custom.setPosition(WorldPresetCustomButtonCallback.MixinData.customizeButton.getX(), WorldPresetCustomButtonCallback.MixinData.customizeButton.getY());
				custom.visible = true;
				WorldPresetCustomButtonCallback.MixinData.customizeButton.visible = false;
			} else {
				if (custom != null) custom.visible = false;
				WorldPresetCustomButtonCallback.MixinData.customizeButton.visible = true;
			}
			if (tabManager.getCurrentTab() != null && !tabManager.getCurrentTab().getTitle().equals(Text.translatable("createWorld.tab.world.title")))
				WORLD_CUSTOMS.forEach((worldType, buttonWidget) -> buttonWidget.visible = false);
		}
	}
	
	/**
	 * 显示自定义按钮
	 */
	@Mixin(targets = "net.minecraft.client.gui.screen.world.CreateWorldScreen$WorldTab")
	abstract class ShowCustom extends GridScreenTab {
		@Final
		@Shadow
		private ButtonWidget customizeButton;
		
		private ShowCustom(Text title) {
			super(title);
		}
		
		@Inject(method = "<init>", at = @At("TAIL"))
		private void init(CreateWorldScreen createWorldScreen, CallbackInfo ci) {
			WorldPresetCustomButtonCallback.MixinData.customizeButton = customizeButton;
		}
	}
}
