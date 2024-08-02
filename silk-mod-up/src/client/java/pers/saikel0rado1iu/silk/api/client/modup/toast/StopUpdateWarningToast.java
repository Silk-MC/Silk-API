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

package pers.saikel0rado1iu.silk.api.client.modup.toast;

import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.api.modup.UpdateData;
import pers.saikel0rado1iu.silk.api.modup.UpdateState;
import pers.saikel0rado1iu.silk.api.client.modup.ClientUpdateManager;

/**
 * <h2 style="color:FFC800">停止更新警告提示框</h2>
 * 停止更新警告提示
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class StopUpdateWarningToast extends UpdateToast {
	/**
	 * @param updateData    更新数据
	 * @param updateManager 更新管理器
	 */
	public StopUpdateWarningToast(UpdateData updateData, ClientUpdateManager updateManager) {
		super(Text.translatable(UpdateState.STOP_UPDATE.title(), updateData.modData().i18nName()), updateData, updateManager, Text.translatable(UpdateState.STOP_UPDATE.text(), updateData.modData().i18nName()));
		UpdateData.setCanShowChangelog(this.updateData.updateSettings(), false);
	}
}