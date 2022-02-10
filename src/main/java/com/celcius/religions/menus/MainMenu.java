package com.celcius.religions.menus;
import com.celcius.religions.Religions;
import com.celcius.religions.object.Religion;
import com.celcius.religions.utils.Chat;
import com.celcius.religions.utils.Menu;
import com.celcius.religions.utils.SetCommand;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainMenu implements Menu {

    Player player;
    Religion religion;

    public MainMenu(Player player, Religion religion){
        this.player = player;
        this.religion = religion;
    }

    Inventory inventory;

    private final Religions plugin = Religions.getPlugin(Religions.class);
    private final Chat chat = plugin.getChat();

    public void insertPlayerInReligion(){
        if(plugin.getDatabase().insertPlayerToReligion(player, religion)){
            SetCommand command = new SetCommand();
            command.launchCommand(plugin.getReligions().getStringList("religions."+religion.getId()+".commands_enter_to_religion"), player);
            player.sendMessage((chat.replace(player, plugin.getLang().getString("you_have_entered_the_religion"), true, true)));
        }
        player.closeInventory();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(chat.replace(player, plugin.getLang().getString("main_menu.name"), true, false))){//plugin.getLang().getString("MainMenu.name"))) {
            if (event.getCurrentItem() == null) {
                return;
            }
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();
            if (event.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                int slot = event.getSlot();
                if(slot == plugin.getConfig().getInt("main_menu.accept.slot")){
                    int maxPlayers = plugin.getReligions().getInt("religions."+religion.getId()+".max_players");
                    int currentPlayers = plugin.getDatabase().getNumberOfPlayerInReligion(religion.getId());
                    if(maxPlayers > currentPlayers){
                        //Con nivel y dependencia habilitada
                        if (plugin.getConfig().getBoolean("mmocore_dependecy.enabled")) {
                            int level = PlayerData.get(p.getUniqueId()).getLevel();
                            if(level >= plugin.getConfig().getInt("mmocore_dependecy.level_to_access_mmocore")) {
                                insertPlayerInReligion();
                            }else{
                                p.sendMessage((chat.replace(p, plugin.getLang().getString("not_level"), true, true)));
                                p.closeInventory();
                            }
                        }else{
                            //Sin nivel
                            insertPlayerInReligion();
                        }
                    }else{
                        p.sendMessage(chat.replace(p, plugin.getLang().getString("religion_full"), true, true));
                    }
                }else if(slot == plugin.getConfig().getInt("main_menu.deny.slot")){
                    p.closeInventory();
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(null, 36, chat.replace(player, plugin.getLang().getString("main_menu.name"), true, false));
        return fillInventory(inventory);
    }

    public Inventory fillInventory(org.bukkit.inventory.Inventory inv) {
        ItemStack item;
        item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(chat.replace(player, plugin.getLang().getString("main_menu.filler.name"), true, false));
        meta.setLore(chat.replaceList(player, null, null,  plugin.getLang().getStringList("main_menu.filler.lore"), true));
        item.setItemMeta(meta);
        for (int i = 0; i <= 35; i++) {
            inv.setItem(i, item);
        }

        if (defineIfItemOrHead(plugin.getConfig().getString("main_menu.accept.item"))) {
            item = new ItemStack(getCustomSkull(plugin.getConfig().getString("main_menu.accept.item")));
        } else {
            item = new ItemStack(Material.valueOf(plugin.getConfig().getString("main_menu.accept.item")));
        }
        meta = item.getItemMeta();
        meta.setDisplayName(chat.replace(player, plugin.getLang().getString("main_menu.accept.name"), true, false));
        meta.setLore(chat.replaceList(player, null, null, plugin.getLang().getStringList("main_menu.accept.lore"), true));
        item.setItemMeta(meta);
        inv.setItem(plugin.getConfig().getInt("main_menu.accept.slot"), item);


        if (defineIfItemOrHead(plugin.getConfig().getString("main_menu.deny.item"))) {
            item = new ItemStack(getCustomSkull(plugin.getConfig().getString("main_menu.deny.item")));
        } else {
            item = new ItemStack(Material.valueOf(plugin.getConfig().getString("main_menu.deny.item")));
        }
        meta = item.getItemMeta();
        meta.setDisplayName(chat.replace(player, plugin.getLang().getString("main_menu.deny.name"), true, false));
        meta.setLore(chat.replaceList(player, null, null, plugin.getLang().getStringList("main_menu.deny.lore"), true));
        item.setItemMeta(meta);
        inv.setItem(plugin.getConfig().getInt("main_menu.deny.slot"), item);

        if (defineIfItemOrHead(plugin.getConfig().getString("main_menu.info.item"))) {
            item = new ItemStack(getCustomSkull(plugin.getConfig().getString("main_menu.info.item")));
        } else {
            item = new ItemStack(Material.valueOf(plugin.getConfig().getString("main_menu.info.item")));
        }
        meta = item.getItemMeta();
        meta.setDisplayName(chat.replace(player, plugin.getLang().getString("main_menu.info.name"), true, false));
        meta.setLore(chat.replaceList(player, null, null, plugin.getLang().getStringList("main_menu.info.lore"), true));
        item.setItemMeta(meta);
        inv.setItem(plugin.getConfig().getInt("main_menu.info.slot"), item);

        if (defineIfItemOrHead(religion.getIcon())) {
            item = new ItemStack(getCustomSkull(religion.getIcon()));
        } else {
            item = new ItemStack(Material.valueOf(religion.getIcon()));
        }

        meta = item.getItemMeta();
        meta.setDisplayName(chat.replace(player, religion.getName(), true, false));
        meta.setLore(chat.replaceList(player, null, null, religion.getLore(), true));
        item.setItemMeta(meta);
        inv.setItem(plugin.getConfig().getInt("main_menu.religion.slot"), item);
        return inv;
    }


    public ItemStack getCustomSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        head.setItemMeta(skullMeta);
        return head;
    }

    public boolean defineIfItemOrHead(String config){
        if(config.substring(0, 2).equals("ey")){
            return true;
        }else{
            return false;
        }
    }
}
