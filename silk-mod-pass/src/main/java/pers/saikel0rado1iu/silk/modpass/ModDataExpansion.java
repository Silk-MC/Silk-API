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

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.modpass.pack.ResourcePack;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * <h2 style="color:FFC800">模组数据拓展</h2>
 * 使用此接口参考 {@link pers.saikel0rado1iu.silk.impl.SilkApi}
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ModDataExpansion extends ModData {
	/**
	 * 获取主题颜色
	 *
	 * @return 主题颜色的值
	 */
	int themeColor();
	
	/**
	 * 获取模组的国际化名称。
	 *
	 * @return 模组的国际化名称。
	 */
	Text i18nName();
	
	/**
	 * 获取模组的国际化摘要。
	 *
	 * @return 模组的国际化摘要。
	 */
	Text i18nSummary();
	
	/**
	 * 获取模组的国际化描述信息。
	 *
	 * @return 模组的国际化描述信息。
	 */
	Text i18nDescription();
	
	/**
	 * 模组提供的数据包
	 *
	 * @return 可选的数据包
	 */
	Optional<DataPack> dataPack();
	
	/**
	 * 模组提供的资源包
	 *
	 * @return 可选的资源包
	 */
	Optional<ResourcePack> resourcePack();
	
	/**
	 * 获取模组的联系邮箱。
	 *
	 * @return 包含联系邮箱的 {@link Optional} 对象。
	 */
	default Optional<String> email() {
		return mod().getMetadata().getContact().get("email");
	}
	
	/**
	 * 获取模组的 IRC 地址。
	 *
	 * @return 包含 IRC 地址的 {@link Optional} 对象。
	 */
	default Optional<String> irc() {
		return mod().getMetadata().getContact().get("irc");
	}
	
	/**
	 * 获取模组的贡献者列表。
	 *
	 * @return 模组的贡献者列表。
	 */
	default Collection<String> contributors() {
		List<String> contributors = new ArrayList<>(2);
		mod().getMetadata().getContributors().stream().toList().forEach(person -> {
			String name = person.getName();
			String magic = "§";
			if (name.contains(magic)) {
				List<String> strings = new ArrayList<>(List.of(name.split("§")));
				strings.replaceAll(s -> !s.isEmpty() ? s.substring(1) : s);
				name = String.join("", strings);
			}
			contributors.add(name);
		});
		return contributors;
	}
	
	/**
	 * 获取模组中包含的嵌套 JAR 文件的集合。
	 *
	 * @return 包含模组中嵌套 JAR 文件的 {@link Collection}。
	 */
	default Collection<ModContainer> nested() {
		return mod().getContainedMods();
	}
	
	/**
	 * 获取模组的依赖列表。
	 *
	 * @return 模组的依赖列表。
	 */
	default Collection<ModDependency> depends() {
		return mod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.DEPENDS).toList();
	}
	
	/**
	 * 获取模组的推荐依赖列表。
	 *
	 * @return 模组的推荐依赖列表。
	 */
	default Collection<ModDependency> recommends() {
		return mod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.RECOMMENDS).toList();
	}
	
	/**
	 * 获取模组的建议依赖列表。
	 *
	 * @return 模组的建议依赖列表。
	 */
	default Collection<ModDependency> suggests() {
		return mod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.SUGGESTS).toList();
	}
	
	/**
	 * 获取模组的冲突依赖列表。
	 *
	 * @return 模组的冲突依赖列表。
	 */
	default Collection<ModDependency> breaks() {
		return mod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.BREAKS).toList();
	}
	
	/**
	 * 获取模组的破坏依赖列表。
	 *
	 * @return 模组的破坏依赖列表。
	 */
	default Collection<ModDependency> conflicts() {
		return mod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.CONFLICTS).toList();
	}
	
	/**
	 * 获取模组的 JAR 文件。
	 *
	 * @return 模组的 JAR 文件。
	 */
	default Path jar() {
		return mod().getOrigin().getPaths().get(0);
	}
	
	/**
	 * 获取模组 JAR 文件的路径。
	 *
	 * @return 模组 JAR 文件的路径。
	 */
	default Path jarPath() {
		String separator = File.separator.contains("\\") ? "\\\\" : File.separator;
		List<String> paths = new ArrayList<>(Arrays.asList(jar().toString().split(separator)));
		String head = paths.get(0);
		paths.remove(0);
		paths.remove(paths.size() - 1);
		return Paths.get(head, paths.toArray(new String[0]));
	}
	
	/**
	 * 获取模组的 JAR 文件名称。
	 *
	 * @return 模组的 JAR 文件名称。
	 */
	default String jarName() {
		String separator = File.separator.contains("\\") ? "\\\\" : File.separator;
		String[] paths = jar().toString().split(separator);
		return paths[paths.length - 1];
	}
}
