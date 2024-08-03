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

package pers.saikel0rado1iu.silk.api.spinningjenny.tag;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import pers.saikel0rado1iu.silk.impl.SilkApi;

/**
 * <h2 style="color:FFC800">方块标签</h2>
 * 用于补充原版应有但缺失的标签以便在模组中使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface BlockTags {
	/**
	 * 在此标签中的方块属于泥土的变种，可被锄犁为普通泥土
	 *
	 * @since 0.1.0
	 */
	TagKey<Block> SOIL = TagKey.of(RegistryKeys.BLOCK, SilkApi.getInstance().ofId("soil"));
	/**
	 * 在此标签中的方块属于可犁地方块，可被锄犁为耕地
	 *
	 * @since 0.1.0
	 */
	TagKey<Block> TILLABLE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, SilkApi.getInstance().ofId("tillable_blocks"));
	/**
	 * 在此标签中的方块使用剪刀或剑会加速破坏
	 *
	 * @since 0.1.0
	 */
	TagKey<Block> COBWEB = TagKey.of(RegistryKeys.BLOCK, SilkApi.getInstance().ofId("cobweb"));
	/**
	 * 在此标签中的方块可以种植树苗
	 *
	 * @since 1.1.0
	 */
	TagKey<Block> SAPLING_GROW_BLOCK = TagKey.of(RegistryKeys.BLOCK, SilkApi.getInstance().ofId("sapling_grow_block"));
	/**
	 * 在此标签中的方块可以种植所有植株
	 *
	 * @since 1.1.0
	 */
	TagKey<Block> PLANT_GROW_BLOCK = TagKey.of(RegistryKeys.BLOCK, SilkApi.getInstance().ofId("plant_grow_block"));
}
