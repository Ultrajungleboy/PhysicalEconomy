package me.ultrajungleboy.physicaleconomy.shops;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.files.FileManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SaveGuiShop {
    public SaveGuiShop() {
    }

    public static void SaveShop(String shopName, Inventory shopInv) {
        ItemStack[] items = new ItemStack[54];

        for(int i = 0; i < 54; ++i) {
            ItemStack item = shopInv.getItem(i);
            if (item == null) {
                shopInv.setItem(i, PhysicalEconomy.placeHolderItem);
            }

            items[i] = shopInv.getItem(i);
        }

        FileManager.SaveShop(shopName, items);
    }
}