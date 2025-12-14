## ⚠️ This project is archived - A remake is releasing (for 1.21.10/11) soon! ⚠️

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
- /heal [allPlayers true/false, alsoFeed true/false] (Heal yourself or all players to max health! 1st true parameter not tested fully.)
- /feed [allPlayers "@a or @s etc."] (Feeds you or other players! Not tested fully.)
- /day (basically /time set 1000)
- /noon (basically /time set 6000)
- /night (basically /time set 13000)
- /midnight (basically /time set 18000)
- /enableExplosiveProjectiles [explosionPower (number)] (Makes all projectiles (eg. snowball, arrow) explode on collision. Toggleable!)
- /modifyTreeHeight [amount of height, default is 1] (Changes how tall the trees can be based on height. Only for fun!)
- /explode [position (x, y, z), explosionPower (number), createFire true/false] (The 1st command with most arguments. Explodes on a set position with specified explosion power and creates fire on explosion!)

### Note
Some or most commands ran through a command block will probably not work. so i recommend running it normally. i didn't test it myself of course.

### Bugs?
To be honest, i dont debug much since i made this when i was bored, so expect some bugs that can occur.

If you find any. You can report them [here](https://github.com/Fesuoy1/Easy-Commands-1.20.X/issues).

## Dependencies
Since this mod uses CommandRegistrationCallback, You will need to install [Fabric API](https://modrinth.com/mod/fabric-api)!

## This is Open Source.
I probably will port this to future versions of minecraft available. But probably no plans for more backporting. If you want to backport to older versions then you can [fork this repository](https://github.com/Fesuoy1/Easy-Commands-1.20.X/fork).
