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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.api.client.event.modplus.ModifySynopsisTabEvents;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.ScreenTab;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.SynopsisTab;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

/**
 * <h2 style="color:FFC800">{@link ModifySynopsisTabEvents} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifySynopsisTabEventsMixin {
	/**
	 * {@link ModifySynopsisTabEvents#MODIFY_ALL} 混入
	 */
	@Mixin(SynopsisTab.class)
	@SuppressWarnings("unused")
	abstract class ModifyAll extends ScreenTab {
		ModifyAll(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@Override
		public ModData modData() {
			return ModifySynopsisTabEvents.MODIFY_ALL.invoker().apply(super.modData()).modData();
		}
	}
	
	/**
	 * {@link ModifySynopsisTabEvents#MODIFY_INTRO_TITLE} 混入
	 */
	@Mixin(SynopsisTab.class)
	abstract class ModifyIntroTitle extends ScreenTab {
		ModifyIntroTitle(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/pattern/widget/WidgetTexts;title(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)L net/minecraft/text/MutableText;", ordinal = 0), index = 0)
		private ModPass modify(ModPass modPass) {
			return ModifySynopsisTabEvents.MODIFY_ALL.invoker().apply(modPass);
		}
	}
	
	/**
	 * {@link ModifySynopsisTabEvents#MODIFY_VISION_TITLE} 混入
	 */
	@Mixin(SynopsisTab.class)
	abstract class ModifyVisionTitle extends ScreenTab {
		ModifyVisionTitle(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/pattern/widget/WidgetTexts;title(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)L net/minecraft/text/MutableText;", ordinal = 1), index = 0)
		private ModPass modify(ModPass modPass) {
			return ModifySynopsisTabEvents.MODIFY_ALL.invoker().apply(modPass);
		}
	}
	
	/**
	 * {@link ModifySynopsisTabEvents#MODIFY_INTRO_TEXT} 混入
	 */
	@Mixin(SynopsisTab.class)
	abstract class ModifyIntroText extends ScreenTab {
		ModifyIntroText(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/pattern/widget/WidgetTexts;text(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)L net/minecraft/text/MutableText;", ordinal = 0), index = 0)
		private ModPass modify(ModPass modPass) {
			return ModifySynopsisTabEvents.MODIFY_ALL.invoker().apply(modPass);
		}
	}
	
	/**
	 * {@link ModifySynopsisTabEvents#MODIFY_VISION_TEXT} 混入
	 */
	@Mixin(SynopsisTab.class)
	abstract class ModifyVisionText extends ScreenTab {
		ModifyVisionText(ModPass modPass, String key) {
			super(modPass, key);
		}
		
		@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/pattern/widget/WidgetTexts;text(L pers/saikel0rado1iu/silk/api/modpass/ModPass;Ljava/lang/String;)L net/minecraft/text/MutableText;", ordinal = 1), index = 0)
		private ModPass modify(ModPass modPass) {
			return ModifySynopsisTabEvents.MODIFY_ALL.invoker().apply(modPass);
		}
	}
}
