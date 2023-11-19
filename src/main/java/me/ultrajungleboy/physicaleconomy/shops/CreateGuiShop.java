package me.ultrajungleboy.physicaleconomy.shops;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.items.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CreateGuiShop implements InventoryHolder {
    private Inventory inv = Bukkit.createInventory(this, 54, "ShopTemplate");
    static ItemStack placeholder = ItemUtil.createPlaceHolder();
    static NamespacedKey namespacedKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "Placeholder");
    static ItemMeta itemMeta;

    public CreateGuiShop() {
        this.init();
    }

    public static void CreateNonMoveablePlaceholder() {
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, 1);
        placeholder.setItemMeta(itemMeta);
    }

    private void init() {
        for(int i = 45; i < 54; ++i) {
            this.inv.setItem(i, placeholder);
        }

    }

    public Inventory getInventory() {
        return this.inv;
    }

    static {
        itemMeta = placeholder.getItemMeta();
    }
}
