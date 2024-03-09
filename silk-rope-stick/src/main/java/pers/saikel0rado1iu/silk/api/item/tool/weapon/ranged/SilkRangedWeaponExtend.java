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

package pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Map;

/**
 * <h2 style="color:FFC800">用于扩展远程武器特性的方法接口</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface SilkRangedWeaponExtend {
	@SilkApi
	String PULLING_KEY = "pulling";
	@SilkApi
	String PULL_KEY = "pull";
	@SilkApi
	String PROJECTILE_ID_KEY = "projectile_id";
	@SilkApi
	float DEFAULT_FIRING_ERROR = 1;
	
	@SilkApi
	float getMaxProjectileSpeed();
	
	@SilkApi
	float getMaxDamageMultiple();
	
	@SilkApi
	float getFiringError();
	
	@SilkApi
	int getMaxUseTicks();
	
	@SilkApi
	float getDamageMultiple();
	
	/**
	 * 设置弹丸 ID 的 NBT 以供 JSON 渲染使用
	 */
	@SilkApi
	void setProjectileId(ItemStack stack, ItemStack useProjectile);
	
	/**
	 * 获取 NBT 弹丸 ID 以供 JSON 渲染使用
	 */
	@SilkApi
	float getProjectileId(ItemStack stack);
	
	/**
	 * 获取所有弹丸的 ID 与他们所对应的名称以供模型生成
	 */
	@SilkApi
	Map<Float, String> getAllProjectile();
	
	record ProjectileIdComponent(@Final float projectileId) {
		public static final Codec<ProjectileIdComponent> CODEC = Codec.FLOAT.xmap(ProjectileIdComponent::new, ProjectileIdComponent::projectileId);
	}
}
