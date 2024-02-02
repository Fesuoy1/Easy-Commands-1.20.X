Modrinth page: https://modrinth.com/mod/fesuoy-easy-commands

## Have you wanted to access minecraft commands easily? 

Suppose you wanted a OP Netherite sword right now without using /enchant command over and over.. now with this mod you can do /enchantsword while holding any sword!

### Current Commands Implemented:

- /repair (Repair a tool in your hand such as a pickaxe that is nearly broken)
- /repairinventory (Repairs everything that is damaged in your inventory)
- /repairall [repairInventory true/false] (Repairs a damaged tool in every player's hands, use true parameter for all players inventory)
- /killall [shouldAlsoKillPlayers true/false] (This explains itself)
- /knockback [amount of levels] (for fun, put knockback on anything! can be 100 or whatever)
- /knockbackstick [amount of levels] (simliar to the command above, give yourself a stick with knockback)
- /enchantsword (Enchants any sword you are holding. Very OP!)
- /heal [allPlayers true/false] (Heal yourself or all players to max health! true parameter not tested fully.)
- /feed [allPlayers "@a or @s etc."] (Feeds you or other players! Not tested fully.)
- /day (basically /time set 1000)
- /noon (basically /time set 6000)
- /night (basically /time set 13000)
- /midnight (basically /time set 18000)

### Note
Commands ran through a command block will probably not work. so i recommand running it normally.

### Bugs?
To be honest, i dont debug much since i made this when i was bored, so expect some bugs that can occur.

If you find any. You can report them [here](https://github.com/Fesuoy1/Easy-Commands-1.20.X/issues).

## Dependencies
Since this mod uses CommandRegistrationCallback, You will need to install [Fabric API](https://modrinth.com/mod/fabric-api)!

## This is Open Source.
I probably will port this to future versions of minecraft available. But no plans for backporting. If you want to backport to older versions then you can [fork this repository](https://github.com/Fesuoy1/Easy-Commands-1.20.X/fork).
