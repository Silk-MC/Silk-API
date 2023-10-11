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

package pers.saikel0rado1iu.silk.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged.Bow;
import pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged.Crossbow;

import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">用于提供模组中常用但未提供更方便方法的模型生成方法</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface SilkModelGenerator {
	@SilkApi
	static JsonObject modelTransModeJson(float[] rotation, float[] translation, float[] scale) {
		JsonObject jsonObject = new JsonObject();
		JsonArray rotationArray = new JsonArray();
		rotationArray.add(rotation[0]);
		rotationArray.add(rotation[1]);
		rotationArray.add(rotation[2]);
		jsonObject.add("rotation", rotationArray);
		JsonArray translationArray = new JsonArray();
		translationArray.add(translation[0]);
		translationArray.add(translation[1]);
		translationArray.add(translation[2]);
		jsonObject.add("translation", translationArray);
		JsonArray scaleArray = new JsonArray();
		scaleArray.add(scale[0]);
		scaleArray.add(scale[1]);
		scaleArray.add(scale[2]);
		jsonObject.add("scale", scaleArray);
		return jsonObject;
	}
	
	@SilkApi
	static void registerOverlayItemJson(ItemModelGenerator itemModelGenerator, Item item) {
		Models.GENERATED_TWO_LAYERS.upload(ModelIds.getItemModelId(item),
				TextureMap.layered(TextureMap.getId(item), TextureMap.getSubId(item, "_overlay")),
				itemModelGenerator.writer);
	}
	
	@SilkApi
	static void registerBowJson(ItemModelGenerator itemModelGenerator, Bow bow, float[] pullStage) {
		Models.GENERATED.upload(ModelIds.getItemModelId(bow), TextureMap.layer0(TextureMap.getSubId(bow, "_standby")), itemModelGenerator.writer, (id, textures) -> {
			JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
			JsonObject display = new JsonObject();
			display.add(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND.asString(), modelTransModeJson(new float[]{-80, 260, -40}, new float[]{-1, -2, 2.5F}, new float[]{0.9F, 0.9F, 0.9F}));
			display.add(ModelTransformationMode.THIRD_PERSON_LEFT_HAND.asString(), modelTransModeJson(new float[]{-80, -280, 40}, new float[]{-1, -2, 2.5F}, new float[]{0.9F, 0.9F, 0.9F}));
			display.add(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND.asString(), modelTransModeJson(new float[]{0, -90, 25}, new float[]{1.13F, 3.2F, 1.13F}, new float[]{0.68F, 0.68F, 0.68F}));
			display.add(ModelTransformationMode.FIRST_PERSON_LEFT_HAND.asString(), modelTransModeJson(new float[]{0, 90, -25}, new float[]{1.13F, 3.2F, 1.13F}, new float[]{0.68F, 0.68F, 0.68F}));
			jsonObject.add("display", display);
			JsonArray jsonArray = new JsonArray();
			for (float projectile : bow.getAllProjectile().keySet()) {
				for (int count = 0; count < pullStage.length; count++) {
					JsonObject predicate = new JsonObject();
					JsonObject object = new JsonObject();
					object.addProperty(Bow.PROJECTILE_ID_KEY.toLowerCase(), projectile);
					object.addProperty(Bow.PULLING_KEY, 1);
					object.addProperty(Bow.PULL_KEY, pullStage[count]);
					predicate.add("predicate", object);
					predicate.addProperty("model", id.withSuffixedPath('_' + bow.getAllProjectile().get(projectile) + '_' + Bow.PULLING_KEY + '_' + count).toString());
					jsonArray.add(predicate);
				}
			}
			jsonObject.add("overrides", jsonArray);
			return jsonObject;
		});
		for (float pull : bow.getAllProjectile().keySet()) {
			for (int count = 0; count < pullStage.length; count++) {
				String suffix = '_' + bow.getAllProjectile().get(pull) + '_' + Bow.PULLING_KEY + '_' + count;
				new Model(Optional.of(Registries.ITEM.getId(bow)), Optional.empty(), TextureKey.LAYER0)
						.upload(ModelIds.getItemSubModelId(bow, suffix), TextureMap.layer0(TextureMap.getSubId(bow, suffix)), itemModelGenerator.writer);
			}
		}
	}
	
	@SilkApi
	static void registerCrossbowJson(ItemModelGenerator itemModelGenerator, Crossbow crossbow, float[] pullStage) {
		Models.GENERATED.upload(ModelIds.getItemModelId(crossbow), TextureMap.layer0(TextureMap.getSubId(crossbow, "_standby")), itemModelGenerator.writer, (id, textures) -> {
			JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
			JsonObject display = new JsonObject();
			display.add(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND.asString(), modelTransModeJson(new float[]{-90, 0, -60}, new float[]{2, 0.1F, -3}, new float[]{0.9F, 0.9F, 0.9F}));
			display.add(ModelTransformationMode.THIRD_PERSON_LEFT_HAND.asString(), modelTransModeJson(new float[]{-90, 0, 30}, new float[]{2, 0.1F, -3}, new float[]{0.9F, 0.9F, 0.9F}));
			display.add(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND.asString(), modelTransModeJson(new float[]{-90, 0, -55}, new float[]{1.13F, 3.2F, 1.13F}, new float[]{0.68F, 0.68F, 0.68F}));
			display.add(ModelTransformationMode.FIRST_PERSON_LEFT_HAND.asString(), modelTransModeJson(new float[]{-90, 0, 35}, new float[]{1.13F, 3.2F, 1.13F}, new float[]{0.68F, 0.68F, 0.68F}));
			jsonObject.add("display", display);
			JsonArray jsonArray = new JsonArray();
			for (int count = 0; count < pullStage.length; count++) {
				JsonObject predicate = new JsonObject();
				JsonObject object = new JsonObject();
				object.addProperty(Crossbow.PULLING_KEY, 1);
				object.addProperty(Crossbow.PULL_KEY, pullStage[count]);
				predicate.add("predicate", object);
				predicate.addProperty("model", id.withSuffixedPath('_' + Crossbow.PULLING_KEY + '_' + count).toString());
				jsonArray.add(predicate);
			}
			for (float projectile : crossbow.getAllProjectile().keySet()) {
				JsonObject predicate = new JsonObject();
				JsonObject object = new JsonObject();
				object.addProperty(Crossbow.CHARGED_KEY.toLowerCase(), 1);
				object.addProperty(Crossbow.PROJECTILE_ID_KEY.toLowerCase(), projectile);
				predicate.add("predicate", object);
				predicate.addProperty("model", id.withSuffixedPath('_' + crossbow.getAllProjectile().get(projectile)).toString());
				jsonArray.add(predicate);
			}
			jsonObject.add("overrides", jsonArray);
			return jsonObject;
		});
		for (int count = 0; count < pullStage.length; count++) {
			String suffix = '_' + Crossbow.PULLING_KEY + '_' + count;
			new Model(Optional.of(Registries.ITEM.getId(crossbow)), Optional.empty(), TextureKey.LAYER0)
					.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), itemModelGenerator.writer);
		}
		for (float projectile : crossbow.getAllProjectile().keySet()) {
			String suffix = '_' + crossbow.getAllProjectile().get(projectile);
			new Model(Optional.of(Registries.ITEM.getId(crossbow)), Optional.empty(), TextureKey.LAYER0)
					.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), itemModelGenerator.writer);
		}
	}
}
