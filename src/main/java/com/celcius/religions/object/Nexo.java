package com.celcius.religions.object;

import com.celcius.religions.Religions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

public class Nexo {
    private final Religions plugin = Religions.getPlugin(Religions.class);
    private Entity entity;
    private Religion religion;
    private double life;
    private double maxLife;
    private ArmorStand religionHologram;
    private boolean needHelp = false;


    public Nexo(Entity entity, Religion religion){
        entity.setPersistent(true);
        this.religion = religion;
        this.entity = entity;
        this.maxLife = buildLife();
        this.life = maxLife;
        ArmorStand hologram = buildHologram();
        this.religionHologram = hologram;
    }

    public Nexo(Entity entity, Religion religion, double life){
        this.religion = religion;
        this.entity = entity;
        this.maxLife = buildLife();
        this.life = life;
        ArmorStand hologram = buildHologram();
        this.religionHologram = hologram;
    }

    public void updateNexo(){
        this.needHelp = false;
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
        Bukkit.getConsoleSender().sendMessage(nearHologram.size()+"");
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
        //plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".uuid", this.getEntity().getUniqueId());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".entity.location", getEntity().getLocation());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".life", getLife());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".hologram", getReligionHologram().getLocation());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".religionId", getReligion().getId());
        plugin.saveConfig();
    }

    public void saveNexoWithoutLocation(){
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".life", getLife());
        plugin.getNexosConfiguration().set("nexos."+this.getEntity().getUniqueId()+".hologram", getReligionHologram().getLocation());
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
