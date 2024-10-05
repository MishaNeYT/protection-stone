package ru.mishaneyt.protectionstone.stone;

import com.google.common.collect.Maps;

import org.bukkit.Material;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.util.file.FileSectionLoader;

import java.util.Map;
import java.util.Optional;

public final class ProtectionStoneService extends FileSectionLoader {
  private final Map<Material, ProtectionStone> blocks;

  public ProtectionStoneService() {
    super("blocks.yml", "blocks", true);
    this.blocks = Maps.newConcurrentMap();
  }

  @Override
  public void load() {
    for (var block : getSection().getKeys(false)) {
      var section = getSection().getConfigurationSection(block);
      if (section == null) return;
      var material = Material.matchMaterial(block.toUpperCase());
      if (material == null || material.isAir()) {
        plugin.getLogger().severe("File 'blocks.yml' material " + block + " is invalid");
        return;
      }
      var displayName = section.getString("display_name");
      var lores = section.getStringList("lores");
      var radius = section.getInt("radius");
      blocks.putIfAbsent(material, new ProtectionStone(material, displayName, lores, radius));
    }
    plugin.getLogger().info("Loaded " + blocks.size() + " protection stones");
  }

  @Override
  public void unload() {
    // TODO unload
  }

  /**
   *
   * @return
   */
  public Map<Material, ProtectionStone> getBlocks() {
    return blocks;
  }

  /**
   *
   * @param material
   * @return
   */
  @Contract("_ -> new")
  public @NotNull Optional<ProtectionStone> getBlockByMaterial(final @NotNull Material material) {
    return Optional.ofNullable(blocks.get(material));
  }
}
