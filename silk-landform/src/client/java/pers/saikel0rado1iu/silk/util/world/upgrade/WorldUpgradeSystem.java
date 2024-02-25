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

package pers.saikel0rado1iu.silk.util.world.upgrade;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.include.com.google.common.collect.Sets;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.gen.world.chunk.UpgradeChunkGenerator;
import pers.saikel0rado1iu.silk.util.world.upgrade.data.WorldUpgradeData;

import java.util.Set;

/**
 * <h2 style="color:FFC800">用于注册世界升级系统，只对使用了 {@link UpgradeChunkGenerator} 的世界有效</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@SilkApi
public class WorldUpgradeSystem {
	private static final Set<WorldUpgradeData<?>> WORLD_UPGRADE_DATA = Sets.newHashSetWithExpectedSize(4);
	
	@ApiStatus.Internal
	public static Set<WorldUpgradeData<?>> getWorldUpgradeData() {
		return WORLD_UPGRADE_DATA;
	}
	
	@SilkApi
	public static void registryUpgradeWorld(WorldUpgradeData<?> worldUpgradeData) {
		WORLD_UPGRADE_DATA.add(worldUpgradeData);
	}
}
