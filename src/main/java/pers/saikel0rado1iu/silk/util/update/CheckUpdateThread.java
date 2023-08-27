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

/**
 * <p><b style="color:FFC800"><font size="+1">用于检查模组更新的线程</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class CheckUpdateThread extends Thread {
	private final UpdateData data;
	
	CheckUpdateThread(UpdateData data) {
		this.data = data;
	}
	
	@Override
	public void run() {
		if (data.canCheckUpdate) {
			UpdateData.State state;
			state = data.checkUpdate();
			data.setUpdateModVersion();
			data.setUpdateMinecraftVersion();
			
			data.state = state;
		}
	}
}
