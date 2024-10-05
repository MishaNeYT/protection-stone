package ru.mishaneyt.protectionstone;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import ru.mishaneyt.protectionstone.region.ProtectionRegionService;
import ru.mishaneyt.protectionstone.stone.ProtectionStoneService;
import ru.mishaneyt.protectionstone.util.loader.Loader;

public final class ProtectionPlugin extends JavaPlugin {
  private static ProtectionPlugin instance;

  private ProtectionStoneService protectionStoneService;
  private ProtectionRegionService protectionRegionService;

  private Loader loader;

  @Override
  public void onLoad() {
    instance = this;
    loader = new Loader();
    protectionStoneService = new ProtectionStoneService();
    protectionRegionService = new ProtectionRegionService();
  }

  @Override
  public void onEnable() {
    loader.addLoadable(protectionStoneService);
    loader.addUnLoadable(protectionStoneService);
    loader.addLoadable(protectionRegionService);
    loader.addUnLoadable(protectionRegionService);

    loader.addLoadable(new ProtectionListener());

    loader.loadAll();
  }

  @Override
  public void onDisable() {
    getServer().getScheduler().cancelTasks(instance);
    HandlerList.unregisterAll(instance);

    loader.unLoadAll();
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
