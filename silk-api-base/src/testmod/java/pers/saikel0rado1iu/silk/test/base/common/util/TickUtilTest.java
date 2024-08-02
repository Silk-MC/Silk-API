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
import pers.saikel0rado1iu.silk.api.base.common.util.TickUtil;

import java.util.concurrent.TimeUnit;

/**
 * Test TickUtil
 */
public interface TickUtilTest {
	/**
	 * 日志
	 */
	Logger LOGGER = LoggerFactory.getLogger("SilkApiBase/TestingTickUtil");
	
	/**
	 * 测试
	 */
	static void test() {
		LOGGER.info("TickUtil.getTick(1) = {}", TickUtil.getTick(1));
		LOGGER.info("TickUtil.getTick(1, TickUtil.Type.NATURAL, TimeUnit.MINUTES) = {}", TickUtil.getTick(1, TickUtil.Type.NATURAL, TimeUnit.MINUTES));
		LOGGER.info("TickUtil.getTime(20) = {}", TickUtil.getTime(20));
		LOGGER.info("TickUtil.getTime(1200, TickUtil.Type.NATURAL, TimeUnit.MINUTES) = {}", TickUtil.getTime(1200, TickUtil.Type.NATURAL, TimeUnit.MINUTES));
	}
}
