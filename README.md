
<p align="center">
<img src="https://raw.githubusercontent.com/optipack/asteroid/main/src/main/resources/assets/meteor-client/textures/meteor.png" alt="asteroid-client-logo" width="15%"/>
</p>

<h1 align="center">Asteroid</h1>
<p align="center">The utility client for Modrinth.</p>

<div align="center">
    <img src="https://img.shields.io/github/last-commit/optipack/asteroid" alt="GitHub last commit"/>
    <img src="https://img.shields.io/github/commit-activity/w/optipack/asteroid" alt="GitHub commit activity"/>
    <img src="https://img.shields.io/github/contributors/optipack/asteroid" alt="GitHub contributors"/>
    <br>
    <img src="https://img.shields.io/github/languages/code-size/optipack/asteroid" alt="GitHub code size in bytes"/>
    <img src="https://tokei.rs/b1/github/optipack/asteroid" alt="GitHub lines of code"/>
</div>

## About
Asteroid is a fork / distribution of the [Meteor Client](https://meteorclient.com/), a fabric based utility client mod. It has had most of Meteor's anarchy modules removed, in order to focus entirely on the utilities. The few modules remaining that could be considered cheats can only be used when the player is OP, or in singleplayer.

I have made this fork as I believe that the user experience of utility clients is rather nice, with their modular approach to UI and configuration. I have found that I often install Meteor client purely to use it's less anarchy-style features, in my own worlds, and I wished I could add the mod to my modpack here on Modrinth. It only made sense to fork it just to focus on those few features, and remove the combat / movement modules that are more anarchy focused, and less in-tune with Modrinth's style of mods.

### Credits
All credits for the mod itself go to the Meteor client team (MineGame, squidoodly and seasnail).

Make sure to give them a star on [Github](https://github.com/MeteorDevelopment/meteor-client), or even [Donate](https://meteorclient.com/donate) to them to help fund development.

## Features
The following is a list of the modules included in the mod. Most of these share functionality with other mods on the site, so I felt they were fine to leave in.

Modules marked with an asterisk (`*`) require OP or Singleplayer to use.

### Player
- Auto Clicker: Automatic clicking on a timer. Useful for mob grinding.
- Auto Replenish: Replaces a tool / food on the hotbar from the inventory when depleted. Nice QoL.
- Auto Respawn: Automatically presses the respawn button on death.
- Auto Tool: Automatically switches to the correct tool when going to mine a block.
- Fake Player: Spawns a completely client side fake player. Provides no actual use cases apart from looking cool, or maybe getting a good screenshot.
- No Interact: Allows the player to blacklist certain blocks from accidentally being interacted with / broken. Useful for amethyst mining.
- No Mining Trace: Allows the player to break blocks even when an entity is blocking them.
- Portals: Allows players to access their inventory whilst sitting in a nether portal. Not sure why that isn't default tbh.
- Rotation: Allows the player to lock their yaw and/or pitch.

### Render
- Better Tab: Customisable improvements to the tab menu.
- Better Tooltips: Shulker box previews, enderchest previews etc.
- Block Selection: Modifies block selection rendering.
- Blur: Blurs some menu backgrounds.
- Breadcrumbs: Draws a line behind the player letting them backtrace. Good for players who get lost easily.
- Break Indicators: Break indicator customization and improvements.
- Camera Tweaks\*: Customize the third person camera. Allows for clipping in to blocks.
- Free Look: Allows the player to position the third person camera, then move their head normally.
- Freecam\*: Frees the players camera to act like spectator mode.
- Fullbright: Disables lighting, similar to night vision.
- Hand View: Customize how items are rendered in the players hand.
- Light Overlay: Renders light levels as an overlay on blocks on the ground.
- Marker: Render shapes as a client side hologram. Useful for planning out builds.
- No Render: Prevent things from rendering, like nether fog, particles or even certain entities.
- Tracers\*: Render lines from the crosshair to the targeted entities. Useful for finding lost items.
- Trail: Renders a trail of particles behind the player. Useless but fun.
- Unfocused CPU: Limits FPS when Minecraft isn't focused.
- Waypoints: Allows you to create and save waypoints, to help with navigation.
- Xray\*: Disables rendering for certain blocks. Useful for finding a particular block.
- Zoom: Zooms your view similar to how a spyglass would.

### Misc
- Ambience: Customize stuff like how the sky renders. Allows for cursed things like End sky in the Overworld. Purely cosmetic.
- Auto Reconnect: Automatically reconnects a user if they somehow lost connection to a server. Useful for AFK sessions on servers that restart at a set time.
- Better Chat: Improvements to chat, like prefixes, suffixes and filter regexes.
- Discord Presence: Customizable Discord Rich Presence for fun.
- Inventory Tweaks: Inventory improvements like auto transfer, chest sorting, and shift + drag to move items.
- Sound Blocker: Fine grained control over what sounds should not play. Arguably a good accessibility feature.

## Usage

### Building
- Clone this repository
- Run `./gradlew build`

### Installation
Follow the [guide](https://meteorclient.com/faq/installation) on the wiki.

## Contributions
We will review and help with all reasonable pull requests as long as the guidelines below are met.

- The license header must be applied to all java source code files.
- IDE or system-related files should be added to the `.gitignore`, never committed in pull requests.
- In general, check existing code to make sure your code matches relatively close to the code already in the project.
- Favour readability over compactness.
- If you need help, check out the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) for a reference.

## Bugs and Suggestions
Bug reports and suggestions should be made in this repo's [issue tracker](https://github.com/optipack/asteroid/issues) using the templates provided.  
Please provide as much information as you can to best help us understand your issue and give a better chance of it being resolved.

## Donations
All of our work is completely free and non-profit (donations pay only for hosting costs), therefore we are very grateful for all donations made to support us in running our community.  
Donations can be made via our [website](https://meteorclient.com/donate) and the minimum amount to get donor benefits is €5.  
You will be rewarded with a role on our Discord server and a customisable in-game cape.  
⚠️ _Make sure to create a Meteor account and link your Discord and Minecraft accounts to fully experience your rewards._ ⚠️

## Credits
[Cabaletta](https://github.com/cabaletta) and [WagYourTail](https://github.com/wagyourtail) for [Baritone](https://github.com/cabaletta/baritone)  
The [Fabric Team](https://github.com/FabricMC) for [Fabric](https://github.com/FabricMC/fabric-loader) and [Yarn](https://github.com/FabricMC/yarn)

## Licensing
This project is licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html). 

If you use **ANY** code from the source:
- You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source and/or obfuscated application.
- You must state clearly and obviously to all end users that you are using code from this project.
- Your application must also be licensed under the same license.

*If you have any other questions, check our [FAQ](https://meteorclient.com/faq) or ask in our [Discord](https://meteorclient.com/discord) server.*
