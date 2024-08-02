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

package pers.saikel0rado1iu.silk.test.ropestick;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EquipmentSlot;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModMain;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.api.ropestick.property.EffectiveItemSlot;
import pers.saikel0rado1iu.silk.api.ropestick.tool.AdjustFovWhileUse;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;

import java.util.Set;

/**
 * 测试
 */
public final class Test implements ModMain {
	/**
	 * 模组主函数
	 *
	 * @param mod 提供的模组通
	 */
	@Override
	public void main(ModPass mod) {
		SilkRopeStick.getInstance().logger().error(AdjustFovWhileUse.VIGNETTE_TEXTURE.toString());
		SilkRopeStick.getInstance().logger().error(AdjustFovWhileUse.PUMPKIN_BLUR.toString());
		SilkRopeStick.getInstance().logger().error(new EffectiveItemSlot(EquipmentSlot.HEAD, EquipmentSlot.CHEST) + "");
	}
	
	/**
	 * 注册表方法，提供注册表以供注册
	 *
	 * @return 注册表的类型集合
	 */
	@Override
	public Set<Class<? extends MainRegistrationProvider<?>>> registry() {
		return ImmutableSet.of(
				Items.class
		);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return SilkRopeStick.getInstance();
	}
}
