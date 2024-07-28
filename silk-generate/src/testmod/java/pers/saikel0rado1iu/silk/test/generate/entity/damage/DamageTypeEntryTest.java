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

package pers.saikel0rado1iu.silk.test.generate.entity.damage;

import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import pers.saikel0rado1iu.silk.generate.entity.damage.DamageTypeEntry;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;
import pers.saikel0rado1iu.silk.test.generate.Main;

/**
 * Test {@link DamageTypeEntry}
 */
public interface DamageTypeEntryTest extends DamageTypeEntry {
	/**
	 * INSTANCE
	 */
	DamageTypeEntryTest INSTANCE = new DamageTypeEntryTest() {
	};
	/**
	 * test
	 */
	RegistryKey<DamageType> TEST = DamageTypeEntry.of(Main.MOD_PASS, "test");
	
	@Override
	default RegistryBuilder.BootstrapFunction<DamageType> bootstrap() {
		return registerable -> {
			registerable.register(TEST, new DamageType("test", 0.1F, DamageEffects.BURNING));
			SilkGenerate.getInstance().logger().error(TEST + "");
		};
	}
}
