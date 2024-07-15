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

package pers.saikel0rado1iu.silk.modpass.pack;

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.modpass.ModPass;

/**
 * <h2 style="color:FFC800">基础包</h2>
 * 包的基础数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface BasePack extends ModPass {
	/**
	 * 包的根目录，数据包目录为 {@code "resourcepacks/packRoot()/..."}
	 *
	 * @return 根目录名称
	 */
	String packRoot();
	
	/**
	 * 包激活类型，参考 {@link ResourcePackActivationType}
	 *
	 * @return 数据包激活类型
	 */
	ResourcePackActivationType type();
	
	/**
	 * 包的唯一标识符
	 *
	 * @return 标识符
	 */
	Identifier id();
	
	/**
	 * 注册包
	 *
	 * @return 是否成功注册
	 */
	boolean registry();
}
