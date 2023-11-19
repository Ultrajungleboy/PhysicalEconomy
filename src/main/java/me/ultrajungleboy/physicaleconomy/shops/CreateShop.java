package me.ultrajungleboy.physicaleconomy.shops;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CreateShop {
    public CreateShop() {
    }

    public static void CreateShop(ItemStack item, Sign sign, int amount, String shopType) {
        NamespacedKey shopTypeNameSpace = new NamespacedKey(PhysicalEconomy.getPlugin(), "shopType");
        NamespacedKey amountNameSpace = new NamespacedKey(PhysicalEconomy.getPlugin(), "amount");
        NamespacedKey itemArrayIndex = new NamespacedKey(PhysicalEconomy.getPlugin(), "itemArrayIndex");
        int itemArrayIndexNum = 0;
        PersistentDataContainer signData = sign.getPersistentDataContainer();
        int loadedcounter = 0;
        if (PhysicalEconomy.data.getConfig().contains("shops.counter")) {
            loadedcounter = PhysicalEconomy.data.getConfig().getInt("shops.counter");
        }

        int newcounter = loadedcounter;
        boolean ranThroughArray = true;

        for(int i = 0; i < loadedcounter; ++i) {
            if (!PhysicalEconomy.data.getConfig().contains("shops." + i + ".itemArrayIndex")) {
                itemArrayIndexNum = i;
                i = loadedcounter;
                ranThroughArray = false;
            } else {
                ranThroughArray = true;
            }
        }

        if (ranThroughArray) {
            itemArrayIndexNum = loadedcounter;
            newcounter = loadedcounter + 1;
        }

        PhysicalEconomy.data.getConfig().set("shops." + itemArrayIndexNum + ".itemArrayIndex", item);
        PhysicalEconomy.data.getConfig().set("shops.counter", newcounter);
        signData.set(shopTypeNameSpace, PersistentDataType.STRING, shopType);
        signData.set(amountNameSpace, PersistentDataType.INTEGER, amount);
        signData.set(itemArrayIndex, PersistentDataType.INTEGER, itemArrayIndexNum);
        PhysicalEconomy.data.saveConfig();
        PhysicalEconomy.data.reloadConfig();
        sign.update(true);
    }
}
