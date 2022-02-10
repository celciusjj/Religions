package com.celcius.religions.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class SetCommand {

    public void launchCommand(List<String> command, OfflinePlayer player){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for(String x: command){
            String newCommand = x.replace("%player%", player.getName());;
            Bukkit.dispatchCommand(console, newCommand);
        }
    }
}
