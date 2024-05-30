package com.celcius.religions.handlers;
import com.celcius.religions.Religions;
import com.celcius.religions.object.Religion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import java.util.List;
import java.util.UUID;

public class RewardsHandler {
    private final Religions plugin = Religions.getPlugin(Religions.class);
    List<String> rewards = plugin.getConfig().getStringList("rewards_by_time_commands");
    List<UUID> players;
    Religion religion;

    public RewardsHandler(List<UUID> players, Religion religion){
        this.players = players;
        this.religion = religion;
    }

    public void generateRewards(){
        setCommands();
    }

    private void setCommands(){
        for(int i = 0; i < rewards.size(); i++) {
            String[] data = rewards.get(i).split(";");
            String theCommand = data[0];
            double theChance = Double.parseDouble(data[1]);
            if (generateRandomNumber() < theChance) {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                if(players.size() > 0){
                    for(UUID player: players){
                        OfflinePlayer realPlayer = Bukkit.getOfflinePlayer(player);
                        if(realPlayer != null){
                            if(realPlayer.isOnline()){
                                String newCommand = theCommand.replace("%player%", realPlayer.getName());
                                Bukkit.dispatchCommand(console, newCommand);
                            }
                        }
                    }
                }
            }
        }
    }


    private double generateRandomNumber(){
        double random = Math.random();
        return random;
    }
}
