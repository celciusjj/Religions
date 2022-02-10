package com.celcius.religions.object;

import java.util.ArrayList;
import java.util.List;

public class Religion {
    private String id;
    private String icon;
    private String name;
    private String placeholder;
    private String leader;
    private double points;
    private List<String> lore = new ArrayList<>();
    private int maxPlayers;
    private String permission;

    public Religion(String id, String name, String placeholder, String leader, List<String> lore, int maxPlayers, String permission, String icon){
        this.setId(id);
        this.setName(name);
        this.setPlaceholder(placeholder);
        this.setLeader(leader);
        this.setLore(lore);
        this.setMaxPlayers(maxPlayers);
        this.setPermission(permission);
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
