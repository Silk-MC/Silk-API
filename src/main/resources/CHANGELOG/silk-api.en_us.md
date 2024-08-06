## 1.0.0-beta.3---2024/08/06

### Features:

- Logic
	- Reconstructed the API architecture to make the API more advanced and robust, while fixing multiple known bugs.

## 0.3.1---2024/03/04

### Fixes:

- Logic
	- Fixed an issue in ChunkStorageData where getBiome() returning null would cause the game to crash.
- Client
	- Fixed an issue in versions 1.20 to 1.20.1 where screens related to world upgrades were lacking backgrounds.
	- Fixed an issue where world loading requiring an upgrade would crash in versions 1.20 to 1.20.1.
	- Fixed an issue where all world generators in a mod shared the same name.
	- Fixed an issue where world upgrades would occasionally use other world generator upgrade methods.

## 0.3.0---2024/02/25

### Features：

- Refactored the code into a multi-project structure similar to Fabric API, greatly facilitating development and adaptation to new versions.
  It still maintains support for the old version of the code.
- As Minecraft is about to welcome version 1.20.5, version 1.20.2 will cease to receive updates.

## 0.2.1---2024/01/27

### Fixes：

- Logic
	- Fixed the problem caused by the duplicate name of getCodec() in UpgradeChunkGenerator.
- Client
	- Fixed the remap crash caused by the getSession() mixin in UpgradeWarningScreenMixin$ShowScreen failing on the client side.

## 0.2.0---2024/01/24

### Features：

- Logic
	- Added a 'World Upgrade System' that allows registering a world upgrader to allow players to upgrade the world.
	- Added chunk generator codec register. (ChunkGeneratorCodecRegistry)
	- Added customizable chunk generator interface. (CustomChunkGenerator)
	- Added modifiable vanilla noise chunk generator. (SilkNoiseChunkGenerator)

### Change：

- Logic
	- Added the methods of SilkVanillaBiomeParameters to make them easier to use.

- Client
	- Fixed the button display problem of WorldPresetCustomButtonCallbackMixin, now the world preset's custom button can correctly override the default button.

## 0.1.3---2023/12/16

### Features：

- Logic
	- Added damage type data generator.

### Change：

- Logic
	- Modified the methods of ModDataGeneration and SilkWorldGenerator to make them easier to understand.
- Client
	- Mod logs use Markdown text format, with added title and list format support. It is recommended to only use title and list formats to write logs.

v0.1.2 2023/12/09

## 0.1.2 Bug Fixes

- Client
	- Once again, fixed the problem of mod log reading being overwritten by API logs, and modified the log path and log template.

v0.1.1 2023/12/08

## 0.1.1 Bug Fixes

- Logic
	- Fixed an issue where vanilla Minecraft items could only be enchanted with 'Unbreaking'
- Client
	- Fixed an issue where mod log reading was overwritten by api logs

v1.20.2-0.1.0 2023/12/01

1. 'Silk API' is officially released!