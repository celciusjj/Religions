package com.celcius.religions;
import com.celcius.religions.api.ReligionAPI;
import com.celcius.religions.command.CommandAdmin;
import com.celcius.religions.command.CommandUser;
import com.celcius.religions.events.ChunkEvent;
import com.celcius.religions.events.EntityEvent;
import com.celcius.religions.events.GuiEvent;
import com.celcius.religions.handlers.RewardsHandler;
import com.celcius.religions.object.Nexo;
import com.celcius.religions.object.Religion;
import com.celcius.religions.placeholderAPI.Placeholders;
import com.celcius.religions.storage.DatabaseManager;
import com.celcius.religions.storage.MySQL;
import com.celcius.religions.storage.SQLite;
import com.celcius.religions.utils.Chat;
import com.celcius.religions.utils.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Religions extends JavaPlugin {
    private File configFile;
    private File langFile;
    private File religionsFile;
    private File nexosFile;
    private FileConfiguration config;
    private FileConfiguration lang;
    private FileConfiguration religionsConfiguration;
    private FileConfiguration nexosConfiguration;
    private Boolean activateCounter = false;

    private final HashMap<UUID, Nexo> nexos = new HashMap<>();
    private final HashMap<UUID, Long> cooldownsHelp = new HashMap<>();
    private final HashMap<UUID, Long> cooldownsTeleportPlayers = new HashMap<>();
    private final ConcurrentHashMap<UUID, Menu> openedMenus = new ConcurrentHashMap<>();
    private final HashMap<String, Religion> religionsList = new HashMap<>();

    private final DecimalFormat df = new DecimalFormat("0.00");
    private DatabaseManager database;
    private String latestversion;
    private String version;
    private PluginDescriptionFile pdfFile = this.getDescription();

    private Chat chat;

    String serverVersion;
    private Long nextTime;
    public Map<UUID, Menu> getPlayerOpenMenu() {
        return this.openedMenus;
    }
    private ReligionAPI religionAPI;

    public HashMap<String, Religion> getReligionsList() {
        return religionsList;
    }

    public ReligionAPI getReligionAPI() {
        return religionAPI;
    }

    public void setReligionAPI(ReligionAPI religionAPI) {
        this.religionAPI = religionAPI;
    }

    public HashMap<UUID, Nexo> getNexos() {
        return nexos;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public Long getNextTime() {
        return nextTime;
    }

    public void setNextTime(Long initialTime) {
        this.nextTime = initialTime;
    }

    public HashMap<UUID, Long> getCooldownsHelp() {
        return cooldownsHelp;
    }

    public HashMap<UUID, Long> getCooldownsTeleportPlayers() {
        return cooldownsTeleportPlayers;
    }

    /*
    public HashMap<UUID, Chunk> getChunkNexos() {
        return chunkNexos;
    }

     */

    public enum DatabaseType { MYSQL, SQLITE }


    @Override
    public void onEnable() {
        this.chat = new Chat();
        chat.registerHooks();
        createFiles();
        Bukkit.getConsoleSender().sendMessage("§5 The plugin has been enabled §b " +  version);
        Bukkit.getConsoleSender().sendMessage("§5 Developed by §b Celcius");
        registerCommands();
        registerEvents();
        setupDatabase();
        setupPlaceholderAPI();
        this.setReligionAPI(new ReligionAPI());
        this.version = this.pdfFile.getVersion();
        generateReligions();
        sendRewards();
        updateNexos();
        generateSavedNexosChunks();
    }

    private void generateReligions(){
        for (String x : getReligions().getConfigurationSection("religions").getKeys(false)) {
            ConfigurationSection info = getReligions().getConfigurationSection("religions." + x);
            String name = info.getString("name");
            String placeholder = info.getString("placeholder");
            String leader = info.getString("leader");
            List<String> lore = info.getStringList("lore");
            int maxPlayers = info.getInt("max_players");
            String permission = info.getString("permission");
            String icon = info.getString("icon");

            Religion religion = new Religion(x, name, placeholder, leader, lore, maxPlayers, permission, icon);
            getReligionsList().put(x, religion);
        }
        Bukkit.getConsoleSender().sendMessage("§5The religions has been initialized");
    }

    void generateSavedNexosChunks(){
        if (this.getNexosConfiguration().getKeys(true).size() != 0 ){
            for (Object nexusYML : this.getNexosConfiguration().getConfigurationSection("nexos").getKeys(false)) {
                ConfigurationSection entityId = this.getNexosConfiguration().getConfigurationSection("nexos." + nexusYML);
                Location l = entityId.getLocation("entity.location");
                Boolean isLoad = l.getWorld().getChunkAt(l).load();
                if(isLoad){
                    Bukkit.getConsoleSender().sendMessage("nexos cargados");
                }



                /*
                //
                //

                List<Entity> entities = Arrays.asList(l.getBlock().getChunk().getEntities());
                Entity findedEntity = Bukkit.getEntity(UUID.fromString(entityId.getString("entity.uuid")));
                Bukkit.getConsoleSender().sendMessage(findedEntity.getUniqueId()+"lo encuentra");
                Bukkit.getConsoleSender().sendMessage(entities.size()+"tamaño");
                Bukkit.getConsoleSender().sendMessage(entities.get(0).getType()+""+entities.get(0).getUniqueId());
                Bukkit.getConsoleSender().sendMessage(entities.get(1).getType()+""+entities.get(1).getUniqueId());
                List<Entity> filterNexos = entities.stream().filter(item -> item.getUniqueId().toString().equals(entityId.getString("entity.uuid")) && item.getType() == EntityType.ENDER_CRYSTAL).toList();
                List<Entity> filterHolograms = entities.stream().filter(item -> item.getUniqueId().toString().equals(entityId.getString("hologram.uuid")) && item.getType() == EntityType.ARMOR_STAND).toList();

                Bukkit.getConsoleSender().sendMessage(filterHolograms.size()+""+filterNexos.size());

                Religion religion = religionsList.get(entityId.getString("religionId"));
                Nexo nexo = new Nexo(filterNexos.get(0), (ArmorStand) filterHolograms.get(0), religion, Double.valueOf(entityId.getString("life")));
                this.getNexos().put(filterNexos.get(0).getUniqueId(), nexo);
                //l.getChunk().setForceLoaded(false);

                 */
            }
        }
    }

    private void setupPlaceholderAPI(){
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new Placeholders(this).register();
        }
    }

    /*
    private void updateChecker() {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection)(new URL("https://api.spigotmc.org/legacy/update.php?resource=93307")).openConnection();
            short var2 = 1250;
            urlConnection.setConnectTimeout(var2);
            urlConnection.setReadTimeout(var2);
            this.latestversion = (new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))).readLine();
            if (this.latestversion.length() <= 7 && !this.version.equals(this.latestversion)) {
                Bukkit.getConsoleSender().sendMessage("§7[§6PersonalBank§7] §8>> §cVersion §e(" + this.latestversion + "§e) §cis available.");
            }
        } catch (Exception var3) {
            Bukkit.getConsoleSender().sendMessage("§7[&6PersonalBank&7] §8>> §cError while checking update.");
        }
    }
     */

    public Chat getChat() {
        return this.chat;
    }


    @Override
    public void onDisable() {
        for (Map.Entry<UUID, Nexo> value: nexos.entrySet()) {
            this.getNexosConfiguration().set("nexos."+value.getKey()+".life", value.getValue().getLife());
        }
        this.saveConfig();
        Bukkit.getConsoleSender()
                .sendMessage("§5 The plugin has been disabled");
    }

    public void setServerVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        this.serverVersion = v.substring(v.lastIndexOf(".") + 1);
    }

    public String getServerVersion() {

        if (this.serverVersion == null || this.serverVersion.isEmpty()) setServerVersion();

        return this.serverVersion;
    }

    public void registerEvents() {
        PluginManager mg = getServer().getPluginManager();
        mg.registerEvents(new GuiEvent(), this);
        mg.registerEvents(new EntityEvent(), this);
        mg.registerEvents(new ChunkEvent(), this);

    }

    public void registerCommands() {
        this.getCommand("religion").setExecutor(new CommandUser());
        this.getCommand("religionadmin").setExecutor(new CommandAdmin());
    }


    public void createFiles() {

        if (!(getDataFolder().exists())) {
            getDataFolder().mkdirs();
        }

        this.configFile = new File(getDataFolder(), "config.yml");

        if (!(this.configFile.exists())) {
            try {
                Files.copy(getResource(this.configFile.getName()), this.configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File configFile = new File(this.getDataFolder() + "/config.yml");
        YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStreamReader internalConfigFileStream = new InputStreamReader(this.getResource("config.yml"), StandardCharsets.UTF_8);
        YamlConfiguration internalYamlConfig = YamlConfiguration.loadConfiguration(internalConfigFileStream);

        for (String string : internalYamlConfig.getKeys(true)) {
            // Checks if the external file contains the key already.
            if (!externalYamlConfig.contains(string)) {
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                externalYamlConfig.set(string, internalYamlConfig.get(string));
            }
        }

        try {
            externalYamlConfig.save(configFile);
        } catch (IOException io) {
            io.printStackTrace();
        }

        this.langFile = new File(getDataFolder(), "lang.yml");

        if (!(this.langFile.exists())) {
            try {
                Files.copy(getResource(this.langFile.getName()), this.langFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File langFile = new File(this.getDataFolder() + "/lang.yml");
        YamlConfiguration externalYamlLang = YamlConfiguration.loadConfiguration(langFile);

        InputStreamReader internalLangFileStream = new InputStreamReader(this.getResource("lang.yml"), StandardCharsets.UTF_8);
        YamlConfiguration internalYamlLang = YamlConfiguration.loadConfiguration(internalLangFileStream);

        for (String string : internalYamlLang.getKeys(true)) {
            // Checks if the external file contains the key already.
            if (!externalYamlLang.contains(string)) {
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                externalYamlLang.set(string, internalYamlLang.get(string));
            }
        }

        try {
            externalYamlLang.save(langFile);
        } catch (IOException io) {
            io.printStackTrace();
        }


        this.religionsFile = new File(getDataFolder(), "religions.yml");

        if (!(this.religionsFile.exists())) {
            try {
                Files.copy(getResource(this.religionsFile.getName()), this.religionsFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.nexosFile = new File(getDataFolder(), "nexos.yml");

        if (!(this.nexosFile.exists())) {
            try {
                Files.copy(getResource(this.nexosFile.getName()), this.nexosFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void reloadConfig(){

        if (this.config != null) this.config = YamlConfiguration.loadConfiguration(this.configFile);

        if (this.lang != null) this.lang = YamlConfiguration.loadConfiguration(this.langFile);

        if (this.religionsConfiguration != null) this.religionsConfiguration = YamlConfiguration.loadConfiguration(this.religionsFile);

        if(this.nexosConfiguration != null) this.nexosConfiguration = YamlConfiguration.loadConfiguration(this.nexosFile);
    }

    @Override
    public void saveConfig() {
        if (this.config != null) {
            try {
                this.config.save(this.configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.lang != null) {
            try {
                this.lang.save(this.langFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.religionsConfiguration != null) {
            try {
                this.religionsConfiguration.save(this.religionsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.nexosConfiguration != null) {
            try {
                this.nexosConfiguration.save(this.nexosFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getLang() {
        if (this.lang != null) return lang;
        this.lang = new YamlConfiguration();
        try {
            assert false;
            this.lang.load(langFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return lang;
    }

    @Override
    public FileConfiguration getConfig() {
        if (this.config != null) return config;
        this.config = new YamlConfiguration();
        try {
            assert false;
            this.config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

    public FileConfiguration getNexosConfiguration() {
        if (this.nexosConfiguration != null) return nexosConfiguration;
        this.nexosConfiguration = new YamlConfiguration();
        try {
            assert false;
            this.nexosConfiguration.load(nexosFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return nexosConfiguration;
    }

    public FileConfiguration getReligions() {
        if (this.religionsConfiguration != null) return religionsConfiguration;
        this.religionsConfiguration = new YamlConfiguration();
        try {
            assert false;
            this.religionsConfiguration.load(religionsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return religionsConfiguration;
    }

    void setupDatabase() {
        DatabaseType databaseType = DatabaseType.valueOf(getConfig().getString("database").toUpperCase());
        switch (databaseType) {

            case MYSQL:
                ConfigurationSection mysqlCredentials = getConfig().getConfigurationSection("mysql_credentials");
                MySQL mySQL = new MySQL();
                mySQL.setup(mysqlCredentials.getString("host"), mysqlCredentials.getString("database"), mysqlCredentials.getString("user"), mysqlCredentials.getString("pass"));
                this.database = mySQL;
                break;

            case SQLITE:
                SQLite sqLite = new SQLite();
                sqLite.setup("", "", "", "");

                this.database = sqLite;
                break;
        }
    }

    public DatabaseManager getDatabase() {
        return database;
    }


    public void updateNexos(){
        BukkitTask task = (new BukkitRunnable() {
            @Override
            public void run() {
                for(Nexo nexo: nexos.values()){
                    nexo.updateNexo();
                }
            }
        }).runTaskTimer(this,0, 20 * this.getConfig().getInt("update_nexos_time"));
    }

    public void sendRewards() {
        BukkitTask task = (new BukkitRunnable() {
            @Override
            public void run() {
                setNextTime(System.currentTimeMillis() + ((long) getConfig().getInt("time_rewards") * 1000));
                if(activateCounter) {
                    for(Religion x: getReligionsList().values()){
                        double points = getDatabase().getPointsFromReligions(x.getId());
                        x.setPoints(points);
                    }
                    Religion winner = religionsList.values().stream().max(Comparator.comparing(v -> v.getPoints())).get();

                    RewardsHandler rewardsHandler = new RewardsHandler(getDatabase().getPlayersFromReligion(winner.getId()), winner);
                    rewardsHandler.generateRewards();

                    String message = getLang().getString("broadcast_message_rewards_by_time");
                    String replacer = message.replaceAll("%religion_name%", winner.getName());
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', replacer));
                }else{
                    activateCounter = true;
                }
            }
        }).runTaskTimer(this,0, 20 * this.getConfig().getInt("rewards_time"));
    }

}
