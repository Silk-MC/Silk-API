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

package pers.saikel0rado1iu.silk;

import net.fabricmc.api.ModInitializer;
import pers.saikel0rado1iu.silk.util.config.ConfigData;
import pers.saikel0rado1iu.silk.util.config.ConfigReader;
import pers.saikel0rado1iu.silk.util.config.ConfigWriter;

/**
 * <p><b style="color:FFC800"><font size="+1">Fabric Loader 最先调用的类，通常用于进行最终注册和模组最先操作</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class Main implements ModInitializer {
	/**
	 * <p>只要 Minecraft 处于 mod-load-ready(模组-加载-准备) 状态, 此代码就会运行.</p>
	 * <p>但是, 有些东西（比如资源）可能仍然未初始化.</p>
	 * <p style="color:DD0000">!谨慎操作!</p>
	 */
	@Override
	public void onInitialize() {
		ConfigData<Silk> configData = new ConfigData<>(Silk.DATA)
				.addSwitch("switch", true)
				.addOption("option", ConfigData.Type.TOML)
				.addIntSlider("int_slider", 0, 50, 25)
				.addFloatSlider("float_slider", -1F, 1F, 0F);
		ConfigData<Silk> configData1 = new ConfigData<>(Silk.DATA)
				.addFloatSlider("float_slider", -1F, 1F, 0F)
				.addSwitch("switch1", false)
				.addSwitch("switch2", false)
				.addSwitch("switch3", false);
		configData.addSubConfigs("sub", configData1);
		ConfigReader configReader = new ConfigReader(configData);
		configReader.load();
		ConfigWriter configWriter = new ConfigWriter(configData);
		configWriter.debug();
		configWriter.save();
	}
}