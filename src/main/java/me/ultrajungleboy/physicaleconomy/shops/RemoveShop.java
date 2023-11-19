package me.ultrajungleboy.physicaleconomy.shops;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RemoveShop {
    public RemoveShop() {
    }

    public static void RemoveShop(Sign sign) {
        NamespacedKey itemArrayIndex = new NamespacedKey(PhysicalEconomy.getPlugin(), "itemArrayIndex");
        PersistentDataContainer signData = sign.getPersistentDataContainer();
        int itemArrayIndexNum = (Integer)signData.get(itemArrayIndex, PersistentDataType.INTEGER);
        PhysicalEconomy.data.getConfig().set("shops." + itemArrayIndexNum + ".itemArrayIndex", (Object)null);
        PhysicalEconomy.data.saveConfig();
        PhysicalEconomy.data.reloadConfig();
    }
}
