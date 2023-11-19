package me.ultrajungleboy.physicaleconomy.items;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {
    private static ItemStack[][] dollararray = new ItemStack[64][6];
    public static String[] dollardisplayname = new String[6];

    public ItemManager() {
    }

    public static void init() {
        dollardisplayname[0] = "§2$1 Dollar";
        dollardisplayname[1] = "§2$5 Dollars";
        dollardisplayname[2] = "§2$10 Dollars";
        dollardisplayname[3] = "§2$20 Dollars";
        dollardisplayname[4] = "§2$50 Dollars";
        dollardisplayname[5] = "§2$100 Dollars";

        for(int x = 0; x < 6; ++x) {
            for(int i = 0; i < 64; ++i) {
                dollararray[i][x] = createMoney(x, 2000 + x, i + 1, i);
            }
        }

    }

    private static ItemStack createMoney(int displayname, int custommodeldata, int amount, int i) {
        ItemStack item = new ItemStack(Material.FLINT, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dollardisplayname[displayname]);
        meta.setCustomModelData(custommodeldata);
        List<String> lore = new ArrayList();
        lore.add("§2This is MONEY");
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getDollarArray(int amount, int type) {
        ItemStack bufferitem = dollararray[amount][type];
        return bufferitem;
    }
}
