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

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import pers.saikel0rado1iu.silk.api.modpass.ModDataExpansion;
import pers.saikel0rado1iu.silk.api.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.api.modpass.pack.ResourcePack;
import pers.saikel0rado1iu.silk.impl.SilkApiBase;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

/**
 * Test {@link ModDataExpansion}
 */
public interface ModDataExpansionTest {
	/**
	 * 日志
	 */
	Logger LOGGER = LoggerFactory.getLogger("SilkModPass/TestingModExtendedData");
	
	/**
	 * 测试
	 */
	static void test() {
		LOGGER.info("SilkModPass.getInstance().themeColor() = {}", SilkModPass.getInstance().themeColor());
		LOGGER.info("SilkModPass.getInstance().i18nName() = {}", SilkModPass.getInstance().i18nName());
		LOGGER.info("SilkModPass.getInstance().i18nSummary() = {}", SilkModPass.getInstance().i18nSummary());
		LOGGER.info("SilkModPass.getInstance().i18nDescription() = {}", SilkModPass.getInstance().i18nDescription());
		SilkModPass.getInstance().writeFormatLog(Level.DEBUG, SilkApiBase.getInstance(), "SilkApiBase/TestingModExtendedData");
		SilkModPass.getInstance().writeFormatLog(Level.INFO, SilkApiBase.getInstance(), "SilkApiBase/TestingModExtendedData");
		SilkModPass.getInstance().writeFormatLog(Level.WARN, SilkApiBase.getInstance(), "SilkApiBase/TestingModExtendedData");
		SilkModPass.getInstance().writeFormatLog(Level.ERROR, SilkApiBase.getInstance(), "SilkApiBase/TestingModExtendedData");
		SilkModPass.getInstance().writeFormatLog(Level.TRACE, SilkApiBase.getInstance(), "SilkApiBase/TestingModExtendedData");
		LOGGER.info("DataPack.create() = {}", DataPack.createSimple("pack", ResourcePackActivationType.ALWAYS_ENABLED, SilkModPass.getInstance()));
		LOGGER.info("DataPack.getDescKey() = {}", DataPack.getDescKey(SilkModPass.getInstance()));
		LOGGER.info("ResourcePack.create() = {}", ResourcePack.createSimple("pack", ResourcePackActivationType.ALWAYS_ENABLED, SilkModPass.getInstance()));
		LOGGER.info("ResourcePack.getDescKey() = {}", ResourcePack.getDescKey(SilkModPass.getInstance()));
		LOGGER.info("SilkModPass.getInstance().email() = {}", SilkModPass.getInstance().email());
		LOGGER.info("SilkModPass.getInstance().irc() = {}", SilkModPass.getInstance().irc());
		LOGGER.info("SilkModPass.getInstance().contributors() = {}", SilkModPass.getInstance().contributors());
		LOGGER.info("SilkModPass.getInstance().nested() = {}", SilkModPass.getInstance().nested());
		LOGGER.info("SilkModPass.getInstance().depends() = {}", SilkModPass.getInstance().depends());
		LOGGER.info("SilkModPass.getInstance().recommends() = {}", SilkModPass.getInstance().recommends());
		LOGGER.info("SilkModPass.getInstance().suggests() = {}", SilkModPass.getInstance().suggests());
		LOGGER.info("SilkModPass.getInstance().breaks() = {}", SilkModPass.getInstance().breaks());
		LOGGER.info("SilkModPass.getInstance().conflicts() = {}", SilkModPass.getInstance().conflicts());
		LOGGER.info("SilkModPass.getInstance().jar() = {}", SilkModPass.getInstance().jar());
		LOGGER.info("SilkModPass.getInstance().jarPath() = {}", SilkModPass.getInstance().jarPath());
		LOGGER.info("SilkModPass.getInstance().jarName() = {}", SilkModPass.getInstance().jarName());
	}
}
