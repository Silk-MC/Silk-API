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

package pers.saikel0rado1iu.silk.landform;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import pers.saikel0rado1iu.silk.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.modpass.ModDataExpansion;

/**
 * <h2 style="color:FFC800">可升级世界信息</h2>
 * 存档中存储的可升级的世界的附加信息
 *
 * @param <T> 区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record UpgradableWorldInfo<T extends ChunkGenerator & ChunkGeneratorUpgradable>
		(ModDataExpansion mod, UpgradableWorldData<T> data, String levelName, Identifier generatorId, String version, int compare) {
	/**
	 * 是否应该升级世界
	 *
	 * @return 是否应该升级世界
	 */
	public boolean shouldUpgradeWorld() {
		return isNonVersion() || compare < 0;
	}
	
	/**
	 * 是否应该降级世界
	 *
	 * @return 是否应该降级世界
	 */
	public boolean shouldDowngradeWorld() {
		return compare > 0;
	}
	
	/**
	 * 是否是无版本
	 *
	 * @return 是否无版本
	 */
	public boolean isNonVersion() {
		return version.equals(ChunkGeneratorUpgradable.NON_VERSION);
	}
}
