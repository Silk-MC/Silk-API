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

package pers.saikel0rado1iu.silk.test.landform.client;

import com.google.common.collect.ImmutableList;
import pers.saikel0rado1iu.silk.landform.ClientUpgradableWorldManager;
import pers.saikel0rado1iu.silk.landform.UpgradableWorldManagerRegistry;
import pers.saikel0rado1iu.silk.test.landform.Test;

import java.util.List;
import java.util.function.Supplier;

import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * Test {@link UpgradableWorldManagerRegistry}
 */
public interface UpgradableWorldManagerRegistryTest extends UpgradableWorldManagerRegistry {
	/**
	 * UPDATE_MANAGER
	 */
	@SuppressWarnings("unused")
	List<ClientUpgradableWorldManager<?>> CLIENT_UPGRADABLE_WORLD_MANAGER_LIST = UpgradableWorldManagerRegistry
			.registrar((Supplier<List<ClientUpgradableWorldManager<?>>>) () -> ImmutableList.of(new ClientUpgradableWorldManager<>(Test.DATA))).register(MOD_PASS);
}
