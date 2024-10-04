package ru.mishaneyt.protectionstone.util.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.ProtectionPlugin;

import java.io.File;

public abstract class FileLoader {
  protected final JavaPlugin plugin;
  protected final FileConfiguration configuration;
  protected final File file;

  protected FileLoader(final @NotNull String fileName, boolean makeFile) {
    this.plugin = JavaPlugin.getPlugin(ProtectionPlugin.class);
    this.file = new File(plugin.getDataFolder(), fileName);
    this.configuration = YamlConfiguration.loadConfiguration(file);
    if (makeFile) {
      plugin.saveResource(file.getPath(), false);
      plugin.getLogger().info("Configuration " + fileName + " has been created!");
    }
  }

  public FileConfiguration getConfiguration() {
    return configuration;
  }

  public File getFile() {
    return file;
  }
}
