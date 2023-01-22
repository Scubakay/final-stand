package scubakay.finalstand.data;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TeamState {
    public static void createTeams(Scoreboard scoreboard) {
        createTeam(scoreboard, "red", Text.literal("Red"), Formatting.RED);
        createTeam(scoreboard, "yellow", Text.literal("Yellow"), Formatting.YELLOW);
        createTeam(scoreboard, "green", Text.literal("Green"), Formatting.GREEN);
    }

    public static void setPlayerTeam(int lives, ServerPlayerEntity player) {
        Scoreboard scoreboard = player.getWorld().getScoreboard();
        if (lives == 0) {
            Team team = scoreboard.getPlayerTeam(player.getEntityName());
            if (team != null) {
                scoreboard.removePlayerFromTeam(player.getEntityName(), team);
            }
        } else if (lives < 2) {
            if(scoreboard.getTeam("red") != null) {
                scoreboard.addPlayerToTeam(player.getEntityName(), scoreboard.getTeam("red"));
            }
        } else if (lives > 2) {
            if(scoreboard.getTeam("green") != null) {
                scoreboard.addPlayerToTeam(player.getEntityName(), scoreboard.getTeam("green"));
            }
        } else {
            if(scoreboard.getTeam("yellow") != null) {
                scoreboard.addPlayerToTeam(player.getEntityName(), scoreboard.getTeam("yellow"));
            }
        }
    }

    private static void createTeam(Scoreboard scoreboard, String name, Text displayName, Formatting color) {
        if (scoreboard.getTeam(name) == null) {
            Team team = scoreboard.addTeam(name);
            team.setDisplayName(displayName);
            team.setColor(color);
        }
    }
}
