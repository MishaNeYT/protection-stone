package ru.mishaneyt.protectionstone.region;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import ru.mishaneyt.protectionstone.ProtectionPlugin;
import ru.mishaneyt.protectionstone.stone.ProtectionStone;
import ru.mishaneyt.protectionstone.region.data.RegionRole;
import ru.mishaneyt.protectionstone.util.serial.LocationSerialization;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ProtectionRegion implements ConfigurationSerializable {
  private final String id;
  private final Location min;
  private final Location max;
  private final Location center;
  private final ProtectionStone protectionStone;
  private final Map<OfflinePlayer, RegionRole> members;

  // todo hologram methods
  // todo gui methods

  public ProtectionRegion(final @NotNull String id, final @NotNull OfflinePlayer owner, final @NotNull ProtectionStone protectionStone, final @NotNull Location center) {
    this.id = id;
    this.protectionStone = protectionStone;
    this.center = center;
    var radius = protectionStone.getRadius();
    this.min = new Location(center.getWorld(), center.getX() - radius, center.getY() - radius, center.getZ() - radius);
    this.max = new Location(center.getWorld(), center.getX() + radius, center.getY() + radius, center.getZ() + radius);
    this.members = Maps.newConcurrentMap();
    this.members.put(owner, RegionRole.OWNER);
  }

  public boolean addMembers(final @NotNull OfflinePlayer... players) {
    Arrays.stream(players).forEach(player -> members.putIfAbsent(player, RegionRole.MEMBER));
    return true;
  }

  public boolean removeMembers(final @NotNull OfflinePlayer... players) {
    Arrays.stream(players).forEach(members::remove);
    return true;
  }

  public String getId() {
    return id;
  }

  public Location getMin() {
    return min;
  }

  public Location getMax() {
    return max;
  }

  public Location getCenter() {
    return center;
  }

  public ProtectionStone getProtectionStone() {
    return protectionStone;
  }

  public Map<OfflinePlayer, RegionRole> getMembers() {
    return members;
  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    final Map<String, Object> serializedMembers = members.entrySet().stream()
      .collect(Collectors.toMap(entry -> entry.getKey().getUniqueId().toString(), entry -> entry.getValue().name()));
    return ImmutableMap.<String, Object>builder()
      .put("id", id)
      .put("material", protectionStone.getMaterial().name())
      .put("min", LocationSerialization.serialize(min))
      .put("max", LocationSerialization.serialize(max))
      .put("members", serializedMembers)
      .build();
  }

  @SuppressWarnings("unchecked")
  public static @NotNull Optional<ProtectionRegion> deserialize(final @NotNull String id, final @NotNull Map<String, Object> data) {
    var owner = (OfflinePlayer) data.get("owner");
    var material = Material.getMaterial((String) data.get("material"));
    if (material == null) {
      ProtectionPlugin.getInstance().getLogger().warning("Не удалось десериализировать " + id + " недопустимый тип материала.");
      return Optional.empty();
    }
    var optionalProtectionStone = ProtectionPlugin.getInstance().getProtectionStoneService().getBlockByMaterial(material);
    if (optionalProtectionStone.isPresent()) {
      var protectionStone = optionalProtectionStone.get();
      var min = LocationSerialization.deserialize((Map<String, Object>) data.get("min"));
      var max = LocationSerialization.deserialize((Map<String, Object>) data.get("max"));
      double x = (min.getX() + max.getX()) / 2;
      double y = (min.getY() + max.getY()) / 2;
      double z = (min.getZ() + max.getZ()) / 2;
      var region = new ProtectionRegion(id, owner, protectionStone, new Location(min.getWorld(), x, y, z));
      final Map<String, String> serializedMembers = (Map<String, String>) data.get("members");
      if (serializedMembers != null) {
        serializedMembers.forEach((name, roleName) -> {
          var uuid = UUID.fromString(name);
          var player = Bukkit.getOfflinePlayer(uuid);
          var role = RegionRole.valueOf(roleName);
          region.members.put(player, role);
        });
      }
      return Optional.of(region);
    } else ProtectionPlugin.getInstance().getLogger().warning("Не удалось десериализировать " + id + " несуществующий блок привата.");
    return Optional.empty();
  }
}
