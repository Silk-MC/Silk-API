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

package pers.saikel0rado1iu.silk.codex;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.codex.screen.SettingScreen;
import pers.saikel0rado1iu.silk.common.util.MathUtil;
import pers.saikel0rado1iu.silk.pattern.screen.ModScreen;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * <h2 style="color:FFC800">设置选项助手</h2>
 * 用于将设置选项转化为可在 {@link OptionListWidget} 中添加的格式化 {@link SimpleOption}
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public final class SettingOptionHelper extends OptionListWidget {
	private SettingOptionHelper(MinecraftClient minecraftClient, int i, int j, int k, int l) {
		super(minecraftClient, i, j, k, l);
	}
	
	/**
	 * 初始化所有选项
	 *
	 * @param optionListWidget 选项列表控件
	 * @param settingData      设置数据
	 * @param parent           父屏幕
	 * @param isDouble         是否为双按钮
	 * @param linkTrusted      是否信任链接
	 * @param save             保存方法，用于保存数据设置
	 */
	@SuppressWarnings("unchecked")
	public static void initOptions(OptionListWidget optionListWidget, SettingData settingData, Screen parent, boolean isDouble, boolean linkTrusted, Runnable save) {
		if (settingData.type() == SettingType.DEVELOPMENT) return;
		SimpleOption<?> prevOption = null;
		for (SettingOption<?> option : settingData.options().keySet()) {
			SimpleOption<?> simpleOption;
			if (option.type() == OptionType.SWITCH) {
				simpleOption = SimpleOption.ofBoolean(
						OptionTexts.textKey(option),
						value -> Tooltip.of(formatText(OptionTexts.tip(option, value ? "on" : "off"), settingData, option)),
						(boolean) settingData.getValue(option),
						(value) -> {
							settingData.setValue(option, value.toString());
							save.run();
						});
			} else if (option.type().clazz().isEnum()) {
				ImmutableList<Object> enumValues = ImmutableList.copyOf(option.type().clazz().getEnumConstants());
				ImmutableList.Builder<Integer> listBuilder = ImmutableList.builder();
				for (int count = 0; count < enumValues.size(); count++) listBuilder.add(count);
				ImmutableList<Integer> values = listBuilder.build();
				Function<Integer, String> optionKey = value -> enumValues.get(value).toString().toLowerCase();
				simpleOption = new SimpleOption<>(
						OptionTexts.textKey(option),
						value -> value >= 0 && value < enumValues.size()
								? Tooltip.of(formatText(OptionTexts.tip(option, optionKey.apply(value)), settingData, option))
								: Tooltip.of(formatText(OptionTexts.tip(option), settingData, option)),
						(optionText, value) -> OptionTexts.text(option, optionKey.apply(value)),
						new SimpleOption.PotentialValuesBasedCallbacks<>(values, Codec.INT),
						enumValues.indexOf(settingData.getValue(option)),
						value -> {
							settingData.setValue(option, enumValues.get(value).toString());
							save.run();
						});
			} else if (option.type() == OptionType.INT_SLIDER) {
				simpleOption = new SimpleOption<>(
						OptionTexts.textKey(option),
						value -> {
							if (value.equals(settingData.getValue(option, OptionValueIndex.DEFAULT))) {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option, "default"), ((SettingOption<Integer>) option).getValue().apply(value)), settingData, option));
							} else if (value.equals(settingData.getValue(option, OptionValueIndex.MIN))) {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option, "min"), ((SettingOption<Integer>) option).getValue().apply(value)), settingData, option));
							} else if (value.equals(settingData.getValue(option, OptionValueIndex.MAX))) {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option, "max"), ((SettingOption<Integer>) option).getValue().apply(value)), settingData, option));
							} else {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option), ((SettingOption<Integer>) option).getValue().apply(value)), settingData, option));
							}
						},
						(optionText, value) -> {
							if (value.equals(settingData.getValue(option, OptionValueIndex.DEFAULT))) {
								return Text.translatable(OptionTexts.textKey(option, "default"), ((SettingOption<Integer>) option).getValue().apply(value));
							} else if (value.equals(settingData.getValue(option, OptionValueIndex.MIN))) {
								return Text.translatable(OptionTexts.textKey(option, "min"), ((SettingOption<Integer>) option).getValue().apply(value));
							} else if (value.equals(settingData.getValue(option, OptionValueIndex.MAX))) {
								return Text.translatable(OptionTexts.textKey(option, "max"), ((SettingOption<Integer>) option).getValue().apply(value));
							} else {
								return Text.translatable(OptionTexts.textKey(option), ((SettingOption<Integer>) option).getValue().apply(value));
							}
						},
						new SimpleOption.ValidatingIntSliderCallbacks((int) settingData.getValue(option, OptionValueIndex.MIN), (int) settingData.getValue(option, OptionValueIndex.MAX)),
						Codec.intRange((int) settingData.getValue(option, OptionValueIndex.MIN), (int) settingData.getValue(option, OptionValueIndex.MAX)),
						(int) settingData.getValue(option),
						value -> {
							settingData.setValue(option, value.toString());
							save.run();
						});
			} else if (option.type() == OptionType.DOUBLE_SLIDER) {
				simpleOption = new SimpleOption<>(
						OptionTexts.textKey(option),
						value -> {
							if (MathUtil.compareFloat(value, (double) settingData.getValue(option, OptionValueIndex.DEFAULT)) == 0) {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option, "default"), ((SettingOption<Double>) option).getValue().apply(value)), settingData, option));
							} else if (MathUtil.compareFloat(value, (double) settingData.getValue(option, OptionValueIndex.MIN)) == 0) {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option, "min"), ((SettingOption<Double>) option).getValue().apply(value)), settingData, option));
							} else if (MathUtil.compareFloat(value, (double) settingData.getValue(option, OptionValueIndex.MAX)) == 0) {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option, "max"), ((SettingOption<Double>) option).getValue().apply(value)), settingData, option));
							} else {
								return Tooltip.of(formatText(Text.translatable(OptionTexts.tipKey(option), ((SettingOption<Double>) option).getValue().apply(value)), settingData, option));
							}
						},
						(optionText, value) -> {
							if (MathUtil.compareFloat(value, (double) settingData.getValue(option, OptionValueIndex.DEFAULT)) == 0) {
								return Text.translatable(OptionTexts.textKey(option, "default"), ((SettingOption<Double>) option).getValue().apply(value));
							} else if (MathUtil.compareFloat(value, (double) settingData.getValue(option, OptionValueIndex.MIN)) == 0) {
								return Text.translatable(OptionTexts.textKey(option, "min"), ((SettingOption<Double>) option).getValue().apply(value));
							} else if (MathUtil.compareFloat(value, (double) settingData.getValue(option, OptionValueIndex.MAX)) == 0) {
								return Text.translatable(OptionTexts.textKey(option, "max"), ((SettingOption<Double>) option).getValue().apply(value));
							} else {
								return Text.translatable(OptionTexts.textKey(option), ((SettingOption<Double>) option).getValue().apply(value));
							}
						},
						new SimpleOption.ValidatingIntSliderCallbacks((int) ((double) settingData.getValue(option, OptionValueIndex.MIN) * 1000), (int) ((double) settingData.getValue(option, OptionValueIndex.MAX) * 1000))
								.withModifier(sliderProgressValue -> sliderProgressValue / 1000.0, value -> (int) (value * 1000)),
						Codec.doubleRange((double) settingData.getValue(option, OptionValueIndex.MIN), (double) settingData.getValue(option, OptionValueIndex.MAX)),
						(double) settingData.getValue(option),
						value -> {
							settingData.setValue(option, value.toString());
							save.run();
						});
			} else if (option.type() == OptionType.LINK) {
				simpleOption = SimpleOption.ofBoolean(
						OptionTexts.textKey(option),
						SimpleOption.constantTooltip(formatText(OptionTexts.tip(option), settingData, option)),
						(optionText, value) -> Text.of(""),
						false,
						(value) -> {
							String url = settingData.getValue(option, OptionValueIndex.DEFAULT).toString();
							MinecraftClient.getInstance().setScreen(new ConfirmLinkScreen(confirmed -> {
								if (confirmed) Util.getOperatingSystem().open(url);
								MinecraftClient.getInstance().setScreen(parent);
							}, url, linkTrusted));
						});
			} else {
				SettingData data = (SettingData) settingData.getValue(option);
				if (data.type() == SettingType.DEVELOPMENT) continue;
				simpleOption = SimpleOption.ofBoolean(
						OptionTexts.textKey(option),
						SimpleOption.constantTooltip(formatText(OptionTexts.tip(option), settingData, option)),
						(optionText, value) -> Text.of(""),
						false,
						(value) -> {
							if (parent instanceof ModScreen modScreen) modScreen.reset();
							MinecraftClient.getInstance().setScreen(new SettingScreen(Optional.of(parent), data, Optional.of(option), isDouble, save));
						});
			}
			if (isDouble) {
				if (prevOption == null) prevOption = simpleOption;
				if (prevOption == simpleOption) continue;
				optionListWidget.addOptionEntry(prevOption, simpleOption);
				prevOption = null;
			} else {
				optionListWidget.addSingleOptionEntry(simpleOption);
			}
		}
		if (isDouble && prevOption != null) optionListWidget.addOptionEntry(prevOption, null);
	}
	
	/**
	 * 格式化选项渲染
	 *
	 * @param optionListWidget 选项列表控件
	 * @param settingData      设置数据
	 */
	@SuppressWarnings("unchecked")
	public static void formatOptionRender(OptionListWidget optionListWidget, SettingData settingData) {
		List<WidgetEntry> children = optionListWidget.children();
		ImmutableList.Builder<ClickableWidget> builder = ImmutableList.builder();
		for (WidgetEntry child : children) builder.addAll((Iterable<? extends ClickableWidget>) child.children());
		ImmutableList<ClickableWidget> optionWidgets = builder.build();
		for (SettingOption<?> option : settingData.options().keySet()) {
			AtomicReference<ClickableWidget> widget = new AtomicReference<>();
			if (optionWidgets.stream().anyMatch(clickableWidget -> {
				if (clickableWidget.getMessage().withoutStyle().toString().contains(OptionTexts.textKey(option).replaceFirst("(?s)(.*)\\.text", "$1"))) {
					widget.set(clickableWidget);
					return true;
				}
				return false;
			})) {
				if (widget.get() == null) continue;
				widget.get().setMessage(settingData.type().getFormatText(widget.get().getMessage()));
				if (settingData.type() == SettingType.DEPRECATED) widget.get().active = false;
				if (!(settingData.getValue(option) instanceof SettingData data)) continue;
				widget.get().setMessage(data.type().getFormatText(widget.get().getMessage()));
				if (data.type() == SettingType.DEPRECATED) widget.get().active = false;
			}
		}
	}
	
	private static Text formatText(MutableText text, SettingData settingData, SettingOption<?> option) {
		Text formatText = settingData.type().getFormatText(text);
		if (!(settingData.getValue(option) instanceof SettingData data)) return formatText;
		return data.type().getFormatText(formatText);
	}
}
