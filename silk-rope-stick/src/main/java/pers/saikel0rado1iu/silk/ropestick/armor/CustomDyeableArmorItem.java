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

package pers.saikel0rado1iu.silk.ropestick.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;

/**
 * <h2 style="color:FFC800">自定义可染色盔甲物品</h2>
 * 可以自己设置自定义默认染色的盔甲物品
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class CustomDyeableArmorItem extends DyeableArmorItem {
	private final int defaultColor;
	
	/**
	 * @param armorMaterial 盔甲材料
	 * @param type          类型
	 * @param settings      设置
	 * @param defaultColor  默认颜色
	 */
	public CustomDyeableArmorItem(ArmorMaterial armorMaterial, Type type, Settings settings, int defaultColor) {
		super(armorMaterial, type, settings);
		this.defaultColor = defaultColor;
	}
	
	@Override
	public int getColor(ItemStack stack) {
		int color = super.getColor(stack);
		return color == DEFAULT_COLOR ? defaultColor : color;
	}
}
