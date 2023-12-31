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

package pers.saikel0rado1iu.silk.gen.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.data.client.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.item.ItemGroupCreator;
import pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged.Bow;
import pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged.Crossbow;
import pers.saikel0rado1iu.silk.util.Minecraft;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">用于提供模组中常用但未提供更方便方法的模型生成方法</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface SilkModelGenerator {
	@SilkApi
	Model TEMPLATE_SPAWN_EGG = new Model(Optional.of(new Identifier(Minecraft.DATA.getId(), "item/template_spawn_egg")), Optional.empty());
	
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
	static void registerId(ItemModelGenerator itemModelGenerator, Identifier id, Model model) {
		model.upload(id, TextureMap.layer0(id), itemModelGenerator.writer);
	}
	
	/**
	 * 如果使用了 {@link ItemGroupCreator#create(ModBasicData, String)}创建了一个专门的物品组图标则使用此方法生成物品模型
	 */
	@SilkApi
	static void registerItemGroup(ItemModelGenerator itemModelGenerator, RegistryKey<ItemGroup> itemGroup) {
		registerId(itemModelGenerator, new Identifier(itemGroup.getValue().getNamespace(), ItemGroupCreator.getIconId(itemGroup.getValue().getPath())).withPrefixedPath("item/"), Models.GENERATED);
	}
	
	@SilkApi
	static void registerBlockItem(ItemModelGenerator itemModelGenerator, BlockItem blockItem) {
		Block block = blockItem.getBlock();
		itemModelGenerator.register(blockItem, new Model(Optional.of(new Identifier(Registries.BLOCK.getId(block).getNamespace(), "block/" + Registries.BLOCK.getId(block).getPath())), Optional.empty()));
	}
	
	@SilkApi
	static void registerOverlayItemJson(ItemModelGenerator itemModelGenerator, Item item) {
		Models.GENERATED_TWO_LAYERS.upload(ModelIds.getItemModelId(item),
				TextureMap.layered(TextureMap.getId(item), TextureMap.getSubId(item, "_overlay")),
				itemModelGenerator.writer);
	}
	
	@SilkApi
	static void registerBowJson(ItemModelGenerator itemModelGenerator, Bow bow, float[] pullStage) {
		Models.GENERATED.upload(ModelIds.getItemModelId(bow), TextureMap.layer0(bow), itemModelGenerator.writer, (id, textures) -> {
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
					if (projectile != 0) object.addProperty(Bow.PROJECTILE_ID_KEY.toLowerCase(), projectile);
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
				new Model(Optional.of(ModelIds.getItemModelId(bow)), Optional.empty(), TextureKey.LAYER0)
						.upload(ModelIds.getItemSubModelId(bow, suffix), TextureMap.layer0(TextureMap.getSubId(bow, suffix)), itemModelGenerator.writer);
			}
		}
	}
	
	@SilkApi
	static void registerCustomDisplayBowJson(ItemModelGenerator itemModelGenerator, Bow bow, JsonObject display, float[] pullStage) {
		Models.GENERATED.upload(ModelIds.getItemModelId(bow), TextureMap.layer0(bow), itemModelGenerator.writer, (id, textures) -> {
			JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
			jsonObject.add("display", display);
			JsonArray jsonArray = new JsonArray();
			for (float projectile : bow.getAllProjectile().keySet()) {
				for (int count = 0; count < pullStage.length; count++) {
					JsonObject predicate = new JsonObject();
					JsonObject object = new JsonObject();
					if (projectile != 0) object.addProperty(Bow.PROJECTILE_ID_KEY.toLowerCase(), projectile);
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
				new Model(Optional.of(ModelIds.getItemModelId(bow)), Optional.empty(), TextureKey.LAYER0)
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
				if (projectile != 0) object.addProperty(Crossbow.PROJECTILE_ID_KEY.toLowerCase(), projectile);
				predicate.add("predicate", object);
				predicate.addProperty("model", id.withSuffixedPath('_' + crossbow.getAllProjectile().get(projectile)).toString());
				jsonArray.add(predicate);
			}
			jsonObject.add("overrides", jsonArray);
			return jsonObject;
		});
		for (int count = 0; count < pullStage.length; count++) {
			String suffix = '_' + Crossbow.PULLING_KEY + '_' + count;
			new Model(Optional.of(ModelIds.getItemModelId(crossbow)), Optional.empty(), TextureKey.LAYER0)
					.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), itemModelGenerator.writer);
		}
		for (float projectile : crossbow.getAllProjectile().keySet()) {
			String suffix = '_' + crossbow.getAllProjectile().get(projectile);
			new Model(Optional.of(ModelIds.getItemModelId(crossbow)), Optional.empty(), TextureKey.LAYER0)
					.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), itemModelGenerator.writer);
		}
	}
	
	@SilkApi
	static void registerCustomDisplayCrossbowJson(ItemModelGenerator itemModelGenerator, Crossbow crossbow, JsonObject display, float[] pullStage) {
		Models.GENERATED.upload(ModelIds.getItemModelId(crossbow), TextureMap.layer0(TextureMap.getSubId(crossbow, "_standby")), itemModelGenerator.writer, (id, textures) -> {
			JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
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
				if (projectile != 0) object.addProperty(Crossbow.PROJECTILE_ID_KEY.toLowerCase(), projectile);
				predicate.add("predicate", object);
				predicate.addProperty("model", id.withSuffixedPath('_' + crossbow.getAllProjectile().get(projectile)).toString());
				jsonArray.add(predicate);
			}
			jsonObject.add("overrides", jsonArray);
			return jsonObject;
		});
		for (int count = 0; count < pullStage.length; count++) {
			String suffix = '_' + Crossbow.PULLING_KEY + '_' + count;
			new Model(Optional.of(ModelIds.getItemModelId(crossbow)), Optional.empty(), TextureKey.LAYER0)
					.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), itemModelGenerator.writer);
		}
		for (float projectile : crossbow.getAllProjectile().keySet()) {
			String suffix = '_' + crossbow.getAllProjectile().get(projectile);
			new Model(Optional.of(ModelIds.getItemModelId(crossbow)), Optional.empty(), TextureKey.LAYER0)
					.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), itemModelGenerator.writer);
		}
	}
	
	@SilkApi
	static void registerTopSoil(BlockStateModelGenerator blockStateModelGenerator, Block soil) {
		Identifier identifier = TextureMap.getId(Blocks.DIRT);
		BlockStateVariant blockStateVariant = BlockStateVariant.create().put(VariantSettings.MODEL, Models.CUBE_BOTTOM_TOP.upload(Blocks.GRASS_BLOCK, "_snow", new TextureMap()
				.put(TextureKey.BOTTOM, identifier).inherit(TextureKey.BOTTOM, TextureKey.PARTICLE)
				.put(TextureKey.TOP, TextureMap.getSubId(Blocks.GRASS_BLOCK, "_top"))
				.put(TextureKey.SIDE, TextureMap.getSubId(Blocks.GRASS_BLOCK, "_snow")), blockStateModelGenerator.modelCollector));
		Identifier modelId = TexturedModel.CUBE_BOTTOM_TOP.get(soil)
				.textures(textures -> textures.put(TextureKey.BOTTOM, identifier))
				.upload(soil, blockStateModelGenerator.modelCollector);
		blockStateModelGenerator.registerTopSoil(soil, modelId, blockStateVariant);
	}
	
	@SilkApi
	static void registerCarpet(BlockStateModelGenerator blockStateModelGenerator, Block carpet, boolean isFlat) {
		if (isFlat) blockStateModelGenerator.registerItemModel(carpet);
		blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator
				.createSingletonBlockState(carpet, TexturedModel.CARPET.get(carpet)
						.upload(carpet, blockStateModelGenerator.modelCollector)));
	}
	
	@SilkApi
	static void registerVines(BlockStateModelGenerator blockStateModelGenerator, Block plant, Block plantStem, BlockStateModelGenerator.TintType tintType) {
		blockStateModelGenerator.registerPlantPart(plant, plantStem, tintType);
		blockStateModelGenerator.registerItemModel(plant, "_plant");
		blockStateModelGenerator.excludeFromSimpleItemModelGeneration(plantStem);
	}
}
