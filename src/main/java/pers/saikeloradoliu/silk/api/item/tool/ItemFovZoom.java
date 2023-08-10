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

package pers.saikeloradoliu.silk.api.item.tool;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.saikeloradoliu.silk.annotation.SilkApi;

import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">物品的视场角缩放通用数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@ApiStatus.Internal
interface ItemFovZoom {
	@SilkApi
	float DEFAULT_FOV_ZOOM = 1.2F;
	@SilkApi
	Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
	@SilkApi
	Identifier PUMPKIN_BLUR = new Identifier("textures/misc/pumpkinblur.png");
	@SilkApi
	Identifier SPYGLASS_SCOPE = new Identifier("textures/misc/spyglass_scope.png");
	@SilkApi
	Identifier POWDER_SNOW_OUTLINE = new Identifier("textures/misc/powder_snow_outline.png");
	
	@SilkApi
	Optional<Identifier> getHubOverlay();
}
