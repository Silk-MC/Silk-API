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

package pers.saikel0rado1iu.silk.api;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.MinecraftVersion;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.SilkData;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">用于构建基础的模组数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface ModBasicData {
	@SilkApi
	static String getMinecraftVer() {
		return MinecraftVersion.CURRENT.getName();
	}
	
	@SilkApi
	default int getThemeColor() {
		return -1;
	}
	
	@SilkApi
	@NotNull String getModId();
	
	@SilkApi
	@NotNull
	default String getModName() {
		return getModInstance().getMetadata().getName();
	}
	
	@SilkApi
	@NotNull
	default String getModVer() {
		return getModInstance().getMetadata().getVersion().getFriendlyString();
	}
	
	@SilkApi
	@NotNull
	default String getModSlug() {
		return getModId();
	}
	
	@SilkApi
	default Collection<Person> getAuthors() {
		return getModInstance().getMetadata().getAuthors();
	}
	
	@SilkApi
	default Collection<String> getLicense() {
		return getModInstance().getMetadata().getLicense();
	}
	
	@SilkApi
	default Optional<Identifier> getIconId() {
		Optional<String> path = getModInstance().getMetadata().getIconPath(4);
		return path.map(Identifier::new);
	}
	
	@SilkApi
	default Optional<URL> getLink(LinkType type) {
		try {
			return switch (type) {
				case HOMEPAGE -> getHomepage();
				case SOURCES -> getSourcesLink();
				case ISSUES -> getIssuesLink();
				case COMMUNITY -> getCommunityLink();
				case SUPPORT -> getSupportLink();
			};
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SilkApi
	@NotNull
	default ModContainer getModInstance() {
		var modContainerOptional = FabricLoader.getInstance().getModContainer(getModId());
		if (modContainerOptional.isEmpty()) {
			String msg = "Mod not found, no mod with id '" + getModId() + "' exists!";
			SilkData.INSTANCE.getLogger().atError().log(msg);
			throw new RuntimeException(msg);
		}
		return modContainerOptional.get();
	}
	
	/**
	 * <p>此记录器用于将文本写入控制台和日志文件。</p>
	 * <p>这样，就很清楚是哪个模组写了信息、警告和错误。</p>
	 */
	@SilkApi
	@NotNull
	default Logger getLogger() {
		return LoggerFactory.getLogger(getModName());
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getHomepage() throws MalformedURLException {
		Optional<String> url = getModInstance().getMetadata().getContact().get("homepage");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getSourcesLink() throws MalformedURLException {
		Optional<String> url = getModInstance().getMetadata().getContact().get("sources");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getIssuesLink() throws MalformedURLException {
		Optional<String> url = getModInstance().getMetadata().getContact().get("issues");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getCommunityLink() throws MalformedURLException {
		return Optional.empty();
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getSupportLink() throws MalformedURLException {
		return Optional.empty();
	}
	
	enum LinkType {
		HOMEPAGE, SOURCES, ISSUES, COMMUNITY, SUPPORT
	}
}
