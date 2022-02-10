package com.celcius.religions.placeholderAPI;

import com.celcius.religions.Religions;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class Placeholders extends PlaceholderExpansion {

    private final Religions plugin;

    public Placeholders(Religions plugin){
        this.plugin = plugin;
    }

    @Override
    public String getAuthor(){
        return "celcius";
    }

    @Override
    public String getIdentifier(){
        return "religion";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(player == null){
            return "";
        }

        if(params.equalsIgnoreCase("name")){
            return plugin.getReligionAPI().getReligionPlaceHolderFromPlayer(player);
        }
        if(params.equalsIgnoreCase("points")){
            return plugin.getReligionAPI().getPointsOfPlayer(player);
        }
        return null;
    }
}
