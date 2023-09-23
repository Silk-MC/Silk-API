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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有方块组成方块集与方块注册</font></b></p>
 * <p style="color:FFC800">模组作者需要自行在子类中提供静态实例化字段或方法以供对象的调用</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class ModBlocks {
	private final Set<Block> blocks = Sets.newLinkedHashSetWithExpectedSize(8);
	private final Map<String, Data> blockSet = Maps.newLinkedHashMapWithExpectedSize(8);
	
	/**
	 * 限定构造方法作用域
	 */
	protected ModBlocks() {
	}
	
	/**
	 * 将方块添加到此类的方块集并且完成自动方块注册
	 *
	 * @param block 添加方块
	 * @param id    方块 id
	 * @return 返回添加的方块
	 */
	@SilkApi
	protected Block block(Block block, String id) {
		blocks.add(block);
		blockSet.put(id, new Data(block, new FabricItemSettings(), Optional.empty()));
		return block;
	}
	
	/**
	 * 将方块添加到此类的方块集并且完成自动方块与其方块物品的注册
	 *
	 * @param block 添加方块
	 * @param id    方块 id
	 * @param group 方块所在的方块组
	 * @return 返回添加的方块
	 */
	@SilkApi
	protected Block block(Block block, String id, RegistryKey<ItemGroup> group) {
		blocks.add(block);
		blockSet.put(id, new Data(block, new FabricItemSettings(), Optional.of(group)));
		return block;
	}
	
	/**
	 * 将方块添加到此类的方块集并且完成自动方块与其方块物品的注册
	 *
	 * @param block    添加方块
	 * @param id       方块 id
	 * @param settings 方块物品设置
	 * @param group    方块所在的方块组
	 * @return 返回添加的方块
	 */
	@SilkApi
	protected Block block(Block block, String id, FabricItemSettings settings, RegistryKey<ItemGroup> group) {
		blocks.add(block);
		blockSet.put(id, new Data(block, settings, Optional.of(group)));
		return block;
	}
	
	/**
	 * 其他需要手动添加的特殊方块注册项
	 *
	 * @param mod 所要注册方块的模组
	 */
	@SilkApi
	protected abstract void otherRegister(ModBasicData mod);
	
	@SilkApi
	public void register(ModBasicData mod) {
		blockSet.forEach((id, data) -> {
			Registry.register(Registries.BLOCK, new Identifier(mod.getId(), id), data.block);
			data.group.ifPresent(itemGroupRegistryKey -> {
				BlockItem item = new BlockItem(data.block, data.settings);
				Registry.register(Registries.ITEM, new Identifier(mod.getId(), id), item);
				ItemGroupEvents.modifyEntriesEvent(itemGroupRegistryKey).register(content -> content.add(item));
			});
		});
		otherRegister(mod);
	}
	
	@SilkApi
	public Set<Block> getBlocks() {
		return blocks;
	}
	
	private record Data(Block block, FabricItemSettings settings, Optional<RegistryKey<ItemGroup>> group) {
	}
}
