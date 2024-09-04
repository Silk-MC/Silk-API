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
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.ModTab;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <h2 style="color:FFC800">修改 {@link ModTab} 事件</h2>
 * 用于动态修改 {@link ModTab} 纹理以及文本的事件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyModTabEvents {
	/**
	 * 此事件将 {@link ModTab} 内的所有内容替换为另外的 {@link ModPass} 的内容
	 */
	Event<ModifyAll> MODIFY_ALL = EventFactory.createArrayBacked(ModifyAll.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyAll event : listeners) mod = event.apply(mod);
		return mod;
	});
	
	/**
	 * 此事件将修改 {@link ModTab} 内的图标
	 */
	Event<ModifyIcon> MODIFY_ICON = EventFactory.createArrayBacked(ModifyIcon.class, listeners -> icon -> {
		Identifier identifier = icon;
		for (ModifyIcon event : listeners) identifier = event.apply(identifier);
		return identifier;
	});
	
	/**
	 * 此事件将 {@link ModTab} 内的链接替换为另外的 {@link ModPass} 的链接
	 */
	Event<ModifyLink> MODIFY_LINK = EventFactory.createArrayBacked(ModifyLink.class, listeners -> (linkType, modPass) -> {
		ModPass mod = modPass;
		for (ModifyLink event : listeners) mod = event.apply(linkType, modPass);
		return mod;
	});
	
	/**
	 * 此事件将 {@link ModTab} 内的标题替换为另外的 {@link ModPass} 的标题
	 */
	Event<ModifyTitle> MODIFY_TITLE = EventFactory.createArrayBacked(ModifyTitle.class, listeners -> (str, modPass) -> {
		ModPass mod = modPass;
		for (ModifyTitle event : listeners) mod = event.apply(str, modPass);
		return mod;
	});
	/**
	 * 此事件将 {@link ModTab} 内的更新日志替换为另外的 {@link ModPass} 的更新日志
	 */
	Event<ModifyChangelog> MODIFY_CHANGELOG = EventFactory.createArrayBacked(ModifyChangelog.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyChangelog event : listeners) mod = event.apply(mod);
		return mod;
	});
	/**
	 * 此事件将 {@link ModTab} 内的目标文本替换为另外的 {@link ModPass} 的目标文本
	 */
	Event<ModifyTarget> MODIFY_TARGET = EventFactory.createArrayBacked(ModifyTarget.class, listeners -> modPass -> {
		ModPass mod = modPass;
		for (ModifyTarget event : listeners) mod = event.apply(mod);
		return mod;
	});
	
	/**
	 * 修改所有
	 */
	@FunctionalInterface
	interface ModifyAll extends Function<ModPass, ModPass> {
	}
	
	/**
	 * 修改图标
	 */
	@FunctionalInterface
	interface ModifyIcon extends Function<Identifier, Identifier> {
	}
	
	/**
	 * 修改链接
	 */
	@FunctionalInterface
	interface ModifyLink extends BiFunction<ModData.LinkType, ModPass, ModPass> {
	}
	
	/**
	 * 修改标题
	 */
	@FunctionalInterface
	interface ModifyTitle extends BiFunction<String, ModPass, ModPass> {
	}
	
	/**
	 * 修改日志
	 */
	@FunctionalInterface
	interface ModifyChangelog extends Function<ModPass, ModPass> {
	}
	
	/**
	 * 修改目标
	 */
	@FunctionalInterface
	interface ModifyTarget extends Function<ModPass, ModPass> {
	}
}
