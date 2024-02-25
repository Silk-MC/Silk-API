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

package pers.saikel0rado1iu.silk.util;

import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

/**
 * <h2 style="color:FFC800">有关文字的所有实用方法</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface TextUtil {
	@SilkApi
	static String configText(ModBasicData mod, String key) {
		return "config." + mod.getId() + '.' + key + ("".equals(key) ? "text" : ".text");
	}
	
	@SilkApi
	static String configTip(ModBasicData mod, String key) {
		return "config." + mod.getId() + '.' + key + ("".equals(key) ? "tip" : ".tip");
	}
	
	@SilkApi
	static String widgetTitle(ModBasicData mod, String key) {
		return "title." + mod.getId() + '.' + key;
	}
	
	@SilkApi
	static String widgetText(ModBasicData mod, String key) {
		return "text." + mod.getId() + '.' + key;
	}
	
}
