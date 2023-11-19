package me.ultrajungleboy.physicaleconomy.banking;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class OpenBank {
    public OpenBank() {
    }

    public static void OpenBankDeposit(Player player) {
        player.openInventory(CreateBankGui(player, "Deposit"));
    }

    public static void OpenBankWithdraw(Player player) {
        player.openInventory(CreateBankGui(player, "Withdraw"));
    }

    public static Inventory CreateBankGui(Player player, String bankType) {
        Inventory inv = Bukkit.createInventory(player, 45, bankType);
        ItemStack placeholder = PhysicalEconomy.placeHolderItem;
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        ItemStack item = new ItemStack(Material.FLINT);
        ItemMeta itemMeta = item.getItemMeta();

        int i;
        for(i = 0; i < 10; ++i) {
            inv.setItem(i, placeholder);
        }

        for(i = 10; i < 13; ++i) {
            inv.setItem(i, ItemManager.getDollarArray(0, i - 10));
        }

        inv.setItem(13, placeholder);

        for(i = 14; i < 17; ++i) {
            inv.setItem(i, ItemManager.getDollarArray(0, i - 11));
        }

        for(i = 17; i < 27; ++i) {
            inv.setItem(i, placeholder);
        }

        item.setType(Material.RED_CONCRETE);
        itemMeta.setDisplayName("§cCANCEL");
        item.setItemMeta(itemMeta);

        for(i = 36; i < 40; ++i) {
            inv.setItem(i, item);
        }

        for(i = 27; i < 29; ++i) {
            inv.setItem(i, item);
        }

        itemMeta.setDisplayName("Zero");
        itemMeta.setCustomModelData(2100);
        item.setType(Material.FLINT);
        item.setItemMeta(itemMeta);

        for(i = 29; i < 34; ++i) {
            inv.setItem(i, item);
        }

        inv.setItem(40, placeholder);
        if (bankType.equals("Deposit")) {
            item.setType(Material.GOLD_BLOCK);
            itemMeta.setDisplayName("§6DEPOSIT ALL");
            item.setItemMeta(itemMeta);
            inv.setItem(40, item);
        }

        item.setType(Material.LIME_CONCRETE);
        itemMeta.setDisplayName("§aACCEPT");
        item.setItemMeta(itemMeta);

        for(i = 41; i < 45; ++i) {
            inv.setItem(i, item);
        }

        for(i = 34; i < 36; ++i) {
            inv.setItem(i, item);
        }

        return inv;
    }
}
