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

package pers.saikel0rado1iu.silk.mixin.client.event.pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.client.event.pattern.AddButtonInTitleScreenCallback;
import pers.saikel0rado1iu.silk.impl.SilkPattern;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <h2 style="color:FFC800">{@link AddButtonInTitleScreenCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@Mixin(TitleScreen.class)
abstract class AddButtonInTitleScreenCallbackMixin extends Screen {
	@Unique
	private static boolean hasMainButton = false;
	
	private AddButtonInTitleScreenCallbackMixin(Text title) {
		super(title);
	}
	
	@Unique
	private int extraButtonLineCount() {
		try {
			int count = 0;
			if (hasMainButton) count++;
			if (FabricLoader.getInstance().isModLoaded("modmenu")) {
				Path path = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "modmenu.json");
				JsonObject modMenuJson = (JsonObject) JsonParser.parseString(Files.readString(path, StandardCharsets.UTF_8));
				String style = modMenuJson.get("mods_button_style").getAsString();
				if ("classic".equals(style)) count++;
			}
			return count;
		} catch (IOException e) {
			String msg = "Unexpected error: Unable to read the modmenu config file content!";
			SilkPattern.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
	}
	
	@ModifyArg(method = "init", at = @At(value = "INVOKE", target = "L net/minecraft/client/realms/gui/screen/RealmsNotificationsScreen;init(L net/minecraft/client/MinecraftClient;II)V"), index = 2)
	private int fixRealms(int height) {
		return height + extraButtonLineCount() * 20;
	}
	
	@ModifyVariable(method = "initWidgetsNormal(II)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private int ifHasModMenu(int y) {
		return extraButtonLineCount() > 0 ? y + (int) ((double) y / width * width / y * extraButtonLineCount() * 5) : y;
	}
	
	@Inject(method = "initWidgetsNormal", at = @At("HEAD"))
	private void addButton(int y, int spacingY, CallbackInfo ci) {
		hasMainButton = AddButtonInTitleScreenCallback.EVENT.invoker().add(client, this, this::addDrawableChild, y, spacingY, hasMainButton);
	}
}
