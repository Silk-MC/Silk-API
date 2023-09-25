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

package pers.saikel0rado1iu.silk.api.registry;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.util.Set;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有方块组成方块集与方块注册</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface ModBlocks {
	Set<Block> ALL_MOD_BLOCKS = Sets.newLinkedHashSetWithExpectedSize(8);
	
	/**
	 * 用于创建方块以及自动注册方块
	 *
	 * @param block  创建的方块
	 * @param id     方块的唯一 ID
	 * @param blocks 属于你模组的方块组
	 * @param mod    你模组的模组数据
	 * @return 创建的方块
	 */
	@SilkApi
	static Block block(Block block, String id, Set<Block> blocks, ModBasicData mod) {
		return block(block, id, null, null, blocks, mod);
	}
	
	/**
	 * 用于创建方块以及自动注册方块以及方块物品
	 *
	 * @param block    创建的方块
	 * @param id       方块的唯一 ID
	 * @param settings 方块物品的设置项
	 * @param blocks   属于你模组的方块组
	 * @param mod      你模组的模组数据
	 * @return 创建的方块
	 */
	@SilkApi
	static Block block(Block block, String id, FabricItemSettings settings, Set<Block> blocks, ModBasicData mod) {
		return block(block, id, settings, null, blocks, mod);
	}
	
	/**
	 * 用于创建方块以及自动注册方块以及方块物品到物品组
	 *
	 * @param block  创建的方块
	 * @param id     方块的唯一 ID
	 * @param group  物品的物品组
	 * @param blocks 属于你模组的方块组
	 * @param mod    你模组的模组数据
	 * @return 创建的方块
	 */
	@SilkApi
	static Block block(Block block, String id, RegistryKey<ItemGroup> group, Set<Block> blocks, ModBasicData mod) {
		return block(block, id, new FabricItemSettings(), group, blocks, mod);
	}
	
	/**
	 * 用于创建方块以及自动注册方块以及方块物品到物品组
	 *
	 * @param block    创建的方块
	 * @param id       方块的唯一 ID
	 * @param settings 方块物品的设置项
	 * @param group    物品的物品组
	 * @param blocks   属于你模组的方块组
	 * @param mod      你模组的模组数据
	 * @return 创建的方块
	 */
	static Block block(Block block, String id, FabricItemSettings settings, RegistryKey<ItemGroup> group, Set<Block> blocks, ModBasicData mod) {
		blocks.add(block);
		ALL_MOD_BLOCKS.add(block);
		Identifier identifier = new Identifier(mod.getId(), id);
		Registry.register(Registries.BLOCK, identifier, block);
		if (settings == null) return block;
		Item item = new BlockItem(block, settings);
		Registry.register(Registries.ITEM, identifier, item);
		if (group != null) ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(item));
		return block;
	}
}
