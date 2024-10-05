package ru.mishaneyt.protectionstone.region;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Unmodifiable;
import ru.mishaneyt.protectionstone.ProtectionPlugin;
import ru.mishaneyt.protectionstone.region.data.RegionPermission;
import ru.mishaneyt.protectionstone.stone.ProtectionStone;
import ru.mishaneyt.protectionstone.region.data.RegionRole;
import ru.mishaneyt.protectionstone.util.serial.LocationSerialization;

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

  public ProtectionRegion(final @NotNull String id, final @NotNull ProtectionStone protectionStone, final @NotNull Location center) {
    this.id = id;
    this.protectionStone = protectionStone;
    this.center = center;
    var radius = protectionStone.getRadius();
    this.min = new Location(center.getWorld(), center.getX() - radius, center.getY() - radius, center.getZ() - radius);
    this.max = new Location(center.getWorld(), center.getX() + radius, center.getY() + radius, center.getZ() + radius);
    this.members = Maps.newConcurrentMap();
  }

  public ProtectionRegion(final @NotNull String id, final @NotNull OfflinePlayer owner, final @NotNull ProtectionStone protectionStone, final @NotNull Location center) {
    this(id, protectionStone, center);
    this.members.put(owner, RegionRole.OWNER);
  }

  public void addMember(final @NotNull OfflinePlayer player, final @NotNull RegionRole role) {
    members.putIfAbsent(player, role);
  }

  public void removeMember(final @NotNull OfflinePlayer player) {
    members.remove(player);
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

  public boolean isMember(final @NotNull Player player) {
    return members.containsKey(player);
  }

  public boolean hasPermission(final @NotNull Player player, final @NotNull RegionPermission permission) {
    if (members.containsKey(player)) {

    }
    return false;
  }

  @Override
  public @NotNull @Unmodifiable Map<String, Object> serialize() {
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
    var materialName = (String) data.get("material");
    var material = Material.matchMaterial(materialName);
    if (material == null) {
      ProtectionPlugin.getInstance().getLogger().warning("Не удалось десериализировать " + id + " недопустимый тип материала.");
      return Optional.empty();
    }
    var optionalProtectionStone = ProtectionPlugin.getInstance().getProtectionStoneService().getBlockByMaterial(material);
    if (optionalProtectionStone.isPresent()) {
      var protectionStone = optionalProtectionStone.get();
      var minSection = data.get("min");
      var min = (minSection instanceof MemorySection)
        ? LocationSerialization.deserialize(((MemorySection) minSection).getValues(false))
        : LocationSerialization.deserialize((Map<String, Object>) minSection);
      var maxSection = data.get("max");
      var max = (maxSection instanceof MemorySection)
        ? LocationSerialization.deserialize(((MemorySection) maxSection).getValues(false))
        : LocationSerialization.deserialize((Map<String, Object>) maxSection);
      double x = (min.getX() + max.getX()) / 2;
      double y = (min.getY() + max.getY()) / 2;
      double z = (min.getZ() + max.getZ()) / 2;
      var region = new ProtectionRegion(id, protectionStone, new Location(min.getWorld(), x, y, z));
      var membersSection = data.get("members");
      if (membersSection instanceof MemorySection) {
        var membersMap = ((MemorySection) membersSection).getValues(false);
        for (var entry : membersMap.entrySet()) {
          var uuid = UUID.fromString(entry.getKey());
          var roleName = (String) entry.getValue();
          var player = Bukkit.getOfflinePlayer(uuid);
          var role = RegionRole.valueOf(roleName);
          region.addMember(player, role);
        }
      } else if (membersSection instanceof Map) {
        var serializedMembers = (Map<String, String>) membersSection;
        serializedMembers.forEach((name, roleName) -> {
          var uuid = UUID.fromString(name);
          var player = Bukkit.getOfflinePlayer(uuid);
          var role = RegionRole.valueOf(roleName);
          region.addMember(player, role);
        });
      } else ProtectionPlugin.getInstance().getLogger().warning("Не удалось десериализировать членов региона для " + id + ": Неверный формат.");
      return Optional.of(region);
    } else ProtectionPlugin.getInstance().getLogger().warning("Не удалось десериализировать " + id + " несуществующий блок привата.");
    return Optional.empty();
  }
}
