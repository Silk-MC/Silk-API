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

package pers.saikeloradoliu.silk;

import pers.saikeloradoliu.silk.api.ModBasicData;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">Silk API 的模组数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 */
final public class SilkData implements ModBasicData {
    public static final SilkData INSTANCE = new SilkData();

    @Override
    public String getModId() {
        return "silk-api";
    }

    @Override
    public Optional<URL> getCommunityLink() {
        String url = getModInstance().getMetadata().getCustomValue("modmenu").getAsObject().get("links").getAsObject().get("modmenu.discord").getAsString();
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<URL> getSupportLink() {
        String url = getModInstance().getMetadata().getCustomValue("modmenu").getAsObject().get("links").getAsObject().get("modmenu.support.patreon").getAsString();
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public URL getAfdianLink() {
        String url = getModInstance().getMetadata().getCustomValue("modmenu").getAsObject().get("links").getAsObject().get("modmenu.support.afdian").getAsString();
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
