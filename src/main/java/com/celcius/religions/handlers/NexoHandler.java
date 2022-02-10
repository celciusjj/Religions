package com.celcius.religions.handlers;
import com.celcius.religions.Religions;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.Religion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class NexoHandler {
    Nexo nexo;
    private final Religions plugin = Religions.getPlugin(Religions.class);
    private double ALPHA = 0;

    public NexoHandler(Nexo nexo, Entity entity){
        if(!nexo.getEntity().isValid()){
            nexo.setEntity(entity);
        }
        this.nexo = nexo;
    }

    public void removeNexo(){ ;
        nexo.getEntity().remove();
        nexo.getReligionHologram().remove();
        plugin.getNexos().remove(nexo.getEntity().getUniqueId());
    }

    public void subtractLife(double damage){
            if(nexo.getLife()-damage < 0){
                nexo.setLife(0);
                nexo.getReligionHologram().setCustomName(ChatColor.translateAlternateColorCodes('&', nexo.getReligion().getName()) + " §c❤ " + plugin.getDf().format(nexo.getLife()) + "/" + 1000);
                deathNexo();
            }else{
                nexo.setLife(nexo.getLife()-damage);
                nexo.getReligionHologram().setCustomName(ChatColor.translateAlternateColorCodes('&', nexo.getReligion().getName()) + " §c❤ " + plugin.getDf().format(nexo.getLife()) + "/" + 1000);
            }
    }

    public void healNexo(double heal){
        if(heal+nexo.getLife() > nexo.getMaxLife()){
            nexo.setLife(nexo.getMaxLife());
        }else{
            nexo.setLife(heal+nexo.getLife());
        }
        nexo.getReligionHologram().setCustomName(ChatColor.translateAlternateColorCodes('&', nexo.getReligion().getName()) + " §c❤ " + plugin.getDf().format(nexo.getLife()) + "/" + 1000);
    }

    public void deathNexo(){
        changeReligionOfNexo();
    }

    public void rewardsForDestroyNexo(Religion religion){
        if(plugin.getDatabase().givePointsToAllPlayersByReligion(religion.getId(), plugin.getConfig().getInt("points_when_nexo_is_destroyed"))){
            Bukkit.getConsoleSender().sendMessage("Se agregaron los puntos a los jugadores de " + religion.getId());
        }
    }

    public void penancesForDestroyingNexus(){
        if(plugin.getDatabase().removePointsToAllPlayersByReligion(nexo.getReligion().getId(), plugin.getConfig().getInt("points_when_nexo_is_destroyed"))){
            Bukkit.getConsoleSender().sendMessage("Se removieron los puntos a los jugadores " + nexo.getReligion().getId());
        }
    }

    public int startParticles(@NotNull Location loc) {
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ALPHA += Math.PI / 16;
            Location firstLocation = loc.clone().add(Math.cos(ALPHA), Math.sin(ALPHA) + 2, Math.sin(ALPHA));
            Location secondLocation = loc.clone().add(Math.cos(ALPHA + Math.PI), Math.sin(ALPHA) + 2,
                    Math.sin(ALPHA + Math.PI));
            loc.getWorld().spawnParticle(Particle.FLAME, firstLocation, 0, 0, 0, 0, 0);
            loc.getWorld().spawnParticle(Particle.FLAME, secondLocation, 0, 0, 0, 0, 0);

        }, 0, 1);
        return taskID;
    }

    public void stopParticles(int particlesId){
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        plugin.getServer().getScheduler().cancelTask(particlesId);
                    }
                },
                5000
        );
    }

    public void changeReligionOfNexo(){
        penancesForDestroyingNexus();
        List<Religion> religions = plugin.getReligionsList().values().stream().collect(Collectors.toList());
        List<Religion> newReligion = religions.stream().filter(item -> item.getId() != nexo.getReligion().getId()).toList();
        rewardsForDestroyNexo(newReligion.get(0));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&5El nexo de "+nexo.getReligion().getName()+ " &5ha sido capturado por "+newReligion.get(0).getName()));
        nexo.setReligion(newReligion.get(0));
        nexo.getReligionHologram().remove();
        int particlesId= startParticles(nexo.getEntity().getLocation());
        stopParticles(particlesId);
        Double maxLife = nexo.buildLife();
        nexo.setMaxLife(maxLife);
        nexo.setLife(maxLife);
        nexo.setReligionHologram(nexo.buildHologram());
        nexo.saveNexoInYAML();
    }
}
