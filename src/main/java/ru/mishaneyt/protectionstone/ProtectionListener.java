package ru.mishaneyt.protectionstone;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.jetbrains.annotations.NotNull;

public final class ProtectionListener implements Listener {

  @EventHandler
  public void onProtectionStonePlace(final @NotNull BlockPlaceEvent event) {
    var player = event.getPlayer();
    var blockPlaced = event.getBlockPlaced();
    var plugin = ProtectionPlugin.getInstance();
    var optionalProtectionStone = plugin.getProtectionStoneService().getBlocks().values()
      .stream()
      .filter(protectionStone -> blockPlaced.getType() == protectionStone.getMaterial())
      .findFirst();
    if (optionalProtectionStone.isPresent()) {
      var protectionStone = optionalProtectionStone.get();
      plugin.getProtectionRegionService().createRegion(player, blockPlaced.getLocation(), protectionStone);
      player.sendMessage("Вы поставили Блок привата! Радиус: " + protectionStone.getRadius());
    }
  }

  @EventHandler
  public void onProtectionStoneBreak(final @NotNull BlockBreakEvent event) {
    var player = event.getPlayer();
    var blockBreak = event.getBlock();
    var plugin = ProtectionPlugin.getInstance();
    var optionalProtectionStone = plugin.getProtectionStoneService().getBlocks().values()
      .stream()
      .filter(protectionStone -> blockBreak.getType() == protectionStone.getMaterial())
      .findFirst();
    if (optionalProtectionStone.isPresent()) {
      var optionalProtectionRegion = plugin.getProtectionRegionService().getRegionByLocation(blockBreak.getLocation());
      if (optionalProtectionRegion.isPresent()) {
        var protectionRegion = optionalProtectionRegion.get();
        plugin.getProtectionRegionService().removeRegion(protectionRegion.getId());
        player.sendMessage("Вы убрали приват");
      }
    }
  }
}
