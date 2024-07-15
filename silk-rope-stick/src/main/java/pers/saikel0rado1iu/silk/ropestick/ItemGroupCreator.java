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

package pers.saikel0rado1iu.silk.ropestick;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.modpass.ModPass;

/**
 * <h2 style="color:FFC800">物品组创建器</h2>
 * 用于方便快速的构建物品组
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ItemGroupCreator {
	/**
	 * 使用自定义的物品作为标志生成物品组<br>
	 * 物品组 ID 为 {@code itemGroup.modPass.id}
	 *
	 * @param icon    图标物品
	 * @param modPass 模组通
	 * @param id      物品组 ID
	 * @return 物品组注册表
	 */
	static RegistryKey<ItemGroup> create(ItemConvertible icon, ModPass modPass, String id) {
		Registry.register(Registries.ITEM_GROUP, modPass.modData().ofId(id),
				FabricItemGroup.builder().icon(() -> new ItemStack(icon)).displayName(getText(modPass, id)).build());
		return RegistryKey.of(RegistryKeys.ITEM_GROUP, modPass.modData().ofId(id));
	}
	
	/**
	 * 自动创建一个新的物品作为标志生成物品组<br>
	 * 标志 ID 为 {@code modPass:group_icon_id}<br>
	 * 物品组 ID 为 {@code itemGroup.modPass.id}
	 *
	 * @param modPass 模组通
	 * @param id      物品组 ID
	 * @return 物品组注册表
	 */
	static RegistryKey<ItemGroup> create(ModPass modPass, String id) {
		Item icon = new Item(new FabricItemSettings());
		Registry.register(Registries.ITEM, modPass.modData().ofId(getIconId(id)), icon);
		Registry.register(Registries.ITEM_GROUP, modPass.modData().ofId(id),
				FabricItemGroup.builder().icon(() -> new ItemStack(icon)).displayName(getText(modPass, id)).build());
		return RegistryKey.of(RegistryKeys.ITEM_GROUP, modPass.modData().ofId(id));
	}
	
	/**
	 * 获取物品组文本
	 *
	 * @param modPass 模组通
	 * @param id      物品组 ID
	 * @return 物品组文本
	 */
	static Text getText(ModPass modPass, String id) {
		return Text.translatable("itemGroup." + modPass.modData().id() + "." + id);
	}
	
	/**
	 * 获取图标 ID
	 *
	 * @param id 图标的基础 ID
	 * @return 图标的完整 ID
	 */
	static String getIconId(String id) {
		return "group_icon_" + id;
	}
}
