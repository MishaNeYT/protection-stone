package ru.mishaneyt.protectionstone;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.util.loader.types.ILoadableListener;

public final class ProtectionListener implements ILoadableListener {

  @EventHandler
  public void onProtectionStonePlace(final @NotNull BlockPlaceEvent event) {
    var player = event.getPlayer();
    var blockPlaced = event.getBlockPlaced();
    var plugin = ProtectionPlugin.getInstance();
    var optionalProtectionStone = plugin.getProtectionStoneService().getBlockByMaterial(blockPlaced.getType());
    if (optionalProtectionStone.isPresent()) {
      var protectionStone = optionalProtectionStone.get();
      plugin.getProtectionRegionService().createRegion(player, blockPlaced.getLocation(), protectionStone)
          .thenAccept(protectionRegion -> player.sendMessage("Вы поставили Блок привата! Радиус: " + protectionStone.getRadius()));
    }
  }

  @EventHandler
  public void onProtectionStoneBreak(final @NotNull BlockBreakEvent event) {
    var player = event.getPlayer();
    var blockBreak = event.getBlock();
    var plugin = ProtectionPlugin.getInstance();
    var optionalProtectionStone = plugin.getProtectionStoneService().getBlockByMaterial(blockBreak.getType());
    if (optionalProtectionStone.isPresent()) {
      var optionalProtectionRegion = plugin.getProtectionRegionService().getRegionByLocation(blockBreak.getLocation());
      if (optionalProtectionRegion.isPresent()) {
        var protectionRegion = optionalProtectionRegion.get();
        if (protectionRegion.isMember(player)) {
          // TODO permission check
        }
        if (plugin.getProtectionRegionService().removeRegion(protectionRegion.getId())) {
          player.sendMessage("Вы убрали приват");
        }
      }
    }
  }
}
