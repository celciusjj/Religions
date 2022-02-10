package com.celcius.religions.command;
import com.celcius.religions.Religions;
import com.celcius.religions.handlers.NexoHandler;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.PlayerAndReligion;
import com.celcius.religions.object.Religion;
import com.celcius.religions.utils.GFG;
import com.celcius.religions.utils.SetCommand;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommandAdmin implements CommandExecutor, TabCompleter {

    private final Religions plugin = Religions.getPlugin(Religions.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            Player player = (Player) sender;
            if (player.hasPermission("religion.admin") || sender instanceof ConsoleCommandSender) {
                if (cmd.getName().equalsIgnoreCase("religionadmin")) {
                    if (args[0].equals("reload")) {
                        plugin.reloadConfig();
                        sender.sendMessage("§aThe plugin has been reloaded");
                    }
                    if(args[0].equals("time")){
                        if(args[1].equals("rewards")){
                            long currentTime = System.currentTimeMillis();
                            Date currentDate = new Date(currentTime);
                            Date nextDate = new Date(plugin.getNextTime());
                            GFG dateDiff = new GFG();
                            player.sendMessage(plugin.getChat().replace(player, plugin.getLang().getString("next_reward_info") +""+ dateDiff.findDifference(currentDate, nextDate), true, true));
                        }
                    }
                    if(args[0].equals("save")){
                        if(args[1].equals("nexos")){
                            for(Nexo nexo: plugin.getNexos().values()){
                                nexo.saveNexoInYAML();
                                nexo.getEntity().remove();
                                nexo.getReligionHologram().remove();
                            }
                            plugin.saveConfig();
                        }
                    }
                    if(args[0].equals("add")){
                        if(args[1].equals("points")){
                            if(!args[2].isEmpty()){
                                if(!args[3].isEmpty()){
                                    Player playerReal = Bukkit.getPlayer(args[2]);
                                    plugin.getDatabase().givePointsToPlayers(playerReal.getUniqueId(), Integer.parseInt(args[3]));
                                }
                            }
                        }
                        if(args[1].equals("nexo")){
                            if(!args[2].isEmpty()){
                                if(plugin.getReligionsList().containsKey(args[2])){
                                    Religion religion = plugin.getReligionsList().get(args[2]);
                                    Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.ENDER_CRYSTAL);
                                    Nexo nexo = new Nexo(entity, religion);
                                    plugin.getNexos().put(entity.getUniqueId(), nexo);
                                    PersistentDataContainer data = entity.getPersistentDataContainer();
                                    data.set(new NamespacedKey(plugin, String.valueOf(entity.getUniqueId())), PersistentDataType.STRING, String.valueOf(entity.getUniqueId()));
                                    nexo.saveNexoInYAML();
                                }
                            }
                        }
                    }
                    if(args[0].equals("remove")){
                        if(args[1].equals("player")){
                            PlayerAndReligion playerReligion = plugin.getDatabase().getPlayerInReligion(player);
                            boolean state = plugin.getDatabase().removePlayerFromReligion(Bukkit.getPlayer(args[2]));
                            if(state){
                                SetCommand command = new SetCommand();
                                command.launchCommand(plugin.getReligions().getStringList("religions."+playerReligion.getReligionID()+".commands_exit_to_religion"), player);
                                player.sendMessage("jugador eliminado de la religión");
                            }
                        }
                        if(args[1].equals("nexo")){
                            if(!args[2].isEmpty()){
                                if(args[2].equals("all")){
                                    List<Nexo> nexos = plugin.getNexos().values().stream().collect(Collectors.toList());
                                    for(Nexo x: nexos){
                                        for(Entity entity : player.getWorld().getEntities()){
                                            if(entity.getUniqueId().equals(x.getEntity().getUniqueId())){
                                                NexoHandler handler = new NexoHandler(x, entity);
                                                handler.removeNexo();
                                            }
                                        }
                                    }
                                    plugin.getNexosConfiguration().set("nexos", null);
                                    plugin.saveConfig();
                                } else if(plugin.getReligionsList().containsKey(args[2])){
                                    List<Nexo> nexos = plugin.getNexos().values().stream().collect(Collectors.toList());
                                    List<Nexo> nexo =  nexos.stream().filter(item -> item.getReligion().getId().equals(args[2])).toList();
                                    for(Nexo x: nexo){
                                        for(Entity entity : player.getWorld().getEntities()){
                                            if(entity.getUniqueId().equals(x.getEntity().getUniqueId())){
                                                NexoHandler handler = new NexoHandler(x, entity);
                                                handler.removeNexo();
                                                plugin.getNexosConfiguration().set("nexos."+x.getEntity().getUniqueId(), null);
                                            }
                                        }
                                    }
                                    plugin.saveConfig();
                                }else{
                                    player.sendMessage("Esta religión no existe");
                                }
                            }else{

                            }
                        }
                    }
                }
            } else {
                player.sendMessage(plugin.getLang().getString("noPermission"));
            }
        } catch (Exception ex) {

        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return null;

        List<String> output = new ArrayList<>();
        Player player = (Player) sender;
        if (player.hasPermission("religion.admin") || sender instanceof ConsoleCommandSender) {
            if (cmd.getName().equalsIgnoreCase("bankadmin") || cmd.getName().equalsIgnoreCase("bancoadmin")) {
                if (args.length == 1) {
                    output.add("reload");
                    return output;
                }
                if(args.length == 1){

                }
            }
        }
        return output;
    }
}
