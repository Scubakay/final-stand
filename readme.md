# Final Stand

Final Stand is the mod used in the second season of the Final Stand Youtube series.
The mod is heavily inspired by 3rd/Double/Last Life, but with our own twist!

Players get a random amount of lives they start with and when they run out, it's game over!
When you get knocked out you will be put in spectator mode, so make sure you hold on to your lives.

Every session a certain amount of Bounty Hunters will get selected. Their job is to kill
their target before the session ends, or they will lose a life themselves! Luckily, the bounty
hunters get a tracking device that helps them find their target.

Also, each session start will spawn a random chest on the surface within the world borders, which 
contains some very good loot! Go and find it to gain some extra advantage on your opponents!

## Contributions

Special thanks to:
- TheNatter for creating the Hunter Tracking Device model!
- Huskarmen for creating the Hunter Tracking Device sounds!

## How to use

### Initialize game
```
/fs init
```
This command initializes a new game. All players online will receive a random amount of lives,
configurable with gamerules.

You can add a player name as an argument to set the lives for that player only.

### Start session
```
/fs start
```
Starts a new Final Stand session. The configured number of Hunters will receive a Hunter Tracking Device
with a selected target they need to hunt down before the end of the session. If the Hunter fails
they will lose a life.

This command will also place a random chest with goodies within the world borders.

### Reset session
```
/fs reset
```
If anything goes wrong, `/fs reset` will remove all Tracking devices from the inventories.
After running the command you can use `/fs start` again to restart the session.

### Place chest
```
/fs placeChest
```
Places a random chest with goodies within the world borders.

### Select hunters
```
/fs selectHunters
```
Selects the configured amount of hunters.

### Gamerules
This mod has a bunch of gamerules allowing you to customize certain aspects of the game.

#### Life amounts
```
/gamerule minLives
/gamerule maxLives
```
Sets the lower and upper bounds for the randomization of lives. If minlives is higher than maxlives,
minLives will be set to the value of maxLives.

#### Hunter amount
```
/gamerule hunterAmount
```
Sets the amount of hunters chosen at session start. If this is higher than the amount of players
all players will be selected as hunters.

#### Tracking device cooldown
```
/gamerule hunterTrackingDeviceCooldown <cooldown>
```
The cooldown for the Hunter Tracking Device. I think this is in ticks, not really sure though.

#### Preventing players on last heart from becoming hunter/target
```
/gamerule preventRedLifeHunter
/gamerule preventRedLifeTarget
```
These gamerules are set to true by default, which will prevent players on their last life from
being a hunter or a target. If you do want the game to pick targets that are on their last life,
set these gamerules to false.