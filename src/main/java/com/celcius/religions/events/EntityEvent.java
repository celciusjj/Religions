package com.celcius.religions.events;

import com.celcius.religions.Religions;
import com.celcius.religions.handlers.NexoHandler;
import com.celcius.religions.menus.MainMenu;
import com.celcius.religions.menus.NexoMenu;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.PlayerAndReligion;
import com.celcius.religions.object.Religion;
import com.celcius.religions.utils.Chat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EntityEvent implements Listener {
    private final Religions plugin = Religions.getPlugin(Religions.class);
    private final Chat chat = plugin.getChat();

    @EventHandler
    public void crystalDamage(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        Entity dañador = event.getDamager();
        if(entity.getType().equals(EntityType.ENDER_CRYSTAL)){
            //PersistentDataContainer data = entity.getPersistentDataContainer();
            //if(data.has(new NamespacedKey(plugin, entity.getUniqueId())))
            if(plugin.getNexos().containsKey(entity.getUniqueId())){
                event.setCancelled(true);
                if(dañador instanceof Player) {
                    Player player = (Player) dañador;
                    Nexo nexo = plugin.getNexos().get(entity.getUniqueId());
                    PlayerAndReligion playerAndReligion = plugin.getDatabase().getPlayerInReligion(player);
                    if(playerAndReligion != null){
                        if(!nexo.getReligion().getId().equals(playerAndReligion.getReligionID())){
                            nexo.setNeedHelp(true);
                            NexoHandler nexoHandler = new NexoHandler(nexo, entity);
                            nexoHandler.subtractLife(event.getDamage());
                            sendMessageHelpReligion(entity, nexo.getReligion());
                        }else{
                            player.sendMessage(chat.replace(player,plugin.getLang().getString("dont_hurt_your_nexo"), true, true));
                        }
                    }else{
                        player.sendMessage(chat.replace(player,plugin.getLang().getString("dont_hurt_if_not_religion"), true, true));
                    }
                }
            }
        }
    }

    @EventHandler(priority= EventPriority.NORMAL, ignoreCancelled=true)
    public void onRightClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (event.getRightClicked().getType()==EntityType.ENDER_CRYSTAL) {
            if (plugin.getNexos().containsKey(entity.getUniqueId())) {
                event.setCancelled(true);
                Nexo nexo = plugin.getNexos().get(entity.getUniqueId());
                PlayerAndReligion playerAndReligion = plugin.getDatabase().getPlayerInReligion(player);
                if(playerAndReligion != null) {
                    if (nexo.getReligion().getId().equals(playerAndReligion.getReligionID())) {
                        NexoMenu nexoMenu = new NexoMenu(player, entity, nexo);
                        plugin.getPlayerOpenMenu().put(player.getUniqueId(), nexoMenu);
                        player.openInventory(nexoMenu.getInventory());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player deathPlayer = event.getEntity();
        Player assasinPlayer = deathPlayer.getKiller();
        if(deathPlayer != null && assasinPlayer != null){
        PlayerAndReligion playerAndReligionDeath = plugin.getDatabase().getPlayerInReligion(deathPlayer);
        PlayerAndReligion playerAndReligionAssasin = plugin.getDatabase().getPlayerInReligion(assasinPlayer);
        if(playerAndReligionDeath != null) {
            if (playerAndReligionAssasin != null) {
                if (!playerAndReligionDeath.getReligionID().equals(playerAndReligionAssasin.getReligionID())) {
                    if (plugin.getDatabase().removePointsToPlayers(deathPlayer.getUniqueId(), plugin.getConfig().getInt("points_when_player_is_killed"))) {
                        Bukkit.getConsoleSender().sendMessage("entra");
                        String message = plugin.getLang().getString("player_lost_points_for_death");
                        deathPlayer.sendMessage(plugin.getChat().replace(deathPlayer, message, true, true));
                    }
                    if (plugin.getDatabase().givePointsToPlayers(assasinPlayer.getUniqueId(), plugin.getConfig().getInt("points_when_player_is_killed"))) {
                        String message = plugin.getLang().getString("player_lost_points_for_death");
                        deathPlayer.sendMessage(plugin.getChat().replace(deathPlayer, message, true, true));
                    }
                }
            }
        }
        }
    }

    void sendMessageHelpReligion(Entity entity, Religion religion){
        if(plugin.getCooldownsHelp().containsKey(entity.getUniqueId())){
            Long timeAgo = plugin.getCooldownsHelp().get(entity.getUniqueId());
            Long currentTime = System.currentTimeMillis();
            long secondsAgo = TimeUnit.MILLISECONDS.toSeconds(timeAgo);
            long secondsCurrent = TimeUnit.MILLISECONDS.toSeconds(currentTime);
            if(secondsCurrent - secondsAgo > plugin.getConfig().getInt("seconds_to_delay_help_messages")){
                sendMessageToUsers(religion);
                plugin.getCooldownsHelp().replace(entity.getUniqueId(), System.currentTimeMillis());
            }
        }else{
            plugin.getCooldownsHelp().put(entity.getUniqueId(), System.currentTimeMillis());
            sendMessageToUsers(religion);
        }
    }

    void sendMessageToUsers(Religion religion){
        List<UUID> players = plugin.getDatabase().getPlayersFromReligion(religion.getId());
        for(UUID player: players){
            Player realPlayer = Bukkit.getPlayer(player);
            TextComponent message = new TextComponent(chat.replace(realPlayer, plugin.getLang().getString("request_help_nexo_underattack"), true, true));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chat.replace(realPlayer,plugin.getLang().getString("hover_request_help_nexo_underattack"), true, false)).create()));
            message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/religion nexo help" ) );
            realPlayer.spigot().sendMessage( message );
        }
    }
}
