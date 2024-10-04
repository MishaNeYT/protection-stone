package ru.mishaneyt.protectionstone.region.data;

import org.bukkit.Material;

public enum RegionRole {

  /**
   *
   */
  OWNER(0, Material.NETHERITE_HELMET, "Создатель", new RegionPermission[] {
    RegionPermission.ADD_MEMBERS,
    RegionPermission.REMOVE_MEMBERS,
    RegionPermission.UPGRADE
  }),

  /**
   *
   */
  CO_OWNER(1, Material.DIAMOND_HELMET, "Совладелец", new RegionPermission[] {
    RegionPermission.ADD_MEMBERS,
    RegionPermission.REMOVE_MEMBERS
  }),

  /**
   *
   */
  MEMBER(2, Material.GOLDEN_HELMET, "Участник", new RegionPermission[] {
  });

  private final int priority;
  private final Material icon;
  private final String translator;
  private final RegionPermission[] permissions;

  RegionRole(int priority, final Material icon, final String translator, final RegionPermission[] permissions) {
    this.priority = priority;
    this.icon = icon;
    this.translator = translator;
    this.permissions = permissions;
  }

  public int getPriority() {
    return priority;
  }

  public Material getIcon() {
    return icon;
  }

  public String getTranslator() {
    return translator;
  }

  public RegionPermission[] getPermissions() {
    return permissions;
  }
}
