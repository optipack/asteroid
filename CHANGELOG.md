# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.4] - 2024-08-12
Speed mine was stuck on.

### Fixed
- Speed mine code completely removed from mod now.

## [0.1.3] - 2024-08-12
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
