# Blacklisting Modules

!!! example "Experimental System!"
    The current blacklist format is experimental, and could change at any time.

Asteroid provides configuration for disabling modules. This is intended for users that play on stricter servers,
or for mod pack developers that wish to disable modules that conflict with other mods.

The configuration file is located at `.minecraft/config/asteroid/blacklist.txt`.
If the file doesn't already exist, you will need to make it yourself.

## Format
Modules to be blacklisted are listed in this file, in plain text, one module per line.

Modules should be specified in kebab case. For example, `Break Indicators` becomes `break-indicators`.

A module can optionally be marked as a [cheat](../faq.md#cheats).
To mark a module as a cheat, an asterisk `*` should be added to the end of the module name.

Lines starting with a hash `#` are ignored as a comment line.

### Example
```title=".minecraft/config/asteroid/blacklist.txt"
# This is a comment

# These modules are blacklisted
auto-clicker
blur
fake-player

# These modules are cheats, because they have a `*`
marker*
rotation*
```
