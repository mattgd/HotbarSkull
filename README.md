# HotbarSkull

HotbarSkull is a Minecraft server plugin for giving players an unmoveable skull in their inventory (requested on /r/admincraft).

## Config
```
# Slot to put the player skull in (0 for first hotbar slot, 8 for last)
skull-slot: 0
```

## Commands
- /hotbarskull help - _display HotbarSkull command help_
- /hotbarskull give <player> - _manually give a player their skull in the configuration specified inventory slot_
- /startup giveall - _give all online players their skull in the configuration specified inventory slot_

The permission required for using these commands is: _hotbarskull.give_
