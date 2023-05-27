# Final Stand

Final Stand is the mod used in the second season of the Final Stand YouTube series.
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

### Sessions

#### Initialize game
```
/fs session init
```
This command initializes a new game. All players online will receive a random amount of lives.

You can add a player name as an argument to set the lives for that player only. Initialized players will have their health, hunger, saturation and inventory reset!

#### Start session
```
/fs session start
```
Starts a new Final Stand session. After a configured amount of time the configured number of Hunters will receive a Hunter Tracking Device
with a selected target they need to hunt down before the end of the session. If the Hunter fails
they will lose a life.

This command will also place a random chest with goodies within the world borders after a configured amount of time.

#### Reset session
```
/fs session reset
```
If anything goes wrong, `/fs reset` will remove all Tracking devices from the inventories.
After running the command you can use `/fs start` again to restart the session.

#### End session
```
/fs session end
```
End a session manually.

#### Pause and resume session
```
/fs session pause
/fs session resume
```
These commands can be used to pause your session.

### Lives

#### Add and remove lives
```
/fs lives add <Player>
/fs lives remove <Player>
```
Adds/removes one life to/from a player.

#### Set lives
```
/fs lives set <Player> <amount>
```
Sets the lives of a player to a specific amount.

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

### Complete bounty
```
/fs completeBounty <players>
```
If a hunter kills a target in a way that is not counted by the game, this command can be used to reward the hunter with the bounty completion.

### Configuration
This mod has a bunch of configuration options allowing you to customize certain aspects of the game.
These options can be found in `./config/finalstand.conf`.
