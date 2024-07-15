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

package pers.saikel0rado1iu.silk.generate.data;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;
import net.minecraft.world.gen.WorldPreset;
import pers.saikel0rado1iu.silk.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

/**
 * <h2 style="color:FFC800">链接语言提供器</h2>
 * 继承 {@link FabricLanguageProvider}，用于提供一些不常用的国际化翻译方法。<br>
 * 并且将 {@link FabricLanguageProvider} 中的 {@link TreeMap} 替换为 {@link LinkedTreeMap} 使其按添加顺序排列
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class LinkedLanguageProvider extends FabricLanguageProvider {
	private final String languageCode;
	
	protected LinkedLanguageProvider(FabricDataOutput dataOutput, String languageCode) {
		super(dataOutput, languageCode);
		this.languageCode = languageCode;
	}
	
	/**
	 * 注释翻译键方法
	 *
	 * @param key 注释键
	 * @return 注释翻译键
	 */
	public static String comment(String key) {
		return "// " + key;
	}
	
	/**
	 * 国际化模组名翻译键方法
	 *
	 * @param modPass 模组通
	 * @return 国际化模组名翻译键
	 */
	public static String i18nName(ModPass modPass) {
		return I18nModInfoProvider.MOD_MENU_I18N_NAME_KEY_PREFIX + modPass.modData().id();
	}
	
	/**
	 * 国际化模组总结翻译键方法
	 *
	 * @param modPass 模组通
	 * @return 国际化模组总结翻译键
	 */
	public static String i18nSummary(ModPass modPass) {
		return I18nModInfoProvider.MOD_MENU_I18N_SUMMARY_KEY_PREFIX + modPass.modData().id();
	}
	
	/**
	 * 国际化模组描述翻译键方法
	 *
	 * @param modPass 模组通
	 * @return 国际化模组描述翻译键
	 */
	public static String i18nDesc(ModPass modPass) {
		return I18nModInfoProvider.MOD_MENU_I18N_DESC_KEY_PREFIX + modPass.modData().id();
	}
	
	/**
	 * 进度标题翻译键方法
	 *
	 * @param advancement 进度条目
	 * @return 进度标题
	 */
	public static String advancementTitle(AdvancementEntry advancement) {
		return advancement.value().display().orElseThrow().getTitle().getString();
	}
	
	/**
	 * 进度描述翻译键方法
	 *
	 * @param advancement 进度条目
	 * @return 进度描述
	 */
	public static String advancementDesc(AdvancementEntry advancement) {
		return advancement.value().display().orElseThrow().getDescription().getString();
	}
	
	/**
	 * 音效字幕翻译键方法
	 *
	 * @param soundEvent 音效
	 * @return 音效字幕
	 */
	public static String soundSub(SoundEvent soundEvent) {
		return "subtitles." + soundEvent.getId().getNamespace() + '.' + soundEvent.getId().getPath();
	}
	
	/**
	 * 世界预设翻译键方法
	 *
	 * @param key 世界预设注册键
	 * @return 世界预设翻译键
	 */
	public static String worldPreset(RegistryKey<WorldPreset> key) {
		return "generator." + key.getValue().getNamespace() + "." + key.getValue().getPath();
	}
	
	/**
	 * 写入路径
	 *
	 * @param writer 数据编写器
	 * @param json   JSON 元素
	 * @param path   路径
	 * @return 异步任务
	 */
	@SuppressWarnings("UnstableApiUsage")
	public static CompletableFuture<?> writeToPath(DataWriter writer, JsonElement json, Path path) {
		return CompletableFuture.runAsync(() -> {
			try {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				HashingOutputStream hashingOutputStream = new HashingOutputStream(Hashing.sha256(), byteArrayOutputStream);
				try (JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(hashingOutputStream, StandardCharsets.UTF_8))) {
					jsonWriter.setSerializeNulls(false);
					jsonWriter.setIndent("  ");
					JsonHelper.writeSorted(jsonWriter, json, null);
				}
				writer.write(path, byteArrayOutputStream.toByteArray(), hashingOutputStream.hash());
			} catch (IOException e) {
				LOGGER.error("Failed to save file to {}", path, e);
			}
		}, Util.getMainWorkerExecutor());
	}
	
	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		LinkedTreeMap<String, String> translationEntries = new LinkedTreeMap<>();
		
		generateTranslations((String key, String value) -> {
			Objects.requireNonNull(key);
			Objects.requireNonNull(value);
			if (translationEntries.containsKey(key)) throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
			translationEntries.put(key, value);
		});
		
		JsonObject langEntryJson = new JsonObject();
		translationEntries.forEach(langEntryJson::addProperty);
		
		return writeToPath(writer, langEntryJson, dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, "lang").resolveJson(new Identifier(dataOutput.getModId(), languageCode)));
	}
}
