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

package pers.saikel0rado1iu.silk.test.base.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.common.util.MathUtil;

/**
 * Test {@link MathUtil}
 */
public interface MathUtilTest {
	/**
	 * 日志
	 */
	Logger LOGGER = LoggerFactory.getLogger("SilkApiBase/TestingMathUtil");
	
	/**
	 * 测试
	 */
	@SuppressWarnings("ConstantValue")
	static void test() {
		LOGGER.info("MathUtil.compareFloat(1, 1) = {}", MathUtil.compareFloat(1, 1));
		LOGGER.info("MathUtil.compareFloat(1, 0.5) = {}", MathUtil.compareFloat(1, 0.5));
		LOGGER.info("MathUtil.compareFloat(1, 1.5) = {}", MathUtil.compareFloat(1, 1.5));
		LOGGER.info("MathUtil.xor(true, true) = {}", MathUtil.xor(true, true));
		LOGGER.info("MathUtil.xor(true, false) = {}", MathUtil.xor(true, false));
		LOGGER.info("MathUtil.xor(false, false) = {}", MathUtil.xor(false, false));
		LOGGER.info("MathUtil.toFloat(0.123456789) = {}", MathUtil.toFloat(0.123456789));
		LOGGER.info("MathUtil.toRadians(180) = {}", MathUtil.toRadians(180));
	}
}
