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

package pers.saikel0rado1iu.silk.api.modup;

import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;

/**
 * <h2 style="color:FFC800">更新状态</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public enum UpdateState {
	/**
	 * 无更新
	 */
	NONE,
	/**
	 * 更新完成
	 */
	DONE,
	/**
	 * 新的 MC 版本的更新
	 */
	NEW_MC_VER,
	/**
	 * 这个 MC 版本的更新
	 */
	THIS_MC_VER,
	/**
	 * 需显示模组更新日志
	 */
	MOD_LOG,
	/**
	 * 停止更新
	 */
	STOP_UPDATE,
	/**
	 * 更新错误
	 */
	UPDATE_FAIL;
	
	/**
	 * 获取更新状态的标题
	 *
	 * @return 标题的键
	 */
	public String title() {
		return WidgetTexts.titleKey(SilkModUp.getInstance(), this.toString().toLowerCase());
	}
	
	/**
	 * 获取更新状态的文本
	 *
	 * @return 文本的键
	 */
	public String text() {
		return WidgetTexts.textKey(SilkModUp.getInstance(), this.toString().toLowerCase());
	}
}
