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

package pers.saikel0rado1iu.silk.mixin.client.event.modplus;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.api.client.event.modplus.ModifyModTabEvents;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.ModTab;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.ScreenTab;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

/**
 * <h2 style="color:FFC800">{@link ModifyModTabEvents} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
interface ModifyModTabEventsMixin {
	/**
	 * {@link ModifyModTabEvents#MODIFY_ALL} 混入
	 */
	@Mixin(ModTab.class)
	@SuppressWarnings("unused")
	abstract class ModifyAll extends ScreenTab {
		ModifyAll(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@Override
		public ModData modData() {
			return ModifyModTabEvents.MODIFY_ALL.invoker().apply(super.modData()).modData();
		}
	}
	
	/**
	 * {@link ModifyModTabEvents#MODIFY_ICON} 混入
	 */
	@Mixin(ModTab.class)
	abstract class ModifyIcon extends ScreenTab {
		ModifyIcon(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/widget/IconWidget;create(IIL net/minecraft/util/Identifier;II)L net/minecraft/client/gui/widget/IconWidget;"), index = 2)
		private Identifier modifyIcon(Identifier texture) {
			return ModifyModTabEvents.MODIFY_ICON.invoker().apply(texture);
		}
	}
	
	/**
	 * {@link ModifyModTabEvents#MODIFY_LINK} 混入
	 */
	@Mixin(ModTab.class)
	abstract class ModifyLink extends ScreenTab {
		ModifyLink(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/client/pattern/widget/ButtonHelper;link(L net/minecraft/client/gui/screen/Screen;L pers/saikel0rado1iu/silk/api/modpass/ModPass;L pers/saikel0rado1iu/silk/api/modpass/ModData$LinkType;Z)L net/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 0), index = 1)
		private ModPass modifyLinkHomepage(ModPass modPass) {
			return ModifyModTabEvents.MODIFY_LINK.invoker().apply(ModData.LinkType.HOMEPAGE, modPass);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/client/pattern/widget/ButtonHelper;link(L net/minecraft/client/gui/screen/Screen;L pers/saikel0rado1iu/silk/api/modpass/ModPass;L pers/saikel0rado1iu/silk/api/modpass/ModData$LinkType;Z)L net/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 1), index = 1)
		private ModPass modifyLinkSupport(ModPass modPass) {
			return ModifyModTabEvents.MODIFY_LINK.invoker().apply(ModData.LinkType.SUPPORT, modPass);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/client/pattern/widget/ButtonHelper;link(L net/minecraft/client/gui/screen/Screen;L pers/saikel0rado1iu/silk/api/modpass/ModPass;L pers/saikel0rado1iu/silk/api/modpass/ModData$LinkType;Z)L net/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 2), index = 1)
		private ModPass modifyLinkCommunity(ModPass modPass) {
			return ModifyModTabEvents.MODIFY_LINK.invoker().apply(ModData.LinkType.COMMUNITY, modPass);
		}
	}
	
	/**
	 * {@link ModifyModTabEvents#MODIFY_TITLE} 混入
	 */
	@Mixin(ModTab.class)
	abstract class ModifyTitle extends ScreenTab {
		ModifyTitle(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/pattern/widget/WidgetTexts;title(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)L net/minecraft/text/MutableText;", ordinal = 0), index = 0)
		private ModPass modifyChangelogTitle(ModPass modPass) {
			return ModifyModTabEvents.MODIFY_TITLE.invoker().apply("changelog", modPass);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/pattern/widget/WidgetTexts;title(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)L net/minecraft/text/MutableText;", ordinal = 1), index = 0)
		private ModPass modifyTargetTitle(ModPass modPass) {
			return ModifyModTabEvents.MODIFY_TITLE.invoker().apply("target", modPass);
		}
	}
	
	/**
	 * {@link ModifyModTabEvents#MODIFY_CHANGELOG} 混入
	 */
	@Mixin(ModTab.class)
	abstract class ModifyChangelog extends ScreenTab {
		ModifyChangelog(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/modpass/log/Changelog;read(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)Ljava/lang/String;"), index = 0)
		private ModPass modifyChangelog(ModPass modPass) {
			return ModifyModTabEvents.MODIFY_CHANGELOG.invoker().apply(modPass);
		}
	}
	
	/**
	 * {@link ModifyModTabEvents#MODIFY_TARGET} 混入
	 */
	@Mixin(ModTab.class)
	abstract class ModifyTarget extends ScreenTab {
		ModifyTarget(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/pattern/widget/WidgetTexts;text(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)L net/minecraft/text/MutableText;"), index = 0)
		private ModPass modifyText(ModPass modPass) {
			return ModifyModTabEvents.MODIFY_TARGET.invoker().apply(modPass);
		}
	}
}
