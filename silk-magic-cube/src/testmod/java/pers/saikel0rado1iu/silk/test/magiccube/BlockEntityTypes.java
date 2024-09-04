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

package pers.saikel0rado1iu.silk.test.magiccube;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import pers.saikel0rado1iu.silk.api.spinningjenny.BlockEntityTypeRegistry;
import pers.saikel0rado1iu.silk.impl.SilkMagicCube;
import pers.saikel0rado1iu.silk.test.magiccube.entity.PhysicsConnectingBlockEntityTest;

/**
 * 方块实体类型
 */
public interface BlockEntityTypes extends BlockEntityTypeRegistry {
	/**
	 * test_block_entity_type
	 */
	BlockEntityType<PhysicsConnectingBlockEntityTest> TEST_BLOCK_ENTITY = BlockEntityTypeRegistry
			.registrar(() -> FabricBlockEntityTypeBuilder.create(PhysicsConnectingBlockEntityTest::new, Blocks.TEST_BLOCK).build())
			.register(SilkMagicCube.getInstance().ofId("test_block_entity_type"));
}
