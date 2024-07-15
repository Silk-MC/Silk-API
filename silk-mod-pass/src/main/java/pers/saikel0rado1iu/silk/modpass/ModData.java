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

package pers.saikel0rado1iu.silk.modpass;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">模组数据</h2>
 * 使用此接口参考 {@link pers.saikel0rado1iu.silk.impl.SilkApi}
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ModData extends ModPass {
	@Override
	default ModData modData() {
		return this;
	}
	
	/**
	 * 获取模组的唯一标识符
	 *
	 * @return 模组的唯一标识符
	 */
	String id();
	
	/**
	 * 用于更快捷的创建基于模组的标识符
	 *
	 * @param path 标识符路径
	 * @return 标识符
	 */
	default Identifier ofId(String path) {
		return new Identifier(id(), path);
	}
	
	/**
	 * 获取模组信息的容器
	 *
	 * @return 包含模组信息的 {@link ModContainer} 对象
	 * @throws RuntimeException 如果未找到指定的模组信息
	 */
	default ModContainer mod() {
		var modContainerOptional = FabricLoader.getInstance().getModContainer(id());
		if (modContainerOptional.isEmpty()) {
			String msg = String.format("The mod was not found: The mod with path '%s' does not exist!", id());
			SilkModPass.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
		return modContainerOptional.get();
	}
	
	/**
	 * 获取记录器
	 * <p>此记录器用于将文本写入控制台和日志文件，以便清楚地区分是哪个模组写了信息、警告和错误</p>
	 *
	 * @return 记录器对象
	 */
	default Logger logger() {
		return LoggerFactory.getLogger(name());
	}
	
	/**
	 * 获取调试名称，用于调试时输出模组名称的同时附带模组 ID
	 *
	 * @return 调试名称
	 */
	default String debugName() {
		return String.format("[%s(%s)]", name(), id());
	}
	
	/**
	 * 获取模组的名称
	 *
	 * @return 模组的名称
	 */
	default String name() {
		return mod().getMetadata().getName();
	}
	
	/**
	 * 获取模组的描述信息
	 *
	 * @return 模组的描述信息
	 */
	default String description() {
		return mod().getMetadata().getDescription();
	}
	
	/**
	 * 获取模组的版本号
	 *
	 * @return 模组的版本号
	 */
	default String version() {
		return mod().getMetadata().getVersion().getFriendlyString();
	}
	
	/**
	 * 获取模组的唯一标识符
	 *
	 * @return 模组的唯一标识符
	 */
	default String slug() {
		return id();
	}
	
	/**
	 * 获取模组的作者列表
	 *
	 * @return 模组的作者列表
	 */
	default Collection<String> authors() {
		List<String> authors = new ArrayList<>(2);
		mod().getMetadata().getAuthors().stream().toList().forEach(person -> {
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
	
	/**
	 * 获取模组的许可证列表
	 *
	 * @return 模组的许可证列表
	 */
	default Collection<String> licenses() {
		return mod().getMetadata().getLicense();
	}
	
	/**
	 * 获取模组的图标
	 *
	 * @return 包含模组图标的 {@link Optional} 对象
	 */
	default Optional<Identifier> icon() {
		Optional<String> path = mod().getMetadata().getIconPath(4);
		return path.map(id -> new Identifier(id(), "icon.png"));
	}
	
	/**
	 * 获取指定类型的链接
	 *
	 * @param type 链接类型
	 * @return 包含链接的 {@link Optional} 对象
	 * @throws RuntimeException 如果链接类型无效或链接格式不正确
	 */
	default Optional<URL> link(LinkType type) {
		try {
			return switch (type) {
				case HOMEPAGE -> homepage();
				case SOURCES -> sources();
				case ISSUES -> issues();
				case COMMUNITY -> community();
				case SUPPORT -> support();
			};
		} catch (MalformedURLException e) {
			String msg = String.format("Mod link error: Mod '%s(%s)' does not have a %s URL or the URL format is incorrect.", name(), id(), type);
			SilkModPass.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
	}
	
	/**
	 * 获取模组主页链接
	 * <p>此方法推荐仅用于重定向链接，推荐使用 {@link ModData#link(LinkType)}</p>
	 *
	 * @return 模组主页的链接
	 * @throws MalformedURLException 如果模组主页链接格式不正确
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> homepage() throws MalformedURLException {
		Optional<String> url = mod().getMetadata().getContact().get("homepage");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 获取模组源代码链接
	 * <p>此方法推荐仅用于重定向链接，推荐使用 {@link ModData#link(LinkType)}</p>
	 *
	 * @return 模组源代码的链接
	 * @throws MalformedURLException 如果模组源代码链接格式不正确
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> sources() throws MalformedURLException {
		Optional<String> url = mod().getMetadata().getContact().get("sources");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 获取模组问题跟踪链接
	 * <p>此方法推荐仅用于重定向链接，推荐使用 {@link ModData#link(LinkType)}</p>
	 *
	 * @return 模组问题跟踪的链接
	 * @throws MalformedURLException 如果模组问题跟踪链接格式不正确
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> issues() throws MalformedURLException {
		Optional<String> url = mod().getMetadata().getContact().get("issues");
		if (url.isEmpty()) return Optional.empty();
		return Optional.of(new URL(url.get()));
	}
	
	/**
	 * 获取模组社区链接
	 * <p>此方法推荐仅用于重定向链接，推荐使用 {@link ModData#link(LinkType)}</p>
	 *
	 * @return 模组社区的链接，如果不存在则返回空的 {@link Optional} 对象
	 * @throws MalformedURLException URL 格式异常
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> community() throws MalformedURLException {
		return Optional.empty();
	}
	
	/**
	 * 获取模组支持链接
	 * <p>此方法推荐
	 * 此方法推荐仅用于重定向链接，推荐使用 {@link ModData#link(LinkType)}
	 *
	 * @return 模组支持的链接，如果不存在则返回空的 {@link Optional} 对象
	 * @throws MalformedURLException URL 格式异常
	 */
	@ApiStatus.OverrideOnly
	default Optional<URL> support() throws MalformedURLException {
		return Optional.empty();
	}
	
	/**
	 * 链接类型枚举
	 */
	enum LinkType {
		/**
		 * 主页
		 */
		HOMEPAGE,
		/**
		 * 源码
		 */
		SOURCES,
		/**
		 * 反馈
		 */
		ISSUES,
		/**
		 * 社区
		 */
		COMMUNITY,
		/**
		 * 支持
		 */
		SUPPORT
	}
}
