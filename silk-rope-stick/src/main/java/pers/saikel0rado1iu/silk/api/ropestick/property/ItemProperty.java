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

package pers.saikel0rado1iu.silk.api.ropestick.property;

import net.minecraft.item.ItemConvertible;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">物品属性</h2>
 * 用于实现以创建物品属性
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ItemProperty {
	/**
	 * 获取物品属性
	 *
	 * @param item  物品
	 * @param clazz 要获取的物品属性
	 * @param <T>   物品属性
	 * @return 物品属性，如果此物品不包括物品属性则会返回 {@link Optional#empty()}
	 */
	static <T extends ItemProperty> Optional<T> get(ItemConvertible item, Class<T> clazz) {
		if (!(item.asItem() instanceof ItemProperties properties)) return Optional.empty();
		for (ItemProperty property : properties.properties()) {
			if (clazz.isInstance(property)) return Optional.of(clazz.cast(property));
		}
		return Optional.empty();
	}
}
