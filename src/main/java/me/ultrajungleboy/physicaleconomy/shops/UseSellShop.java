package me.ultrajungleboy.physicaleconomy.shops;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.commands.Commands;
import me.ultrajungleboy.physicaleconomy.players.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UseSellShop {
    public UseSellShop() {
    }

    public static void useSellShop(Player player, int amount, Sign sign) {
        String[] simulateargs = new String[]{Integer.toString(amount)};
        PersistentDataContainer signData = sign.getPersistentDataContainer();
        NamespacedKey itemArrayIndex = new NamespacedKey(PhysicalEconomy.getPlugin(), "itemArrayIndex");
        int itemIndex = (Integer)signData.get(itemArrayIndex, PersistentDataType.INTEGER);
        ItemStack itemToTake = PhysicalEconomy.data.getConfig().getItemStack("shops." + itemIndex + ".itemArrayIndex");
        amount = itemToTake.getAmount();
        if (Util.playerHasItems(player, itemToTake)) {
            int i = 0;

            while(amount > 0) {
                int indexofitemtoreplace = player.getInventory().first(itemToTake.getType());
                ItemStack itemtoreplace = player.getInventory().getItem(indexofitemtoreplace);
                if (amount - itemtoreplace.getAmount() < 1) {
                    itemtoreplace.setAmount(itemtoreplace.getAmount() - amount);
                    player.getInventory().setItem(indexofitemtoreplace, itemtoreplace);
                    amount = 0;
                } else {
                    player.getInventory().setItem(indexofitemtoreplace, (ItemStack)null);
                    amount -= itemtoreplace.getAmount();
                }

                ++i;
                if (i >= 40) {
                    player.sendMessage("§4§lCritical ERROR line:41 UseSellShop method in UseSellShop.java please contact an admin.");
                    amount = 0;
                }
            }

            Commands.giveMoney(simulateargs, player);
            if (Integer.parseInt(simulateargs[0]) > 1) {
                player.sendMessage("§eYou received §2$" + Integer.parseInt(simulateargs[0]) + " Dollars§r.");
            } else {
                player.sendMessage("§eYou received §2$" + Integer.parseInt(simulateargs[0]) + " Dollar§r.");
            }
        } else {
            player.sendMessage("You don't have enough items!");
        }

    }
}
