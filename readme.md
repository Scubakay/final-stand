# Last Stand

# Initialize game

```
/ls init
```

This command initializes a new game. All players online will receive a random amount of lives,
configurable with gamerules.

# Start session

```
/ls start
```

Starts a new Last Stand session. The configured number of Hunters will receive a Hunter Tracking Device
with a selected target they need to hunt down before the end of the session. If the Hunter fails
they will lose a life.

# Reset session

```
/ls reset
```

If anything goes wrong, `/laststand reset` will remove all Tracking devices from the inventories.
After running the command you can use `/laststand start` again to restart the session.

# Gamerules

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