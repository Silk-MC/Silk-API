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

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">模组更新管理器</h2>
 * 只能更新上传在 Modrinth 上的模组，并且从 Modrinth API 上查找所需的更新包<br>
 * 该管理器统一进行更新检测、通知用户、模组下载等更新操作
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class UpdateManager extends Thread implements ModPass {
	private static final ScheduledExecutorService UPDATE_MANAGER_POOL = new ScheduledThreadPoolExecutor(1,
			new BasicThreadFactory.Builder().daemon(true).build());
	private final UpdateData.Builder updateDataBuilder;
	protected String prevModVersion = "";
	protected boolean canShowUpdateNotify = true;
	
	protected UpdateManager(UpdateData.Builder updateDataBuilder) {
		this.updateDataBuilder = updateDataBuilder;
	}
	
	/**
	 * 运行方法<br>
	 * 启动更新管理器
	 *
	 * @param updateManager 更新管理器
	 */
	public static void run(UpdateManager updateManager) {
		UPDATE_MANAGER_POOL.schedule(updateManager, 0, TimeUnit.SECONDS);
	}
	
	@Override
	public void run() {
		super.run();
		operation(() -> UpdateChecker.check(updateDataBuilder));
	}
	
	/**
	 * 更新操作
	 *
	 * @param updateDataSupplier 更新数据提供器
	 */
	public abstract void operation(Supplier<Future<UpdateData>> updateDataSupplier);
	
	@Override
	public ModData modData() {
		return updateDataBuilder.modData();
	}
}
