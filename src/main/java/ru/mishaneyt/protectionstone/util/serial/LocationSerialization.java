package ru.mishaneyt.protectionstone.util.serial;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public class LocationSerialization {

  public static @NotNull @Unmodifiable Map<String, Object> serialize(final @NotNull Location location) {
    return ImmutableMap.<String, Object> builder()
      .put("world", location.getWorld().getName())
      .put("x", location.getBlockX())
      .put("y", location.getBlockY())
      .put("z", location.getBlockZ())
      .build();
  }

  public static @NotNull Location deserialize(final @NotNull Map<String, Object> data) {
    var worldName = (String) data.get("world");
    var world = Bukkit.getWorld(worldName);
    var x = (double) data.get("x");
    var y = (double) data.get("y");
    var z = (double) data.get("z");
    return new Location(world, x, y, z);
  }
}
