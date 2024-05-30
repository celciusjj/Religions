package com.celcius.religions.menus;

import com.celcius.religions.Religions;
import com.celcius.religions.handlers.NexoHandler;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.Religion;
import com.celcius.religions.utils.Chat;
import com.celcius.religions.utils.Menu;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NexoMenu implements Menu {

    private final Religions plugin = Religions.getPlugin(Religions.class);
    private final Chat chat = plugin.getChat();
    Player player;
    Entity entity;
    Nexo nexo;
    Inventory inventory;

    public NexoMenu(Player player, Entity entity, Nexo nexo){
        this.player = player;
        this.entity = entity;
        this.nexo = nexo;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(chat.replace(player, plugin.getLang().getString("nexo_menu.name"), true, false))){
            if (event.getCurrentItem() == null) {
                return;
            }
            Player p = (Player) event.getWhoClicked();
            if (event.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                int slot = event.getSlot();
                if(slot != 13){
                    event.setCancelled(true);
                }else{

                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        ItemStack item = event.getInventory().getItem(13);
        if(item != null){
            for (String x : plugin.getConfig().getConfigurationSection("nexo_items_heal").getKeys(false)) {
                ConfigurationSection info = plugin.getConfig().getConfigurationSection("nexo_items_heal." + x);

                if(info.getString("mode").equals("vanilla")){
                    if(Material.valueOf(info.getString("id")).equals(item.getType())){
                        if(info.getString("effect").equals("heal")){
                            double heal = info.getDouble("heal");
                            double realHeal = item.getAmount() * heal;
                            NexoHandler nexoHandler = new NexoHandler(nexo, entity);
                            nexoHandler.healNexo(realHeal);
                        }
                        /*
                        if (info.getString("effect").equals("potion")) {
                            NexoHandler nexoHandler = new NexoHandler(nexo, entity);
                            nexoHandler.healNexo(realHeal);
                        }

                         */
                    }
                }else if(info.getString("mode").equals("mmoitem")){
                    String type = info.getString("type");
                    ItemStack itemmmo = MMOItems.plugin.getItem(MMOItems.plugin.getTypes().get(type), info.getString("id"));
                    ItemMeta currentItemMeta = itemmmo.getItemMeta();
                    Bukkit.getConsoleSender().sendMessage(currentItemMeta.getDisplayName());
                    Bukkit.getConsoleSender().sendMessage(item.getItemMeta().getDisplayName());
                    if(currentItemMeta.getDisplayName().equals(item.getItemMeta().getDisplayName())){
                        if(currentItemMeta.getLore().equals(item.getItemMeta().getLore())) {
                            if (info.getString("effect").equals("heal")) {
                                double heal = info.getDouble("heal");
                                double realHeal = item.getAmount() * heal;
                                NexoHandler nexoHandler = new NexoHandler(nexo, entity);
                                nexoHandler.healNexo(realHeal);
                            }
                            /*
                            if (info.getString("effect").equals("potion")) {
                                NexoHandler nexoHandler = new NexoHandler(nexo, entity);
                                nexoHandler.healNexo(realHeal);
                            }

                             */
                        }
                    }
                }

            }
        }
    }

    public Inventory fillInventory(org.bukkit.inventory.Inventory inv) {
        ItemStack item;
        item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(chat.replace(player, plugin.getLang().getString("nexo_menu.filler.name"), true, false));
        meta.setLore(chat.replaceList(player, null, null,  plugin.getLang().getStringList("nexo_menu.filler.lore"), true));
        item.setItemMeta(meta);
        for (int i = 0; i <= 12; i++) {
            inv.setItem(i, item);
        }

        for (int i = 14; i <= 26; i++) {
            inv.setItem(i, item);
        }
        return inv;
    }

    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(null, 27, chat.replace(player, plugin.getLang().getString("nexo_menu.name"), true, false));
        return fillInventory(inventory);
    }
}
