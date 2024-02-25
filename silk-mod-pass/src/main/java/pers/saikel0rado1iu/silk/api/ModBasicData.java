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
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">用于构建基础的模组数据</h2>
 * <p style="color:FFC800">推荐使用模组 ID 或模组缩写作为模组数据类名，DATA 作为实例常量名，并且私有化构造函数</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface ModBasicData {
	@SilkApi
	default int getThemeColor() {
		return -1;
	}
	
	@SilkApi
	@NotNull String getId();
	
	@SilkApi
	@NotNull
	default String getName() {
		return getMod().getMetadata().getName();
	}
	
	@SilkApi
	default String getDescription() {
		return getMod().getMetadata().getDescription();
	}
	
	@SilkApi
	@NotNull
	default String getVersion() {
		return getMod().getMetadata().getVersion().getFriendlyString();
	}
	
	@SilkApi
	@NotNull
	default String getSlug() {
		return getId();
	}
	
	@SilkApi
	default Collection<String> getAuthors() {
		List<String> authors = new ArrayList<>(2);
		getMod().getMetadata().getAuthors().stream().toList().forEach(person -> {
			String name = person.getName();
			String magic = "§";
			if (name.contains(magic)) {
				List<String> strings = new ArrayList<>(List.of(name.split("§")));
				strings.replaceAll(s -> !s.isEmpty() ? s.substring(1) : s);
				name = String.join("", strings);
			}
			authors.add(name);
		});
		return authors;
	}
	
	@SilkApi
	default Collection<String> getLicenses() {
		return getMod().getMetadata().getLicense();
	}
	
	@SilkApi
	default Optional<Identifier> getIcon() {
		Optional<String> path = getMod().getMetadata().getIconPath(4);
		return path.map(id -> new Identifier(getId(), "icon.png"));
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
	default ModContainer getMod() {
		var modContainerOptional = FabricLoader.getInstance().getModContainer(getId());
		if (modContainerOptional.isEmpty()) {
			String msg = "Mod not found, no mod with id '" + getId() + "' exists!";
			Silk.DATA.logger().atError().log(msg);
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
	default Logger logger() {
		return LoggerFactory.getLogger(getName());
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getHomepage() throws MalformedURLException {
		Optional<String> url = getMod().getMetadata().getContact().get("homepage");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getSourcesLink() throws MalformedURLException {
		Optional<String> url = getMod().getMetadata().getContact().get("sources");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModBasicData#getLink(LinkType)}
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> getIssuesLink() throws MalformedURLException {
		Optional<String> url = getMod().getMetadata().getContact().get("issues");
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
