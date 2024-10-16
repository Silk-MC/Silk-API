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

package pers.saikel0rado1iu.silk.api.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

/**
 * <h2 style="color:FFC800">用于方便快速的构建物品组集</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface ItemGroupCreator {
	/**
	 * 使用自带的物品作为标志生成物品组，物品组 ID 为 itemGroup.namespace.id
	 */
	@SilkApi
	static RegistryKey<ItemGroup> create(Item icon, ModBasicData namespace, String id) {
		Registry.register(Registries.ITEM_GROUP, new Identifier(namespace.getId(), id),
				FabricItemGroup.builder().icon(() -> new ItemStack(icon)).displayName(getGroupText(namespace, id)).build());
		return RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(namespace.getId(), id));
	}
	
	/**
	 * 自动创建一个新的物品作为标志生成物品组，标志 ID 为 namespace:group_icon_id；
	 * 物品组 ID 为 itemGroup.namespace.id
	 */
	@SilkApi
	static RegistryKey<ItemGroup> create(ModBasicData namespace, String id) {
		Item icon = new Item(new Item.Settings());
		Registry.register(Registries.ITEM, new Identifier(namespace.getId(), getIconId(id)), icon);
		Registry.register(Registries.ITEM_GROUP, new Identifier(namespace.getId(), id),
				FabricItemGroup.builder().icon(() -> new ItemStack(icon)).displayName(getGroupText(namespace, id)).build());
		return RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(namespace.getId(), id));
	}
	
	static Text getGroupText(ModBasicData namespace, String id) {
		return Text.translatable("itemGroup." + namespace.getId() + "." + id);
	}
	
	static String getIconId(String id) {
		return "group_icon_" + id;
	}
}
