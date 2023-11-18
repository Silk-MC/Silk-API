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

package pers.saikel0rado1iu.silk.util.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.screen.mod.ScreenTab;
import pers.saikel0rado1iu.silk.util.screen.widget.ConfigListWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pers.saikel0rado1iu.silk.util.TextUtil.configText;
import static pers.saikel0rado1iu.silk.util.TextUtil.configTip;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组配置选项卡</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class ConfigTab extends ScreenTab {
	private final boolean isDouble;
	private final ConfigData configData;
	public ConfigListWidget configListWidget;
	protected List<SimpleOption<?>> simpleOptionList;
	
	@SilkApi
	public ConfigTab(ModBasicData mod, ConfigData configData) {
		this(mod, configData, false);
	}
	
	public ConfigTab(ModBasicData mod, ConfigData configData, boolean isDouble) {
		super(mod, "config");
		this.isDouble = isDouble;
		this.configData = configData;
		this.configData.reader().load();
	}
	
	@Override
	public void init(MinecraftClient client, TextRenderer textRenderer, int width, int height) {
		// 添加黑色透明窗口
		configListWidget = new ConfigListWidget(client, width, height, 32, height - 40, 25);
		if (configData.type == ConfigData.Type.DEV) return;
		simpleOptionList = addSimpleOption();
		addWidget(configListWidget);
	}
	
	private List<SimpleOption<?>> addSimpleOption() {
		List<SimpleOption<?>> simpleOptionList = new ArrayList<>(8);
		// 添加所有配置选项按钮
		Object prev = null;
		for (String key : configData.configs.keySet()) {
			SimpleOption<?> simpleOption = null;
			Object defaultConfig = configData.defaults.get(key);
			Object object = configData.configs.get(key);
			if (object instanceof Boolean bool) {
				simpleOption = SimpleOption.ofBoolean(configText(configData.mod, key), value -> Tooltip.of(Text.translatable(configTip(configData.mod, key + (value ? ".on" : ".off")))), bool, (value) -> {
					configData.setConfig(key, value);
					configData.getMainConfig().writer().save();
				});
			} else if (object instanceof Enum<?> e) {
				List<Object> enums = List.of(Arrays.stream(e.getDeclaringClass().getEnumConstants()).toArray());
				simpleOption = new SimpleOption<>(configText(configData.mod, key), value -> {
					for (Object o : enums)
						if (o == e) return Tooltip.of(Text.translatable(configTip(configData.mod, key + '.' + enums.get(value).toString().toLowerCase())));
					return Tooltip.of(Text.translatable(configTip(configData.mod, key)));
				}, (optionText, value) -> Text.translatable(configText(configData.mod, key + '.' + enums.get(value).toString().toLowerCase())), new SimpleOption.MaxSuppliableIntCallbacks(0, () -> enums.size() - 1, enums.size() - 1), enums.indexOf(e), value -> {
					configData.setConfig(key, enums.get(value));
					configData.getMainConfig().writer().save();
				});
			} else if (object instanceof List<?> list) {
				if (list.get(2) instanceof Integer i) {
					simpleOption = new SimpleOption<>(configText(configData.mod, key), value -> {
						if (value == list.get(0)) return Tooltip.of(Text.translatable(configTip(configData.mod, key + ".min"), value));
						else if (value == list.get(1)) return Tooltip.of(Text.translatable(configTip(configData.mod, key + ".max"), value));
						else if (value == defaultConfig) return Tooltip.of(Text.translatable(configTip(configData.mod, key + ".default"), value));
						else return Tooltip.of(Text.translatable(configTip(configData.mod, key), value));
					}, (optionText, value) -> {
						if (value == list.get(0)) return Text.translatable(configText(configData.mod, key + ".min"), value);
						else if (value == list.get(1)) return Text.translatable(configText(configData.mod, key + ".max"), value);
						else if (value == defaultConfig) return Text.translatable(configText(configData.mod, key + ".default"), value);
						else return Text.translatable(configText(configData.mod, key), value);
					}, new SimpleOption.ValidatingIntSliderCallbacks((int) list.get(0), (int) list.get(1)), Codec.intRange((int) list.get(0), (int) list.get(1)), i, value -> {
						configData.setConfig(key, value);
						configData.getMainConfig().writer().save();
					});
				} else if (list.get(2) instanceof Float f) {
					simpleOption = new SimpleOption<>(configText(configData.mod, key), value -> {
						if (value == list.get(0)) return Tooltip.of(Text.translatable(configTip(configData.mod, key + ".min"), value));
						else if (value == list.get(1)) return Tooltip.of(Text.translatable(configTip(configData.mod, key + ".max"), value));
						else if (value == defaultConfig) return Tooltip.of(Text.translatable(configTip(configData.mod, key + ".default"), value));
						else return Tooltip.of(Text.translatable(configTip(configData.mod, key), value));
					}, (optionText, value) -> {
						if (value == list.get(0)) return Text.translatable(configText(configData.mod, key + ".min"), value);
						else if (value == list.get(1)) return Text.translatable(configText(configData.mod, key + ".max"), value);
						else if (value == defaultConfig) return Text.translatable(configText(configData.mod, key + ".default"), value);
						else return Text.translatable(configText(configData.mod, key), value);
					}, new SimpleOption.ValidatingIntSliderCallbacks((int) ((float) list.get(0) * 100), (int) ((float) list.get(1) * 100)).withModifier(sliderProgressValue -> (float) sliderProgressValue / 100.0, value -> (int) (value * 100)), Codec.doubleRange((float) list.get(0), (float) list.get(1)), Double.valueOf(f), value -> {
						configData.setConfig(key, value.floatValue());
						configData.getMainConfig().writer().save();
					});
				}
			} else if (object instanceof ConfigData cd) {
				if (cd.getType() == ConfigData.Type.DEV) continue;
				simpleOption = SimpleOption.ofBoolean(configText(configData.mod, key), SimpleOption.constantTooltip(Text.translatable(configTip(configData.mod, key))), (optionText, value) -> Text.of(""), false, (value) -> MinecraftClient.getInstance().setScreen(new ConfigScreen(parent, isDouble, cd, key + '.', Text.translatable(configText(configData.mod, key))) {
					@Override
					public boolean linkTrusted() {
						return ConfigTab.this.linkTrusted();
					}
				}));
			}
			simpleOptionList.add(simpleOption);
			if (!isDouble) {
				configListWidget.addSingleOptionEntry(simpleOption);
			} else if (simpleOptionList.size() % 2 == 0) {
				SimpleOption<?> prevOption = simpleOptionList.get(simpleOptionList.size() - 2);
				configListWidget.addOptionEntry(prevOption, simpleOption);
				if (prev instanceof ConfigData cd) {
					if (cd.type == ConfigData.Type.EXPERIMENTAL) {
						ClickableWidget widget = configListWidget.getWidgetFor(prevOption);
						if (widget != null) widget.setMessage(widget.getMessage().copy().formatted(Formatting.RED));
					} else if (cd.type == ConfigData.Type.DEPRECATED) {
						ClickableWidget widget = configListWidget.getWidgetFor(prevOption);
						if (widget != null) widget.setMessage(widget.getMessage().copy().formatted(Formatting.YELLOW, Formatting.ITALIC));
					}
				}
			}
			prev = object;
			if (!(object instanceof ConfigData cd)) continue;
			if (cd.type == ConfigData.Type.EXPERIMENTAL) {
				ClickableWidget widget = configListWidget.getWidgetFor(simpleOption);
				if (widget != null) widget.setMessage(widget.getMessage().copy().formatted(Formatting.RED));
			} else if (cd.type == ConfigData.Type.DEPRECATED) {
				ClickableWidget widget = configListWidget.getWidgetFor(simpleOption);
				if (widget != null) widget.setMessage(widget.getMessage().copy().formatted(Formatting.YELLOW, Formatting.ITALIC));
			}
		}
		if (isDouble && simpleOptionList.size() % 2 == 1) configListWidget.addOptionEntry(simpleOptionList.get(simpleOptionList.size() - 1), null);
		return simpleOptionList;
	}
	
	@Override
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
		configListWidget.render(context, mouseX, mouseY, delta);
		if (configData.type == ConfigData.Type.EXPERIMENTAL) {
			simpleOptionList.forEach(simpleOption -> {
				ClickableWidget widget = configListWidget.getWidgetFor(simpleOption);
				if (widget == null || widget.getMessage().copy().getStyle().isItalic()) return;
				widget.setMessage(widget.getMessage().copy().formatted(Formatting.RED));
			});
		} else if (configData.type == ConfigData.Type.DEPRECATED) {
			simpleOptionList.forEach(simpleOption -> {
				ClickableWidget widget = configListWidget.getWidgetFor(simpleOption);
				if (widget == null) return;
				if (widget.getMessage().getStyle().getColor() == null || !widget.getMessage().getStyle().getColor().getName().equals(Formatting.RED.getName()))
					widget.setMessage(widget.getMessage().copy().formatted(Formatting.YELLOW, Formatting.ITALIC));
				widget.active = false;
			});
		}
	}
}
