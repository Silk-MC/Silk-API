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

package pers.saikel0rado1iu.silk.test.modup.client;

import com.google.common.collect.ImmutableList;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.modup.UpdateManagerRegistry;
import pers.saikel0rado1iu.silk.test.modup.Test;
import pers.saikel0rado1iu.silk.update.ClientUpdateManager;

import java.util.List;
import java.util.function.Supplier;

/**
 * Test {@link UpdateManagerRegistry}
 */
public interface UpdateManagerRegistryTest extends UpdateManagerRegistry {
	/**
	 * CLIENT_UPDATE_MANAGER
	 */
	ClientUpdateManager CLIENT_UPDATE_MANAGER = new ClientUpdateManager(Test.UPDATE_DATA_BUILDER);
	/**
	 * UPDATE_MANAGER
	 */
	@SuppressWarnings("unused")
	List<ClientUpdateManager> CLIENT_UPDATE_MANAGER_LIST = UpdateManagerRegistry.registrar((Supplier<List<ClientUpdateManager>>) () -> ImmutableList.of(CLIENT_UPDATE_MANAGER)).register(SilkModUp.getInstance());
}
