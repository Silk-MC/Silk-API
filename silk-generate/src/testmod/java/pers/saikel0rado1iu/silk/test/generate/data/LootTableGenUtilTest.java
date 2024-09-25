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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.generate.data.AdvancementGenUtil;
import pers.saikel0rado1iu.silk.api.generate.data.LootTableGenUtil;

import java.util.function.BiConsumer;

import static net.minecraft.data.server.loottable.EntityLootTableGenerator.NEEDS_ENTITY_ON_FIRE;

/**
 * Test {@link AdvancementGenUtil}
 */
public interface LootTableGenUtilTest {
	/**
	 * Block
	 */
	final class Block extends FabricBlockLootTableProvider {
		/**
		 * @param dataOutput 数据输出
		 */
		public Block(FabricDataOutput dataOutput) {
			super(dataOutput);
		}
		
		@Override
		public void generate() {
			LootTableGenUtil.addBlockDrop(this::addDrop, Blocks.GRASS_BLOCK, Items.DIRT, Items.SHORT_GRASS);
		}
	}
	
	/**
	 * Entity
	 */
	final class Entity extends SimpleFabricLootTableProvider {
		/**
		 * @param output 数据输出
		 */
		public Entity(FabricDataOutput output) {
			super(output, LootContextTypes.ENTITY);
		}
		
		@Override
		public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
			exporter.accept(EntityType.PIG.getLootTableId(), LootTable.builder()
					.pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1))
							.with(ItemEntry.builder(Items.BEEF)
									.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0, 2)))
									.apply(FurnaceSmeltLootFunction.builder().conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, NEEDS_ENTITY_ON_FIRE))))
							.apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0, 1))))
					.pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1))
							.with(ItemEntry.builder(Items.ACACIA_BOAT)
									.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-1, 1))))
							.apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0, 1)))));
		}
	}
}
