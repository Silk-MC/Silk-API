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

package pers.saikel0rado1iu.silk.generate.entity.damage;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import pers.saikel0rado1iu.silk.generate.DynamicDataEntry;
import pers.saikel0rado1iu.silk.modpass.ModPass;

/**
 * <h2 style="color:FFC800">伤害类型条目</h2>
 * 用于创建、注册、生成伤害类型
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface DamageTypeEntry extends DynamicDataEntry<DamageType> {
	/**
	 * 获取伤害类型
	 *
	 * @param modPass 模组通
	 * @param id      伤害类型 ID
	 * @return 伤害类型
	 */
	static RegistryKey<DamageType> of(ModPass modPass, String id) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, modPass.modData().ofId(id));
	}
	
	@Override
	default RegistryKey<? extends Registry<DamageType>> registryKey() {
		return RegistryKeys.DAMAGE_TYPE;
	}
}
