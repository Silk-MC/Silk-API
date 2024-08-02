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

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import pers.saikel0rado1iu.silk.api.spore.projectile.FixedDamage;

/**
 * Test {@link FixedDamage}
 */
public final class FixedDamageTest extends PersistentProjectileEntity implements FixedDamage {
	private static final ItemStack DEFAULT_STACK = new ItemStack(Items.FIXED_DAMAGE_TEST_ITEM);
	
	/**
	 * @param entityType test
	 * @param world      test
	 */
	public FixedDamageTest(EntityType<? extends FixedDamageTest> entityType, World world) {
		super(entityType, world, DEFAULT_STACK);
	}
	
	/**
	 * @param world test
	 * @param x     test
	 * @param y     test
	 * @param z     test
	 * @param stack test
	 */
	public FixedDamageTest(World world, double x, double y, double z, ItemStack stack) {
		super(EntityTypes.FIXED_DAMAGE_TEST, x, y, z, world, stack);
	}
	
	@Override
	public void tick() {
		if (getWorld().isClient && !inGround) getWorld().addParticle(ParticleTypes.INSTANT_EFFECT, getX(), getY(), getZ(), 0, 0, 0);
		super.tick();
	}
	
	@Override
	public float fixedDamage() {
		return 19;
	}
}
