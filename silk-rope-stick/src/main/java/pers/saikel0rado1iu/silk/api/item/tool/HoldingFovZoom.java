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

package pers.saikel0rado1iu.silk.api.item.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <h2 style="color:FFC800">持有物品时的视场角缩放</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface HoldingFovZoom extends ItemFovZoom {
	@SilkApi
	float getHoldingFovZoom();
	
	@SilkApi
	default boolean canChangeFov(ItemStack stack) {
		return true;
	}
	
	@SilkApi
	boolean isConflictItems(Item checkItem);
	
	@ApiStatus.Internal
	default float getHoldingFovMultiple() {
		return 1 / getHoldingFovZoom();
	}
}
