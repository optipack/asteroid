# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Changes to Meteor are not documented here, only Asteroid specific changes.

## [0.4.0] - 2026-05-21
Rebased to Meteor Client 26.1.2, Build #21

### Added
#### New Modules
- `Better Farming`: Improvements to manual crop farming.
  - Automatically replant harvested crops.
  - Suppress player crop trampling.
    - Note: this could possibly trigger certain anti-cheats, use with caution.
  - Prevent breaking unripe crops.
  - Prevent breaking the base block of sugarcane / bamboo.
- `Weather Changer`: Change the weather client side. Brought over from Meteor.

### Fixed
- Shulker box tooltip no longer uses a deprecated translation string with Better Tooltips enabled.

## [0.4.0-alpha] - 2026-04-22
Rebased to Big-Iron-Cheems/26.1-update, Commit f3f8d10

This is a alpha release of Asteroid, based off an in-development fork of Meteor.
Please report any bugs / issues you encounter.

### Changed
- Minecraft is now at 26.1.2

## [0.3.4] - 2025-12-15
Rebased to Meteor Client 1.21.11, Build #4

### Added
#### New Modules
- `Auto Sign`: Automatically writes signs, using the text of the first placed sign. Useful for mob farms, where many signs have to be placed.

### Changed
- Minecraft is now at 1.21.11

## [0.3.3] - 2025-10-21
Rebased to Meteor Client 1.21.10, Build #7

### Added
#### New `.item` command
- Manages item lists in various modules.
- Meant to make these features quicker / easier to use.
```
.item highlight              | Adds currently held item to Item Highlight whitelist
.item highlight <item>       | Adds <item> to Item Highlight whitelist
.item highlight clear        | Clears currently held item from Item Highlight whitelist
.item highlight clear <item> | Clears <item> from Item Highlight whitelist
.item highlight clear *      | Clears all items from Item Highlight whitelist

.item block                  | Adds currently held item to auto drop list in Inventory Tweaks
.item block <item>           | Adds <item> to auto drop list in Inventory Tweaks
.item block clear            | Clears currently held item from auto drop list in Inventory Tweaks
.item block clear <item>     | Clears <item> from auto drop list in Inventory Tweaks
.item block clear *          | Clears all items from auto drop list in Inventory Tweaks

.item lock                   | Adds currently held item to anti drop list in Inventory Tweaks
.item lock <item>            | Adds <item> to anti drop list in Inventory Tweaks
.item lock clear             | Clears currently held item from anti drop list in Inventory Tweaks
.item lock clear <item>      | Clears <item> from anti drop list in Inventory Tweaks
.item lock clear *           | Clears all items from anti drop list in Inventory Tweaks
```

### Changed
- Minecraft is now at 1.21.10
- Waypoints can now be configured to delete themselves, alongside hiding themselves when the player is near enough.
  - Death waypoints are now set to auto delete, instead of auto hide.
- Hud is now shown behind the click gui once again.
- Middle Click Extras is now disabled in Creative mode by default.
  - This can is toggled with a setting in the module.

## [0.3.2] - 2025-08-31
Rebased to Meteor Client 1.21.8, Build #31.

### Fixed
- Automatic death waypoints no longer immediately hide themselves on creation.

## [0.3.1] - 2025-08-31
Rebased to Meteor Client 1.21.8, Build #26.

### Added
- Configuration in waypoints to allow them to auto-hide when the player gets in range.
  - Automatic death waypoints now auto-hide when the player gets within 4 blocks.

### Changed
- Updated some formatting in the changelog.
- Module tooltips now contain their hyphenated id.
- A 1 tick attack in `.input` will now perform a punch rather than a dig.
#### `.macro` Command Overhaul
In the past, calling a macro from itself would cause a game crash.
To mitigate this, the `.macro` command now schedules macros to be run in the next tick.

As such, the syntax of the command has been overhauled:
```
.macro <name>        | Schedules the given macro
.macro <name> <time> | Schedules the given macro after <time> duration
.macro clear         | Clears all scheduled macros
.macro clear <name>  | Clears the given macro
```

## [0.3.0] - 2025-07-18
Rebased to Meteor Client 1.21.8, Build #5.

Minor version bumped as a few new modules have been brought over from Meteor.

### Added
- `MeteorClient.BYPASS_CHEATS` boolean flag.
  - When set to `true`, cheat modules can be used in Multiplayer without OP.
  - The version of Asteroid uploaded to Modrinth will always have this set to `false`.
#### New Modules
- `Entity Owner`: Displays the owner of tamed animals above them.
- `Item Physics`: Items render as if they are laying on the ground, rather than floating.
- `Middle Click Extra`: Allows a couple quick actions to be bound to middle click.
- `Name Protect`: Basically Streamer Mode, hides your username, and optionally your skin.
- `Nametags`: Renders customisable nametags on select entities.

### Changed
- Minecraft version is now at 1.21.8.
- Modules that are cheats are now labelled as such in their description.

## [0.2.7] - 2025-06-22
Rebased to Meteor Client 1.21.6, Build #1.

Currently Meteor has a render order issue with certain screens. To mitigate this, I have disabled the in-game HUD from
rendering whilst the Asteroid menu is open.

