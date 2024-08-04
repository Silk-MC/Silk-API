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

package pers.saikel0rado1iu.silk.test.pattern.client.tab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import pers.saikel0rado1iu.silk.api.client.pattern.screen.PlaceholderScreen;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.OptionsTab;
import pers.saikel0rado1iu.silk.api.client.pattern.widget.OptionMenuWidget;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.impl.SilkPattern;
import pers.saikel0rado1iu.silk.test.pattern.client.screen.ButtonHelperScreen;

/**
 * Test {@link OptionsTab}
 */
public final class OptionsTabTest extends OptionsTab {
	/**
	 * @param modPass 模组通
	 */
	public OptionsTabTest(ModPass modPass) {
		super(modPass, "");
	}
	
	@Override
	protected void addOptions(OptionMenuWidget optionMenuWidget) {
		optionMenuWidget.addSingleOptionEntry(SimpleOption.ofBoolean(
				WidgetTexts.textKey(modPass, "test"),
				SimpleOption.constantTooltip(WidgetTexts.title(modPass, "test")),
				(optionText, value) -> WidgetTexts.text(modPass, "test"),
				false,
				(value) -> MinecraftClient.getInstance().setScreen(new PlaceholderScreen(parent, SilkPattern.getInstance()))));
		optionMenuWidget.addSingleOptionEntry(SimpleOption.ofBoolean(
				WidgetTexts.textKey(modPass, "test"),
				SimpleOption.constantTooltip(WidgetTexts.title(modPass, "test")),
				(optionText, value) -> WidgetTexts.text(modPass, "test"),
				false,
				(value) -> MinecraftClient.getInstance().setScreen(new ButtonHelperScreen(parent, SilkPattern.getInstance()))));
	}
}
