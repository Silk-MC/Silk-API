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

package pers.saikeloradoliu.silk.api;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.MinecraftVersion;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">用于构建基础的模组数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 */
public interface ModBasicData {
    default String getMinecraftVer() {
        return MinecraftVersion.CURRENT.getName();
    }

    String getModId();

    default ModContainer getModInstance() {
        var modContainerOptional = FabricLoader.getInstance().getModContainer(getModId());
        if (modContainerOptional.isEmpty())
            throw new RuntimeException("Mod not found, no mod with id '" + getModId() + "' exists!");
        return modContainerOptional.get();
    }

    default String getModName() {
        return getModInstance().getMetadata().getName();
    }

    default String getModVer() {
        return getModInstance().getMetadata().getVersion().getFriendlyString();
    }

    /**
     * <p>此记录器用于将文本写入控制台和日志文件。</p>
     * <p>使用您的模组 ID 作为记录器的名称被认为是最佳实践。</p>
     * <p>这样，就很清楚是哪个模组写了信息、警告和错误。</p>
     */
    default Logger getLogger() {
        return LoggerFactory.getLogger(getModName());
    }

    default Optional<Identifier> getIconId() {
        Optional<String> path = getModInstance().getMetadata().getIconPath(4);
        return path.map(Identifier::new);
    }

    default int getThemeColor() {
        return -1;
    }

    default Optional<URL> getHomepage() {
        Optional<String> url = getModInstance().getMetadata().getContact().get("homepage");
        if (url.isEmpty()) return Optional.empty();
        try {
            return Optional.of(new URL(url.get()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    default Optional<URL> getSourcesLink() {
        Optional<String> url = getModInstance().getMetadata().getContact().get("sources");
        if (url.isEmpty()) return Optional.empty();
        try {
            return Optional.of(new URL(url.get()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    default Optional<URL> getIssuesLink() {
        Optional<String> url = getModInstance().getMetadata().getContact().get("issues");
        if (url.isEmpty()) return Optional.empty();
        try {
            return Optional.of(new URL(url.get()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    Optional<URL> getCommunityLink();

    Optional<URL> getSupportLink();
}
