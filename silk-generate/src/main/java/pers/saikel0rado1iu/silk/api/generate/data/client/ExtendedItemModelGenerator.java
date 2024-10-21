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

package pers.saikel0rado1iu.silk.api.generate.data.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import pers.saikel0rado1iu.silk.api.generate.data.ModelGenUtil;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.ropestick.ItemGroupCreator;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.BowLikeItem;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.Crossbow;
import pers.saikel0rado1iu.silk.impl.SilkApi;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static pers.saikel0rado1iu.silk.api.generate.data.client.Models.STEREOSCOPIC_ITEM;

/**
 * <h2 style="color:FFC800">扩展物品模型生成器</h2>
 * 提供了其他可能用到的高级物品模型方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class ExtendedItemModelGenerator extends ItemModelGenerator {
	/**
	 * @param itemModelGenerator 物品模型生成器
	 */
	public ExtendedItemModelGenerator(ItemModelGenerator itemModelGenerator) {
		this(itemModelGenerator.writer);
	}
	
	/**
	 * @param writer 写入器
	 */
	public ExtendedItemModelGenerator(BiConsumer<Identifier, Supplier<JsonElement>> writer) {
		super(writer);
	}
	
	/**
	 * 注册物品组图标模型<br>
	 * 如果使用了 {@link ItemGroupCreator#create(ModPass, String)} 创建了一个专门的物品组图标则使用此方法生成物品模型
	 *
	 * @param itemGroup 物品组
	 */
	public void registerItemGroup(RegistryKey<ItemGroup> itemGroup) {
		registerModel(new Identifier(itemGroup.getValue().getNamespace(), ItemGroupCreator.getIconId(itemGroup.getValue().getPath())).withPrefixedPath("item/"), Models.GENERATED);
	}
	
	/**
	 * 通过 ID 注册模型
	 *
	 * @param id    ID
	 * @param model 模型
	 */
	public void registerModel(Identifier id, Model model) {
		model.upload(id, TextureMap.layer0(id), writer);
	}
	
	/**
	 * 注册方块物品模型
	 *
	 * @param blockItem 方块物品
	 */
	public void registerBlockItem(BlockItem blockItem) {
		Block block = blockItem.getBlock();
		register(blockItem, new Model(Optional.of(new Identifier(Registries.BLOCK.getId(block).getNamespace(), "block/" + Registries.BLOCK.getId(block).getPath())), Optional.empty()));
	}
	
	/**
	 * 注册叠加层物品模型
	 *
	 * @param item 物品
	 */
	public void registerOverlayItem(Item item) {
		Models.GENERATED_TWO_LAYERS.upload(ModelIds.getItemModelId(item), TextureMap.layered(TextureMap.getId(item), TextureMap.getSubId(item, "_overlay")), writer);
	}
	
	/**
	 * 注册生成蛋物品模型
	 *
	 * @param spawnEggItem 物品
	 */
	public void registerSpawnEggItem(SpawnEggItem spawnEggItem) {
		register(spawnEggItem, ModelGenUtil.TEMPLATE_SPAWN_EGG);
	}
	
	/**
	 * 注册立体物品模型
	 *
	 * @param item             物品
	 * @param textureSuffixMap 纹理键名称后缀图表
	 */
	public void registerStereoscopicItem(Item item, Map<TextureKey, String> textureSuffixMap) {
		TextureMap textureMap = new TextureMap();
		for (TextureKey textureKey : TextureKeys.STEREOSCOPIC_ITEM_TEXTURE_KEYS) {
			textureMap.put(textureKey, textureSuffixMap.containsKey(textureKey)
					? TextureMap.getSubId(item, textureSuffixMap.get(textureKey))
					: SilkApi.getInternal().ofId("item/none"));
		}
		STEREOSCOPIC_ITEM.upload(ModelIds.getItemModelId(item), textureMap, writer);
	}
	
	/**
	 * 注册弓模型
	 *
	 * @param bow       弓
	 * @param pullStage 拉弓状态
	 */
	public void registerBow(BowLikeItem bow, float[] pullStage) {
		JsonObject display = new JsonObject();
		display.add(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(-80, 260, -40), new Vec3d(-1, -2, 2.5), new Vec3d(0.9, 0.9, 0.9)));
		display.add(ModelTransformationMode.THIRD_PERSON_LEFT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(-80, -280, 40), new Vec3d(-1, -2, 2.5), new Vec3d(0.9, 0.9, 0.9)));
		display.add(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(0, -90, 25), new Vec3d(1.13, 3.2, 1.13), new Vec3d(0.68, 0.68, 0.68)));
		display.add(ModelTransformationMode.FIRST_PERSON_LEFT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(0, 90, -25), new Vec3d(1.13, 3.2, 1.13), new Vec3d(0.68, 0.68, 0.68)));
		registerBow(bow, display, pullStage);
	}
	
	/**
	 * 注册弩模型
	 *
	 * @param crossbow  弩
	 * @param pullStage 拉弓状态
	 */
	public void registerCrossbow(Crossbow crossbow, float[] pullStage) {
		JsonObject display = new JsonObject();
		display.add(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(-90, 0, -60), new Vec3d(2, 0.1, -3), new Vec3d(0.9, 0.9, 0.9)));
		display.add(ModelTransformationMode.THIRD_PERSON_LEFT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(-90, 0, 30), new Vec3d(2, 0.1, -3), new Vec3d(0.9, 0.9, 0.9)));
		display.add(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(-90, 0, -55), new Vec3d(1.13, 3.2, 1.13), new Vec3d(0.68, 0.68, 0.68)));
		display.add(ModelTransformationMode.FIRST_PERSON_LEFT_HAND.asString(), ModelGenUtil.modelTransModeJson(new Vec3d(-90, 0, 35), new Vec3d(1.13, 3.2, 1.13), new Vec3d(0.68, 0.68, 0.68)));
		registerCrossbow(crossbow, display, pullStage);
	}
	
	/**
	 * 注册一个有自定义显示的弓
	 *
	 * @param bow       弓
	 * @param display   自定义显示
	 * @param pullStage 拉弓状态
	 */
	public void registerBow(BowLikeItem bow, JsonObject display, float[] pullStage) {
		Models.GENERATED.upload(ModelIds.getItemModelId(bow), TextureMap.layer0(bow), writer, (id, textures) -> {
			JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
			jsonObject.add("display", display);
			JsonArray jsonArray = new JsonArray();
			for (Item projectile : bow.launchableProjectiles()) {
				for (int count = 0; count < pullStage.length; count++) {
					JsonObject predicate = new JsonObject();
					JsonObject object = new JsonObject();
					float index = bow.getProjectileIndex(projectile);
					if (index != 0) object.addProperty(Crossbow.PROJECTILE_INDEX_KEY, index);
					object.addProperty(BowLikeItem.PULLING_KEY, 1);
					object.addProperty(BowLikeItem.PULL_KEY, pullStage[count]);
					predicate.add("predicate", object);
					predicate.addProperty("model", id.withSuffixedPath('_' + Registries.ITEM.getId(projectile).getPath() + '_' + BowLikeItem.PULLING_KEY + '_' + count).toString());
					jsonArray.add(predicate);
				}
			}
			jsonObject.add("overrides", jsonArray);
			return jsonObject;
		});
		for (Item projectile : bow.launchableProjectiles()) {
			for (int count = 0; count < pullStage.length; count++) {
				String suffix = '_' + Registries.ITEM.getId(projectile).getPath() + '_' + BowLikeItem.PULLING_KEY + '_' + count;
				new Model(Optional.of(ModelIds.getItemModelId(bow)), Optional.empty(), TextureKey.LAYER0)
						.upload(ModelIds.getItemSubModelId(bow, suffix), TextureMap.layer0(TextureMap.getSubId(bow, suffix)), writer);
			}
		}
	}
	
	/**
	 * 注册一个有自定义显示的弩
	 *
	 * @param crossbow  弩
	 * @param display   自定义显示
	 * @param pullStage 拉弓状态
	 */
	public void registerCrossbow(Crossbow crossbow, JsonObject display, float[] pullStage) {
		Models.GENERATED.upload(ModelIds.getItemModelId(crossbow), TextureMap.layer0(TextureMap.getSubId(crossbow, "_standby")), writer, (id, textures) -> {
			JsonObject jsonObject = Models.GENERATED.createJson(id, textures);
			jsonObject.add("display", display);
			JsonArray jsonArray = new JsonArray();
			for (Item projectile : crossbow.launchableProjectiles()) {
				for (int count = 0; count < pullStage.length; count++) {
					JsonObject predicate = new JsonObject();
					JsonObject object = new JsonObject();
					float index = crossbow.getProjectileIndex(projectile);
					if (index != 0) object.addProperty(Crossbow.PROJECTILE_INDEX_KEY, index);
					object.addProperty(BowLikeItem.PULLING_KEY, 1);
					object.addProperty(BowLikeItem.PULL_KEY, pullStage[count]);
					predicate.add("predicate", object);
					predicate.addProperty("model", id.withSuffixedPath('_' + Registries.ITEM.getId(projectile).getPath() + '_' + BowLikeItem.PULLING_KEY + '_' + count).toString());
					jsonArray.add(predicate);
				}
				JsonObject predicate = new JsonObject();
				JsonObject object = new JsonObject();
				object.addProperty(Crossbow.CHARGED_KEY.toLowerCase(), 1);
				float index = crossbow.getProjectileIndex(projectile);
				if (index != 0) object.addProperty(Crossbow.PROJECTILE_INDEX_KEY, index);
				predicate.add("predicate", object);
				predicate.addProperty("model", id.withSuffixedPath('_' + Registries.ITEM.getId(projectile).getPath()).toString());
				jsonArray.add(predicate);
			}
			jsonObject.add("overrides", jsonArray);
			return jsonObject;
		});
		for (Item projectile : crossbow.launchableProjectiles()) {
			for (int count = 0; count < pullStage.length; count++) {
				String suffix = '_' + Registries.ITEM.getId(projectile).getPath() + '_' + Crossbow.PULLING_KEY + '_' + count;
				new Model(Optional.of(ModelIds.getItemModelId(crossbow)), Optional.empty(), TextureKey.LAYER0)
						.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), writer);
			}
		}
		for (Item projectile : crossbow.launchableProjectiles()) {
			String suffix = '_' + Registries.ITEM.getId(projectile).getPath();
			new Model(Optional.of(ModelIds.getItemModelId(crossbow)), Optional.empty(), TextureKey.LAYER0)
					.upload(ModelIds.getItemSubModelId(crossbow, suffix), TextureMap.layer0(TextureMap.getSubId(crossbow, suffix)), writer);
		}
	}
}
