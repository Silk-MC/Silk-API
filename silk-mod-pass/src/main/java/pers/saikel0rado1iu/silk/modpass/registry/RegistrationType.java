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

package pers.saikel0rado1iu.silk.modpass.registry;

/**
 * <h2 style="color:FFC800">注册类型</h2>
 * 用于给 {@link RegisterableModPass} 进行判断的注册类型
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public enum RegistrationType {
	/**
	 * 仅客户端
	 */
	CLIENT_ONLY("client"),
	/**
	 * 仅服务端
	 */
	SERVER_ONLY("server"),
	/**
	 * 原版的主要注册类型，即客户端与服务端双注册
	 */
	VANILLA_MAIN("main"),
	/**
	 * 特殊的模组注册类型，用于在游戏启动前进行的预启动注册
	 */
	PRE_LAUNCH("launch");
	private final String key;
	
	RegistrationType(String key) {
		this.key = key;
	}
	
	/**
	 * 获取键
	 *
	 * @return 键
	 */
	public String key() {
		return key;
	}
}
