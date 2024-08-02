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

package pers.saikel0rado1iu.silk.test.magiccube;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import pers.saikel0rado1iu.silk.api.spinningjenny.BlockRegistry;
import pers.saikel0rado1iu.silk.impl.SilkMagicCube;
import pers.saikel0rado1iu.silk.test.magiccube.cauldron.CauldronLikeBehaviorTest;
import pers.saikel0rado1iu.silk.test.magiccube.cauldron.EmptyPotBlock;
import pers.saikel0rado1iu.silk.test.magiccube.cauldron.LavaPotBlock;
import pers.saikel0rado1iu.silk.test.magiccube.cauldron.LeveledCauldronLikeBlockTest;
import pers.saikel0rado1iu.silk.test.magiccube.infect.InfectMushroomTest;
import pers.saikel0rado1iu.silk.test.magiccube.infect.InfectSaplingTest;

/**
 * 方块
 */
public interface Blocks extends BlockRegistry {
	/**
	 * pots
	 */
	TagKey<Block> POTS = TagKey.of(RegistryKeys.BLOCK, SilkMagicCube.getInstance().ofId("pots"));
	/**
	 * test_block
	 */
	PhysicsConnectingBlockTest TEST_BLOCK = BlockRegistry.registrar(new PhysicsConnectingBlockTest(AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("test_block"));
	/**
	 * test_plantable_block
	 */
	PlantableBlockTest TEST_PLANTABLE_BLOCK = BlockRegistry.registrar(new PlantableBlockTest(AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("test_plantable_block"));
	/**
	 * test_spreadable_soil_block
	 */
	SpreadableSoilBlockTest TEST_SPREADABLE_SOIL_BLOCK = BlockRegistry.registrar(new SpreadableSoilBlockTest(AbstractBlock.Settings.create().ticksRandomly()))
			.register(SilkMagicCube.getInstance().ofId("test_spreadable_soil_block"));
	/**
	 * test_infect_sapling_block
	 */
	InfectSaplingTest TEST_INFECT_SAPLING_BLOCK = BlockRegistry.registrar(new InfectSaplingTest(AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("test_infect_sapling_block"));
	/**
	 * test_infect_mushroom_block
	 */
	InfectMushroomTest TEST_INFECT_MUSHROOM_BLOCK = BlockRegistry.registrar(new InfectMushroomTest(AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("test_infect_mushroom_block"));
	/**
	 * empty_pot_block
	 */
	EmptyPotBlock EMPTY_POT_BLOCK = BlockRegistry.registrar(new EmptyPotBlock(AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("empty_pot_block"));
	/**
	 * lava_pot_block
	 */
	LavaPotBlock LAVA_POT_BLOCK = BlockRegistry.registrar(new LavaPotBlock(AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("lava_pot_block"));
	/**
	 * water_pot_block
	 */
	LeveledCauldronLikeBlockTest WATER_POT_BLOCK = BlockRegistry.registrar(new LeveledCauldronLikeBlockTest(Biome.Precipitation.RAIN, CauldronLikeBehaviorTest.INSTANCE.waterBehavior, AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("water_pot_block"));
	/**
	 * powder_snow_pot_block
	 */
	LeveledCauldronLikeBlockTest POWDER_SNOW_POT_BLOCK = BlockRegistry.registrar(new LeveledCauldronLikeBlockTest(Biome.Precipitation.SNOW, CauldronLikeBehaviorTest.INSTANCE.powderSnowBehavior, AbstractBlock.Settings.create()))
			.register(SilkMagicCube.getInstance().ofId("powder_snow_pot_block"));
	/**
	 * tnt_like_block
	 */
	TntLikeBlockTest TNT_LIKE_BLOCK = BlockRegistry.registrar(new TntLikeBlockTest(AbstractBlock.Settings.create()))
			.other(block -> FlammableBlockRegistry.getDefaultInstance().add(block, 15, 100))
			.register(SilkMagicCube.getInstance().ofId("tnt_like_block"));
}
