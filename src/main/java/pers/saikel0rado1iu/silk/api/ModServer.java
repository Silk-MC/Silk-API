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

package pers.saikel0rado1iu.silk.api;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.pack.DataPack;

import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">用作模组服务端主类，继承自 {@link DedicatedServerModInitializer}。所有模组注册或操作由此开始</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class ModServer implements DedicatedServerModInitializer {
	private final ModBasicData mod;
	
	/**
	 * 此构造方法仅用于父级构造（super() 构造方法），不可在子类实现
	 *
	 * @param mod 此模组的基础数据类
	 */
	protected ModServer(ModBasicData mod) {
		this.mod = mod;
	}
	
	@Override
	public void onInitializeServer() {
		server(mod);
		modDataPack(mod, ResourcePackActivationType.ALWAYS_ENABLED).ifPresent(DataPack::registry);
	}
	
	/**
	 * 最先运行的模组初始化函数
	 */
	protected abstract void server(ModBasicData mod);
	
	/**
	 * 模组自带的单独数据包，模组的数据包不是集成在 Fabric 中，而是独立数据包
	 *
	 * @param mod  你的模组数据
	 * @param type 默认包类型
	 * @return 模组数据包，如果为 {@link Optional#empty()} 则模组数据包集成在 Fabric 中
	 */
	@SilkApi
	protected Optional<DataPack> modDataPack(ModBasicData mod, ResourcePackActivationType type) {
		return Optional.empty();
	}
}
