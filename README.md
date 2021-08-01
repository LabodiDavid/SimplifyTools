# SimplifyTools  
### An 'All-in-one' helper plugin for Minecraft Spigot Servers.
|  Build Status |                    [![Build Status](https://ci.dit-services.tk/job/SimplifyTools/badge/icon)](https://ci.dit-services.tk/job/SimplifyTools/)                   |
|:-------------:|:-----------------------------------------------------------------------------------------------------------------:|
|   MC Version  | ![ MC Version](https://ci.dit-services.tk/job/SimplifyTools/badge/icon?subject=MC&status=1.16.5&color=darkblue) |
| Lines of code |               ![ Lines of code](https://tokei.rs/b1/github/LabodiDavid/SimplifyTools?category=code)               |
|     Files     |                   ![ Files](https://tokei.rs/b1/github/LabodiDavid/SimplifyTools?category=files)                  |
## Features

 - **Tab Manager** - Allows you to specify colored texts, with animations on the tab window.
		 Also you can display the average ping, Server RAM statistics.
 ![Tab manager preview](docs/img/1.gif)
 - **Automatic/command saving** - Allows you to schedule auto saving of the world/player data on your server, or you can initiate a save by a command.
 - **Custom Connect/Disconnect messages** - Allows you to customize the message that is broadcasted when someone joins or leaves the server.
  ![Connect messages preview](docs/img/2.gif)
 - **Plugin Manager** - Allows you to unload/load plugins without a server restart.
 (Note: This may be removed in 1.17.x versions for security reasons)
 - **Gameplay statistics** - You can check your gameplay statistics such as player/mob kills, etc.
 (Note: The plugin just shows the stats not recording itself, so stats before installing this plugin are counted too.)
  ![Stats preview](docs/img/3.gif)
 - **Custom Advancement Messages** - You can customize the advancement messages in three categories: advancement, goal, challenge. 
 (Note: The advancement names are currently only can be displayed in english. In future versions there will be a feature to translate to any languages.)
  ![Advancement Messages preview](docs/img/4.gif)
 - **Logging** - A logger with fully customizable format for dis/connect, chat, commands actions.
### [Check the config file for more explanation and examples](https://github.com/LabodiDavid/SimplifyTools/blob/main/src/main/resources/config.yml)

### [Commands & Permissions](docs/cmd_perms.md)

### If you have an idea or bug report [Create an issue](https://github.com/LabodiDavid/SimplifyTools/issues/new/choose) or [Create a pull request](https://github.com/LabodiDavid/SimplifyTools/compare)

## Main goal
My main goal is to create a single plugin that has many features, so it's can replace so many plugins that i often use on my servers while lowering the plugins count.

## 3rd party libraries used by this plugin
### [Config-Updater by tchristofferson](https://github.com/tchristofferson/Config-Updater)