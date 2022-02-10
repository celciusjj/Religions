package com.celcius.religions.utils;
import com.celcius.religions.Religions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat  {


    private final Religions plugin = Religions.getPlugin(Religions.class);

    private final String serverVersion = plugin.getServerVersion();

    private final Pattern pattern = Pattern.compile("[%]([^%]+)[%]");

    private static final Map<String, IReplacementHook> hookMap = new HashMap<>();

    public String str(String textToTranslate) {

        int index = serverVersion.indexOf("_");
        int finalIndex = serverVersion.indexOf("_", index + 1);

        int minorVersion = Integer.parseInt(serverVersion.substring(index + 1, finalIndex));

        return ChatColor.translateAlternateColorCodes('&', textToTranslate);
    }

    public void registerHooks() {
        hookMap.put("religions", new ReplacementHook());
    }

    public void registerHook(String key, IReplacementHook hook) {
        hookMap.put(key, hook);
    }

    public void unRegisterHook(String key) {
        hookMap.remove(key);
    }

    public Map<String, IReplacementHook> getHooks() {
        return hookMap;
    }



    public String replace(OfflinePlayer player, String text, boolean color, boolean prefix) {
        return replace(player, null, null, text, color, prefix);
    }


    public String replace(OfflinePlayer player, OfflinePlayer target, String text, boolean color, boolean prefix) {
        return replace(player, target, null, text, color, prefix);
    }

    public String replace(OfflinePlayer player, String str, String text, boolean color, boolean prefix) {
        return replace(player, null, str, text, color, prefix);
    }

    public String replace(OfflinePlayer player, OfflinePlayer target, String str, String text, boolean color, boolean prefix) {

        if (text == null) return null;

        Matcher matcher = this.pattern.matcher(text);

        while (matcher.find()) {

            String var = matcher.group(1);
            int index = var.indexOf("_");

            if (index <= 0 || index >= var.length())
                continue;

            String beginning = var.substring(0, index);
            String next = var.substring(index + 1);

            if (hookMap.containsKey(beginning)) {
                String value = (hookMap.get(beginning)).replace(player, target, str, next);

                if (value != null)
                    text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));

            }

        }
        if(prefix){
            return color ? str(plugin.getLang().getString("prefix") + text) : plugin.getLang().getString("prefix") + text;
        }else{
            return color ? str( text) :  text;
        }
    }

    public List<String> replaceList(OfflinePlayer player, OfflinePlayer target, String str, List<String> text, boolean color){
        List<String> lore = new ArrayList<>();

        for(int i = 0; i < text.size(); i ++){
            if (text.get(i) == null) return null;
            String t = text.get(i);
            Matcher matcher = this.pattern.matcher(text.get(i));

            while (matcher.find()) {

                String var = matcher.group(1);
                int index = var.indexOf("_");

                if (index <= 0 || index >= var.length())
                    continue;

                String beginning = var.substring(0, index);
                String next = var.substring(index + 1);

                if (hookMap.containsKey(beginning)) {
                    String value = (hookMap.get(beginning)).replace(player, target, str, next);

                    if (value != null) {
                        t = text.get(i).replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));
                    }
                }
            }
            lore.add(str(t));
        }
        return lore;
    }

}

