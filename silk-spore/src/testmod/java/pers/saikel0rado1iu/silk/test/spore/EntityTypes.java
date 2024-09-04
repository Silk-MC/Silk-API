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

package pers.saikel0rado1iu.silk.test.spore;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import pers.saikel0rado1iu.silk.api.spinningjenny.EntityTypeRegistry;
import pers.saikel0rado1iu.silk.impl.SilkSpore;

/**
 * EntityTypes
 */
public interface EntityTypes extends EntityTypeRegistry {
	/**
	 * fixed_damage_test
	 */
	EntityType<FixedDamageTest> FIXED_DAMAGE_TEST = EntityTypeRegistry.registrar(() -> FabricEntityTypeBuilder.<FixedDamageTest>create(SpawnGroup.MISC, FixedDamageTest::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackRangeBlocks(4).trackedUpdateRate(20).build())
			.other(entityType -> DispenserBlock.registerBehavior(Items.FIXED_DAMAGE_TEST_ITEM, new ProjectileDispenserBehavior() {
				@Override
				protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
					FixedDamageTest arrowEntity = new FixedDamageTest(world, position.getX(), position.getY(), position.getZ(), stack);
					arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
					return arrowEntity;
				}
			})).register(SilkSpore.getInstance().ofId("fixed_damage_test"));
}
