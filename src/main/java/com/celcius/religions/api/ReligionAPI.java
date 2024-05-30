package com.celcius.religions.api;

import com.celcius.religions.Religions;
import com.celcius.religions.object.PlayerAndReligion;
import org.bukkit.OfflinePlayer;

public class ReligionAPI {
    private final Religions plugin = Religions.getPlugin(Religions.class);

    public String getReligionPlaceHolderFromPlayer(OfflinePlayer p){
        PlayerAndReligion playerReligion = plugin.getDatabase().getPlayerInReligion(p);
        if(playerReligion != null){
            String placeholder = plugin.getReligions().getString("religions."+playerReligion.getReligionID()+".placeholder");
            return placeholder;
        }else{
            String placeholder = plugin.getLang().getString("not_religion");
            return placeholder;
        }
    }

    public String getLevelToEnterReligionMMOCORE(){
        return String.valueOf(plugin.getConfig().getInt("mmocore_dependecy.level_to_access_mmocore"));
    }

    public String getReligionIdFromPlayer(OfflinePlayer p){
        PlayerAndReligion playerReligion = plugin.getDatabase().getPlayerInReligion(p);
        if(playerReligion != null){
            String name = playerReligion.getReligionID();
            return name;
        }else{
            String name = plugin.getLang().getString("not_religion");
            return name;
        }
    }

    public String getPointsOfPlayer(OfflinePlayer p){
        PlayerAndReligion playerReligion = plugin.getDatabase().getPlayerInReligion(p);
        if(playerReligion != null){
            return playerReligion.getPoints() + "";
        }else{
            return 0 + "";
        }
    }

    public double getPointsOfReligion(String religionId){
        double points = plugin.getDatabase().getPointsFromReligions(religionId);
        return points;
    }
}
