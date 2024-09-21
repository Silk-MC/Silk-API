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

package pers.saikel0rado1iu.silk.api.event.modplus;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;

/**
 * <h2 style="color:FFC800">修改配方事件</h2>
 * 用于动态修改配方的事件<br>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyRecipeEvents {
	/**
	 * 此事件将删除一个已经定义的配方
	 */
	Event<Remove> REMOVE = EventFactory.createArrayBacked(Remove.class, listeners -> recipeId -> {
		for (Remove event : listeners) if (event.canRemove(recipeId)) return true;
		return false;
	});
	
	/**
	 * 移除配方
	 */
	@FunctionalInterface
	interface Remove {
		/**
		 * 用于判断是否能移除一个配方
		 *
		 * @param recipeId 需要判断配方的 {@link Identifier}
		 * @return 是否能够删除配方
		 */
		boolean canRemove(Identifier recipeId);
	}
}
