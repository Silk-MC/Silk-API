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

package pers.saikel0rado1iu.silk.util.world.upgrade.info;

import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.gen.world.chunk.UpgradeChunkGenerator;
import pers.saikel0rado1iu.silk.util.TextUtil;

import java.nio.file.Path;

/**
 * <h2 style="color:FFC800">可升级关卡摘要</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@ApiStatus.Internal
public class UpgradeLevelSummary extends LevelSummary {
	@ApiStatus.Internal
	public static final String TEXT_PREFIX = "world_upgrade.";
	private final ModWorldInfo modWorldInfo;
	
	public UpgradeLevelSummary(LevelInfo levelInfo, SaveVersionInfo versionInfo, ModWorldInfo modWorldInfo, String name, boolean requiresConversion, boolean locked, boolean experimental, Path iconPath) {
		super(levelInfo, versionInfo, name, requiresConversion, locked, experimental, iconPath);
		this.modWorldInfo = modWorldInfo;
	}
	
	@Override
	public boolean isRecreatable() {
		return super.isRecreatable() && 0 == modWorldInfo.compare();
	}
	
	@Override
	public boolean isSelectable() {
		return super.isSelectable() && !shouldDowngradeWorld();
	}
	
	@ApiStatus.Internal
	public Text addDetails(MutableText origin) {
		origin.append(Text.literal(", "));
		MutableText prefix = Text.translatable(TextUtil.widgetText(modWorldInfo.mod(), "generator"));
		if (isNonVersion()) {
			origin.append(prefix.formatted(Formatting.ITALIC, Formatting.GOLD))
					.append(ScreenTexts.SPACE)
					.append(Text.translatable(TextUtil.widgetText(modWorldInfo.mod(), "generator." + UpgradeChunkGenerator.NON_VERSION)).formatted(Formatting.ITALIC, Formatting.GOLD));
		} else if (shouldUpgradeWorld()) {
			origin.append(prefix.formatted(Formatting.GOLD))
					.append(ScreenTexts.SPACE)
					.append(Text.literal(modWorldInfo.version()).formatted(Formatting.GOLD));
		} else if (shouldDowngradeWorld()) {
			origin.append(prefix.formatted(Formatting.RED))
					.append(ScreenTexts.SPACE)
					.append(Text.literal(modWorldInfo.version()).formatted(Formatting.RED));
		} else {
			origin.append(prefix).append(ScreenTexts.SPACE).append(modWorldInfo.version());
		}
		return origin;
	}
	
	@ApiStatus.Internal
	public Text getUpgradeTooltip() {
		return Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "upgrade.tooltip"), modWorldInfo.mod().getLocalizedName()).formatted(Formatting.GOLD);
	}
	
	@ApiStatus.Internal
	public Text getDowngradeTooltip() {
		return Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "downgrade.tooltip"), modWorldInfo.mod().getLocalizedName()).formatted(Formatting.RED);
	}
	
	@ApiStatus.Internal
	public boolean shouldUpgradeWorld() {
		return isNonVersion() || modWorldInfo.compare() < 0;
	}
	
	@ApiStatus.Internal
	public boolean shouldDowngradeWorld() {
		return modWorldInfo.compare() > 0;
	}
	
	@ApiStatus.Internal
	public boolean isNonVersion() {
		return modWorldInfo.version().equals(UpgradeChunkGenerator.NON_VERSION);
	}
	
	@ApiStatus.Internal
	public ModWorldInfo getModWorldInfo() {
		return modWorldInfo;
	}
}
