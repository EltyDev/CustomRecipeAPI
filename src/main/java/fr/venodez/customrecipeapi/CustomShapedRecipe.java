package fr.venodez.customrecipeapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CustomShapedRecipe extends org.bukkit.scheduler.BukkitRunnable implements org.bukkit.event.Listener  {
	
	private HashMap<UUID, Boolean> players = new HashMap<UUID, Boolean>();
	private Plugin plugin;
	private ItemStack result;
	private List<String> shapeStrings = new ArrayList<String>();
	private Map<Integer, Character> shape = new HashMap<Integer, Character>();
	private Map<Character, ItemStack> ingredients = new HashMap<Character, ItemStack>();
	
	public CustomShapedRecipe(ItemStack result, Plugin plugin) {
		
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		ingredients.put(' ', null);
		this.result = result;
		this.plugin = plugin;

	}
	
	public void setIngredient(Character chara, ItemStack item) {
		
		ingredients.put(chara, item);
		
	}
	
	public void shape(String row1, String row2, String row3) {
		
		int i = 1;
		
		for (Character chara : row1.toCharArray()) {
			
			shape.put(i, chara);
			i += 1;
		
		}
		
		for (Character chara : row2.toCharArray()) {
			
			shape.put(i, chara);
			i += 1;
		
		}
		
		for (Character chara : row3.toCharArray()) {
			
			shape.put(i, chara);
			i += 1;
		
		}

		shapeStrings.add(row1);
		shapeStrings.add(row2);
		shapeStrings.add(row3);
		
	}
	
	public ItemStack getResult() {
		
		return result;
		
	}
	
	public Map<Character, ItemStack> getIngredientMap() {
		
		return ingredients;
		
	}
	
	public String[] getShape() {
		
		return (String[]) shapeStrings.toArray();
		
	}
	
	public void register() {
		
		this.runTaskTimer(plugin, 0, 1);
		
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

	@Override
	public void run() {

		for (UUID uuid : players.keySet()) {
			
			Player player = Bukkit.getPlayer(uuid);
			Inventory inv = player.getOpenInventory().getTopInventory();
			int n = 0;
			
			for (int i = 1; i<10; i++) {
				
				ItemStack itemCraft = ingredients.get(shape.get(i));
				ItemStack itemPlayer = inv.getItem(i);
				
				if (itemCraft == null && itemPlayer == null) {
					
					n += 1;
					
				}
				
				if (itemCraft == null) continue;
				
				if (itemCraft.equals(itemPlayer)) {
					
					n += 1;
					
				}	
				
			}
			

			
			if (n == 9) {
			
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
 