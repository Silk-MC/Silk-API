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

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * <h2 style="color:FFC800">用于构建扩展的模组数据</font></b></p>
 * <p style="color:FFC800">推荐使用模组 ID 或模组缩写作为模组数据类名，DATA 作为实例常量名，并且私有化构造函数</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface ModExtendedData extends ModBasicData {
	@SilkApi
	default String getJarName() {
		String separator = File.separator.contains("\\") ? "\\\\" : File.separator;
		String[] paths = getJar().toString().split(separator);
		return paths[paths.length - 1];
	}
	
	@SilkApi
	default Path getPath() {
		String separator = File.separator.contains("\\") ? "\\\\" : File.separator;
		List<String> paths = new ArrayList<>(Arrays.stream(getJar().toString().split(separator)).toList());
		String head = paths.get(0);
		paths.remove(0);
		paths.remove(paths.size() - 1);
		return Paths.get(head, paths.toArray(new String[0]));
	}
	
	@SilkApi
	default Path getJar() {
		return getMod().getOrigin().getPaths().get(0);
	}
	
	@SilkApi
	default String getLocalizedName() {
		String str = "modmenu.nameTranslation." + getId();
		return str.equals(Text.translatable(str).getString()) ? getName() : Text.translatable(str).getString();
	}
	
	@SilkApi
	default Text getLocalizedSummary() {
		return Text.translatable("modmenu.summaryTranslation." + getId());
	}
	
	@SilkApi
	default Text getLocalizedDescription() {
		return Text.translatable("modmenu.descriptionTranslation." + getId());
	}
	
	@SilkApi
	default Optional<String> getEmail() {
		return getMod().getMetadata().getContact().get("email");
	}
	
	@SilkApi
	default Optional<String> getIrc() {
		return getMod().getMetadata().getContact().get("irc");
	}
	
	@SilkApi
	default Collection<String> getContributors() {
		List<String> contributors = new ArrayList<>(2);
		getMod().getMetadata().getContributors().stream().toList().forEach(person -> {
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
	
	@SilkApi
	default Collection<ModContainer> getNestedJars() {
		return getMod().getContainedMods();
	}
	
	@SilkApi
	default Collection<ModDependency> getDepends() {
		return getMod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.DEPENDS).toList();
	}
	
	@SilkApi
	default Collection<ModDependency> getRecommends() {
		return getMod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.RECOMMENDS).toList();
	}
	
	@SilkApi
	default Collection<ModDependency> getSuggests() {
		return getMod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.SUGGESTS).toList();
	}
	
	@SilkApi
	default Collection<ModDependency> getBreaks() {
		return getMod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.BREAKS).toList();
	}
	
	@SilkApi
	default Collection<ModDependency> getConflicts() {
		return getMod().getMetadata().getDependencies().stream().filter(mod -> mod.getKind() == ModDependency.Kind.CONFLICTS).toList();
	}
	
}
