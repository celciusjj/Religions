package com.celcius.religions.object;

import com.celcius.religions.Religions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Nexo {
    private final Religions plugin = Religions.getPlugin(Religions.class);
    private Entity entity;
    private Religion religion;
    private double life;
    private double maxLife;
    private ArmorStand religionHologram;
    private boolean needHelp = false;


    public Nexo(Entity entity, Religion religion) {
        entity.setPersistent(true);
        this.religion = religion;
        this.entity = entity;
        this.maxLife = buildLife();
        this.life = maxLife;
        ArmorStand hologram = buildHologram();
        this.religionHologram = hologram;

        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, "nexo"), PersistentDataType.STRING, String.valueOf(entity.getUniqueId()));
    }

    public Nexo(Entity entity, ArmorStand hologram, Religion religion, double life){
        this.religion = religion;
        this.entity = entity;
        this.maxLife = buildLife();
        this.life = life;
        this.religionHologram = hologram;
        updateNexo();
    }

    public void updateNexo(){
        if(plugin.getDatabase().givePointsToAllPlayersByReligion(this.religion.getId(), plugin.getConfig().getInt("points_for_maintain_nexo"))){
            Bukkit.getConsoleSender().sendMessage("Se agregaron los puntos a los jugadores de " + this.religion.getId());
        }
        this.needHelp = false;
        this.religionHologram.setCustomName(ChatColor.translateAlternateColorCodes('&', religion.getName()) + " §c❤ " + plugin.getDf().format(life) + "/" + plugin.getDf().format(maxLife));
    }

    public ArmorStand buildHologram(){
        ArmorStand hologram = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation().add(0, 0.2, 0), EntityType.ARMOR_STAND);
        hologram.setGravity(false);
        hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', religion.getName()) + " §c❤ " + plugin.getDf().format(life) + "/" + plugin.getDf().format(maxLife));
        hologram.setCustomNameVisible(true);
        hologram.setVisible(false);
        hologram.setPersistent(true);
        return hologram;
    }

    public double buildLife(){
        Double formula = (plugin.getDatabase().getPointsFromReligions(religion.getId()) * 10) + 1000;
        return formula;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        List<Entity> near =  this.entity.getNearbyEntities(0, 2, 0);
        List<Entity> nearHologram = near.stream().filter(item -> item.getUniqueId().equals(religionHologram.getUniqueId())).toList();
        this.religionHologram = (ArmorStand) nearHologram.get(0);
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public ArmorStand getReligionHologram() {
        return religionHologram;
    }

    public void setReligionHologram(ArmorStand religionHologram) {
        this.religionHologram = religionHologram;
    }

    public void saveNexoInYAML(){
        plugin.getNexosConfiguration().createSection("nexos."+this.getEntity().getUniqueId());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".entity.uuid",""+this.getEntity().getUniqueId());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".entity.location", getEntity().getLocation());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".life", getLife());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".hologram.uuid",""+this.getReligionHologram().getUniqueId());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".hologram.location", getReligionHologram().getLocation());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".religionId", getReligion().getId());
        plugin.saveConfig();
    }

    public double getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(double maxLife) {
        this.maxLife = maxLife;
    }

    public boolean isNeedHelp() {
        return needHelp;
    }

    public void setNeedHelp(boolean needHelp) {
        this.needHelp = needHelp;
    }
}
