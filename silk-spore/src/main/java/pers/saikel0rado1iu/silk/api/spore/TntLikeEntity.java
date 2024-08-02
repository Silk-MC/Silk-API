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

package pers.saikel0rado1iu.silk.api.spore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">TNT 类实体</h2>
 * 用于创建类似于 TNT 或是扩展于 TNT 的实体
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.0
 */
public abstract class TntLikeEntity extends Entity implements Ownable {
	protected static final TrackedData<Integer> FUSE = DataTracker.registerData(TntLikeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<BlockState> BLOCK_STATE = DataTracker.registerData(TntLikeEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE);
	private Optional<LivingEntity> causingEntity;
	
	protected TntLikeEntity(EntityType<?> type, World world, Optional<LivingEntity> causingEntity) {
		super(type, world);
		this.causingEntity = causingEntity;
		this.intersectionChecked = true;
	}
	
	protected TntLikeEntity(EntityType<?> type, World world, double x, double y, double z, Optional<LivingEntity> igniter) {
		this(type, world, igniter);
		double d = world.random.nextDouble() * Math.PI * 2;
		setVelocity(-Math.sin(d) * 0.02, 0.2F, -Math.cos(d) * 0.02);
		setPosition(x, y, z);
		setFuse(fuseTick());
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}
	
	/**
	 * 爆炸方法<br>
	 * 修改此方法来自定义爆炸效果
	 */
	protected void explode() {
		getWorld().createExplosion(this, getX(), getBodyY(0.0625), getZ(), 4, World.ExplosionSourceType.TNT);
	}
	
	/**
	 * 修改此方法来自定义其他爆炸效果<br>
	 * 如果修改了此方法需要在此方法中重新使用 {@link TntLikeEntity#explode()}
	 */
	@Override
	public void tick() {
		if (!hasNoGravity()) setVelocity(getVelocity().add(0, -0.04, 0));
		move(MovementType.SELF, getVelocity());
		setVelocity(getVelocity().multiply(0.98));
		if (isOnGround()) setVelocity(getVelocity().multiply(0.7, -0.5, 0.7));
		int fuse = getFuse() - 1;
		setFuse(fuse);
		if (fuse <= 0) {
			discard();
			if (!getWorld().isClient) explode();
		} else {
			updateWaterState();
			if (getWorld().isClient) getWorld().addParticle(ParticleTypes.SMOKE, getX(), getY() + EntityUtil.POS_SHIFTING, getZ(), 0, 0, 0);
		}
	}
	
	@Override
	public boolean canHit() {
		return !isRemoved();
	}
	
	@Override
	protected Entity.MoveEffect getMoveEffect() {
		return Entity.MoveEffect.NONE;
	}
	
	@Override
	public void copyFrom(Entity original) {
		super.copyFrom(original);
		if (original instanceof TntLikeEntity tnt) causingEntity = tnt.causingEntity;
	}
	
	@Override
	protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.15F;
	}
	
	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(FUSE, fuseTick());
		dataTracker.startTracking(BLOCK_STATE, tntBlock().getDefaultState());
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		nbt.putShort(TntEntity.FUSE_NBT_KEY, (short) getFuse());
		nbt.put(TntEntity.BLOCK_STATE_NBT_KEY, NbtHelper.fromBlockState(getBlockState()));
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		setFuse(nbt.getShort(TntEntity.FUSE_NBT_KEY));
		if (!nbt.contains(TntEntity.BLOCK_STATE_NBT_KEY, NbtElement.COMPOUND_TYPE)) return;
		setBlockState(NbtHelper.toBlockState(getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound(TntEntity.BLOCK_STATE_NBT_KEY)));
	}
	
	@Nullable
	@Override
	public LivingEntity getOwner() {
		return causingEntity.orElse(null);
	}
	
	/**
	 * 获取引信
	 *
	 * @return 引信
	 */
	public int getFuse() {
		return this.dataTracker.get(FUSE);
	}
	
	/**
	 * 设置引信
	 *
	 * @param fuse 引信
	 */
	public void setFuse(int fuse) {
		this.dataTracker.set(FUSE, fuse);
	}
	
	/**
	 * 获取方块状态
	 *
	 * @return 方块状态
	 */
	public BlockState getBlockState() {
		return this.dataTracker.get(BLOCK_STATE);
	}
	
	/**
	 * 设置方块状态
	 *
	 * @param state 方块状态
	 */
	public void setBlockState(BlockState state) {
		this.dataTracker.set(BLOCK_STATE, state);
	}
	
	/**
	 * 引信刻数
	 *
	 * @return 引燃 TNT 所需的刻数
	 */
	public abstract int fuseTick();
	
	/**
	 * TNT 方块
	 *
	 * @return 此实体所依赖的 TNT 块
	 */
	public abstract Block tntBlock();
}
