package ru.mishaneyt.protectionstone.util.loader;

import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.util.loader.list.UnLoadableList;

public class Loader extends UnLoadableList {

  public void addLoadable(final @NotNull ILoadable loadable) {
    loadableList.add(loadable);
  }

  public void addUnLoadable(final @NotNull IUnLoadable unLoadable) {
    unLoadableList.add(unLoadable);
  }

  public void load(final @NotNull ILoadable loadable) {
    try {
      loadable.load();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public void unload(final @NotNull IUnLoadable unLoadable) {
    try {
      unLoadable.unload();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public void loadAll() {
    loadableList.forEach(this::load);
  }

  public void unLoadAll() {
    unLoadableList.forEach(this::unload);
  }
}
