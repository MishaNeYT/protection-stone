package ru.mishaneyt.protectionstone;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.mishaneyt.protectionstone.region.ProtectionRegionService;
import ru.mishaneyt.protectionstone.stone.ProtectionStoneService;

public final class ProtectionPlugin extends JavaPlugin {
  private static ProtectionPlugin instance;

  private ProtectionStoneService protectionStoneService;
  private ProtectionRegionService protectionRegionService;

  @Override
  public void onEnable() {
    instance = this;
    protectionStoneService = new ProtectionStoneService(instance);
    protectionRegionService = new ProtectionRegionService();

    // Listener initialization
    Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
  }

  @Override
  public void onDisable() {
  }

  /**
   * Returns the singleton instance of the plugin.
   *
   * @return the instance of ProtectionStone
   */
  public static ProtectionPlugin getInstance() {
    return instance;
  }

  public ProtectionStoneService getProtectionStoneService() {
    return protectionStoneService;
  }

  public ProtectionRegionService getProtectionRegionService() {
    return protectionRegionService;
  }
}
