package ru.mishaneyt.protectionstone.stone;

import com.google.common.collect.Maps;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import ru.mishaneyt.protectionstone.util.file.FileSectionLoader;

import java.util.Map;
import java.util.Optional;

public final class ProtectionStoneService extends FileSectionLoader {
  private final Map<Material, ProtectionStone> blocks = Maps.newConcurrentMap();

  public ProtectionStoneService(final JavaPlugin plugin) {
    super("blocks.yml", "blocks", true);
    for (var key : getSection().getKeys(false)) {
      var section = getSection();
      if (section == null) return;
      var material = Material.getMaterial(key.toUpperCase());
      if (material == null || material.isAir()) {
        plugin.getLogger().severe("File 'blocks.yml' material " + key + " is invalid");
        return;
      }
      var displayName = section.getString("display_name");
      var lores = section.getStringList("lores");
      var radius = section.getInt("radius");
      blocks.putIfAbsent(material, new ProtectionStone(material, displayName, lores, radius));
    }
    plugin.getLogger().info("Loaded " + blocks.size() + " protection stones");
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
  public Optional<ProtectionStone> getBlockByMaterial(final Material material) {
    return Optional.of(blocks.get(material));
  }
}
