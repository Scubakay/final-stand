# Final Stand

# Initialize game
```
/fs init
```
This command initializes a new game. All players online will receive a random amount of lives,
configurable with gamerules.

# Start session
```
/fs start
```
Starts a new Final Stand session. The configured number of Hunters will receive a Hunter Tracking Device
with a selected target they need to hunt down before the end of the session. If the Hunter fails
they will lose a life.

# Reset session
```
/fs reset
```
If anything goes wrong, `/fs reset` will remove all Tracking devices from the inventories.
After running the command you can use `/fs start` again to restart the session.

# Gamerules
This mod has a bunch of gamerules allowing you to customize certain aspects of the game.

## Life amounts
```
/gamerule minLives
/gamerule maxLives
```
Sets the lower and upper bounds for the randomization of lives. If minlives is higher than maxlives,
minLives will be set to the value of maxLives.

## Hunter amount
```
/gamerule hunterAmount
```
Sets the amount of hunters chosen at session start. If this is higher than the amount of players
all players will be selected as hunters.

## Tracking device cooldown
```
/gamerule hunterTrackingDeviceCooldown <cooldown>
```
The cooldown for the Hunter Tracking Device. I think this is in ticks, not really sure though.

## Preventing players on last heart from becoming hunter/target
```
/gamerule preventRedLifeHunter
/gamerule preventRedLifeTarget
```
These gamerules are set to true by default, which will prevent players on their last life from
being a hunter or a target. If you do want the game to pick targets that are on their last life,
set these gamerules to false.