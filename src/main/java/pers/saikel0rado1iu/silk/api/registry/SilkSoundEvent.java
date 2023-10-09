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

package pers.saikel0rado1iu.silk.api.registry;

import com.google.common.collect.Sets;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.Set;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有声音事件组成声音事件集与声音事件注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#soundEvents()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkSoundEvent {
	public static final Set<SoundEvent> ALL_MOD_SOUNDS = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static <S extends SoundEvent> Builder<S> builder(S soundEvent) {
		return new Builder<>(soundEvent);
	}
	
	@SilkApi
	public static final class Builder<S extends SoundEvent> {
		private final S soundEvent;
		
		@SilkApi
		private Builder(S soundEvent) {
			ALL_MOD_SOUNDS.add(this.soundEvent = soundEvent);
		}
		
		@SilkApi
		public Builder<S> put(Set<SoundEvent> soundEvents) {
			soundEvents.add(soundEvent);
			return this;
		}
		
		@SilkApi
		public S build() {
			Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent);
			return soundEvent;
		}
	}
}
