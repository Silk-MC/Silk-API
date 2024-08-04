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

package pers.saikel0rado1iu.silk.api.spinningjenny.world.biome.source;

import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import pers.saikel0rado1iu.silk.api.event.registry.RegisterBiomeSourceParamListPresetCallback;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.modpass.registry.LaunchRegistrationProvider;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

import java.lang.reflect.Field;

/**
 * <h2 style="color:FFC800">{@link MultiNoiseBiomeSourceParameterList.Preset} 注册表</h2>
 * 用于注册 {@link MultiNoiseBiomeSourceParameterList.Preset} 的注册表
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface BiomeSourceParamListPresetRegistry extends LaunchRegistrationProvider<MultiNoiseBiomeSourceParameterList.Preset> {
	/**
	 * 预启动注册方法<br>
	 * 此方法方法一个预启动注册器，注册器注册返回注册对象<br>
	 *
	 * @param preset 注册对象
	 * @return 预启动注册器
	 */
	static BiomeSourceParamListPresetRegistry.LaunchRegistrar registrar(MultiNoiseBiomeSourceParameterList.Preset preset) {
		return new BiomeSourceParamListPresetRegistry.LaunchRegistrar(preset);
	}
	
	/**
	 * 注册方法
	 *
	 * @param field 注册字段
	 */
	static void register(Field field) {
		RegisterBiomeSourceParamListPresetCallback.EVENT.register(list -> {
			try {
				list.add((MultiNoiseBiomeSourceParameterList.Preset) field.get(new Object()));
			} catch (IllegalAccessException e) {
				String msg = String.format("Register field '%s' with illegal access modifier.", field.getName());
				SilkModPass.getInstance().logger().error(msg);
				throw new RuntimeException(msg);
			}
		});
	}
	
	/**
	 * 多重噪声生物群系源参数列表预设预启动注册器
	 */
	final class LaunchRegistrar extends Registrar<MultiNoiseBiomeSourceParameterList.Preset> {
		LaunchRegistrar(MultiNoiseBiomeSourceParameterList.Preset type) {
			super(type);
		}
		
		public MultiNoiseBiomeSourceParameterList.Preset register(ModPass modPass, String id) {
			return super.register(modPass, id);
		}
	}
}
