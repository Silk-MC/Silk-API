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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import pers.saikel0rado1iu.silk.modpass.ModData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h2 style="color:FFC800">Minecraft 数据</h2>
 * 独立的 Minecraft 数据集
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface Minecraft extends ModData {
	/**
	 * 提供实例
	 *
	 * @return 模组数据实例
	 */
	static Minecraft getInstance() {
		return new Minecraft() {
		};
	}
	
	/**
	 * 比较 MC 版本
	 *
	 * @param version        原始版本
	 * @param compareVersion 需要比较的版本
	 * @return -1 为小于；0 为等于；1 为大于
	 */
	static int compareVersion(String version, String compareVersion) {
		version = StringUtils.substringBefore(version, "-");
		compareVersion = StringUtils.substringBefore(compareVersion, "-");
		if (version.contains("w") || version.contains("a") || compareVersion.contains("w") || compareVersion.contains("a")) return 0;
		
		ArrayList<String> originalVerList = new ArrayList<>(Arrays.asList(version.split("\\.")));
		version = version.replaceAll("\\.", "");
		if (originalVerList.size() < 3) version += "0";
		ArrayList<String> compareVerList = new ArrayList<>(Arrays.asList(compareVersion.split("\\.")));
		compareVersion = compareVersion.replaceAll("\\.", "");
		if (compareVerList.size() < 3) compareVersion += "0";
		
		return Integer.compare(Integer.parseInt(compareVersion), Integer.parseInt(version));
	}
	
	/**
	 * 获取 Modrinth API 中模组对象的最新 MC 版本
	 *
	 * @param data Modrinth API 中模组对象
	 * @return 模组的最新 MC 版本
	 */
	static String getLatestVersion(JsonObject data) {
		List<String> verList = new ArrayList<>(4);
		JsonArray verArray = data.getAsJsonArray("game_versions");
		for (JsonElement object : verArray) verList.add(object.getAsString());
		String latestMinecraftVer = verList.get(0);
		for (int count = 0; count < verList.size() - 1; count++) {
			latestMinecraftVer = compareVersion(verList.get(count), verList.get(count + 1)) < 0 ? verList.get(count) : verList.get(count + 1);
		}
		return latestMinecraftVer;
	}
	
	/**
	 * 比较 MC 版本
	 *
	 * @param compareVersion 需要比较的版本
	 * @return -1 为小于；0 为等于；1 为大于
	 */
	default int compareVersion(String compareVersion) {
		return compareVersion(version(), compareVersion);
	}
	
	@Override
	default String id() {
		return "minecraft";
	}
}
