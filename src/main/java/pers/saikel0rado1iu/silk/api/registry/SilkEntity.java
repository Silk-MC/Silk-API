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

package pers.saikel0rado1iu.silk.api.registry;

import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.Set;
import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有实体类型组成实体类型集与实体类型注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#entities()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkEntity {
	@SilkApi
	public static final float POS_SHIFTING = 0.5F;
	@SilkApi
	public static final float PROJECTILE_BOX = 0.25F;
	@SilkApi
	public static final int PROJECTILE_RANGE = 4;
	@SilkApi
	public static final int PROJECTILE_UPDATE_RATE = 20;
	public static final Set<EntityType<?>> ALL_MOD_ENTITY_TYPES = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static <E extends Entity> Builder<E> builder(EntityType<E> entityType) {
		return new Builder<>(entityType);
	}
	
	/**
	 * 你需要在 client 模块中重新创建继承 {@link SilkEntity} 类来创建一个专用于客户端的注册类并覆盖 ModClient.entityType()方法
	 */
	@SilkApi
	@Environment(EnvType.CLIENT)
	protected static <E extends Entity> void clientRegister(EntityType<E> entityType, Consumer<EntityType<E>> clientRegister) {
		clientRegister.accept(entityType);
	}
	
	@SilkApi
	public static final class Builder<E extends Entity> {
		private final EntityType<E> entityType;
		
		@SilkApi
		private Builder(EntityType<E> entityType) {
			ALL_MOD_ENTITY_TYPES.add(this.entityType = entityType);
		}
		
		@SilkApi
		public Builder<E> put(Set<EntityType<?>> entityType) {
			entityType.add(this.entityType);
			return this;
		}
		
		@SilkApi
		public Builder<E> otherRegister(Consumer<EntityType<E>> entityTypeRegister) {
			entityTypeRegister.accept(entityType);
			return this;
		}
		
		@SilkApi
		public EntityType<E> build(ModBasicData mod, String id) {
			Registry.register(Registries.ENTITY_TYPE, new Identifier(mod.getId(), id), entityType);
			return entityType;
		}
	}
}
