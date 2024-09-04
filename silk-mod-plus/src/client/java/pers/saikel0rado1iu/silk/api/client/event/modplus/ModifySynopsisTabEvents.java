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

package pers.saikel0rado1iu.silk.api.client.event.modplus;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.SynopsisTab;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">修改 {@link SynopsisTab} 事件</h2>
 * 用于动态修改 {@link SynopsisTab} 纹理以及文本的事件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifySynopsisTabEvents {
	/**
	 * 此事件将 {@link SynopsisTab} 内的所有内容替换为另外的 {@link ModPass} 的内容
	 */
	Event<ModifyAll> MODIFY_ALL = EventFactory.createArrayBacked(ModifyAll.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyAll event : listeners) mod = event.apply(mod);
		return mod;
	});
	
	/**
	 * 此事件将 {@link SynopsisTab} 内的介绍标题替换为另外的 {@link ModPass} 的介绍标题
	 */
	Event<ModifyIntroTitle> MODIFY_INTRO_TITLE = EventFactory.createArrayBacked(ModifyIntroTitle.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyIntroTitle event : listeners) mod = event.apply(mod);
		return mod;
	});
	
	/**
	 * 此事件将 {@link SynopsisTab} 内的愿景标题替换为另外的 {@link ModPass} 的愿景标题
	 */
	Event<ModifyVisionTitle> MODIFY_VISION_TITLE = EventFactory.createArrayBacked(ModifyVisionTitle.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyVisionTitle event : listeners) mod = event.apply(mod);
		return mod;
	});
	
	/**
	 * 此事件将 {@link SynopsisTab} 内的介绍文本替换为另外的 {@link ModPass} 的介绍文本
	 */
	Event<ModifyIntroText> MODIFY_INTRO_TEXT = EventFactory.createArrayBacked(ModifyIntroText.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyIntroText event : listeners) mod = event.apply(mod);
		return mod;
	});
	
	/**
	 * 此事件将 {@link SynopsisTab} 内的愿景文本替换为另外的 {@link ModPass} 的愿景文本
	 */
	Event<ModifyVisionText> MODIFY_VISION_TEXT = EventFactory.createArrayBacked(ModifyVisionText.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyVisionText event : listeners) mod = event.apply(mod);
		return mod;
	});
	
	/**
	 * 修改所有
	 */
	@FunctionalInterface
	interface ModifyAll extends Function<ModPass, ModPass> {
	}
	
	/**
	 * 修改介绍标题
	 */
	@FunctionalInterface
	interface ModifyIntroTitle extends Function<ModPass, ModPass> {
	}
	
	/**
	 * 修改愿景标题
	 */
	@FunctionalInterface
	interface ModifyVisionTitle extends Function<ModPass, ModPass> {
	}
	
	/**
	 * 修改介绍文本
	 */
	@FunctionalInterface
	interface ModifyIntroText extends Function<ModPass, ModPass> {
	}
	
	/**
	 * 修改愿景文本
	 */
	@FunctionalInterface
	interface ModifyVisionText extends Function<ModPass, ModPass> {
	}
}
