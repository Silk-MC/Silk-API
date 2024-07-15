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

package pers.saikel0rado1iu.silk.impl;

import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.event.Level;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModDataExpansion;
import pers.saikel0rado1iu.silk.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.modpass.pack.ResourcePack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">Silk API</h2>
 * Silk API 的模组数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface SilkApi extends ModDataExpansion {
	/**
	 * 提供实例
	 *
	 * @return 模组数据实例
	 */
	static SilkApi getInstance() {
		return new SilkApi() {
		};
	}
	
	/**
	 * 提供内部实例
	 *
	 * @return 模组数据实例
	 */
	@ApiStatus.Internal
	static SilkApi getInternal() {
		return new SilkApi() {
			@Override
			public String name() {
				return "Silk API";
			}
		};
	}
	
	@Override
	default int themeColor() {
		return 0;
	}
	
	@Override
	default Text i18nName() {
		return Text.translatable(String.format("modmenu.nameTranslation.%s", id()));
	}
	
	@Override
	default Text i18nSummary() {
		return Text.translatable(String.format("modmenu.summaryTranslation.%s", id()));
	}
	
	@Override
	default Text i18nDescription() {
		return Text.translatable(String.format("modmenu.descriptionTranslation.%s", id()));
	}
	
	@Override
	default Optional<DataPack> dataPack() {
		return Optional.empty();
	}
	
	@Override
	default Optional<ResourcePack> resourcePack() {
		return Optional.empty();
	}
	
	/**
	 * 写入格式化日志。
	 *
	 * @param level 日志级别
	 * @param mod   此模组或其他模组数据需求
	 * @param mes   要写入的格式化日志消息
	 * @param args  其他格式化参数
	 */
	default void writeFormatLog(Level level, ModData mod, String mes, Object... args) {
		switch (level) {
			case DEBUG -> logger().debug(String.format("From mod %s: %s", mod.debugName(), mes), args);
			case INFO -> logger().info(String.format("From mod %s: %s", mod.debugName(), mes), args);
			case WARN -> logger().warn(String.format("From mod %s: %s", mod.debugName(), mes), args);
			case ERROR -> logger().error(String.format("From mod %s: %s", mod.debugName(), mes), args);
			case TRACE -> logger().trace(String.format("From mod %s: %s", mod.debugName(), mes), args);
		}
	}
	
	@Override
	default String id() {
		return SilkId.SILK_API;
	}
	
	@Override
	default Optional<URL> community() throws MalformedURLException {
		String url = mod().getMetadata().getCustomValue("modmenu").getAsObject().get("links").getAsObject().get("modmenu.discord").getAsString();
		return Optional.of(new URL(url));
	}
	
	@Override
	default Optional<URL> support() throws MalformedURLException {
		String url = mod().getMetadata().getCustomValue("modmenu").getAsObject().get("links").getAsObject().get("modmenu.support.patreon").getAsString();
		if ("zh".equals(Locale.getDefault().getLanguage()) && "CN".equals(Locale.getDefault().getCountry()))
			url = mod().getMetadata().getCustomValue("modmenu").getAsObject().get("links").getAsObject().get("modmenu.support.afdian").getAsString();
		return Optional.of(new URL(url));
	}
}
