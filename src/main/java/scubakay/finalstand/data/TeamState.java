package scubakay.finalstand.data;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import scubakay.finalstand.FinalStand;

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
            addPlayerToTeam(scoreboard, player, lives, "red");
        } else if (lives > 2) {
            addPlayerToTeam(scoreboard, player, lives, "green");
        } else {
            addPlayerToTeam(scoreboard, player, lives, "yellow");
        }
    }

    private static void addPlayerToTeam(Scoreboard scoreboard, ServerPlayerEntity player, int lives, String team) {
        if(scoreboard.getTeam(team) != null) {
            scoreboard.addPlayerToTeam(player.getEntityName(), scoreboard.getTeam(team));
            FinalStand.LOGGER.info(String.format("%s added to %s team with %d lives", player.getEntityName(), team, lives));
        }
    }

    private static void createTeam(Scoreboard scoreboard, String name, Text displayName, Formatting color) {
        if (scoreboard.getTeam(name) == null) {
            FinalStand.LOGGER.info(String.format("Creating %s team", name));
            Team team = scoreboard.addTeam(name);
            team.setDisplayName(displayName);
            team.setColor(color);
        } else {
            FinalStand.LOGGER.info(String.format("Team %s already exists", name));
        }
    }
}
