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

package pers.saikel0rado1iu.silk.api.generate.data;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

/**
 * <h2 style="color:FFC800">战利品表生成实用工具</h2>
 * 用于提供模组中常用但未提供更方便方法的战利品表方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 */
public interface LootTableGenUtil {
	/**
	 * 添加方块掉落物
	 *
	 * @param addDrop 添加方法
	 * @param block   方块
	 * @param drops   掉落物
	 */
	static void addBlockDrop(BiConsumer<Block, LootTable.Builder> addDrop, Block block, ItemConvertible... drops) {
		LootTable.Builder lootTableBuilder = LootTable.builder();
		for (ItemConvertible drop : drops) {
			lootTableBuilder = lootTableBuilder.pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1))
					.with(ItemEntry.builder(drop).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))));
		}
		addDrop.accept(block, lootTableBuilder);
	}
	
	/**
	 * 添加实体掉落物
	 *
	 * @param exporter 添加方法
	 * @param entity   实体
	 * @param builder  战利品表构建器
	 */
	static void addEntityDrop(BiConsumer<Identifier, LootTable.Builder> exporter, EntityType<?> entity, LootTable.Builder builder) {
		exporter.accept(new Identifier(Registries.ENTITY_TYPE.getId(entity).getNamespace(), "entities/" + Registries.ENTITY_TYPE.getId(entity).getPath()), builder);
	}
}
