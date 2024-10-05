package ru.mishaneyt.protectionstone.util.loader.types;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import ru.mishaneyt.protectionstone.ProtectionPlugin;
import ru.mishaneyt.protectionstone.util.loader.ILoadable;

public interface ILoadableListener extends Listener, ILoadable {

  /**
   * Register bukkit listener
   */
  default void load() {
    Bukkit.getPluginManager().registerEvents(this, ProtectionPlugin.getInstance());
  }
}
