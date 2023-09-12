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

package pers.saikel0rado1iu.silk;

import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.NotNull;
import pers.saikel0rado1iu.silk.api.ModExpansionData;
import pers.saikel0rado1iu.silk.util.config.ConfigData;
import pers.saikel0rado1iu.silk.util.update.UpdateData;
import pers.saikel0rado1iu.silk.util.update.UpdateSystem;

import static pers.saikel0rado1iu.silk.Main.CONFIG_DATA;

/**
 * @author Saike
 */
public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 此入口点适用于设置客户端特定的逻辑，例如渲染。
        ModExpansionData sr = new ModExpansionData() {
            @Override
            public @NotNull String getId() {
                return "spontaneous_replace";
            }

            @Override
            public @NotNull String getSlug() {
                return "spontaneous-replace";
            }
        };
        ConfigData configData = ConfigData.builder(sr).build();
        CONFIG_DATA.addSubConfigs(sr.getId(), configData);
        UpdateSystem.registryUpdate(new UpdateData(sr, configData));
    }
}