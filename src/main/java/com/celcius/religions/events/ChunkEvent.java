package com.celcius.religions.events;

import com.celcius.religions.Religions;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.Religion;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import java.util.List;
import java.util.UUID;

public class ChunkEvent implements Listener {
    private final Religions plugin = Religions.getPlugin(Religions.class);


    @EventHandler
    public void onChunkLoad(EntitiesLoadEvent event) {
            if (plugin.getNexosConfiguration().getKeys(true).size() != 0) {
                List<Entity> entities = (event.getEntities());
                if (entities.size() > 0) {
                    for (Object nexusYML : plugin.getNexosConfiguration().getConfigurationSection("nexos").getKeys(false)) {
                        ConfigurationSection entityId = plugin.getNexosConfiguration().getConfigurationSection("nexos." + nexusYML);
                        if (plugin.getNexos().get(UUID.fromString(entityId.getString("entity.uuid"))) == null) {
                            List<Entity> filterNexos = entities.stream().filter(item -> item.getUniqueId().toString().equals(entityId.getString("entity.uuid")) && item.getType() == EntityType.ENDER_CRYSTAL).toList();
                            List<Entity> filterHolograms = entities.stream().filter(item -> item.getUniqueId().toString().equals(entityId.getString("hologram.uuid")) && item.getType() == EntityType.ARMOR_STAND).toList();
                            if (filterNexos.size() > 0 && filterHolograms.size() > 0) {
                                Religion religion = plugin.getReligionsList().get(entityId.getString("religionId"));
                                Nexo nexo = new Nexo(filterNexos.get(0), (ArmorStand) filterHolograms.get(0), religion, Double.valueOf(entityId.getString("life")));
                                plugin.getNexos().put(filterNexos.get(0).getUniqueId(), nexo);
                            }
                        }
                    }
                }
            }
    }
}
