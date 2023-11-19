package me.ultrajungleboy.physicaleconomy.files;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class FileManager {
    public FileManager() {
    }

    public static boolean SaveShop(String filename, ItemStack[] items) {
        String path = PhysicalEconomy.getPlugin().getDataFolder() + "/SavedShops";
        File file = new File(path, filename + ".yml");

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException var7) {
            PhysicalEconomy.getPlugin().getLogger().log(Level.SEVERE, ChatColor.RED + "An error occured while creating the file! Is there enough space left?");
            return false;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        for(int i = 0; i < 54; ++i) {
            yaml.set("item" + i, items[i]);
        }

        try {
            yaml.save(file);
            return true;
        } catch (IOException var6) {
            PhysicalEconomy.getPlugin().getLogger().log(Level.SEVERE, ChatColor.RED + "An error occured while creating the file! Is there enough space left?");
            return false;
        }
    }

    public static ItemStack[] LoadShop(String filename) {
        String path = PhysicalEconomy.getPlugin().getDataFolder() + "/SavedShops";
        File file = new File(path, filename + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ItemStack[] items = new ItemStack[54];

        for(int i = 0; i < 54; ++i) {
            ItemStack item = yaml.getItemStack("item" + i);
            items[i] = item;
        }

        return items;
    }

    public static boolean DoesShopExist(String filename) {
        String path = PhysicalEconomy.getPlugin().getDataFolder() + "/SavedShops";
        File file = new File(path, filename + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return yaml.isItemStack("item0");
    }
}
