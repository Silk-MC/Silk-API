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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;
import pers.saikel0rado1iu.silk.spinningjenny.StatusEffectRegistry;

/**
 * Test {@link StatusEffectRegistry}
 */
public interface StatusEffectRegistryTest extends StatusEffectRegistry {
	/**
	 * test_status_effect
	 */
	@SuppressWarnings("unused")
	TestStatusEffect TEST_STATUS_EFFECT = StatusEffectRegistry.registrar(new TestStatusEffect())
			.register(SilkSpinningJenny.getInstance(), "test_status_effect");
	
	/**
	 * TestStatusEffect
	 */
	class TestStatusEffect extends StatusEffect {
		private TestStatusEffect() {
			super(StatusEffectCategory.HARMFUL, 0xFF6A00);
		}
		
		@Override
		public void applyUpdateEffect(LivingEntity entity, int amplifier) {
			super.applyUpdateEffect(entity, amplifier);
			entity.damage(entity.getDamageSources().create(DamageTypes.ARROW), 1);
		}
		
		@Override
		public boolean canApplyUpdateEffect(int duration, int amplifier) {
			int mod = 0x19 >> amplifier;
			if (mod > 0) return duration % mod == 0;
			return true;
		}
	}
}