### Added
- One-shot module type
  - Modules of this type do not stay active, instead running once on click.
  - This is intended for modules, such as `Fake Player`, that do nothing when active.
  - One-shot modules show as blue by default, and have a cog icon next to their name in the menu.

### Changed
- Minecraft version is now at 1.21.6.
- In-game HUD no longer renders behind Asteroid menu
- Waypoints module now defaults to saving the last 5 deaths, and logging them in chat.
- `Fake Player` is now a one-shot type module.

## [0.2.6] - 2025-05-31
Rebased to Meteor Client 1.21.5, Build #28.

This should allow Asteroid to work with the mod "Hold My Items". Refer to Meteor Client for additional fixes.

### Changed
- Asteroid should now work with most Meteor Client addons, granted they do not rely on modules removed by Asteroid.

## [0.2.5] - 2025-04-24
Rebased to Meteor Client 1.21.5, Build #12.

### Changed
- Minecraft version is now at 1.21.5.

### Fixed
- Microsoft account login doesn't work in the current Meteor Client, but it has been patched here.
  - Note: Player head preview images are incorrect due to the nature of this patch.

## [0.2.4] - 2025-02-03
Rebased to Meteor Client 1.21.4, Build #22.

Please note that Meteor Client has changed their versioning scheme. Asteroid will continue with its own for now.

### Changed
- Minecraft version is now at 1.21.4.
- Improved metadata for Mod Menu.

## [0.2.3] - 2024-12-03
Rebased to Meteor Client v0.5.9, Dev Build #2209.

This fixes an issue with `.enchant`.

### Changed
- Minecraft version now at 1.21.3.

### Removed
- Baritone support was stripped, as maintaining it was a burden.
  - This was part of a larger refactor that stripped a lot of unused systems.

## [0.2.2] - 2024-10-03
Rebased to Meteor Client v0.5.8, Dev Build #2174.

This fixes Indium incompatibility.

### Added
- Documentation:
  - Blacklist system now has a dedicated page in the documentation.
  - Macros now have their own page as well.

### Changed
- Macros no longer require a keybind to be saved.

## [0.2.1] - 2024-08-12
Rebased to Meteor Client v0.5.8, Dev Build #2149.

Minor improvements to Modules system for use with Addons.

### Changed
- Minecraft version now at 1.21.1.
- Implemented some helpful methods for working with Modules in Addons.

## [0.2.0] - 2024-08-06
Rebased to Meteor Client v0.5.8, Dev Build #2143.

Updates to UI, various improvements.

### Added
- Account Manager from Meteor has been added back.
  - It can be disabled again in the Config tab, if using AuthMe etc.
  - It lacks Altening / EasyMC account options, unlike its Meteor counterpart.
- A bunch of new waypoint icons, courtesy of Wulfian.
- Configuration option, in the GUI tab, to change the font scale of the titles of widgets.
- Work-in-progress documentation, available at [asteroid.moxvallix.com](https://asteroid.moxvallix.com).
- Module blacklist system:
  - **This system is experimental, and the format could change at any future release**.
  - Modules can be blacklisted by creating a file called `blacklist.txt`, in the Asteroid config directory.
  - Modules can be specified as blacklisted by writing their name, in kebab case, to a line in the file.
  - If a module name is immediately followed by `*`, it is instead marked as a cheat.
  - Lines beginning with a `#` are treated as a comment and ignored.

### Changed
- The Modules menu is now centered in the screen, to cater for having less categories than Meteor.
- Updated default colours and theme configuration to be distinct from Meteor:
  - Now has a new default colour (#C42E00).
  - GUI now uses default Minecraft font, rather than the custom Meteor font, by default.
- Waypoint icons have been refreshed:
  - Waypoints now use pixel art style icons, to better fit the new Asteroid theme.
  - The icon refresh is courtesy of the aforementioned Wulfian.

### Removed
- Some configuration options that no longer worked.

### Fixed
- Module search no longer shows disabled modules.
- XCarry is no longer accidentally constantly on.

## [0.1.4] - 2024-07-12
Speed mine was stuck on.

### Fixed
- Speed mine code completely removed from mod now.

## [0.1.3] - 2024-07-12
Multiplayer broke.

### Fixed
- Fixed multiplayer.

## [0.1.2] - 2024-07-12
Major refactor.

### Changed
- Zoom module now defaults to the keybind `C`, and toggles when the keybind is released.

### Removed
- Most of the code for the disabled modules has been removed from the codebase. This was done to make dependencies in other modules easier to find.

### Fixed
- Modules AutoReplenish and BreakIndicator should no longer crash.

## [0.1.1] - 2024-07-08
Rebased to Meteor Client v0.5.8, Dev Build #2125

### Added
- CHANGELOG.md to keep track of changes.

### Changed
- Minecraft version now at 1.21.
- README.md
  - Now updated to match Modrinth description.
  - Added documentation for dot-commands and macros.
- Default sort keybind for Inventory Tweaks module is now period (`.`).

### Removed
- Removed Meteor's cape feature.

## [0.1.0] - 2024-06-19
Initial release of Asteroid, forked from Meteor Client v0.5.7, Dev Build #2098

### Changed
- Some modules now require Singleplayer or OP to function.

### Removed
- Removed most anarchy related modules and commands.
