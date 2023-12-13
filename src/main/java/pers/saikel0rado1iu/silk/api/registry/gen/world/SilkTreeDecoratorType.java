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

package pers.saikel0rado1iu.silk.api.registry.gen.world;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.registry.SilkWorldData;

/**
 * <h2 style="color:FFC800">用于模组所有树木装饰器类型与注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldData} 中覆盖 {@link SilkWorldData#foliagePlacerTypes()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkTreeDecoratorType {
	protected static <P extends TreeDecorator> Builder<P> builder(TreeDecoratorType<P> treeDecoratorType) {
		return new Builder<>(treeDecoratorType);
	}
	
	@SilkApi
	public static final class Builder<P extends TreeDecorator> {
		private final TreeDecoratorType<P> treeDecoratorType;
		
		@SilkApi
		private Builder(TreeDecoratorType<P> treeDecoratorType) {
			this.treeDecoratorType = treeDecoratorType;
		}
		
		@SilkApi
		public TreeDecoratorType<P> build(ModBasicData mod, String id) {
			Registry.register(Registries.TREE_DECORATOR_TYPE, new Identifier(mod.getId(), id), treeDecoratorType);
			return treeDecoratorType;
		}
	}
}
