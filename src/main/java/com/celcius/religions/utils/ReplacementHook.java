package com.celcius.religions.utils;
import com.celcius.religions.Religions;
import org.bukkit.OfflinePlayer;

public class ReplacementHook implements IReplacementHook {
    private final Religions plugin = Religions.getPlugin(Religions.class);
    public String replace(OfflinePlayer player, OfflinePlayer target, String str, String var) {
        if(var.equals("points")){
            //int time = plugin.getConfig().getInt("timeForInvestment");
            //int hours = time / 3600;
            //return String.valueOf(hours);
        }
        if(var.equals("poins_killed")){
            return String.valueOf(plugin.getConfig().getInt("points_when_player_is_killed"));
        }
        if(var.equals("lost_points_nexo")){
            return String.valueOf(plugin.getConfig().getString("points_when_nexo_is_destroyed"));
        }
        if(var.equals("player_religion")){
            return plugin.getReligionAPI().getReligionIdFromPlayer(player);
        }
        if(var.equals("level_enter")){
            return plugin.getReligionAPI().getLevelToEnterReligionMMOCORE();
        }
        return null;
    }

}
