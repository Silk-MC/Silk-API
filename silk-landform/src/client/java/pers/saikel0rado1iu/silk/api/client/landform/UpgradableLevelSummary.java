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

package pers.saikel0rado1iu.silk.api.client.landform;

import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.SaveVersionInfo;
import pers.saikel0rado1iu.silk.api.landform.UpgradableWorldInfo;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.impl.SilkLandform;

import java.nio.file.Path;

/**
 * <h2 style="color:FFC800">可升级关卡摘要</h2>
 *
 * @param <T> 区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public class UpgradableLevelSummary<T extends ChunkGenerator & ChunkGeneratorUpgradable> extends LevelSummary {
	private final UpgradableWorldInfo<T> upgradableWorldInfo;
	
	/**
	 * @param levelInfo           关卡信息
	 * @param versionInfo         存档版本信息
	 * @param upgradableWorldInfo 可升级世界信息
	 * @param name                名称
	 * @param requiresConversion  需要转换
	 * @param locked              已锁定
	 * @param experimental        是实验性的
	 * @param iconPath            图标路径
	 */
	public UpgradableLevelSummary(LevelInfo levelInfo, SaveVersionInfo versionInfo, UpgradableWorldInfo<T> upgradableWorldInfo, String name, boolean requiresConversion, boolean locked, boolean experimental, Path iconPath) {
		super(levelInfo, versionInfo, name, requiresConversion, locked, experimental, iconPath);
		this.upgradableWorldInfo = upgradableWorldInfo;
	}
	
	/**
	 * 添加详细信息
	 *
	 * @param origin 原始信息
	 * @return 详细信息
	 */
	public Text addDetails(MutableText origin) {
		origin.append(Text.literal(", "));
		MutableText text = ScreenTexts.composeGenericOptionText(WidgetTexts.text(upgradableWorldInfo.mod(), "generator." + upgradableWorldInfo.generatorId().getPath()),
				upgradableWorldInfo.isNonVersion() ? WidgetTexts.text(upgradableWorldInfo.mod(), "generator." + ChunkGeneratorUpgradable.NON_VERSION) : Text.literal(upgradableWorldInfo.version()));
		if (upgradableWorldInfo.isNonVersion()) origin.append(text.formatted(Formatting.ITALIC, Formatting.GOLD));
		else if (upgradableWorldInfo.shouldUpgradeWorld()) origin.append(text.formatted(Formatting.GOLD));
		else if (upgradableWorldInfo.shouldDowngradeWorld()) origin.append(text.formatted(Formatting.RED));
		else origin.append(text);
		return origin;
	}
	
	/**
	 * 获取升级提示
	 *
	 * @return 升级提示文本
	 */
	public Text getUpgradeTooltip() {
		return Text.translatable(WidgetTexts.textKey(SilkLandform.getInstance(), "upgrade.tooltip"), upgradableWorldInfo.mod().i18nName()).formatted(Formatting.GOLD);
	}
	
	/**
	 * 获取降级提示
	 *
	 * @return 降级提示
	 */
	public Text getDowngradeTooltip() {
		return Text.translatable(WidgetTexts.textKey(SilkLandform.getInstance(), "downgrade.tooltip"), upgradableWorldInfo.mod().i18nName()).formatted(Formatting.RED);
	}
	
	/**
	 * 获取可升级世界信息
	 *
	 * @return 可升级世界信息
	 */
	public UpgradableWorldInfo<T> info() {
		return upgradableWorldInfo;
	}
	
	@Override
	public boolean isRecreatable() {
		return super.isRecreatable() && 0 == upgradableWorldInfo.compare();
	}
	
	@Override
	public boolean isSelectable() {
		return super.isSelectable() && !upgradableWorldInfo.shouldDowngradeWorld();
	}
}
