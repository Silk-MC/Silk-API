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

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import pers.saikel0rado1iu.silk.api.generate.data.client.ExtendedBlockStateModelGenerator;
import pers.saikel0rado1iu.silk.api.generate.data.client.ExtendedItemModelGenerator;
import pers.saikel0rado1iu.silk.api.generate.data.client.TextureKeys;
import pers.saikel0rado1iu.silk.api.magiccube.cauldron.LeveledCauldronLikeBlock;

import static pers.saikel0rado1iu.silk.test.generate.Items.TEST_BOW;
import static pers.saikel0rado1iu.silk.test.generate.Items.TEST_CROSSBOW;

/**
 * ModelProvider
 */
public final class ModelProvider extends FabricModelProvider {
	/**
	 * @param output 数据输出
	 */
	public ModelProvider(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		ExtendedBlockStateModelGenerator generator = new ExtendedBlockStateModelGenerator(blockStateModelGenerator);
		generator.registerCubeColumn(Blocks.OAK_LOG, TextureMap.getId(Blocks.OAK_LOG), TextureMap.getId(Blocks.OAK_LEAVES));
		generator.registerCarpet(Blocks.BLACK_CARPET, true);
		generator.registerTopSoil(Blocks.GRASS_BLOCK);
		generator.registerVines(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, BlockStateModelGenerator.TintType.NOT_TINTED);
		generator.registerConnectingBlock(Blocks.CHORUS_PLANT);
		generator.registerEmptyCauldron(Blocks.CAULDRON, false);
		generator.registerLavaCauldron(Blocks.LAVA_CAULDRON, Models.TEMPLATE_CAULDRON_FULL);
		try {
			generator.registerWaterCauldron((LeveledCauldronLikeBlock) Blocks.WATER_CAULDRON, Models.TEMPLATE_CAULDRON_LEVEL1, Models.TEMPLATE_CAULDRON_LEVEL2, Models.TEMPLATE_CAULDRON_FULL);
			generator.registerSnowCauldron((LeveledCauldronLikeBlock) Blocks.POWDER_SNOW_CAULDRON, Models.TEMPLATE_CAULDRON_LEVEL1, Models.TEMPLATE_CAULDRON_LEVEL2, Models.TEMPLATE_CAULDRON_FULL);
		} catch (Exception e) {
		}
	}
	
	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		ExtendedItemModelGenerator generator = new ExtendedItemModelGenerator(itemModelGenerator);
		generator.registerItemGroup(ItemGroups.COMBAT);
		generator.registerBlockItem((BlockItem) Items.GRASS_BLOCK);
		generator.registerOverlayItem(Items.DIAMOND);
		generator.registerSpawnEggItem((SpawnEggItem) Items.ALLAY_SPAWN_EGG);
		generator.registerBow(TEST_BOW, new float[]{0, 0.65F, 0.9F});
		generator.registerCrossbow(TEST_CROSSBOW, new float[]{0, 0.65F, 0.9F});
		generator.registerStereoscopicItem(Items.COPPER_INGOT, ImmutableMap.of(TextureKeys.LAYER7, ""));
	}
}