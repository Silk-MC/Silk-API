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

package pers.saikel0rado1iu.silk.test.base.common.collect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.common.collect.MultiWayTree;

/**
 * Test MultiWayTree
 */
public interface MultiWayTreeTest {
	/**
	 * 日志
	 */
	Logger LOGGER = LoggerFactory.getLogger("SilkApiBase/TestingMultiWayTree");
	
	/**
	 * 测试
	 */
	static void test() {
		MultiWayTree<String, Integer> tree = new MultiWayTree<>("root", 0);
		tree.addChild("first", 1).addChild("second", 2).addChild("third", 3);
		tree.addChild("monday", 1).addChild("tuesday", 2).addChild("wednesday", 3);
		tree.addChild("january", 1);
		tree.addChild(tree.getChild("january"), "february", 2).addChild("march", 3);
		LOGGER.info("\n{}", tree);
		LOGGER.info("remove = {}\n{}", tree.removeChild(tree.getChild("january").getChild("february"), tree.getChild("january").getChild("february").getChild("march")), tree);
		LOGGER.info("remove = {}\n{}", tree.removeChild(tree.getChild("january"), tree.getChild("january").getKey()), tree);
		LOGGER.info("remove = {}\n{}", tree.removeChild(tree.getChild("monday")), tree);
		LOGGER.info("remove = {}\n{}", tree.removeChild(tree.getChild("january").getKey()), tree);
		LOGGER.info("height = {}", tree.getHeight());
		LOGGER.info("size = {}", tree.getSize());
		LOGGER.info("root = {}", tree.getRoot());
	}
}
