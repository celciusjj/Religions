package com.celcius.religions.handlers;

import com.celcius.religions.Religions;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.PlayerAndReligion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TeleportHandler {
    private final Religions plugin = Religions.getPlugin(Religions.class);
    Player player;
    public TeleportHandler(Player player){
        this.player = player;
    }

    public void teleportPlayer(){
        PlayerAndReligion religion = plugin.getDatabase().getPlayerInReligion(player);
        List<Nexo> nexos = plugin.getNexos().values().stream().collect(Collectors.toList());
        List<Nexo> nexo =  nexos.stream().filter(item -> item.isNeedHelp() && item.getReligion().getId().equals(religion.getReligionID())).toList();
        if(nexo.size() > 0){
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(nexo.size());
            int coords = randomGenerator.nextInt(3);
            Location l = nexo.get(index).getEntity().getLocation();
            l.add(coords, 3, coords);
            player.teleport(l);
            //player.teleport(nexo.get(index).getEntity().getLocation());
            plugin.getCooldownsTeleportPlayers().put(player.getUniqueId(), System.currentTimeMillis());
        }else{
            player.sendMessage("Ningun nexo requiere su ayuda");
        }
    }
}
