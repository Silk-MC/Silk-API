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

package pers.saikel0rado1iu.silk.api.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <h2 style="color:FFC800">用于补充原版应有但缺失的标签以便在模组中使用</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface SilkEntityTypeTags {
	/**
	 * 在此标签中的实体类型属于怪物
	 */
	@SilkApi
	TagKey<EntityType<?>> MONSTERS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Silk.DATA.getId(), "monsters"));
}
