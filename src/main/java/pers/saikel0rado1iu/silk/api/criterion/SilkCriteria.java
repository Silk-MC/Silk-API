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

package pers.saikel0rado1iu.silk.api.criterion;

import net.minecraft.advancement.criterion.Criteria;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.registry.datagen.SilkCriterion;

/**
 * <p><b style="color:FFC800"><font size="+1">用于补充原版的标准以便在模组中使用</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class SilkCriteria extends SilkCriterion {
	@SilkApi
	public static final ShotProjectileCriterion SHOT_PROJECTILE_CRITERION = builder(Criteria.register(new ShotProjectileCriterion())).build();
	@SilkApi
	public static final RangedKilledEntityCriterion RANGED_KILLED_ENTITY_CRITERION = builder(Criteria.register(new RangedKilledEntityCriterion())).build();
}
