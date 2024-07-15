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

package pers.saikel0rado1iu.silk.test.spinningjenny;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;
import pers.saikel0rado1iu.silk.spinningjenny.BlockEntityTypeRegistry;

/**
 * Test {@link BlockEntityTypeRegistry}
 */
public interface BlockEntityTypeRegistryTest extends BlockEntityTypeRegistry {
	/**
	 * test_block_entity_type
	 */
	@SuppressWarnings("unused")
	BlockEntityType<ChestBlockEntity> TEST_BLOCK_ENTITY_TYPE = BlockEntityTypeRegistry.registrar(FabricBlockEntityTypeBuilder.create(ChestBlockEntity::new, BlockRegistryTest.TEST_BLOCK).build())
			.register(SilkSpinningJenny.getInstance(), "test_block");
}
