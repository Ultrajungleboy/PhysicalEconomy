package me.ultrajungleboy.physicaleconomy;

import org.bukkit.plugin.java.JavaPlugin;

import me.ultrajungleboy.physicaleconomy.banking.BankingUtil;
import me.ultrajungleboy.physicaleconomy.betonquest.HasMoneyCondition;
import me.ultrajungleboy.physicaleconomy.commands.Commands;
import me.ultrajungleboy.physicaleconomy.events.Events;
import me.ultrajungleboy.physicaleconomy.events.ItemClicked;
import me.ultrajungleboy.physicaleconomy.events.OnInventoryExit;
import me.ultrajungleboy.physicaleconomy.files.DataManager;
import me.ultrajungleboy.physicaleconomy.items.ItemManager;
import me.ultrajungleboy.physicaleconomy.items.ItemUtil;
import me.ultrajungleboy.physicaleconomy.shops.CreateGuiShop;
import net.milkbowl.vault.economy.Economy;
import org.betonquest.betonquest.BetonQuest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PhysicalEconomy extends JavaPlugin {
    public static DataManager data;
    private static PhysicalEconomy plugin;
    public static ItemStack placeHolderItem;
    private static Economy econ = null;

    public PhysicalEconomy() {
    }

    public void onEnable() {
        if (!this.setupEconomy()) {
            System.out.println("No economy plugin found. Disabling vault");
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            BetonQuest.getInstance().registerConditions("hasmoney", HasMoneyCondition.class);
            data = new DataManager(this);
            plugin = this;
            ItemManager.init();
            placeHolderItem = ItemUtil.createPlaceHolder();
            CreateGuiShop.CreateNonMoveablePlaceholder();
            BankingUtil.init();
            this.getServer().getPluginManager().registerEvents(new Events(), this);
            this.getServer().getPluginManager().registerEvents(new ItemClicked(), this);
            this.getServer().getPluginManager().registerEvents(new OnInventoryExit(), this);
            this.getCommand("givemoney").setExecutor(new Commands());
            this.getCommand("removemoney").setExecutor(new Commands());
            this.getCommand("itemslot").setExecutor(new Commands());
            this.getCommand("shophand").setExecutor(new Commands());
            this.getCommand("createshop").setExecutor(new Commands());
            this.getCommand("openshop").setExecutor(new Commands());
            this.getCommand("editshop").setExecutor(new Commands());
            this.getCommand("withdraw").setExecutor(new Commands());
            this.getCommand("banking").setExecutor(new Commands());
        }
    }

    public static PhysicalEconomy getPlugin() {
        return plugin;
    }

    public void onDisable() {
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            } else {
                econ = (Economy)rsp.getProvider();
                return econ != null;
            }
        }
    }

    public static Economy getEconomy() {
        return econ;
    }
}
