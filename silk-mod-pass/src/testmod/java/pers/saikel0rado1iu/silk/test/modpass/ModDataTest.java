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

package pers.saikel0rado1iu.silk.test.modpass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.impl.SilkModPass;
import pers.saikel0rado1iu.silk.modpass.ModData;

/**
 * Test {@link ModData}
 */
public interface ModDataTest {
	/**
	 * 日志
	 */
	Logger LOGGER = LoggerFactory.getLogger("SilkModPass/TestingModBasicData");
	
	/**
	 * 测试
	 */
	static void test() {
		LOGGER.info("SilkModPass.getInstance().id() = {}", SilkModPass.getInstance().id());
		LOGGER.info("SilkModPass.getInstance().debugName() = {}", SilkModPass.getInstance().debugName());
		LOGGER.info("SilkModPass.getInstance().mod() = {}", SilkModPass.getInstance().mod());
		LOGGER.info("SilkModPass.getInstance().logger() = {}", SilkModPass.getInstance().logger());
		LOGGER.info("SilkModPass.getInstance().name() = {}", SilkModPass.getInstance().name());
		LOGGER.info("SilkModPass.getInstance().description() = {}", SilkModPass.getInstance().description());
		LOGGER.info("SilkModPass.getInstance().version() = {}", SilkModPass.getInstance().version());
		LOGGER.info("SilkModPass.getInstance().slug() = {}", SilkModPass.getInstance().slug());
		LOGGER.info("SilkModPass.getInstance().authors() = {}", SilkModPass.getInstance().authors());
		LOGGER.info("SilkModPass.getInstance().licenses() = {}", SilkModPass.getInstance().licenses());
		LOGGER.info("SilkModPass.getInstance().icon() = {}", SilkModPass.getInstance().icon());
		LOGGER.info("SilkModPass.getInstance().link(ModData.LinkType.HOMEPAGE) = {}", SilkModPass.getInstance().link(ModData.LinkType.HOMEPAGE));
		LOGGER.info("SilkModPass.getInstance().link(ModData.LinkType.SOURCES) = {}", SilkModPass.getInstance().link(ModData.LinkType.SOURCES));
		LOGGER.info("SilkModPass.getInstance().link(ModData.LinkType.ISSUES) = {}", SilkModPass.getInstance().link(ModData.LinkType.ISSUES));
		LOGGER.info("SilkModPass.getInstance().link(ModData.LinkType.COMMUNITY) = {}", SilkModPass.getInstance().link(ModData.LinkType.COMMUNITY));
		LOGGER.info("SilkModPass.getInstance().link(ModData.LinkType.SUPPORT) = {}", SilkModPass.getInstance().link(ModData.LinkType.SUPPORT));
	}
}
