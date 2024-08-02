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

package pers.saikel0rado1iu.silk.api.landform.gen.chunk;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import pers.saikel0rado1iu.silk.api.landform.ChunkStorageData;

/**
 * <h2 style="color:FFC800">区块生成器可升级</h2>
 * 此接口可让 {@link ChunkGenerator} 可以支持区块升级
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public interface ChunkGeneratorUpgradable {
	/**
	 * 在 {@link Codec} 中的应使用的版本号键
	 */
	String VERSION_KEY = "version";
	/**
	 * 无版本号时的默认版本信息
	 */
	String NON_VERSION = "non_version";
	
	/**
	 * 用于提供生成器版本以供判断，需要在 {@link Codec} 中添加此参数，这样才能正确保存版本号
	 *
	 * @return 生成器版本
	 */
	String version();
	
	/**
	 * 用于判断生成器版本号的方法
	 *
	 * @param savedVersion 已保存在存档中的版本号
	 * @return -1 为小于；0 为等于；1 为大于
	 */
	int compare(String savedVersion);
	
	/**
	 * 通过块坐标解析 {@link ChunkStorageData} 的数据进行判断是否可刷新坐标中的区块
	 *
	 * @param pos     需判断块坐标
	 * @param chunk   区块数据
	 * @param version 区块当前的生成器版本
	 * @return 是否需要刷新区块
	 */
	boolean needRefresh(BlockPos pos, ChunkStorageData chunk, String version);
	
	/**
	 * 通过块坐标解析 {@link ChunkStorageData} 的数据进行判断是否可升级坐标中的区块
	 *
	 * @param pos     需判断块坐标
	 * @param chunk   区块数据
	 * @param version 区块当前的生成器版本
	 * @return 是否需要升级区块
	 */
	boolean needUpgrade(BlockPos pos, ChunkStorageData chunk, String version);
	
	/**
	 * 通过块坐标解析 {@link ChunkStorageData} 的数据进行判断是否可降级坐标中的区块
	 *
	 * @param pos     需判断块坐标
	 * @param chunk   区块数据
	 * @param version 区块当前的生成器版本
	 * @return 是需要降级区块
	 */
	boolean needDowngrade(BlockPos pos, ChunkStorageData chunk, String version);
	
	/**
	 * 用于提供区块生成器的 {@link Codec}
	 *
	 * @return 区块生成器的 {@link Codec}
	 */
	Codec<? extends ChunkGenerator> getCodec();
}
