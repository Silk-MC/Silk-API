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

package pers.saikel0rado1iu.silk.generate.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.client.Model;
import net.minecraft.util.math.Vec3d;
import pers.saikel0rado1iu.silk.impl.Minecraft;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">模型生成实用工具</h2>
 * 用于提供模组中常用但未提供更方便方法的模型生成方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ModelGenUtil {
	/**
	 * 生成蛋模板模型
	 */
	Model TEMPLATE_SPAWN_EGG = new Model(Optional.of(Minecraft.getInstance().ofId("item/template_spawn_egg")), Optional.empty());
	
	/**
	 * 模型变换模式 JSON
	 *
	 * @param rotation    旋转
	 * @param translation 变换
	 * @param scale       缩放
	 * @return JSON 对象
	 */
	static JsonObject modelTransModeJson(Vec3d rotation, Vec3d translation, Vec3d scale) {
		JsonObject jsonObject = new JsonObject();
		JsonArray rotationArray = new JsonArray();
		rotationArray.add(rotation.getX());
		rotationArray.add(rotation.getY());
		rotationArray.add(rotation.getZ());
		jsonObject.add("rotation", rotationArray);
		JsonArray translationArray = new JsonArray();
		translationArray.add(translation.getX());
		translationArray.add(translation.getY());
		translationArray.add(translation.getZ());
		jsonObject.add("translation", translationArray);
		JsonArray scaleArray = new JsonArray();
		scaleArray.add(scale.getX());
		scaleArray.add(scale.getY());
		scaleArray.add(scale.getZ());
		jsonObject.add("scale", scaleArray);
		return jsonObject;
	}
}
