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

package pers.saikel0rado1iu.silk.api.generate.advancement.criterion;

import pers.saikel0rado1iu.silk.api.spinningjenny.data.gen.CriterionRegistry;
import pers.saikel0rado1iu.silk.impl.SilkApi;

/**
 * <h2 style="color:FFC800">标准</h2>
 * 用于补充原版的标准以便在模组中使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface Criteria extends CriterionRegistry {
	/**
	 * 射击发射物标准
	 */
	ShotProjectileCriterion SHOT_PROJECTILE_CRITERION = CriterionRegistry.registrar(ShotProjectileCriterion::new).register(SilkApi.getInternal().ofId("shot_projectile"));
	/**
	 * 远程武器击杀实体标准
	 */
	RangedKilledEntityCriterion RANGED_KILLED_ENTITY_CRITERION = CriterionRegistry.registrar(RangedKilledEntityCriterion::new).register(SilkApi.getInternal().ofId("ranged_killed_entity"));
}
