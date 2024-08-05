# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0] - 2024-08-06
Rebased to Meteor Client v0.5.8, Dev Build #2143

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
