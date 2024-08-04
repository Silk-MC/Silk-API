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

package pers.saikel0rado1iu.silk.api.modpass.pack;

import net.minecraft.resource.ResourcePackSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pers.saikel0rado1iu.silk.api.modpass.ModDataExpansion;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

/**
 * <h2 style="color:FFC800">组资源包源</h2>
 * 用于组资源包的源
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class GroupResourcePackSource implements ResourcePackSource {
	private final ModPass modPass;
	
	/**
	 * @param modPass 模组名称
	 */
	public GroupResourcePackSource(ModPass modPass) {
		this.modPass = modPass;
	}
	
	@Override
	public boolean canBeEnabledLater() {
		return true;
	}
	
	@Override
	public Text decorate(Text packName) {
		return Text.translatable("pack.nameAndSource", packName, Text.translatable("pack.source.builtinMod", (modPass.modData() instanceof ModDataExpansion e ? e.i18nName() : Text.literal(modPass.modData().name())))).formatted(Formatting.GRAY);
	}
}
