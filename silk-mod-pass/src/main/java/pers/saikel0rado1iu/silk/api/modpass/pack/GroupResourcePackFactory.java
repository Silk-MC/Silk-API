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

import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.minecraft.resource.OverlayResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2 style="color:FFC800">组资源包工厂类</h2>
 * 用于快速构建组资源包的工厂类
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 版本
 */
public record GroupResourcePackFactory(ModResourcePack pack) implements ResourcePackProfile.PackFactory {
	@Override
	public ResourcePack open(String name) {
		return pack;
	}
	
	@Override
	public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
		/*if (metadata.overlays().isEmpty()) {
			return pack;
		} else {*/
			List<ResourcePack> overlays = new ArrayList<>(metadata.overlays().size());
			
			for (String overlay : List.of("silk-generate-test")) {
				overlays.add(pack.createOverlay(overlay));
			}
			
			return new OverlayResourcePack(pack, overlays);
		//}
	}
}
