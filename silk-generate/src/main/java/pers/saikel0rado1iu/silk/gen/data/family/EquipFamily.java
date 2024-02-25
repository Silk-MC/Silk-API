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

package pers.saikel0rado1iu.silk.gen.data.family;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Map;

/**
 * <h2 style="color:FFC800">用于创建装备家族以便生成数据包</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public class EquipFamily {
	final Map<Variant, Item> variants = Maps.newHashMap();
	
	private EquipFamily() {
	}
	
	@SilkApi
	public static Builder builder() {
		return new Builder();
	}
	
	@SilkApi
	public Map<Variant, Item> getVariants() {
		return variants;
	}
	
	@SilkApi
	public Item getVariant(Variant variant) {
		return variants.get(variant);
	}
	
	public enum Variant {
		SHOVEL,
		PICKAXE,
		AXE,
		HOE,
		SWORD,
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS
	}
	
	public static class Builder {
		private final EquipFamily family;
		
		@SilkApi
		private Builder() {
			family = new EquipFamily();
		}
		
		@SilkApi
		public Builder shovel(Item equip) {
			family.variants.put(Variant.SHOVEL, equip);
			return this;
		}
		
		@SilkApi
		public Builder pickaxe(Item equip) {
			family.variants.put(Variant.PICKAXE, equip);
			return this;
		}
		
		@SilkApi
		public Builder axe(Item equip) {
			family.variants.put(Variant.AXE, equip);
			return this;
		}
		
		@SilkApi
		public Builder hoe(Item equip) {
			family.variants.put(Variant.HOE, equip);
			return this;
		}
		
		@SilkApi
		public Builder sword(Item equip) {
			family.variants.put(Variant.SWORD, equip);
			return this;
		}
		
		@SilkApi
		public Builder helmet(Item equip) {
			family.variants.put(Variant.HELMET, equip);
			return this;
		}
		
		@SilkApi
		public Builder chestplate(Item equip) {
			family.variants.put(Variant.CHESTPLATE, equip);
			return this;
		}
		
		@SilkApi
		public Builder leggings(Item equip) {
			family.variants.put(Variant.LEGGINGS, equip);
			return this;
		}
		
		@SilkApi
		public Builder boots(Item equip) {
			family.variants.put(Variant.BOOTS, equip);
			return this;
		}
		
		@SilkApi
		public EquipFamily build() {
			return family;
		}
	}
}
