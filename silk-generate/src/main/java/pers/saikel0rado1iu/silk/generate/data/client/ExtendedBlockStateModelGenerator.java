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

package pers.saikel0rado1iu.silk.generate.data.client;

import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">扩展方块状态模型生成器</h2>
 * 提供了其他可能用到的高级方块状态模型方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class ExtendedBlockStateModelGenerator extends BlockStateModelGenerator {
	/**
	 * @param blockStateModelGenerator 方块模型生成器
	 */
	public ExtendedBlockStateModelGenerator(BlockStateModelGenerator blockStateModelGenerator) {
		this(blockStateModelGenerator.blockStateCollector, blockStateModelGenerator.modelCollector, blockStateModelGenerator.simpleItemModelExemptionCollector);
	}
	
	/**
	 * @param blockStateCollector               方块状态收集器
	 * @param modelCollector                    模型收集器
	 * @param simpleItemModelExemptionCollector 简单物品模型排除收集器
	 */
	public ExtendedBlockStateModelGenerator(Consumer<BlockStateSupplier> blockStateCollector, BiConsumer<Identifier, Supplier<JsonElement>> modelCollector, Consumer<Item> simpleItemModelExemptionCollector) {
		super(blockStateCollector, modelCollector, simpleItemModelExemptionCollector);
	}
	
	/**
	 * 注册地毯模型
	 *
	 * @param carpet 地毯方块
	 * @param isFlat 是平面
	 */
	public void registerCarpet(Block carpet, boolean isFlat) {
		if (isFlat) registerItemModel(carpet);
		blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(carpet, TexturedModel.CARPET.get(carpet).upload(carpet, modelCollector)));
	}
	
	/**
	 * 注册顶层土块模型
	 *
	 * @param soil 泥土方块
	 */
	public void registerTopSoil(Block soil) {
		Identifier identifier = TextureMap.getId(Blocks.DIRT);
		BlockStateVariant blockStateVariant = BlockStateVariant.create().put(VariantSettings.MODEL, Models.CUBE_BOTTOM_TOP.upload(Blocks.GRASS_BLOCK, "_snow", new TextureMap()
				.put(TextureKey.BOTTOM, identifier).inherit(TextureKey.BOTTOM, TextureKey.PARTICLE)
				.put(TextureKey.TOP, TextureMap.getSubId(Blocks.GRASS_BLOCK, "_top"))
				.put(TextureKey.SIDE, TextureMap.getSubId(Blocks.GRASS_BLOCK, "_snow")), modelCollector));
		Identifier modelId = TexturedModel.CUBE_BOTTOM_TOP.get(soil)
				.textures(textures -> textures.put(TextureKey.BOTTOM, identifier))
				.upload(soil, modelCollector);
		registerTopSoil(soil, modelId, blockStateVariant);
	}
	
	/**
	 * 注册藤蔓模型
	 *
	 * @param plant     植株方块
	 * @param plantStem 植株茎
	 * @param tintType  着色类型
	 */
	public void registerVines(Block plant, Block plantStem, BlockStateModelGenerator.TintType tintType) {
		registerPlantPart(plant, plantStem, tintType);
		registerItemModel(plant, "_plant");
		excludeFromSimpleItemModelGeneration(plantStem);
	}
}
