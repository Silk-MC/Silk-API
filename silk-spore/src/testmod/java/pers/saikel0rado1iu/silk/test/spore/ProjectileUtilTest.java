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

import pers.saikel0rado1iu.silk.impl.SilkSpore;
import pers.saikel0rado1iu.silk.spore.projectile.ProjectileUtil;

/**
 * ProjectileUtilTest
 */
public class ProjectileUtilTest {
	/**
	 * 测试
	 */
	static void test() {
		SilkSpore.getInstance().logger().error("ProjectileUtil.POS_SHIFTING: " + ProjectileUtil.POS_SHIFTING);
		SilkSpore.getInstance().logger().error("ProjectileUtil.PROJECTILE_BOX: " + ProjectileUtil.PROJECTILE_BOX);
		SilkSpore.getInstance().logger().error("ProjectileUtil.PROJECTILE_RANGE: " + ProjectileUtil.PROJECTILE_RANGE);
		SilkSpore.getInstance().logger().error("ProjectileUtil.PROJECTILE_UPDATE_RATE: " + ProjectileUtil.PROJECTILE_UPDATE_RATE);
	}
}
