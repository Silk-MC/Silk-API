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

package pers.saikel0rado1iu.silk.gen.data;

import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.WorldPreset;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

/**
 * <h2 style="color:FFC800">用于提供一些不常用的本地化翻译方法，并且将 {@link FabricLanguageProvider} 中的 {@link TreeMap} 替换为 {@link LinkedTreeMap}</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkLanguageProvider extends FabricLanguageProvider {
	private final String languageCode;
	private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
	
	protected SilkLanguageProvider(FabricDataOutput dataOutput, String languageCode, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, languageCode, registryLookup);
		this.languageCode = languageCode;
		this.registryLookup = registryLookup;
	}
	
	@SilkApi
	public static String getCommentKey(String key) {
		return "// " + key;
	}
	
	@SilkApi
	public static String getWorldPresetKey(RegistryKey<WorldPreset> key) {
		return "generator." + key.getValue().getNamespace() + "." + key.getValue().getPath();
	}
	
	@SilkApi
	public static String getLocalizationNameKey(ModBasicData mod) {
		return "modmenu.nameTranslation." + mod.getId();
	}
	
	@SilkApi
	public static String getLocalizationSummaryKey(ModBasicData mod) {
		return "modmenu.summaryTranslation." + mod.getId();
	}
	
	@SilkApi
	public static String getLocalizationDescriptionKey(ModBasicData mod) {
		return "modmenu.descriptionTranslation." + mod.getId();
	}
	
	@SilkApi
	public static String getSoundEventSubtitlesKey(SoundEvent soundEvent) {
		return "subtitles." + soundEvent.getId().getNamespace() + '.' + soundEvent.getId().getPath();
	}
	
	@SilkApi
	public static String getAdvancementTitleKey(AdvancementEntry advancement) {
		return advancement.value().display().orElseThrow().getTitle().getString();
	}
	
	@SilkApi
	public static String getAdvancementDescriptionKey(AdvancementEntry advancement) {
		return advancement.value().display().orElseThrow().getDescription().getString();
	}
	
	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		LinkedTreeMap<String, String> translationEntries = new LinkedTreeMap<>();
		
		return registryLookup.thenCompose(lookup -> {
			generateTranslations(lookup, (String key, String value) -> {
				Objects.requireNonNull(key);
				Objects.requireNonNull(value);
				
				if (translationEntries.containsKey(key))
					throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
				translationEntries.put(key, value);
			});
			
			JsonObject langEntryJson = new JsonObject();
			
			for (Map.Entry<String, String> entry : translationEntries.entrySet()) langEntryJson.addProperty(entry.getKey(), entry.getValue());
			
			return DataProvider.writeToPath(writer, langEntryJson, getLangFilePath(this.languageCode));
		});
	}
	
	private Path getLangFilePath(String code) {
		return dataOutput
				.getResolver(DataOutput.OutputType.RESOURCE_PACK, "lang")
				.resolveJson(new Identifier(dataOutput.getModId(), code));
	}
}
