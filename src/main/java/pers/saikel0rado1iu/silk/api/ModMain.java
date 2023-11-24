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

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.pack.DataPack;
import pers.saikel0rado1iu.silk.api.registry.*;

/**
 * <p><b style="color:FFC800"><font size="+1">用作模组主类，继承自 {@link ModInitializer}。所有模组注册或操作由此开始</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class ModMain implements ModInitializer {
	private final ModBasicData mod;
	
	/**
	 * 此构造方法仅用于父级构造（super() 构造方法），不可在子类实现
	 *
	 * @param mod 此模组的基础数据类
	 */
	protected ModMain(ModBasicData mod) {
		this.mod = mod;
	}
	
	/**
	 * <p>只要 Minecraft 处于 mod-load-ready(模组-加载-准备) 状态, 此代码就会运行.</p>
	 * <p>但是, 有些东西（比如资源）可能仍然未初始化.</p>
	 * <p style="color:DD0000">!谨慎操作!</p>
	 */
	@Override
	@ApiStatus.Internal
	public void onInitialize() {
		main(mod);
		datagen();
		items();
		blocks();
		entityTypes();
		soundEvents();
		statusEffects();
		particleTypes();
		blockEntities();
		if (worldData() != null) worldData().register();
		if (modDataPack(mod, ResourcePackActivationType.ALWAYS_ENABLED) != null) modDataPack(mod, ResourcePackActivationType.ALWAYS_ENABLED).registry();
	}
	
	/**
	 * 最先运行的模组初始化函数
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public abstract void main(ModBasicData mod);
	
	/**
	 * 提供来自模组的数据生成内容以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public void datagen() {
	}
	
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
	 * 提供来自模组的地物以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkWorldData worldData() {
		return null;
	}
	
	/**
	 * 提供来自模组的实体类型以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkEntityType entityTypes() {
		return null;
	}
	
	/**
	 * 提供来自模组的声音事件以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkSoundEvent soundEvents() {
		return null;
	}
	
	/**
	 * 提供来自模组的状态效果以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkStatusEffect statusEffects() {
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
	 * 提供来自模组的方块实体类型以供注册
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkBlockEntity blockEntities() {
		return null;
	}
	
	/**
	 * 模组自带的单独数据包，模组的数据包不是集成在 Fabric 中，而是独立数据包
	 *
	 * @param mod  你的模组数据
	 * @param type 默认包类型
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public DataPack modDataPack(ModBasicData mod, ResourcePackActivationType type) {
		return null;
	}
}
