package ru.mishaneyt.protectionstone.util.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.ProtectionPlugin;
import ru.mishaneyt.protectionstone.util.loader.IUnLoadable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class FileLoader implements IUnLoadable {
  protected final JavaPlugin plugin;
  protected final FileConfiguration configuration;
  protected final File file;

  protected FileLoader(final @NotNull String fileName, boolean makeFile) {
    this.plugin = ProtectionPlugin.getInstance();
    this.file = new File(plugin.getDataFolder(), fileName);
    if (makeFile) {
      plugin.saveResource(file.getName(), false);
      plugin.getLogger().info("Configuration " + fileName + " has been created!");
    }
    this.configuration = YamlConfiguration.loadConfiguration(file);
  }

  public void save() {
    try {
      configuration.save(file);
    } catch (IOException exception) {
      plugin.getLogger().warning("Could not save config to " + file.getName());
    }
  }
}
