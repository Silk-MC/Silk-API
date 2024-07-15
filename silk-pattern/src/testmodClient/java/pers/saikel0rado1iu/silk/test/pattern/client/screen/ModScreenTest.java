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

package pers.saikel0rado1iu.silk.test.pattern.client.screen;

import net.minecraft.client.gui.screen.Screen;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.pattern.screen.ModScreen;
import pers.saikel0rado1iu.silk.test.pattern.client.tab.ModTabTest;
import pers.saikel0rado1iu.silk.test.pattern.client.tab.OptionsTabTest;
import pers.saikel0rado1iu.silk.test.pattern.client.tab.SynopsisTabTest;

import java.util.Optional;

/**
 * Test {@link ModScreen}
 */
public final class ModScreenTest extends ModScreen {
	/**
	 * @param parent  父屏幕
	 * @param modPass 模组通
	 */
	public ModScreenTest(Optional<Screen> parent, ModPass modPass) {
		super(parent, new ModTabTest(modPass), new SynopsisTabTest(modPass), new OptionsTabTest(modPass));
	}
}
