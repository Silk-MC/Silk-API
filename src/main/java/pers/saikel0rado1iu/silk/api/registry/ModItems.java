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
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
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
 * <p><b style="color:FFC800"><font size="+1">用于模组所有物品组成物品集与物品注册</font></b></p>
 * <p style="color:FFC800">模组作者需要自行在子类中提供静态实例化字段或方法以供对象的调用</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class ModItems {
	private final Set<Item> items = Sets.newLinkedHashSetWithExpectedSize(8);
	private final Map<String, Data> itemSet = Maps.newLinkedHashMapWithExpectedSize(8);
	
	/**
	 * 限定构造方法作用域
	 */
	protected ModItems() {
	}
	
	/**
	 * 将物品添加到此类的物品集并且完成自动物品注册
	 *
	 * @param item 添加物品
	 * @param id   物品 id
	 * @return 返回添加的物品
	 */
	@SilkApi
	protected Item item(Item item, String id) {
		items.add(item);
		itemSet.put(id, new Data(item, Optional.empty()));
		return item;
	}
	
	/**
	 * 将物品添加到此类的物品集并且完成自动物品注册
	 *
	 * @param item  添加物品
	 * @param id    物品 id
	 * @param group 物品所在的物品组
	 * @return 返回添加的物品
	 */
	@SilkApi
	protected Item item(Item item, String id, RegistryKey<ItemGroup> group) {
		items.add(item);
		itemSet.put(id, new Data(item, Optional.of(group)));
		return item;
	}
	
	/**
	 * 其他需要手动添加的特殊物品注册项
	 *
	 * @param mod 所要注册物品的模组
	 */
	@SilkApi
	protected abstract void otherRegister(ModBasicData mod);
	
	@SilkApi
	public void register(ModBasicData mod) {
		itemSet.forEach((id, data) -> {
			Registry.register(Registries.ITEM, new Identifier(mod.getId(), id), data.item);
			data.group.ifPresent(itemGroupRegistryKey -> ItemGroupEvents.modifyEntriesEvent(itemGroupRegistryKey).register(content -> content.add(data.item)));
		});
		otherRegister(mod);
	}
	
	@SilkApi
	public Set<Item> getItems() {
		return items;
	}
	
	private record Data(Item item, Optional<RegistryKey<ItemGroup>> group) {
	}
}
