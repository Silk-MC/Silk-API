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

package pers.saikel0rado1iu.silk.api.item;

import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.util.AmountType;

import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">用于自带弹射物保护的物品</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface WithProjectileProtection extends EffectiveEquipmentSlot {
	/**
	 * @return 如果 {@link WithProjectileProtection#getPrPrType()}
	 * 的返回值为 {@link AmountType#VALUE}，则此方法返回减少的伤害值；
	 * 如果返回值为 {@link AmountType#RATIO}，则此方法返回减少的伤害比例
	 */
	@SilkApi
	float getPrPrAmount();
	
	@SilkApi
	AmountType getPrPrType();
	
	/**
	 * 设置此效果的可叠加次数
	 *
	 * @return 如果值为 {@link Optional#empty()}，则此效果可无限叠加
	 */
	@SilkApi
	Optional<Integer> getPrPrStackingCount();
}
