package ru.mishaneyt.protectionstone.stone;

import org.bukkit.Material;

import java.util.List;

public final class ProtectionStone {
  private final Material material;
  private final String displayName;
  private final List<String> lores;
  private final int radius;

  public ProtectionStone(final Material material, final String displayName, final List<String> lores, int radius) {
    this.material = material;
    this.displayName = displayName;
    this.lores = lores;
    this.radius = radius;
  }

  public Material getMaterial() {
    return material;
  }

  public String getDisplayName() {
    return displayName;
  }

  public List<String> getLores() {
    return lores;
  }

  public int getRadius() {
    return radius;
  }
}
