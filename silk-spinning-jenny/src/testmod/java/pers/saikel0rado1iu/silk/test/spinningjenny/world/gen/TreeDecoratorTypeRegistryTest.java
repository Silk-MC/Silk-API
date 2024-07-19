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

package pers.saikel0rado1iu.silk.test.spinningjenny.world.gen;

import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import pers.saikel0rado1iu.silk.spinningjenny.world.gen.TreeDecoratorTypeRegistry;

/**
 * Test {@link TreeDecoratorTypeRegistry}
 */
public interface TreeDecoratorTypeRegistryTest extends TreeDecoratorTypeRegistry {
	/**
	 * test_tree_decorator_type
	 */
	@SuppressWarnings("unused")
	TreeDecoratorType<TreeDecorator> TEST_TREE_DECORATOR_TYPE = TreeDecoratorTypeRegistry.registrar(new TreeDecoratorType<>(TreeDecorator.TYPE_CODEC))
			.register("test_tree_decorator_type");
}
