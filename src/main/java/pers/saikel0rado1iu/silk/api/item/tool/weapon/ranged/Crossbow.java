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

package pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged;

import com.google.common.collect.Lists;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.criterion.SilkCriteria;
import pers.saikel0rado1iu.silk.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><b style="color:FFC800"><font size="+1">辅助弩的创建的数据直观和清晰</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class Crossbow extends CrossbowItem implements SilkCrossbowExtend {
	protected boolean charged = false;
	protected boolean loaded = false;
	
	public Crossbow(Settings settings) {
		super(settings);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#createArrow(World, LivingEntity, ItemStack, ItemStack)}
	 */
	@SuppressWarnings("JavadocReference")
	protected PersistentProjectileEntity createArrow(World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		// 创建箭实体
		ArrowItem arrowItem = (ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.ARROW);
		PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, arrow, entity);
		// 如果是玩家则可以触发暴击
		if (entity instanceof PlayerEntity) persistentProjectileEntity.setCritical(true);
		// 设置声音
		persistentProjectileEntity.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
		// 设置由弩射击
		persistentProjectileEntity.setShotFromCrossbow(true);
		// 设置“穿透”效果
		int piercingLevel = EnchantmentHelper.getLevel(Enchantments.PIERCING, crossbow);
		if (piercingLevel > 0) persistentProjectileEntity.setPierceLevel((byte) piercingLevel);
		
		return persistentProjectileEntity;
	}
	
	/**
	 * 重实现 {@link CrossbowItem#getSoundPitches(Random)}
	 */
	@SuppressWarnings("JavadocReference")
	protected float[] getSoundPitches(Random random) {
		boolean randomBool = random.nextBoolean();
		return new float[]{1, getSoundPitch(randomBool, random), getSoundPitch(!randomBool, random)};
	}
	
	/**
	 * 重实现 {@link CrossbowItem#getSoundPitch(boolean, Random)}
	 */
	@SuppressWarnings("JavadocReference")
	protected float getSoundPitch(boolean flag, Random random) {
		float pitch = flag ? 0.63F : 0.43F;
		return 1 / (random.nextFloat() * 0.5F + 1.8F) + pitch;
	}
	
	/**
	 * 重实现 {@link CrossbowItem#postShoot(World, LivingEntity, ItemStack)}
	 */
	@SuppressWarnings("JavadocReference")
	protected void postShootProjectile(World world, LivingEntity entity, ItemStack stack) {
		// 如果实体为玩家实体
		if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
			// 触发弩的射击
			if (!world.isClient) Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
		}
		// 清除所有弹药
		clearAllProjectile(stack);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#loadProjectiles(LivingEntity, ItemStack)}
	 */
	@SuppressWarnings("JavadocReference")
	protected boolean loadAllProjectile(LivingEntity shooter, ItemStack crossbow) {
		// 获取弹药数
		int projectilesNum = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, crossbow) == 0 ? 1 : 3;
		// 如果实体为玩家且在创造模式
		boolean isPlayerAndInCreative = shooter instanceof PlayerEntity player && player.getAbilities().creativeMode;
		// 获取弹药
		ItemStack projectile = shooter.getProjectileType(crossbow);
		ItemStack projectileCopy = projectile.copy();
		for (int count = 0; count < projectilesNum; ++count) {
			// 如果有多重射击则多重射击弹药复制主弹药
			if (count > 0) projectile = projectileCopy.copy();
			// 如果没有弹药且在创造模式
			if (projectile.isEmpty() && isPlayerAndInCreative) {
				projectile = new ItemStack(Items.ARROW);
				projectileCopy = projectile.copy();
			}
			// 如果无法装填弹药
			if (!loadProjectile(shooter, crossbow, projectile, count > 0, isPlayerAndInCreative)) return false;
		}
		return true;
	}
	
	/**
	 * 重实现 {@link CrossbowItem#loadProjectile(LivingEntity, ItemStack, ItemStack, boolean, boolean)}
	 */
	@SuppressWarnings("JavadocReference")
	protected boolean loadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative) {
		// 如果没弹药
		if (projectile.isEmpty()) return false;
		
		ItemStack projectileCopy;
		// 如果不在创造模式且不是多重射击则减少弹药
		if (!creative && !simulated) {
			projectileCopy = projectile.split(1);
			if (projectile.isEmpty() && shooter instanceof PlayerEntity player) {
				player.getInventory().removeOne(projectile);
			}
		} else projectileCopy = projectile.copy();
		// 设置弹药
		putProjectile(crossbow, projectileCopy);
		return true;
	}
	
	/**
	 * 重实现 {@link CrossbowItem#putProjectile(ItemStack, ItemStack)}
	 */
	@SuppressWarnings("JavadocReference")
	protected void putProjectile(ItemStack crossbow, ItemStack projectile) {
		NbtCompound nbtCompound = crossbow.getOrCreateNbt();
		NbtList nbtList = nbtCompound.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE) ? nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE) : new NbtList();
		NbtCompound nbtCompound2 = new NbtCompound();
		projectile.writeNbt(nbtCompound2);
		nbtList.add(nbtCompound2);
		nbtCompound.put(CHARGED_PROJECTILES_KEY, nbtList);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#getProjectiles(ItemStack)}
	 */
	@SuppressWarnings("JavadocReference")
	protected List<ItemStack> getAllProjectile(ItemStack crossbow) {
		NbtList nbtList;
		ArrayList<ItemStack> list = Lists.newArrayList();
		NbtCompound nbtCompound = crossbow.getNbt();
		if (nbtCompound != null && nbtCompound.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE) && (nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE)) != null) {
			for (int count = 0; count < nbtList.size(); ++count) {
				NbtCompound nbtCompound2 = nbtList.getCompound(count);
				list.add(ItemStack.fromNbt(nbtCompound2));
			}
		}
		return list;
	}
	
	/**
	 * 重实现 {@link CrossbowItem#clearProjectiles(ItemStack)}
	 */
	@SuppressWarnings("JavadocReference")
	protected void clearAllProjectile(ItemStack crossbow) {
		NbtCompound nbtCompound = crossbow.getNbt();
		if (nbtCompound != null) {
			NbtList nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE);
			nbtList.clear();
			nbtCompound.put(CHARGED_PROJECTILES_KEY, nbtList);
		}
	}
	
	@Override
	public boolean isHubStretch() {
		return false;
	}
	
	@Override
	public boolean canChangeFov(ItemStack stack) {
		return isCharged(stack);
	}
	
	@Override
	public boolean canChangeMovement(ItemStack stack) {
		return isCharged(stack);
	}
	
	@Override
	public int getEnchantability() {
		return 10;
	}
	
	/**
	 * <p>设置自定义伤害</p>
	 * 重实现 {@link CrossbowItem#shoot(World, LivingEntity, Hand, ItemStack, ItemStack, float, boolean, float, float, float)}
	 */
	@SuppressWarnings("JavadocReference")
	protected void shootProjectile(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
		// 在服务端操作
		if (world.isClient) return;
		ProjectileEntity projectileEntity;
		// 如果弹药是烟花火箭
		boolean projectileIsFireworkRocket = projectile.isOf(Items.FIREWORK_ROCKET);
		// 构建弹丸实体
		if (projectileIsFireworkRocket) {
			projectileEntity = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - (double) 0.15f, shooter.getZ(), true);
		} else {
			projectileEntity = createArrow(world, shooter, crossbow, projectile);
			// 设置基础伤害增加
			((PersistentProjectileEntity) projectileEntity).setDamage(((PersistentProjectileEntity) projectileEntity).getDamage() * getDamageMultiple());
			// 如果在创造模式或不是'多重射击'弹药则设置为仅创造模式可拾起
			if (creative || MathUtil.compareFloat(simulated, 0) != 0) {
				((PersistentProjectileEntity) projectileEntity).pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
			}
		}
		// 如果使用者是弩使用者
		if (shooter instanceof CrossbowUser crossbowUser) {
			// 直接由弩使用者发射
			crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectileEntity, simulated);
		} else {
			// 设置弹药速度
			Vec3d vec3d = shooter.getOppositeRotationVector(1);
			Quaternionf quaternionf = new Quaternionf().setAngleAxis(simulated * ((float) Math.PI / 180), vec3d.x, vec3d.y, vec3d.z);
			Vec3d vec3d2 = shooter.getRotationVec(1);
			Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
			projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
		}
		// 设置弩损伤
		crossbow.damage(projectileIsFireworkRocket ? 3 : 1, shooter, e -> e.sendToolBreakStatus(hand));
		// 生成弹药
		world.spawnEntity(projectileEntity);
		// 播放音效
		world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), getShootSound(), SoundCategory.PLAYERS, 1.0f, soundPitch);
		if (shooter instanceof ServerPlayerEntity serverPlayer) SilkCriteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, crossbow, projectileEntity, 1);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#shootAll(World, LivingEntity, Hand, ItemStack, float, float)}
	 */
	public void shootAllProjectile(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence) {
		// 获取弹药
		List<ItemStack> projectiles = getAllProjectile(stack);
		// 获取所有音高
		float[] soundPitches = getSoundPitches(entity.getRandom());
		
		// 发射所有弹药
		for (int count = 0; count < projectiles.size(); ++count) {
			ItemStack itemStack = projectiles.get(count);
			// 如果实体为玩家且在创造模式
			boolean isPlayerAndInCreative = entity instanceof PlayerEntity player && player.getAbilities().creativeMode;
			if (itemStack.isEmpty()) continue;
			// 设置“多重射击”的不同角度弹药发射
			switch (count) {
				case 0 -> shootProjectile(world, entity, hand, stack, itemStack, soundPitches[count], isPlayerAndInCreative, speed, divergence, 0.0f);
				case 1 -> shootProjectile(world, entity, hand, stack, itemStack, soundPitches[count], isPlayerAndInCreative, speed, divergence, -10.0f);
				case 2 -> shootProjectile(world, entity, hand, stack, itemStack, soundPitches[count], isPlayerAndInCreative, speed, divergence, 10.0f);
			}
		}
		// 射击后操作
		postShootProjectile(world, entity, stack);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#onStoppedUsing(ItemStack, World, LivingEntity, int)}
	 */
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		// 获取已使用游戏刻
		int usedTicks = getMaxUseTime(stack) - remainingUseTicks;
		// 获取张弩进度
		float pullProgress = getUsingProgress(usedTicks, stack);
		// 如果张弩进度 ≥ 1 且未装填并装填所有弹药
		if (pullProgress >= 1.0f && !isCharged(stack) && loadAllProjectile(user, stack)) {
			// 设置已装填
			setCharged(stack, true);
			// 获取声音类别
			SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
			// 播放弩装填结束音效
			world.playSound(null, user.getX(), user.getY(), user.getZ(), getLoadingEndSound(), soundCategory, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
		}
	}
	
	/**
	 * <p>设置自定义的弹药速度与射击误差</p>
	 * 重实现 {@link CrossbowItem#use(World, PlayerEntity, Hand)}
	 */
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		// 如果已装填
		if (isCharged(stack)) {
			// 发射所有
			shootAllProjectile(world, user, hand, stack, getMaxProjectileSpeed(stack), getFiringError());
			// 设置未装填
			setCharged(stack, false);
			return TypedActionResult.consume(stack);
		}
		// 如果使用者有弹药
		if (!user.getProjectileType(stack).isEmpty()) {
			// 但未装填
			if (!isCharged(stack)) {
				charged = false;
				loaded = false;
				user.setCurrentHand(hand);
			}
			return TypedActionResult.consume(stack);
		}
		// 如果未装填
		return TypedActionResult.fail(stack);
	}
	
	/**
	 * <p>因私有字段问题</p>
	 * 重实现 {@link CrossbowItem#usageTick(World, LivingEntity, ItemStack, int)}
	 */
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (!world.isClient) {
			// 设置“快速装填”音效
			int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
			SoundEvent soundEvent = getQuickChargeSound(quickChargeLevel);
			SoundEvent soundEvent2 = quickChargeLevel == 0 ? getLoadingSound() : null;
			// 获取张弩进度
			float pullProgress = (float) (stack.getMaxUseTime() - remainingUseTicks) / getMaxPullTicks(stack);
			if (pullProgress < 0.2F) {
				charged = false;
				loaded = false;
			} else if (pullProgress >= 0.2F && !charged) {
				charged = true;
				if (soundEvent != null) world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
			} else if (pullProgress >= 0.5F && soundEvent2 != null && !loaded) {
				loaded = true;
				world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
		}
	}
	
	/**
	 * <p>因私有字段问题</p>
	 * 重实现 {@link CrossbowItem#getQuickChargeSound(int)}
	 */
	@SuppressWarnings("JavadocReference")
	protected SoundEvent getQuickChargeSound(int stage) {
		return switch (stage) {
			case 1 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
			case 2 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
			case 3 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
			default -> SoundEvents.ITEM_CROSSBOW_LOADING_START;
		};
	}
	
	/**
	 * 获取装填中音效
	 */
	protected SoundEvent getLoadingSound() {
		return SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE;
	}
	
	/**
	 * 获取装填结束音效
	 */
	protected SoundEvent getLoadingEndSound() {
		return SoundEvents.ITEM_CROSSBOW_LOADING_END;
	}
	
	/**
	 * 获取弩发射音效
	 */
	protected SoundEvent getShootSound() {
		return SoundEvents.ITEM_CROSSBOW_SHOOT;
	}
	
	/**
	 * 获取最大使用时间
	 */
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return getMaxPullTicks(stack);
	}
}
