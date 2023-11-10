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

package pers.saikel0rado1iu.silk.util.update;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.Sets;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModExtendedData;

import java.util.Set;

/**
 * <p><b style="color:FFC800"><font size="+1">用于注册更新系统，只对在 Modrinth 上发布的模组有效</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class UpdateSystem {
	private static final Set<UpdateShow> UPDATE_SHOWS = Sets.newHashSetWithExpectedSize(4);
	
	@ApiStatus.Internal
	public static Set<UpdateShow> getUpdateShows() {
		return UPDATE_SHOWS;
	}
	
	@SilkApi
	public static @Nullable UpdateShow getUpdateShow(ModExtendedData mod) {
		for (UpdateShow show : UPDATE_SHOWS) {
			if (show.getMod().equals(mod)) return show;
		}
		return null;
	}
	
	@SilkApi
	public static void registryUpdate(UpdateData updateData) {
		UpdateShow updateShow = new UpdateShow(new CheckUpdateThread(updateData));
		if (UPDATE_SHOWS.add(updateShow)) updateShow.runUpdateThread();
	}
}
