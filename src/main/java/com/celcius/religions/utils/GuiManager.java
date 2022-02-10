package com.celcius.religions.utils;

import com.celcius.religions.Religions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiManager {

    private final Religions plugin = Religions.getPlugin(Religions.class);

    public void onOpen(InventoryOpenEvent event) {

        if (event.getPlayer() instanceof Player) {

            Player player = (Player) event.getPlayer();
            Menu menu = plugin.getPlayerOpenMenu().get(player.getUniqueId());
            //Menu menu = plugin.getPlayerOpenMenu().get(player.getUniqueId());

            if (menu != null) menu.onOpen(event);

        }

    }

    public void onClick(InventoryClickEvent event) {

        if (event.getWhoClicked() instanceof Player) {

            Player player = ((Player) event.getWhoClicked()).getPlayer();
            assert player != null;

            Menu menu = plugin.getPlayerOpenMenu().get(player.getUniqueId());

            if (menu != null) menu.onClick(event);

        }

    }

    public void onClose(InventoryCloseEvent event) {

        if (event.getPlayer() instanceof Player) {

            Player player = (Player) event.getPlayer();

            Menu menu = plugin.getPlayerOpenMenu().get(player.getUniqueId());

            if (menu != null) menu.onClose(event);

        }
    }

    public void onDrag(InventoryDragEvent event) {

        if (event.isCancelled()) return;

        if (event.getWhoClicked() instanceof Player) {

            Player player = (Player) event.getWhoClicked();

            Menu menu = plugin.getPlayerOpenMenu().get(player.getUniqueId());

            if (menu != null) menu.onDrag(event);

        }
    }

}
