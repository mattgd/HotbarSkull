package me.mattgd.hotbarskull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * HotbarSkull plugin main class.
 * 
 * @author mattgd
 */
public class HotbarSkull extends JavaPlugin implements Listener {
	
	/** Inventory slot to place the skull in (from configuration) */
	private int skullSlot = 0;
	
    /**
     * Enable the HotbarSkull plugin.
     */
	@Override
	public void onEnable() {
 		saveDefaultConfig(); // Create default the configuration if config.yml doesn't exist
 		skullSlot = getConfig().getInt("skull-slot", 0);
		getCommand("startup").setExecutor(this); // Setup commands
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Enabled!");
	}
	
	/**
     * Disable the HotbarSkull plugin.
     */
	@Override
	public void onDisable() {       
		getConfig().options().copyDefaults(true);
		getLogger().info("Disabled!");
	}
	
	/**
	 * Call the appropriate command based on player command input, or
	 * show plugin information or reload the plugin if specified.
	 * @return true always
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageManager msg = MessageManager.getInstance();
		
		if (args.length == 0) {
			msg.good(sender, helpMessage());
			return true;
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("give")) {
				msg.severe(sender, "You must specify a player to give a skull to.");
			} else if (args[0].equalsIgnoreCase("help")) {
				msg.good(sender, helpMessage());
			} else {
				msg.severe(sender, "Invalid command usage. Type /hotbarskull help for help.");
			}
		} else if (args.length > 1) {
			if (args[0].equalsIgnoreCase("give")) {
				String playerName = args[1];
				
				Player p = Bukkit.getPlayer(playerName);
				
				if (p == null) {
					msg.severe(sender, "Invalid player " + playerName + ".");
				} else {
					p.getInventory().setItem(skullSlot, getPlayerSkull(p));
					msg.good(sender, p.getName() + " has been given their skull.");
				}
			} else if (args[0].equalsIgnoreCase("giveall")) {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					p.getInventory().setItem(skullSlot, getPlayerSkull(p));
				}
				
				msg.good(sender, "All online players have been given their skulls.");
			} else {
				msg.severe(sender, "Invalid command usage. Type /hotbarskull help for help.");
			}
		} else {
			msg.good(sender, helpMessage());
		}
		
		return true;
	}
	
	/**
	 * Returns a String with the StartupCommands help message.
	 * @return a String with the StartupCommands help message.
	 */
	private String helpMessage() {
		MessageManager msg = MessageManager.getInstance();
		String msgStr = msg.messageTitle("HotbarSkull Help", ChatColor.AQUA, ChatColor.YELLOW);
		
		msgStr += "\n&a/hotbarskull give <player> &7- &amanually give a player their skull in the configuration specified inventory slot"
				+ "\n&a/hotbarskull giveall &7- &agive all online players their skull in the configuration specified inventory slot";
		
		msgStr += msg.messageTrail(ChatColor.YELLOW); // Add message trail
		return msgStr;
	}
	
	/**
	 * Creates a player skull ItemStack for Player p.
	 * @param p The skull owner.
	 * @return a player skull ItemStack for Player p.
	 */
	private ItemStack getPlayerSkull(Player p) {
		ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta sm = (SkullMeta) is.getItemMeta();
		sm.setOwner(p.getName());
		is.setItemMeta(sm);
		
		return is;
	}
	
	/**
	 * Gives players their player skull on join.
	 * @param e The PlayerJoinEvent instance.
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		PlayerInventory inv = e.getPlayer().getInventory();
		
		if (inv != null) {
			if (!inv.getContents()[skullSlot].getType().equals(Material.SKULL_ITEM)) {
				inv.setItem(skullSlot, getPlayerSkull(e.getPlayer()));
			}
		}
	}
	
	/**
	 * Prevents player from removing the player skull from their inventory.
	 * @param e The InventoryClickEvent instance.
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.isCancelled())
			return;
		
		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		
		if (inv != null && item != null) {
			if (e.getSlot() == skullSlot && item.getType().equals(Material.SKULL_ITEM)) {
				e.setCancelled(true);
			}
		}
	}
	
	/**
	 * Prevents player from dropping the player skull from their inventory.
	 * @param e The PlayerDropItemEvent instance.
	 */
	@EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (e.isCancelled())
			return;
		
		ItemStack item = e.getItemDrop().getItemStack();
		
		if (item.getType().equals(Material.SKULL_ITEM) 
				&& item.getItemMeta().getDisplayName().equals(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
    }
	
}
