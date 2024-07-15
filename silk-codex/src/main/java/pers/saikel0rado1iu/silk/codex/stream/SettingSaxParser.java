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

package pers.saikel0rado1iu.silk.codex.stream;

import com.google.common.collect.Maps;
import net.minecraft.util.Identifier;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import pers.saikel0rado1iu.silk.codex.OptionType;
import pers.saikel0rado1iu.silk.codex.SettingData;
import pers.saikel0rado1iu.silk.codex.SettingOption;

import java.util.LinkedHashMap;

/**
 * <h2 style="color:FFC800">设置 SAX 解析器</h2>
 * XML 格式设置文件的数据解析器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
final class SettingSaxParser extends DefaultHandler {
	private final LinkedHashMap<SettingOption<?>, SettingData> superSettingData = Maps.newLinkedHashMapWithExpectedSize(8);
	private SettingData settingData;
	private SettingOption<?> option;
	
	SettingSaxParser(SettingData settingData) {
		this.settingData = settingData;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals(settingData.modData().id())) return;
		option = settingData.getOption(Identifier.tryParse(qName));
		if (option.type() == OptionType.SETTINGS) {
			superSettingData.put(option, settingData);
			settingData = (SettingData) settingData.getValue(option);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if (superSettingData.isEmpty()) return;
		SettingOption<?> option = superSettingData.keySet().toArray(new SettingOption<?>[]{})[superSettingData.keySet().size() - 1];
		if (qName.equals(option.id().toString())) {
			settingData = superSettingData.get(option);
			superSettingData.remove(option);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		String value = new String(ch, start, length).trim();
		if (value.isEmpty()) return;
		settingData.setValue(option, value);
	}
}
