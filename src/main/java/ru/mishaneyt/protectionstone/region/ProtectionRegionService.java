package ru.mishaneyt.protectionstone.region;

import com.google.common.collect.Maps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.ProtectionPlugin;
import ru.mishaneyt.protectionstone.stone.ProtectionStone;
import ru.mishaneyt.protectionstone.stone.exception.InvalidProtectionStone;
import ru.mishaneyt.protectionstone.util.file.FileSectionLoader;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class ProtectionRegionService extends FileSectionLoader {
  private final Map<String, ProtectionRegion> regions = Maps.newConcurrentMap();

  public ProtectionRegionService() {
    super("regions.yml", "regions", true);
    for (var id : getSection().getKeys(false)) {
      var section = getSection();
      if (section == null) return;
      var optionalProtectionRegion = ProtectionRegion.deserialize(id, section.getValues(false));
      if (optionalProtectionRegion.isPresent()) {
        var protectionRegion = optionalProtectionRegion.get();
        regions.put(id, protectionRegion);
      }
    }
  }

  public @NotNull CompletableFuture<ProtectionRegion> createRegion(final @NotNull Player owner, final @NotNull Location location, final @NotNull ProtectionStone protectionStone) {
    final CompletableFuture<ProtectionRegion> completableFuture = new CompletableFuture<>();
    var uniqueId = owner.getUniqueId();
    var id = uniqueId.toString();
    var protectionRegion = new ProtectionRegion(id, owner, protectionStone, location);
    regions.put(id, protectionRegion);
    completableFuture.complete(protectionRegion);
    return completableFuture;
  }

  public @NotNull CompletableFuture<ProtectionRegion> createRegion(final @NotNull Player owner, final @NotNull Location location) {
    final CompletableFuture<ProtectionRegion> completableFuture = new CompletableFuture<>();
    var optionalProtectionStone = ProtectionPlugin.getInstance().getProtectionStoneService().getBlockByMaterial(location.getBlock().getType());
    if (optionalProtectionStone.isPresent()) {
      var protectionStone = optionalProtectionStone.get();
      return createRegion(owner, location, protectionStone);
    } else {
      completableFuture.completeExceptionally(new InvalidProtectionStone("Не существующий блок привата"));
    }
    return completableFuture;
  }

  public boolean removeRegion(final @NotNull String id) {
    // todo remove hologram
    regions.remove(id);
    return true;
  }

  @Contract("_ -> new")
  public @NotNull Optional<ProtectionRegion> getRegionByKey(final @NotNull String key) {
    return Optional.of(regions.get(key));
  }

  public @NotNull Optional<ProtectionRegion> getRegionByLocation(final @NotNull Location location) {
    return regions.values().stream()
      .filter(region -> region.getCenter().equals(location))
      .findFirst();
  }

  public Map<String, ProtectionRegion> getRegions() {
    return regions;
  }
}
