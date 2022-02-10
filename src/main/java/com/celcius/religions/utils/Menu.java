package com.celcius.religions.utils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public interface Menu extends InventoryHolder {

    void onOpen(InventoryOpenEvent event);

    void onClick(InventoryClickEvent event);

    void onDrag(InventoryDragEvent event);

    void onClose(InventoryCloseEvent event);

}
