package net.jzx7.regios.bukkit.entity;

import java.util.Map.Entry;

import net.jzx7.regios.messages.MsgFormat;
import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.entity.RegiosPlayer;
import net.jzx7.regiosapi.inventory.RegiosItemStack;
import net.jzx7.regiosapi.location.RegiosPoint;
import net.jzx7.regiosapi.worlds.RegiosWorld;

import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BukkitPlayer implements RegiosPlayer {

	private Player bukkitPlayer;
	
	public BukkitPlayer(Player p) {
		this.bukkitPlayer = p;
	}
	
	public Player getPlayer() {
		return bukkitPlayer;
	}
	
	@Override
	public RegiosWorld getRegiosWorld() {
		return RegiosConversions.getRegiosWorld(bukkitPlayer.getWorld());
	}

	@Override
	public void sendMessage(String str) {
		bukkitPlayer.sendMessage(MsgFormat.colourFormat(str));
	}

	@Override
	public RegiosPoint getPoint() {
		return RegiosConversions.getPoint(bukkitPlayer.getLocation());
	}

	@Override
	public String getName() {
		return bukkitPlayer.getName();
	}

	@Override
	public void teleport(RegiosPoint l) {
		bukkitPlayer.teleport(RegiosConversions.getLocation(l));
		
	}

	@Override
	public RegiosItemStack getItemInHand() {
		return RegiosConversions.getRegiosItemStack(bukkitPlayer.getItemInHand());
	}

	@Override
	public void setItemInHand(RegiosItemStack ris) {
		bukkitPlayer.setItemInHand(RegiosConversions.getItemStack(ris));
		
	}

	@Override
	public boolean hasPermission(String node) {
		return bukkitPlayer.hasPermission(node);
	}

	@Override
	public double getHealth() {
		return bukkitPlayer.getHealth();
	}

	@Override
	public void setHealth(double h) {
		bukkitPlayer.setHealth(h);
	}

	@Override
	public int getGameMode() {
		return bukkitPlayer.getGameMode().getValue();
	}

	@Override
	public void setGameMode(int gm) {
		bukkitPlayer.setGameMode(GameMode.getByValue(gm));
	}

	@Override
	public boolean performCommand(String cmd) {
		return bukkitPlayer.performCommand(cmd);
	}

	@Override
	public void closeInventory() {
		bukkitPlayer.closeInventory();
	}

	@Override
	public void setVelocity(RegiosPoint l) {
		bukkitPlayer.setVelocity(RegiosConversions.getLocation(l).toVector());
	}

	@Override
	public RegiosItemStack[] getInventoryContents() {
		return RegiosConversions.convertItemStack(bukkitPlayer.getInventory().getContents());
	}

	@Override
	public RegiosItemStack[] getArmorContents() {
		return RegiosConversions.convertItemStack(bukkitPlayer.getInventory().getArmorContents());
	}
	
	@Override
	public void addItem(RegiosItemStack ris) {
		ItemStack is = new ItemStack(ris.getId(), ris.getAmount(), ris.getData());
		
		try {
            for (Entry<Integer, Integer> entry : ris.getEnchantments().entrySet()) {
                is.addEnchantment(Enchantment.getById(entry.getKey()), entry.getValue());
            }
        } catch (Throwable ignore) {}
	
		bukkitPlayer.getInventory().addItem(is);
	}

	@Override
	public void setInventoryContents(RegiosItemStack[] ris) {
		bukkitPlayer.getInventory().setContents(RegiosConversions.convertRegiosItemStack(ris));
	}

	@Override
	public void setArmorContents(RegiosItemStack[] ris) {
		bukkitPlayer.getInventory().setArmorContents(RegiosConversions.convertRegiosItemStack(ris));
	}

	@Override
	public void addItem(int id, int amount) {
		bukkitPlayer.getInventory().addItem(new ItemStack(id, amount));
	}

	@Override
	public void clearInventory() {
		bukkitPlayer.getInventory().clear();
	}

	@Override
	public boolean inventoryContains(RegiosItemStack ris) {
		return bukkitPlayer.getInventory().contains(RegiosConversions.getItemStack(ris));
	}

	@Override
	public void damage(double damage) {
		bukkitPlayer.damage(damage);
	}

	@Override
	public boolean isOp() {
		return bukkitPlayer.isOp();
	}
}