package com.celcius.religions.events;

import com.celcius.religions.Religions;
import com.celcius.religions.handlers.NexoHandler;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.Religion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChunkEvent implements Listener {
    private final Religions plugin = Religions.getPlugin(Religions.class);

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (plugin.getNexosConfiguration().getKeys(true).size() > 0 ) {
            for (Object nexusYML : plugin.getNexosConfiguration().getConfigurationSection("nexos").getKeys(false)) {
                ConfigurationSection entityId = plugin.getNexosConfiguration().getConfigurationSection("nexos." + nexusYML);
                Location l = entityId.getLocation("entity.location");
                if (l.getChunk().equals(event.getChunk())) {
                    Entity entity = l.getWorld().spawnEntity(l, EntityType.ENDER_CRYSTAL);
                    Religion religion = plugin.getReligionsList().get(entityId.getString("religionId"));
                    Nexo nexo = new Nexo(entity, religion, Double.valueOf(entityId.getString("life")));
                    plugin.getNexos().put(entity.getUniqueId(), nexo);
                    plugin.getNexosConfiguration().set("nexos."+entityId, null);
                    nexo.saveNexoInYAML();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        if(plugin.getNexos().size() > 0){
            List<Entity> entities = Arrays.asList(event.getChunk().getEntities());
            List<Entity> result = entities.stream().filter(item -> plugin.getNexos().keySet().contains(item.getUniqueId())).collect(Collectors.toList());
            if(result.size() > 0){
               for(Entity entity: result){
                   Entity newEntity = Bukkit.getEntity(entity.getUniqueId());
                   Nexo nexo = plugin.getNexos().get(entity.getUniqueId());
                   NexoHandler handler = new NexoHandler(nexo, newEntity);
                   handler.removeNexo();
                   nexo.saveNexoWithoutLocation();
               }
            }
        }
    }
}
