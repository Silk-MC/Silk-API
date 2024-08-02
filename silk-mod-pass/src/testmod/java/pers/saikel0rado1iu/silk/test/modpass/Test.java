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

package pers.saikel0rado1iu.silk.test.modpass;

import pers.saikel0rado1iu.silk.impl.SilkModPass;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModMain;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.test.modpass.log.ChangelogTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

/**
 * 测试
 */
public final class Test implements ModMain {
	/**
	 * 模组主函数
	 *
	 * @param mod 提供的模组通
	 */
	@Override
	public void main(ModPass mod) {
		ModDataTest.test();
		ModDataExpansionTest.test();
		ChangelogTest.test();
		// test mod() exception
		// SilkApi.DATA.mod();
		// test link exception
		new ModData() {
			@Override
			public String id() {
				return "silk-mod-pass-test";
			}
			
			@Override
			public Optional<URL> community() throws MalformedURLException {
				return Optional.of(new URL(""));
			}
		}.link(ModData.LinkType.COMMUNITY);
	}
	
	/**
	 * 注册表方法，提供注册表以供注册
	 *
	 * @return 注册表的类型集合
	 */
	@Override
	public Set<Class<? extends MainRegistrationProvider<?>>> registry() {
		return Set.of();
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return SilkModPass.getInstance();
	}
}
