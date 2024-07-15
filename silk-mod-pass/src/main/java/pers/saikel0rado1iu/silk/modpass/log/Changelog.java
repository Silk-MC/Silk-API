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

package pers.saikel0rado1iu.silk.modpass.log;

import pers.saikel0rado1iu.silk.impl.SilkModPass;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">更新日志</h2>
 * 用于读取模组的更新日志
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface Changelog {
	/**
	 * 获取更新日志路径<br>
	 * 读取的更新日志位置于资源包根目录下的 {@code CHANGELOG} 文件夹内
	 *
	 * @return 更新日志路径
	 */
	static Path path() {
		String msg = "Unexpected error: Unable to read the changelog path!";
		try {
			return Path.of(Optional.ofNullable(Changelog.class.getResource("/CHANGELOG")).orElseThrow(() -> {
				SilkModPass.getInstance().logger().error(msg);
				return new RuntimeException(msg);
			}).toURI());
		} catch (URISyntaxException e) {
			SilkModPass.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
	}
	
	/**
	 * 获取特定语言的更新日志<br>
	 * 不同语言的日志文件名为 {@code "id().langCode.md"}
	 *
	 * @param modPass  模组通
	 * @param langCode 语言代码
	 * @return 如果有 {@code langCode} 的更新日志则返回此语言的更新日志<br>
	 * 如果没有 {@code langCode} 的更新日志则返回 {@code en_us} 语言的更新日志<br>
	 * 如果没有更新日志则返回 {@link  Optional#empty()}
	 */
	static Optional<Path> get(ModPass modPass, String langCode) {
		String ChangelogName = String.format("%s.%s.md", modPass.modData().id(), langCode);
		Path Changelog = path().resolve(ChangelogName);
		if (Files.exists(Changelog)) return Optional.of(Changelog);
		ChangelogName = String.format("%s.%s.md", modPass.modData().id(), "en_us");
		Changelog = path().resolve(ChangelogName);
		if (Files.exists(Changelog)) return Optional.of(Changelog);
		return Optional.empty();
	}
	
	/**
	 * 读取更新日志
	 *
	 * @param modPass  模组通
	 * @param langCode 语言代码
	 * @return 日志的字符串
	 */
	static String read(ModPass modPass, String langCode) {
		try {
			Optional<Path> path = get(modPass, langCode);
			if (path.isEmpty()) return "Changelog does not exist!";
			StringBuilder changelog = new StringBuilder().append(Files.readString(path.get(), StandardCharsets.UTF_8));
			// 把不同的标题大小归一化
			for (int count = 0; count < 5; count++) changelog = new StringBuilder(changelog.toString().replaceAll("## ", "# "));
			// 修正换行符问题
			String[] str = changelog.toString().replaceAll("\r", "\n").replaceAll("\n\n", "\n").split("\n");
			// 把 md 标题切换为 mc 粗体
			for (int count = 0; count < str.length; count++) {
				if (str[count].contains("# ")) {
					changelog.setLength(0);
					str[count] = changelog.append(str[count].replaceFirst("# ", "§l")).append("§r").toString();
				}
			}
			changelog.setLength(0);
			for (String s : str) changelog.append(s).append("\n");
			// 处理缩进与无序列表
			changelog = new StringBuilder(changelog.toString().replaceAll("- ", "・").replaceAll("\\* ", "・").replaceAll("\\+ ", "・"));
			if (langCode.contains("zh")) changelog = new StringBuilder(changelog.toString().replaceAll("\t", "　").replaceAll(" ", "·"));
			else changelog = new StringBuilder(changelog.toString().replaceAll("\t", "  "));
			return changelog.toString();
		} catch (IOException e) {
			return "Changelog does not exist!";
		}
	}
}
