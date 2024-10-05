package ru.mishaneyt.protectionstone.util.file;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public abstract class FileSectionLoader extends FileLoader {
  protected final ConfigurationSection section;

  protected FileSectionLoader(final @NotNull String fileName, final @NotNull String section, boolean makeFile) {
    super(fileName, makeFile);
    this.section = configuration.getConfigurationSection(section);
  }

  public ConfigurationSection getSection() {
    return section;
  }
}
