package me.ultrajungleboy.physicaleconomy.commands;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.banking.BankingUtil;
import me.ultrajungleboy.physicaleconomy.events.Events;
import me.ultrajungleboy.physicaleconomy.files.FileManager;
import me.ultrajungleboy.physicaleconomy.items.ItemManager;
import me.ultrajungleboy.physicaleconomy.shops.CreateGuiShop;
import me.ultrajungleboy.physicaleconomy.shops.CreateShop;
import me.ultrajungleboy.physicaleconomy.shops.UpdateShop;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Commands implements CommandExecutor {
    public Commands() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player;
        PersistentDataContainer playerData;
        NamespacedKey ItemIndexKey;
        NamespacedKey ValueAmountKey;
        ItemStack[] items;
        NamespacedKey ShopTypeKey;
        ItemMeta itemMeta;
        if (cmd.getName().equalsIgnoreCase("openshop")) {
            if (args.length > 1) {
                player = Bukkit.getPlayer(args[1]);
            } else {
                player = (Player)sender;
                if (!player.hasPermission("physicaleconomy.openshop")) {
                    player.sendMessage("You cannot open shops freely!");
                    return false;
                }
            }

            if (args.length == 0) {
                player.sendMessage("/openshop shopname");
                return false;
            } else if (!FileManager.DoesShopExist(args[0])) {
                player.sendMessage("That shop doesnt exist");
                return false;
            } else {
                NamespacedKey quantityKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "Quantity");
                playerData = player.getPersistentDataContainer();
                playerData.set(quantityKey, PersistentDataType.INTEGER, 1);
                Inventory inv = Bukkit.createInventory(player, 54, args[0]);
                inv.setContents(FileManager.LoadShop(args[0]));
                items = FileManager.LoadShop(args[0]);
                ItemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopName");
                ValueAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ValueAmount");
                //ShopTypeKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingShop");
                NamespacedKey viewingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingShop");
                ShopTypeKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopType");

                int i;
                for(i = 0; i < 45; ++i) {
                    itemMeta = items[i].getItemMeta();
                    PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
                    if (!itemMeta.getDisplayName().equalsIgnoreCase(" ") && itemData.has(ShopTypeKey, PersistentDataType.STRING)) {
                        if (((String)itemData.get(ShopTypeKey, PersistentDataType.STRING)).equalsIgnoreCase("buy")) {
                            itemMeta.setLore(Collections.singletonList("§2BUY: $" + itemData.get(ValueAmountKey, PersistentDataType.INTEGER)));
                            items[i].setItemMeta(itemMeta);
                        } else if (((String)itemData.get(ShopTypeKey, PersistentDataType.STRING)).equalsIgnoreCase("sell")) {
                            itemMeta.setLore(Collections.singletonList("§4SELL: $" + itemData.get(ValueAmountKey, PersistentDataType.INTEGER)));
                            items[i].setItemMeta(itemMeta);
                        }
                    }
                }

                for(i = 45; i < 49; ++i) {
                    items[i].setType(Material.FLINT);
                    itemMeta = items[i].getItemMeta();
                    itemMeta.setCustomModelData(2114 + i - 45);
                    items[i].setItemMeta(itemMeta);
                }

                items[49].setType(Material.BLACK_CONCRETE);
                items[53].setType(Material.LIME_WOOL);
                items[53].getItemMeta().setDisplayName("Accept");
                inv.setContents(items);
                playerData.set(viewingShopKey, PersistentDataType.INTEGER, 1);
                playerData.set(ShopTypeKey, PersistentDataType.INTEGER, 1);
                playerData.set(ItemIndexKey, PersistentDataType.STRING, args[0]);
                UpdateShop.UpdateShopInventory(player, inv);
                player.openInventory(inv);
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("withdraw")) {
            if (args.length > 1) {
                player = Bukkit.getPlayer(args[1]);
            } else {
                player = (Player)sender;
                if (!player.hasPermission("physicaleconomy.withdraw")) {
                    player.sendMessage("You cannot withdraw freely!");
                    return false;
                }
            }

            if (args.length == 0) {
                player.sendMessage("/openbank");
                return false;
            } else {
                int amount = Integer.parseInt(args[0]);
                if (BankingUtil.getBalance(player) >= (double)amount && BankingUtil.WithdrawMoney(player, amount)) {
                    String[] simulateargs = new String[]{args[0]};
                    int amountDropped = giveMoney(simulateargs, player);
                    player.sendMessage("amountDropped = " + amountDropped);
                    if (amountDropped == 0) {
                        player.sendMessage("You withdrew a §2$" + amount + " Dollar§r Bill");
                    } else {
                        player.sendMessage("You withdrew and dropped a §2$" + amount + " Dollar§r Bill");
                    }
                }

                return true;
            }
        } else {
            NamespacedKey creatingShopKey;
            NamespacedKey namespacedKey;
            Inventory inv;
            int balance;
            if (cmd.getName().equalsIgnoreCase("banking")) {
                if (args.length > 0) {
                    player = Bukkit.getPlayer(args[0]);
                } else {
                    player = (Player)sender;
                }

                if (sender instanceof Player && !player.hasPermission("physicaleconomy.banking")) {
                    player.sendMessage("You cannot withdraw freely!");
                    return false;
                } else {
                    playerData = player.getPersistentDataContainer();
                    namespacedKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingBank");
                    creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "BillAmount");
                    inv = Bukkit.createInventory(player, 18, "Banking");
                    ItemStack placeHolderItem = PhysicalEconomy.placeHolderItem;

                    for(int i = 0; i < inv.getSize(); ++i) {
                        inv.setItem(i, placeHolderItem);
                    }

                    inv.setItem(0, placeHolderItem);
                    ItemStack item = new ItemStack(Material.FLINT);
                    itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName("Withdraw");
                    itemMeta.setCustomModelData(2121);
                    item.setItemMeta(itemMeta);
                    inv.setItem(1, item);
                    itemMeta.setCustomModelData(2122);
                    item.setItemMeta(itemMeta);
                    inv.setItem(2, item);

                    for(balance = 3; balance < 6; ++balance) {
                        inv.setItem(balance, placeHolderItem);
                    }

                    itemMeta.setDisplayName("Deposit");
                    item.setItemMeta(itemMeta);
                    inv.setItem(6, item);
                    itemMeta.setCustomModelData(2120);
                    item.setItemMeta(itemMeta);
                    inv.setItem(7, item);
                    inv.setItem(8, placeHolderItem);
                    player.openInventory(inv);
                    playerData.set(namespacedKey, PersistentDataType.INTEGER, 1);
                    playerData.set(creatingShopKey, PersistentDataType.STRING, "00:00:00:00:00:00");
                    balance = (int)BankingUtil.getBalance(player);
                    itemMeta.setCustomModelData(2100);
                    itemMeta.setDisplayName(" ");
                    item.setItemMeta(itemMeta);

                    int numberposition;
                    for(numberposition = 9; numberposition < 18; ++numberposition) {
                        inv.setItem(numberposition, item);
                    }

                    numberposition = 17;

                    for(int i = 1; i <= 100000000; i *= 10) {
                        if (balance / 10 < 1) {
                            if (balance >= 1) {
                                itemMeta.setCustomModelData(2100 + balance);
                                item.setItemMeta(itemMeta);
                                inv.setItem(numberposition, item);
                            }

                            return true;
                        }

                        itemMeta.setCustomModelData(2100 + balance % 10);
                        item.setItemMeta(itemMeta);
                        inv.setItem(numberposition, item);
                        --numberposition;
                        balance /= 10;
                    }

                    return true;
                }
            } else if (cmd.getName().equalsIgnoreCase("givemoney")) {
                try {
                    String[] tempargs = new String[1];
                    if (sender instanceof Player) {
                        player = (Player)sender;
                        if (!player.hasPermission("physicaleconomy.givemoney")) {
                            player.sendMessage("You cannot give money!");
                            return false;
                        }

                        if (args.length == 1) {
                            giveMoney(args, player);
                            return true;
                        }
                    } else if (args.length == 1) {
                        sender.sendMessage("givemoney player amount");
                        return false;
                    }

                    tempargs[0] = args[1];
                    player = Bukkit.getPlayer(args[0]);
                    giveMoney(tempargs, player);
                    return true;
                } catch (IllegalArgumentException var19) {
                    sender.sendMessage("Please input a valid number");
                    return false;
                }
            } else if (cmd.getName().equalsIgnoreCase("removemoney")) {
                try {
                    if (sender instanceof Player) {
                        player = (Player)sender;
                        if (!player.hasPermission("physicaleconomy.removemoney")) {
                            player.sendMessage("You cannot remove money!");
                            return false;
                        }

                        if (args.length == 0) {
                            removeMoney(Integer.parseInt(args[1]), player);
                            return true;
                        }
                    } else if (args.length == 1) {
                        sender.sendMessage("removemoney player amount");
                        return false;
                    }

                    player = Bukkit.getPlayer(args[0]);
                    removeMoney(Integer.parseInt(args[1]), player);
                    return true;
                } catch (IllegalArgumentException var20) {
                    sender.sendMessage("Please input a valid number");
                    return false;
                }
            } else if (!(sender instanceof Player)) {
                sender.sendMessage("only players can use that command");
                return false;
            } else {
                player = (Player)sender;
                if (cmd.getName().equalsIgnoreCase("shophand")) {
                    if (args.length != 2) {
                        player.sendMessage("/shophand <dollars> <sell/buy>");
                        return true;
                    }

                    Sign sign = null;
                    playerData = player.getPersistentDataContainer();
                    creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "hasCreatedBuySign");
                    NamespacedKey createdBuySignLoc = new NamespacedKey(PhysicalEconomy.getPlugin(), "createdBuySign");
                    ItemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "hasCreatedSellSign");
                    ValueAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "createdSellSign");
                    int[] xyz;
                    Location signLoc;
                    if (playerData.has(creatingShopKey, PersistentDataType.INTEGER) && (Integer)playerData.get(creatingShopKey, PersistentDataType.INTEGER) == 1 && args[1].equalsIgnoreCase("buy")) {
                        xyz = (int[])playerData.get(createdBuySignLoc, PersistentDataType.INTEGER_ARRAY);
                        signLoc = new Location(player.getWorld(), (double)xyz[0], (double)xyz[1], (double)xyz[2]);
                        if (signLoc.getBlock().getState() instanceof Sign) {
                            sign = (Sign)signLoc.getBlock().getState();
                        }

                        new NamespacedKey(PhysicalEconomy.getPlugin(), "signType");
                        new NamespacedKey(PhysicalEconomy.getPlugin(), "signType");
                        new NamespacedKey(PhysicalEconomy.getPlugin(), "signType");
                        ItemStack item = new ItemStack(player.getInventory().getItemInMainHand());
                        PersistentDataContainer signData = sign.getPersistentDataContainer();
                        CreateShop.CreateShop(item, sign, Integer.parseInt(args[0]), "buy");
                        player.sendMessage("Buy Shop Created!");
                        playerData.set(creatingShopKey, PersistentDataType.INTEGER, 0);
                    } else if (playerData.has(ItemIndexKey, PersistentDataType.INTEGER) && (Integer)playerData.get(ItemIndexKey, PersistentDataType.INTEGER) == 1 && args[1].equalsIgnoreCase("sell")) {
                        xyz = (int[])playerData.get(ValueAmountKey, PersistentDataType.INTEGER_ARRAY);
                        signLoc = new Location(player.getWorld(), (double)xyz[0], (double)xyz[1], (double)xyz[2]);
                        if (signLoc.getBlock().getState() instanceof Sign) {
                            sign = (Sign)signLoc.getBlock().getState();
                        }

                        ItemStack item = new ItemStack(player.getInventory().getItemInMainHand());
                        CreateShop.CreateShop(item, sign, Integer.parseInt(args[0]), "sell");
                        player.sendMessage("Sell Shop Created!");
                        playerData.set(ItemIndexKey, PersistentDataType.INTEGER, 0);
                    } else {
                        player.sendMessage("Please create a buy/sell sign first.");
                    }
                } else if (cmd.getName().equalsIgnoreCase("createshop") && player.hasPermission("physicaleconomy.createshop")) {
                    if (args.length == 0) {
                        player.sendMessage("You need to specify a shop name");
                        return false;
                    }

                    playerData = player.getPersistentDataContainer();
                    namespacedKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopName");
                    creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "CreatingShop");
                    PersistentDataContainer itemData;
                    //Inventory inv;
                    if (args[0].equalsIgnoreCase("buy")) {
                        if (args.length == 1) {
                            player.sendMessage("Please specify an amount");
                            return false;
                        }

                        items = FileManager.LoadShop((String)playerData.get(namespacedKey, PersistentDataType.STRING));
                        ItemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ItemIndex");
                        ValueAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ValueAmount");
                        ShopTypeKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopType");
                        balance = (Integer)playerData.get(ItemIndexKey, PersistentDataType.INTEGER);
                        itemMeta = items[balance].getItemMeta();
                        itemData = itemMeta.getPersistentDataContainer();
                        itemData.set(ValueAmountKey, PersistentDataType.INTEGER, Integer.parseInt(args[1]));
                        itemData.set(ShopTypeKey, PersistentDataType.STRING, "buy");
                        items[balance].addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
                        items[balance].setItemMeta(itemMeta);
                        inv = Bukkit.createInventory(player, 54, (String)playerData.get(namespacedKey, PersistentDataType.STRING));
                        inv.setContents(items);
                        player.openInventory(inv);
                        playerData.set(creatingShopKey, PersistentDataType.INTEGER, 1);
                    } else if (args[0].equalsIgnoreCase("sell")) {
                        if (args.length == 1) {
                            player.sendMessage("Please specify an amount");
                            return false;
                        }

                        items = FileManager.LoadShop((String)playerData.get(namespacedKey, PersistentDataType.STRING));
                        ItemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ItemIndex");
                        ValueAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ValueAmount");
                        ShopTypeKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopType");
                        balance = (Integer)playerData.get(ItemIndexKey, PersistentDataType.INTEGER);
                        itemMeta = items[balance].getItemMeta();
                        itemData = itemMeta.getPersistentDataContainer();
                        itemData.set(ValueAmountKey, PersistentDataType.INTEGER, Integer.parseInt(args[1]));
                        itemData.set(ShopTypeKey, PersistentDataType.STRING, "sell");
                        items[balance].setItemMeta(itemMeta);
                        inv = Bukkit.createInventory(player, 54, (String)playerData.get(namespacedKey, PersistentDataType.STRING));
                        inv.setContents(items);
                        player.openInventory(inv);
                        playerData.set(creatingShopKey, PersistentDataType.INTEGER, 1);
                    } else if (FileManager.DoesShopExist(args[0])) {
                        player.sendMessage("Error saving to file.yml");
                    } else {
                        playerData.set(namespacedKey, PersistentDataType.STRING, args[0]);
                        playerData.set(creatingShopKey, PersistentDataType.INTEGER, 1);
                        CreateGuiShop gui = new CreateGuiShop();
                        player.openInventory(gui.getInventory());
                    }
                } else if (cmd.getName().equalsIgnoreCase("editshop") && player.hasPermission("physicaleconomy.editshop")) {
                    if (args.length == 0) {
                        player.sendMessage("You need to specify a shop to open");
                    }

                    playerData = player.getPersistentDataContainer();
                    namespacedKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopName");
                    creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "CreatingShop");
                    playerData.set(creatingShopKey, PersistentDataType.INTEGER, 1);
                    playerData.set(namespacedKey, PersistentDataType.STRING, args[0]);
                    inv = Bukkit.createInventory(player, 54, args[0]);
                    inv.setContents(FileManager.LoadShop(args[0]));
                    player.openInventory(inv);
                }

                return true;
            }
        }
    }

    public static boolean removeMoney(int removeamount, Player player) {
        if (removeamount < 0) {
            player.sendMessage("§eYou can't remove negative values, use /givemoney <amt> for that");
            return true;
        } else {
            HashMap<Integer, ? extends ItemStack> potentialslots = player.getInventory().all(Material.FLINT);
            int[] intArray = convertHashMapToIntArray(potentialslots);
            ItemStack[] itemArray = convertHashMapToItemStackArray(potentialslots);
            int[] removeamounts = new int[2];
            int amountininventory = 0;
            List<ItemStack> removeindex = new ArrayList();
            List<Integer> setindex = new ArrayList();
            List<Integer> setamtindex = new ArrayList();
            List<Integer> setdollartypeindex = new ArrayList();

            int i;
            for(i = 5; i > -1; --i) {
                for(int x = 0; x < itemArray.length; ++x) {
                    if (itemArray[x].getItemMeta().hasCustomModelData() && itemArray[x].getItemMeta().getCustomModelData() == 2000 + i) {
                        int totalamount = Events.showValue(i, itemArray[x].getAmount());
                        if (totalamount > removeamount) {
                            int amttoberemoved = removeamount / Events.showValue(i);
                            int amttoset = itemArray[x].getAmount() - amttoberemoved - 1;
                            setindex.add(intArray[x]);
                            setamtindex.add(amttoset);
                            setdollartypeindex.add(i);
                            removeamount -= Events.showValue(i, amttoberemoved);
                        } else if (totalamount == removeamount) {
                            removeamount -= removeamount;
                            removeindex.add(itemArray[x]);
                        } else {
                            removeamount -= totalamount;
                            removeindex.add(itemArray[x]);
                        }

                        amountininventory += totalamount;
                    }
                }
            }

            if (removeamount > amountininventory) {
                player.sendMessage("You dont have enough money!");
                return true;
            } else {
                for(i = 0; i < setindex.size(); ++i) {
                    player.getInventory().setItem((Integer)setindex.get(i), ItemManager.getDollarArray((Integer)setamtindex.get(i), (Integer)setdollartypeindex.get(i)));
                }

                for(i = 0; i < removeindex.size(); ++i) {
                    player.getInventory().removeItem(new ItemStack[]{(ItemStack)removeindex.get(i)});
                }

                for(i = 4; i > -1; --i) {
                    if (removeamount == removeamount) {
                        String[] customargs = new String[]{Integer.toString(Events.showValue(i))};
                        removeamounts = replaceBiggestDollar(player, removeamount, setdollartypeindex, setindex, customargs, i);
                        removeamount = removeamounts[0];
                    } else {
                        i = -1;
                    }
                }

                if (removeamount > 1) {
                    player.sendMessage("§2$" + removeamount + " Dollars §ehave been removed from your Inventory");
                } else if (removeamount == 1) {
                    player.sendMessage("§2$" + removeamount + " Dollar §ehas been removed from your Inventory");
                }

                if (removeamounts[1] > 0) {
                    player.sendMessage("§eYour inventory doesn't have any slots for your change! You dropped: §2$" + removeamounts[1] + " of it!");
                }

                return false;
            }
        }
    }

    private static int[] replaceBiggestDollar(Player player, int removeamount, List<Integer> setdollartype, List<Integer> setindex, String[] customargs, int dollartype) {
        int[] returnamounts = new int[]{removeamount, 0};
        if (removeamount / Events.showValue(dollartype) >= 1) {
            int index = 0;
            int dollaroffset = 0;

            do {
                ++dollaroffset;
                index = setdollartype.indexOf(dollartype + dollaroffset);
                if (dollaroffset > 5) {
                    player.sendMessage("§4§lCritical ERROR line:127 getRemoveAmount method in Commands.java please contact an admin.");
                    return returnamounts;
                }
            } while(index == -1);

            index = (Integer)setindex.get(index);
            if (player.getInventory().getItem(index).getAmount() - 2 > -1) {
                player.getInventory().setItem(index, ItemManager.getDollarArray(player.getInventory().getItem(index).getAmount() - 2, dollartype + dollaroffset));
            } else {
                player.getInventory().removeItem(new ItemStack[]{player.getInventory().getItem(index)});
            }

            customargs[0] = Integer.toString(Events.showValue(dollartype + dollaroffset) - removeamount);
            returnamounts[1] = giveMoney(customargs, player, false);
            returnamounts[0] = 0;
        }

        return returnamounts;
    }

    public static int giveMoney(String[] args, Player player) {
        int amount = 0;
        int giveamount = 0;
        int amountdropped = 0;
        switch (args.length) {
            case 0:
                giveamount = 1;
                break;
            case 1:
                giveamount = Integer.parseInt(args[0]);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + args.length);
        }

        String[] customargs = new String[2];

        for(int i = 5; i >= 0; --i) {
            if (giveamount / Events.showValue(i) >= 1) {
                customargs[0] = Integer.toString(giveamount / Events.showValue(i));
                customargs[1] = Integer.toString(i);
                Integer[] givemoneyitemsarray = giveMoneyItems(customargs, player);
                giveamount -= giveamount / Events.showValue(i) * Events.showValue(i);
                amountdropped += givemoneyitemsarray[1];
                amount += givemoneyitemsarray[0];
            }
        }

        player.sendMessage("§eYou have been given: §2$" + args[0] + " Dollars");
        if (amountdropped > 0) {
            player.sendMessage("§eBut your inventory is full! You dropped: §2$" + amountdropped + " of it!");
        }

        return amountdropped;
    }

    public static int giveMoney(int money, Player player) {
        int giveamount = 0;
        int amountdropped = 0;
        String[] customargs = new String[2];

        for(int i = 5; i >= 0; --i) {
            if (money / Events.showValue(i) >= 1) {
                customargs[0] = Integer.toString(money / Events.showValue(i));
                customargs[1] = Integer.toString(i);
                Integer[] givemoneyitemsarray = giveMoneyItems(customargs, player);
                money -= money / Events.showValue(i) * Events.showValue(i);
                amountdropped += givemoneyitemsarray[1];
                giveamount += givemoneyitemsarray[0];
            }
        }

        player.sendMessage("§eYou have been given: §2$" + money + " Dollars");
        if (amountdropped > 0) {
            player.sendMessage("§eBut your inventory is full! You dropped: §2$" + amountdropped + " of it!");
        }

        return amountdropped;
    }

    public static int giveMoney(String[] args, Player player, boolean notification) {
        int amount = 0;
        int giveamount = 0;
        int amountdropped = 0;
        switch (args.length) {
            case 0:
                giveamount = 1;
                break;
            case 1:
                giveamount = Integer.parseInt(args[0]);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + args.length);
        }

        String[] customargs = new String[2];

        for(int i = 5; i >= 0; --i) {
            if (giveamount / Events.showValue(i) >= 1) {
                customargs[0] = Integer.toString(giveamount / Events.showValue(i));
                customargs[1] = Integer.toString(i);
                Integer[] givemoneyitemsarray = giveMoneyItems(customargs, player);
                giveamount -= giveamount / Events.showValue(i) * Events.showValue(i);
                amountdropped += givemoneyitemsarray[1];
                amount += givemoneyitemsarray[0];
            }
        }

        if (notification) {
            player.sendMessage("§eYou have been given: §2$" + args[0] + " Dollars");
        }

        if (amountdropped > 0) {
            player.sendMessage("§eBut your inventory is full! You dropped: §2$" + amountdropped + " of it!");
        }

        return amountdropped;
    }

    public static Integer[] giveMoneyItems(String[] args, Player player) {
        int dollarcmd = 2000;
        int dollartype = 0;
        int amountdropped = 0;
        int amount;
        switch (args.length) {
            case 0:
                amount = 1;
                break;
            case 2:
                dollartype = Integer.parseInt(args[1]);
            case 1:
                amount = Integer.parseInt(args[0]);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + args.length);
        }

        int[] tempamount = new int[]{amount};
        HashMap<Integer, ? extends ItemStack> potentialslots = player.getInventory().all(Material.FLINT);
        List<Integer> fillindex = new ArrayList();
        switch (dollartype) {
            case 1:
                dollarcmd = 2001;
                break;
            case 2:
                dollarcmd = 2002;
                break;
            case 3:
                dollarcmd = 2003;
                break;
            case 4:
                dollarcmd = 2004;
                break;
            case 5:
                dollarcmd = 2005;
        }

        int finalDollarcmd = dollarcmd;
        potentialslots.entrySet().forEach((entry) -> {
            if (((ItemStack)entry.getValue()).getItemMeta().hasCustomModelData() && ((ItemStack)entry.getValue()).getItemMeta().getCustomModelData() == finalDollarcmd && ((ItemStack)entry.getValue()).getAmount() < 64 && 64 - ((ItemStack)entry.getValue()).getAmount() < tempamount[0]) {
                fillindex.add((Integer)entry.getKey());
                tempamount[0] -= 64 - ((ItemStack)entry.getValue()).getAmount();
            }

        });
        int stacks;
        if (fillindex.size() <= 0 && amount <= 64) {
            stacks = player.getInventory().firstEmpty();
            if (stacks == -1) {
                player.getLocation().getWorld().dropItem(player.getLocation(), ItemManager.getDollarArray(tempamount[0] - 1, dollartype));
                amountdropped += tempamount[0];
            } else {
                player.getInventory().addItem(new ItemStack[]{ItemManager.getDollarArray(tempamount[0] - 1, dollartype)});
            }
        } else {
            for(stacks = 0; stacks < fillindex.size(); ++stacks) {
                player.getInventory().setItem((Integer)fillindex.get(stacks), ItemManager.getDollarArray(63, dollartype));
            }

            stacks = tempamount[0] / 64;

            int emptyIndex;
            for(emptyIndex = 0; emptyIndex < stacks; ++emptyIndex) {
                emptyIndex = player.getInventory().firstEmpty();
                if (emptyIndex == -1) {
                    player.getLocation().getWorld().dropItem(player.getLocation(), ItemManager.getDollarArray(63, dollartype));
                    amountdropped += 64;
                } else {
                    player.getInventory().setItem(emptyIndex, ItemManager.getDollarArray(63, dollartype));
                }
            }

            tempamount[0] -= stacks * 64;
            emptyIndex = player.getInventory().firstEmpty();
            if (emptyIndex == -1) {
                player.getLocation().getWorld().dropItem(player.getLocation(), ItemManager.getDollarArray(tempamount[0] - 1, dollartype));
                amountdropped += tempamount[0];
            } else {
                player.getInventory().setItem(emptyIndex, ItemManager.getDollarArray(tempamount[0] - 1, dollartype));
            }
        }

        Integer[] returnArray = new Integer[]{Events.showValue(dollartype, amount), Events.showValue(dollartype, amountdropped)};
        fillindex.clear();
        return returnArray;
    }

    private static int[] convertHashMapToIntArray(HashMap<Integer, ? extends ItemStack> hashMap) {
        int[] i = new int[]{0};
        int[] intArray = new int[hashMap.size()];
        hashMap.entrySet().forEach((entry) -> {
            intArray[i[0]] = (Integer)entry.getKey();
            int var10002 = i[0]++;
        });
        return intArray;
    }

    private static ItemStack[] convertHashMapToItemStackArray(HashMap<Integer, ? extends ItemStack> hashMap) {
        int[] i = new int[]{0};
        ItemStack[] itemArray = new ItemStack[hashMap.size()];
        hashMap.entrySet().forEach((entry) -> {
            itemArray[i[0]] = (ItemStack)entry.getValue();
            int var10002 = i[0]++;
        });
        return itemArray;
    }
}
