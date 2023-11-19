package me.ultrajungleboy.physicaleconomy.files;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataManager {
    private PhysicalEconomy plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(PhysicalEconomy plugin) {
        this.plugin = plugin;
        this.saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("data.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultconfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultconfig);
        }

    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null) {
            this.reloadConfig();
        }

        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig != null && this.configFile != null) {
            try {
                this.getConfig().save(this.configFile);
            } catch (IOException var2) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, var2);
            }

        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
        }

        if (!this.configFile.exists()) {
            this.plugin.saveResource("data.yml", false);
        }

    }
}
