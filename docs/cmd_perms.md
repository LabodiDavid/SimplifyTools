## SimplifyTools - Commands
You can use the following commands to use the features of SimplifyTools.
- **/st**: The main SimplifyTools command.<br />Requires `st.st`
- **/st help**: Displays all SimplifyTools related commands.<br />Requires `st.help`
- **/st ping**: Displays your ping to the current server.<br />Requires `st.ping`
- **/st tps**: Displays plugin calculated TPS.<br />Requires `st.tps`
- **/st stats**: Displays your gameplay statistics.<br />Requires `st.stats`
- **/slogin**: Used to login if server password enabled.<br />Requires `st.slogin`
- **/st save-all**: Saves the players, worlds data on the server.<br />Requires `st.save`
- **/st pmanager** <LOAD/UNLOAD>: A built-in plugin manager.<br />Requires `st.pmanager.*`
- **/st pmanager load** <PLUGIN-NAME>: Plugin loading.<br />Requires `st.pmanager.load (can be inherited from st.pmanager.*)`
- **/st pmanager unload** <PLUGIN-NAME>: Plugin unload.<br />Requires `st.pmanager.unload  (can be inherited from st.pmanager.*)`
- **/st reload**: Plugin settings reload.<br />Requires `st.reload`
## SimplifyTools - Permissions
- `st.slogin.skip` - If has this permission the user dont have to provide server password upon connect if enabled.
## SimplifyTools - Wildcard Permissions

- **st.*** -  Wildcard permission for all commands, permissions for this plugin.
- **st.pmanager.*** - Wildcard permission for the plugin manager commands.
- **st.admin** - A wildcard permission for admins. This is used for notify when update available, also grants usage for `st.save` and `st.pmanager.*`