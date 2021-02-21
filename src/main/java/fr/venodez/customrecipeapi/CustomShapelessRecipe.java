package fr.venodez.customrecipeapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CustomShapelessRecipe extends org.bukkit.scheduler.BukkitRunnable implements org.bukkit.event.Listener  {
	
	private Map<UUID, Boolean> players = new HashMap<UUID, Boolean>();
	private ItemStack result;
	private List<ItemStack> ingredients = new ArrayList<ItemStack>();
	
	public CustomShapelessRecipe(ItemStack result) {
		
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin(Main.class));
		this.result = result;

	}
	
	public void addIngredient(ItemStack item) {
		
		ingredients.add(item);
		
	}
	
	public void removeIngredient(ItemStack item) {
		
		 ingredients.remove(item);
		
	}
	
	public ItemStack getResult() {
		
		return result;
		
	}
	
	public List<ItemStack> getIngredients() {
		
		return ingredients;
		
	}
	
	public void register() {
		
		for (int i = ingredients.size();i<9;i++) {
			
			ingredients.add(null);
			
		}
		
		this.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
		
	}
	
	@org.bukkit.event.EventHandler
	private void onInventoryOpen(org.bukkit.event.inventory.InventoryOpenEvent event) {
		
		if (event.getInventory().getName() != "container.crafting") return;
		players.put(event.getPlayer().getUniqueId(), false);
		
	}
	
	@org.bukkit.event.EventHandler
	private void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
		
		UUID uuid = event.getPlayer().getUniqueId();
		
		if (event.getInventory().getName() != "container.crafting") return;
		
		if (players.containsKey(uuid)) {
			
			players.remove(uuid);
		
		}
		
	}
	
	private Boolean areSameItemStack(List<ItemStack> list1, List<ItemStack> list2) {
		
		Map<String, ItemStack> map1 = new HashMap<String, ItemStack>();
		Map<String, ItemStack> map2 = new HashMap<String, ItemStack>();
		List<ItemStack> newList1 = new ArrayList<ItemStack>();
		List<ItemStack> newList2 = new ArrayList<ItemStack>();
		
		for (int i = 0;i<9;i++) {
			
			ItemStack item1 = list1.get(i);
			ItemStack item2 = list2.get(i);
			
			if (item1 != null) {
				
				map1.put(item1.getType().name(), item1);
				
			}
			
			if (item2 != null) {
				
				map2.put(item2.getType().name(), item2);
				
			}
			
		}
		
		List<String> sortedKeys1 = new ArrayList<String>(map1.keySet());
		Collections.sort(sortedKeys1);
		List<String> sortedKeys2 = new ArrayList<String>(map1.keySet());
		Collections.sort(sortedKeys2);
		
		for (int i = 0;i<sortedKeys1.size();i++) {
			
			String key1 = sortedKeys1.get(i);
			newList1.add(map1.get(key1));
			
		}
		
		for (int i = 0;i<sortedKeys2.size();i++) {
			
			String key2 = sortedKeys2.get(i);
			newList2.add(map2.get(key2));
			
		}
		
		return newList1.equals(newList2);
		
	}

	@Override
	public void run() {

		for (UUID uuid : players.keySet()) {
			
			Player player = Bukkit.getPlayer(uuid);
			Inventory inv = player.getOpenInventory().getTopInventory();
			List<ItemStack> itemsPlayer = new ArrayList<ItemStack>();
			
			for (int i = 1; i<10; i++) {
			
				itemsPlayer.add(inv.getItem(i));
				
			}
			
			System.out.println(itemsPlayer);
			System.out.println(ingredients);
			System.out.println(players.get(uuid));
			
			if (areSameItemStack(ingredients, itemsPlayer)) {
			
				if (players.get(uuid) && inv.getItem(0) == null) {
					
					for (int i = 1; i<10;i++) {
							
						inv.setItem(i, null);
						player.updateInventory();
							
					}
						
					players.replace(uuid, false);
					
					
				}
				
				else {
					
					players.replace(uuid, true);
					inv.setItem(0, result);
					player.updateInventory();
					
				}
				
			}
			
			else {
				
				if (players.get(uuid)) {
				
					players.replace(uuid, false);
					inv.setItem(0, null);
					player.updateInventory();
					
				}
				
			}
			
		}
		
	}

}
 