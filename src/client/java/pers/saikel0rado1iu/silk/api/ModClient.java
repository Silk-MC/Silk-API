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

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.pack.ResourcesPack;
import pers.saikel0rado1iu.silk.api.registry.*;

/**
 * <h2 style="color:FFC800">用作模组客户端主类，继承自 {@link ClientModInitializer}。所有模组注册或操作由此开始</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class ModClient implements ClientModInitializer {
	private final ModBasicData mod;
	
	/**
	 * 此构造方法仅用于父级构造（super() 构造方法），不可在子类实现
	 *
	 * @param mod 此模组的基础数据类
	 */
	protected ModClient(ModBasicData mod) {
		this.mod = mod;
	}
	
	@Override
	@ApiStatus.Internal
	public void onInitializeClient() {
		client(mod);
		if (items() != null) mod.logger().info("register " + items().getClass().getSimpleName() + " (client)" + Silk.DATA.getInfo());
		if (blocks() != null) mod.logger().info("register " + blocks().getClass().getSimpleName() + " (client)" + Silk.DATA.getInfo());
		if (modelLayers() != null) mod.logger().info("register " + modelLayers().getClass().getSimpleName() + " (client)" + Silk.DATA.getInfo());
		if (entityTypes() != null) mod.logger().info("register " + entityTypes().getClass().getSimpleName() + " (client)" + Silk.DATA.getInfo());
		if (particleTypes() != null) mod.logger().info("register " + particleTypes().getClass().getSimpleName() + " (client)" + Silk.DATA.getInfo());
		if (modResourcesPack(mod, ResourcePackActivationType.ALWAYS_ENABLED) != null)
			modResourcesPack(mod, ResourcePackActivationType.ALWAYS_ENABLED).registry();
	}
	
	/**
	 * 最先运行的模组初始化函数
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public abstract void client(ModBasicData mod);
	
	/**
	 * 提供来自模组的物品以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkItem items() {
		return null;
	}
	
	/**
	 * 提供来自模组的方块以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkBlock blocks() {
		return null;
	}
	
	/**
	 * 提供来自模组的模型图层以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkModelLayer modelLayers() {
		return null;
	}
	
	/**
	 * 提供来自模组的实体以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkEntityType entityTypes() {
		return null;
	}
	
	/**
	 * 提供来自模组的粒子以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkParticleType particleTypes() {
		return null;
	}
	
	/**
	 * 模组自带的单独资源包，模组的资源包不是集成在 Fabric 中，而是独立资源包
	 *
	 * @param mod  你的模组数据
	 * @param type 默认包类型
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public ResourcesPack modResourcesPack(ModBasicData mod, ResourcePackActivationType type) {
		return null;
	}
}
