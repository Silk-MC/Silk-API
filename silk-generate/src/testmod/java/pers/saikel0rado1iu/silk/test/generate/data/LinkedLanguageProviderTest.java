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

package pers.saikel0rado1iu.silk.test.generate.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.gen.WorldPresets;
import pers.saikel0rado1iu.silk.api.generate.data.LinkedLanguageProvider;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;

/**
 * Test {@link LinkedLanguageProvider}
 */
public final class LinkedLanguageProviderTest extends LinkedLanguageProvider {
	/**
	 * @param dataOutput 数据输出
	 */
	public LinkedLanguageProviderTest(FabricDataOutput dataOutput) {
		super(dataOutput, "zh_cn");
	}
	
	@Override
	public void generateTranslations(TranslationBuilder translationBuilder) {
		translationBuilder.add(comment("test"), "test");
		translationBuilder.add(i18nName(SilkGenerate.getInstance()), "i18nName");
		translationBuilder.add(i18nSummary(SilkGenerate.getInstance()), "i18nSummary");
		translationBuilder.add(i18nDesc(SilkGenerate.getInstance()), "i18nDesc");
		translationBuilder.add(advancementTitle(AdvancementGenUtilTest.TEST), "advancementTitle");
		translationBuilder.add(advancementDesc(AdvancementGenUtilTest.TEST), "advancementDesc");
		translationBuilder.add(soundSub(SoundEvents.ITEM_ARMOR_EQUIP_TURTLE), "equip");
		translationBuilder.add(worldPreset(WorldPresets.DEFAULT), "default");
	}
}
