package me.ultrajungleboy.physicaleconomy.events;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.items.ItemManager;
import me.ultrajungleboy.physicaleconomy.players.Util;
import me.ultrajungleboy.physicaleconomy.shops.RemoveShop;
import me.ultrajungleboy.physicaleconomy.shops.UseBuyShop;
import me.ultrajungleboy.physicaleconomy.shops.UseSellShop;
import java.util.Arrays;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Events implements Listener {
    static boolean createdBuySign;
    static Player playerWhoCreatedBuySign;
    static Sign createdSign;

    public Events() {
    }

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        //int totalamount = false;
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                int totalamount;
                if (event.getItem().getItemMeta().equals(ItemManager.getDollarArray(0, 0).getItemMeta())) {
                    totalamount = showValue(0, event.getItem().getAmount());
                    player.sendMessage("§eYou are holding §2$" + totalamount + " Dollars");
                } else if (event.getItem().getItemMeta().equals(ItemManager.getDollarArray(0, 1).getItemMeta())) {
                    totalamount = showValue(1, event.getItem().getAmount());
                    player.sendMessage("§eYou are holding §2$" + totalamount + " Dollars");
                } else if (event.getItem().getItemMeta().equals(ItemManager.getDollarArray(0, 2).getItemMeta())) {
                    totalamount = showValue(2, event.getItem().getAmount());
                    player.sendMessage("§eYou are holding §2$" + totalamount + " Dollars");
                } else if (event.getItem().getItemMeta().equals(ItemManager.getDollarArray(0, 3).getItemMeta())) {
                    totalamount = showValue(3, event.getItem().getAmount());
                    player.sendMessage("§eYou are holding §2$" + totalamount + " Dollars");
                } else if (event.getItem().getItemMeta().equals(ItemManager.getDollarArray(0, 4).getItemMeta())) {
                    totalamount = showValue(4, event.getItem().getAmount());
                    player.sendMessage("§eYou are holding §2$" + totalamount + " Dollars");
                } else if (event.getItem().getItemMeta().equals(ItemManager.getDollarArray(0, 5).getItemMeta())) {
                    totalamount = showValue(5, event.getItem().getAmount());
                    player.sendMessage("§eYou are holding §2$" + totalamount + " Dollars");
                }
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign)event.getClickedBlock().getState();
                PersistentDataContainer signData = sign.getPersistentDataContainer();
                NamespacedKey itemArrayIndex = new NamespacedKey(PhysicalEconomy.getPlugin(), "itemArrayIndex");
                if (signData.has(itemArrayIndex, PersistentDataType.INTEGER)) {
                    NamespacedKey signType = new NamespacedKey(PhysicalEconomy.getPlugin(), "shopType");
                    NamespacedKey amount = new NamespacedKey(PhysicalEconomy.getPlugin(), "amount");
                    int shopAmount = (Integer)signData.get(amount, PersistentDataType.INTEGER);
                    if (((String)signData.get(signType, PersistentDataType.STRING)).equalsIgnoreCase("buy")) {
                        int amountInInventory = Util.cashOnPlayer(player);
                        if (amountInInventory >= shopAmount) {
                            UseBuyShop.useBuyShop(player, shopAmount, sign);
                        } else if (shopAmount - amountInInventory > 1) {
                            player.sendMessage("You don't have enough money! You are §2$" + (shopAmount - amountInInventory) + " Dollars away");
                        } else {
                            player.sendMessage("You don't have enough money! You are §2$" + (shopAmount - amountInInventory) + " Dollar away");
                        }
                    } else if (((String)signData.get(signType, PersistentDataType.STRING)).equalsIgnoreCase("sell")) {
                        UseSellShop.useSellShop(player, shopAmount, sign);
                    }
                }
            }
        }

    }

    @EventHandler
    public static void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getState() instanceof Sign && player.hasPermission("physicaleconomy.createsignshop")) {
            Sign sign = (Sign)event.getBlock().getState();
            PersistentDataContainer playerData;
            NamespacedKey hasCreatedSellSign;
            NamespacedKey createdSellSignLoc;
            int[] signLoc;
            if (event.getLine(0).equalsIgnoreCase("[buy]")) {
                event.setLine(0, "[§1Buy§r]");
                playerData = player.getPersistentDataContainer();
                hasCreatedSellSign = new NamespacedKey(PhysicalEconomy.getPlugin(), "hasCreatedBuySign");
                createdSellSignLoc = new NamespacedKey(PhysicalEconomy.getPlugin(), "createdBuySign");
                signLoc = new int[]{sign.getLocation().getBlockX(), sign.getLocation().getBlockY(), sign.getLocation().getBlockZ()};
                playerData.set(hasCreatedSellSign, PersistentDataType.INTEGER, 1);
                playerData.set(createdSellSignLoc, PersistentDataType.INTEGER_ARRAY, signLoc);
                player.sendMessage("signLoc " + Arrays.toString((int[])playerData.get(createdSellSignLoc, PersistentDataType.INTEGER_ARRAY)));
                playerWhoCreatedBuySign = event.getPlayer();
                createdSign = sign;
            } else if (event.getLine(0).equalsIgnoreCase("[sell]")) {
                event.setLine(0, "[§1Sell§r]");
                playerData = player.getPersistentDataContainer();
                hasCreatedSellSign = new NamespacedKey(PhysicalEconomy.getPlugin(), "hasCreatedSellSign");
                createdSellSignLoc = new NamespacedKey(PhysicalEconomy.getPlugin(), "createdSellSign");
                signLoc = new int[]{sign.getLocation().getBlockX(), sign.getLocation().getBlockY(), sign.getLocation().getBlockZ()};
                playerData.set(hasCreatedSellSign, PersistentDataType.INTEGER, 1);
                playerData.set(createdSellSignLoc, PersistentDataType.INTEGER_ARRAY, signLoc);
                player.sendMessage("signLoc " + Arrays.toString((int[])playerData.get(createdSellSignLoc, PersistentDataType.INTEGER_ARRAY)));
                playerWhoCreatedBuySign = event.getPlayer();
                createdSign = sign;
            }
        }

    }

    @EventHandler
    public static void onBlockDestruction(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign)event.getBlock().getState();
            PersistentDataContainer signData = sign.getPersistentDataContainer();
            NamespacedKey itemArrayIndex = new NamespacedKey(PhysicalEconomy.getPlugin(), "itemArrayIndex");
            if (signData.has(itemArrayIndex, PersistentDataType.INTEGER)) {
                RemoveShop.RemoveShop(sign);
                player.sendMessage("Shop removed!");
            }
        }

    }

    public static int showValue(int dollarType, int amount) {
        int totalamount = 1;
        switch (dollarType) {
            case 0:
                totalamount = amount;
                break;
            case 1:
                totalamount = amount * 5;
                break;
            case 2:
                totalamount = amount * 10;
                break;
            case 3:
                totalamount = amount * 20;
                break;
            case 4:
                totalamount = amount * 50;
                break;
            case 5:
                totalamount = amount * 100;
        }

        return totalamount;
    }

    public static int showValue(int dollarType) {
        int totalamount = 1;
        switch (dollarType) {
            case 0:
            default:
                break;
            case 1:
                totalamount = 5;
                break;
            case 2:
                totalamount = 10;
                break;
            case 3:
                totalamount = 20;
                break;
            case 4:
                totalamount = 50;
                break;
            case 5:
                totalamount = 100;
        }

        return totalamount;
    }
}
