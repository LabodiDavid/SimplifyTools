# SimplifyTools  
### An 'All-in-one' plugin for MC servers. 
#### TabList Customizing, Custom Advancement, Connect messages, AutoSave, Logger
 [![Build Status](https://ci.ditservices.hu/job/SimplifyTools/badge/icon)](https://ci.ditservices.hu/job/SimplifyTools/) ![ MC Version](https://ci.ditservices.hu/job/SimplifyTools/badge/icon?subject=MC&status=1.12%20-%201.20.1&color=darkblue)  ![Git latest release](https://img.shields.io/github/v/release/LabodiDavid/SimplifyTools)  [![bStats](https://ci.ditservices.hu/job/SimplifyTools/badge/icon?subject=bStats&status=3.0&color=brightgreen)](https://bstats.org/plugin/bukkit/SimplifyTools/15108) ![Lines of code](https://tokei.rs/b1/github/LabodiDavid/SimplifyTools?category=code)
<br>[Changelog](docs/ChangeLog.md)
<hr>



## Features

 - **Tab Manager** - Allows you to specify colored texts, with animations on the tab window.
		 Also you can display the average ping, Server RAM statistics.
 ![Tab manager preview](docs/img/1.gif)
 - **Automatic/command saving** - Allows you to schedule auto saving of the world/player data on your server, or you can initiate a save by a command.
 - **Custom Connect/Disconnect messages** - Allows you to customize the message that is broadcasted when someone joins or leaves the server.
  ![Connect messages preview](docs/img/2.gif)
 - **Plugin Manager** - Allows you to unload/load plugins without a server restart. (Disabled by default since v1.2.1)
 - **Gameplay statistics** - You can check your gameplay statistics such as player/mob kills, etc.
 (Note: The plugin just shows the stats not recording itself, so stats before installing this plugin are counted too.)
  ![Stats preview](docs/img/3.gif)
 - **Custom Advancement Messages** - You can customize the advancement messages in three categories: advancement, goal, challenge. 
 (Note: The advancement names are currently only can be displayed in english. In future versions there will be a feature to translate to any languages.)
  ![Advancement Messages preview](docs/img/4.gif)
 - **Logging** - A logger with fully customizable format for dis/connect, chat, commands actions.
### [Check the config file for more explanation and examples](https://github.com/LabodiDavid/SimplifyTools/blob/main/src/main/resources/config.yml)
_________________
### [Commands & Permissions](docs/cmd_perms.md)
_________________
### [Building from source](docs/BUILDING.md)
_________________
### If you have an idea or bug report [Create an issue](https://github.com/LabodiDavid/SimplifyTools/issues/new/choose) or [Create a pull request](https://github.com/LabodiDavid/SimplifyTools/compare)
_________________
## Main goal
My main goal is to create a single plugin that has many features, so it's can replace plugins that i often use on my servers while lowering the plugins count.
_________________
## 3rd party libraries used by this plugin
### [Config-Updater by tchristofferson](https://github.com/tchristofferson/Config-Updater)
### [SpigotUpdateChecker by JEFF-Media-GbR](https://github.com/JEFF-Media-GbR/Spigot-UpdateChecker)