package com.celcius.religions.command;
import com.celcius.religions.Religions;
import com.celcius.religions.handlers.TeleportHandler;
import com.celcius.religions.menus.MainMenu;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.PlayerAndReligion;
import com.celcius.religions.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CommandUser implements CommandExecutor {

    private final Religions plugin = Religions.getPlugin(Religions.class);
    private final Chat chat = plugin.getChat();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            if (player.hasPermission("religion.default") || player.isOp() || sender instanceof ConsoleCommandSender) {
                if (cmd.getName().equalsIgnoreCase("religion")) {
                    if(args[0].equals("enter")){
                        if(!args[1].isEmpty()){
                            if(plugin.getReligionsList().containsKey(args[1])){
                                if(!plugin.getDatabase().verifyPlayerInReligion(player)){
                                    MainMenu mainMenu = new MainMenu(player, plugin.getReligionsList().get(args[1]));
                                    plugin.getPlayerOpenMenu().put(player.getUniqueId(), mainMenu);
                                    player.openInventory(mainMenu.getInventory());
                                }else{
                                    player.sendMessage(chat.replace(player ,plugin.getLang().getString("you_have_religion"), true, true));
                                }
                            }else{
                                player.sendMessage(chat.replace(player, plugin.getLang().getString("religion_not_found"), true, true));
                            }
                        }
                    }else if(args[0].equals("help")){

                    }else if(args[0].equals("nexo")){
                        if(args[1].equals("help")){
                            if(plugin.getCooldownsTeleportPlayers().containsKey(player.getUniqueId())){
                                Long timeAgo = plugin.getCooldownsTeleportPlayers().get(player.getUniqueId());
                                Long currentTime = System.currentTimeMillis();
                                long secondsAgo = TimeUnit.MILLISECONDS.toSeconds(timeAgo);
                                long secondsCurrent = TimeUnit.MILLISECONDS.toSeconds(currentTime);
                                if(secondsCurrent - secondsAgo > plugin.getConfig().getInt("seconds_delay_teleport")){
                                    TeleportHandler teleport = new TeleportHandler(player);
                                    teleport.teleportPlayer();
                                    plugin.getCooldownsTeleportPlayers().replace(player.getUniqueId(), System.currentTimeMillis());
                                }else{
                                    player.sendMessage(chat.replace(player, plugin.getLang().getString("cooldown_teleport_nexo"), true, true));
                                }
                            }else{
                                TeleportHandler teleport = new TeleportHandler(player);
                                teleport.teleportPlayer();
                            }
                        }
                    }
                }
            }else{
                player.sendMessage(chat.replace(player,plugin.getLang().getString("noPermission"), true, true));
            }
        } catch (Exception ex) {

        }

        return false;
    }
}