<img src="https://cdn.modrinth.com/data/cFGzaOGg/c3e4d8f30f741767471b922167f245d5c8243dcd.png" alt="Asteroid Logo" width=128>

# Asteroid
The utility client for Modrinth. Press `Right Shift` to open the menu in-game.

## About
Asteroid is a fork / distribution of the [Meteor Client](https://meteorclient.com/), a fabric based utility client mod. It has had most of Meteor's anarchy modules removed, in order to focus entirely on the utilities. The few modules remaining that could be considered cheats can only be used when the player is OP, or in singleplayer.

I have made this fork as I believe that the user experience of utility clients is rather nice, with their modular approach to UI and configuration. I have found that I often install Meteor client purely to use it's less anarchy-style features, in my own worlds, and I wished I could add the mod to my modpack here on Modrinth. It only made sense to fork it just to focus on those few features, and remove the combat / movement modules that are more anarchy focused, and less in-tune with Modrinth's style of mods.

### Credits
All credits for the mod itself go to the Meteor client team (MineGame, squidoodly and seasnail).

Make sure to give them a star on [Github](https://github.com/MeteorDevelopment/meteor-client), or even [Donate](https://meteorclient.com/donate) to them to help fund development.

Updated waypoint textures by Wulfian.

## Features
### Modules
The following is a list of the modules included in the mod. Most of these share functionality with other mods on the site, so I felt they were fine to leave in.

Modules marked with an asterisk (`*`) require OP or Singleplayer to use.

#### Player
- Auto Clicker: Automatic clicking on a timer. Useful for mob grinding.
- Auto Replenish: Replaces a tool / food on the hotbar from the inventory when depleted. Nice QoL.
- Auto Respawn: Automatically presses the respawn button on death.
- Auto Tool: Automatically switches to the correct tool when going to mine a block.
- Fake Player: Spawns a completely client side fake player. Provides no actual use cases apart from looking cool, or maybe getting a good screenshot.
- No Interact: Allows the player to blacklist certain blocks from accidentally being interacted with / broken. Useful for amethyst mining.
- No Mining Trace: Allows the player to break blocks even when an entity is blocking them.
- Portals: Allows players to access their inventory whilst sitting in a nether portal. Not sure why that isn't default tbh.
- Rotation: Allows the player to lock their yaw and/or pitch.

#### Render
- Better Tab: Customisable improvements to the tab menu.
- Better Tooltips: Shulker box previews, enderchest previews etc.
- Block ESP\*: Renders outlines around blocks in the whitelist. Helpful for locating a particular block type.
- Block Selection: Modifies block selection rendering.
- Blur: Blurs some menu backgrounds.
- Breadcrumbs: Draws a line behind the player letting them backtrace. Good for players who get lost easily.
- Break Indicators: Break indicator customization and improvements.
- Camera Tweaks\*: Customize the third person camera. Allows for clipping in to blocks.
- Free Look: Allows the player to position the third person camera, then move their head normally.
- Freecam\*: Frees the players camera to act like spectator mode.
- Fullbright: Disables lighting, similar to night vision.
- Hand View: Customize how items are rendered in the players hand.
- Item Highlight: Highlights particular items in GUIs, to allow you to find the item you are looking for easier.
- Light Overlay: Renders light levels as an overlay on blocks on the ground.
- Marker: Render shapes as a client side hologram. Useful for planning out builds.
- No Render: Prevent things from rendering, like nether fog, particles or even certain entities.
- Tracers\*: Render lines from the crosshair to the targeted entities. Useful for finding lost items.
- Trail: Renders a trail of particles behind the player. Useless but fun.
- Unfocused CPU: Limits FPS when Minecraft isn't focused.
- Waypoints: Allows you to create and save waypoints, to help with navigation.
- Xray\*: Disables rendering for certain blocks. Useful for finding a particular block.
- Zoom: Zooms your view similar to how a spyglass would.

#### Misc
- Ambience: Customize stuff like how the sky renders. Allows for cursed things like End sky in the Overworld. Purely cosmetic.
- Auto Reconnect: Automatically reconnects a user if they somehow lost connection to a server. Useful for AFK sessions on servers that restart at a set time.
- Better Chat: Improvements to chat, like prefixes, suffixes and filter regexes.
- Discord Presence: Customizable Discord Rich Presence for fun.
- Inventory Tweaks: Inventory improvements like auto transfer, chest sorting, and shift + drag to move items.
- Sound Blocker: Fine grained control over what sounds should not play. Arguably a good accessibility feature.

### Commands
The following is a list of commands Asteroid provides. These are all prefixed with a `.`.

- Dismount (`.dismount`): Dismounts the player from their current vehicle.
- Disconnect (`.disconnect`): Disconnects the player from the server.
- Drop (`.drop`): Drops items in their inventory.
- Enchant (`.enchant`): Enchants an item. Can only be used in Creative.
- Fake Player (`.fake-player`): Manage fake players.
- Friend (`.friend`): Manage friends.
- Help (`.help`): Displays a list of commands.
- NBT (`.nbt`): Modify the nbt of an item. Can only be used in Creative.
- Peek (`.peek`): Displays the contents of a held shulker box or other container item.
- Ender Chest (`.ec`): Displays the contents of their Ender Chest.
- Profiles (`.profiles`): Manage profiles.
- Reload (`.reload`): Reloads Asteroid.
- Reset (`.reset`): Allows certain configurations to be reset.
- Say (`.say`): Sends a message to the chat, allowing use of [StarScript](https://github.com/MeteorDevelopment/starscript/wiki) variables.
- Server (`.server`): Displays some info about the server.
- Toggle (`.toggle`): Toggle modules.
- Settings (`.settings`): Open settings page for modules.
- Save Map (`.sm`): Save the held map item as an image.
- Macro (`.macro`): Runs a given macro.
- Modules (`.modules`): Lists available modules.
- Binds (`.binds`): Lists bound modules.
- Give (`.give`): Gives the player an item. Can only be used in Creative.
- Bind (`.bind`): Binds the given module to the next pressed key.
- FOV (`.fov`): Adjusts the field of view.
- Rotation (`.rotation`): Adjusts the rotation of the player.
- Waypoint (`.wp`): Manage waypoints.
- Input (`.input`): Simulate a given input.

#### Macros
Asteroid allows players to create macros, which can chain these commands.
They can be used to automate some tasks, and can be bound to keyboard shortcuts.
