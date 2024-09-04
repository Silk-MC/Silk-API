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

package pers.saikel0rado1iu.silk.test.spinningjenny.data.gen;

import net.minecraft.advancement.criterion.BeeNestDestroyedCriterion;
import net.minecraft.advancement.criterion.Criterion;
import pers.saikel0rado1iu.silk.api.spinningjenny.data.gen.CriterionRegistry;

/**
 * Test {@link CriterionRegistry}
 */
public interface CriterionRegistryTest extends CriterionRegistry {
	/**
	 * test_criterion
	 */
	@SuppressWarnings("unused")
	Criterion<BeeNestDestroyedCriterion.Conditions> TEST_CRITERION = CriterionRegistry.registrar(BeeNestDestroyedCriterion::new)
			.register("test_criterion");
}
