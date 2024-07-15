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

package pers.saikel0rado1iu.silk.test.magiccube.cauldron;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;
import pers.saikel0rado1iu.silk.magiccube.cauldron.CauldronLikeBehavior;
import pers.saikel0rado1iu.silk.magiccube.cauldron.LeveledCauldronLikeBlock;
import pers.saikel0rado1iu.silk.test.magiccube.Blocks;

/**
 * Test {@link CauldronLikeBehavior}
 */
public final class CauldronLikeBehaviorTest extends CauldronLikeBehavior {
	/**
	 * 实例
	 */
	public static final CauldronLikeBehaviorTest INSTANCE = new CauldronLikeBehaviorTest();
	
	/**
	 * 水平面坩埚类方块
	 *
	 * @return 水平面坩埚类方块
	 */
	@Override
	public LeveledCauldronLikeBlock leveledCauldronLikeBlock() {
		return Blocks.WATER_POT_BLOCK;
	}
	
	/**
	 * 空坩埚类方块
	 *
	 * @return 空坩埚类方块
	 */
	@Override
	public AbstractCauldronBlock emptyCauldronLikeBlock() {
		return Blocks.EMPTY_POT_BLOCK;
	}
	
	/**
	 * 含水坩埚类方块
	 *
	 * @return 含水坩埚类方块
	 */
	@Override
	public AbstractCauldronBlock waterCauldronLikeBlock() {
		return Blocks.WATER_POT_BLOCK;
	}
	
	/**
	 * 含熔岩坩埚类方块
	 *
	 * @return 含熔岩坩埚类方块
	 */
	@Override
	public AbstractCauldronBlock lavaCauldronLikeBlock() {
		return Blocks.LAVA_POT_BLOCK;
	}
	
	/**
	 * 含细雪坩埚类方块
	 *
	 * @return 含细雪坩埚类方块
	 */
	@Override
	public AbstractCauldronBlock powderSnowCauldronLikeBlock() {
		return Blocks.POWDER_SNOW_POT_BLOCK;
	}
	
	/**
	 * 同种坩埚方块标签，在标签内的方块被认为是同一种坩埚类方块的不同变体
	 *
	 * @return 方块标签
	 */
	@Override
	public TagKey<Block> sameCauldronBlockTag() {
		return Blocks.POTS;
	}
}
