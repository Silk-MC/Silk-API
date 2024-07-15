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

package pers.saikel0rado1iu.silk.test.spinningjenny.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;
import pers.saikel0rado1iu.silk.spinningjenny.BlockRegistry;

import java.util.List;
import java.util.function.Supplier;

/**
 * Test {@link BlockRegistry}
 */
public interface BlockRegistryTest extends BlockRegistry {
	/**
	 * 客户端方块注册
	 */
	@SuppressWarnings("unused")
	List<Block> CLIENT = BlockRegistry.registrar((Supplier<List<Block>>) () -> {
		BlockRenderLayerMap.INSTANCE.putBlock(pers.saikel0rado1iu.silk.test.spinningjenny.BlockRegistryTest.TEST_BLOCK, RenderLayer.getCutout());
		return List.of(pers.saikel0rado1iu.silk.test.spinningjenny.BlockRegistryTest.TEST_BLOCK);
	}).register(SilkSpinningJenny.getInstance());
}
