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

import net.minecraft.advancement.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.modpass.ModPass;

/**
 * <h2 style="color:FFC800">进度生成实用工具</h2>
 * 用于构建属于自己模组的进度，而不是依附在 minecraft 命名空间下
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface AdvancementGenUtil {
	/**
	 * 进度标题方法
	 *
	 * @param modPass 模组通
	 * @param id      进度 id
	 * @return 标题文本
	 */
	static Text title(ModPass modPass, String id) {
		return Text.translatable(String.format("advancements.%s.%s.title", modPass.modData().id(), id));
	}
	
	/**
	 * 进度描述方法
	 *
	 * @param modPass 模组通
	 * @param id      进度 id
	 * @return 描述文本
	 */
	static Text desc(ModPass modPass, String id) {
		return Text.translatable(String.format("advancements.%s.%s.description", modPass.modData().id(), id));
	}
	
	/**
	 * 构建器方法
	 *
	 * @param modPass 模组通
	 * @param path    进度路径
	 * @param id      进度 ID
	 * @return 模组进度构建器
	 */
	static Builder builder(ModPass modPass, String path, String id) {
		Builder builder = new Builder(modPass, path, id);
		builder.sendsTelemetryEvent();
		return builder;
	}
	
	/**
	 * 构建器方法
	 *
	 * @param modPass 模组通
	 * @param id      进度 ID
	 * @return 模组进度构建器
	 */
	static Builder builder(ModPass modPass, String id) {
		Builder builder = new Builder(modPass, id);
		builder.sendsTelemetryEvent();
		return builder;
	}
	
	/**
	 * 模组进度构建器
	 */
	class Builder extends Advancement.Builder {
		private final ModPass modPass;
		private final String path;
		private final String id;
		
		private Builder(ModPass modPass, String path, String id) {
			super();
			this.modPass = modPass;
			this.path = path;
			this.id = id;
		}
		
		private Builder(ModPass modPass, String id) {
			this(modPass, "", id);
		}
		
		/**
		 * 显示方法
		 *
		 * @param icon           物品堆栈图标
		 * @param background     背景
		 * @param frame          进度框架
		 * @param showToast      显示提示窗
		 * @param announceToChat 显示聊天通知
		 * @param hidden         隐藏进度
		 * @return 构建器
		 */
		public Builder display(ItemStack icon, @Nullable Identifier background, AdvancementFrame frame, boolean showToast, boolean announceToChat, boolean hidden) {
			this.display(icon.getItem(), title(modPass, id), desc(modPass, id), background, frame, showToast, announceToChat, hidden);
			return this;
		}
		
		/**
		 * 显示方法
		 *
		 * @param icon           物品图标
		 * @param background     背景
		 * @param frame          进度框架
		 * @param showToast      显示提示窗
		 * @param announceToChat 显示聊天通知
		 * @param hidden         隐藏进度
		 * @return 构建器
		 */
		public Builder display(ItemConvertible icon, @Nullable Identifier background, AdvancementFrame frame, boolean showToast, boolean announceToChat, boolean hidden) {
			return display(icon.asItem().getDefaultStack(), background, frame, showToast, announceToChat, hidden);
		}
		
		@Override
		public Builder parent(AdvancementEntry parent) {
			super.parent(parent);
			return this;
		}
		
		@Override
		public Builder criterion(String name, AdvancementCriterion<?> criterion) {
			super.criterion(name, criterion);
			return this;
		}
		
		@Override
		public Builder criteriaMerger(AdvancementRequirements.CriterionMerger merger) {
			super.criteriaMerger(merger);
			return this;
		}
		
		@Override
		public Builder requirements(AdvancementRequirements requirements) {
			super.requirements(requirements);
			return this;
		}
		
		@Override
		public Builder rewards(AdvancementRewards.Builder builder) {
			super.rewards(builder);
			return this;
		}
		
		@Override
		public Builder rewards(AdvancementRewards rewards) {
			super.rewards(rewards);
			return this;
		}
		
		/**
		 * 构建进度条目
		 *
		 * @return 进度条目
		 */
		public AdvancementEntry build() {
			return build(modPass.modData().ofId(path.isEmpty() ? "" : path + '/' + id));
		}
	}
}
