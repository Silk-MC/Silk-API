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

package pers.saikel0rado1iu.silk.api.block;

import net.minecraft.block.Block;
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
public interface SilkBlockTags {
	/**
	 * 在此标签中的方块属于泥土的变种，可被锄犁为普通泥土
	 */
	@SilkApi
	TagKey<Block> DIRT_VARIETY = TagKey.of(RegistryKeys.BLOCK, new Identifier(Silk.DATA.getId(), "dirt_variety"));
	
	/**
	 * 在此标签中的方块属于可犁地方块，可被锄犁为耕地
	 */
	@SilkApi
	TagKey<Block> CAN_TILL_BLOCK = TagKey.of(RegistryKeys.BLOCK, new Identifier(Silk.DATA.getId(), "can_till_block"));
	/**
	 * <p>方块标签：蛛网</p>
	 * <p>在此标签中的方块使用剪刀或剑会加速破坏</p>
	 */
	@SilkApi
	TagKey<Block> COBWEBS = TagKey.of(RegistryKeys.BLOCK, new Identifier(Silk.DATA.getId(), "cobwebs"));
}
