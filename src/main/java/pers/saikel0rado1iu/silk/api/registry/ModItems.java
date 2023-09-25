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
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
 * <p><b style="color:FFC800"><font size="+1">用于模组所有物品组成物品集与物品注册</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface ModItems {
	Set<Item> ALL_MOD_ITEMS = Sets.newLinkedHashSetWithExpectedSize(8);
	
	/**
	 * 用于创建物品以及自动注册物品
	 *
	 * @param item  创建的物品
	 * @param id    物品的唯一 ID
	 * @param items 属于你模组的物品组
	 * @param mod   你模组的模组数据
	 * @return 创建的物品
	 */
	static Item item(Item item, String id, Set<Item> items, ModBasicData mod) {
		return item(item, id, null, items, mod);
	}
	
	/**
	 * 用于创建物品以及自动注册物品到物品组
	 *
	 * @param item  创建的物品
	 * @param id    物品的唯一 ID
	 * @param group 物品的物品组
	 * @param items 属于你模组的物品组
	 * @param mod   你模组的模组数据
	 * @return 创建的物品
	 */
	static Item item(Item item, String id, RegistryKey<ItemGroup> group, Set<Item> items, ModBasicData mod) {
		items.add(item);
		ALL_MOD_ITEMS.add(item);
		Registry.register(Registries.ITEM, new Identifier(mod.getId(), id), item);
		if (group != null) ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(item));
		return item;
	}
}
