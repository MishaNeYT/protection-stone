package ru.mishaneyt.protectionstone.util.loader.list;

import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.util.loader.ILoadable;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadableList implements ILoadable {
  protected List<ILoadable> loadableList;

  protected LoadableList() {
    loadableList = new ArrayList<>();
  }

  public void addAllLoadable(final @NotNull List<ILoadable> loadable) {
    loadableList.addAll(loadable);
  }

  public void load() {
    for (var loadable : loadableList)
      try {
        loadable.load();
      } catch (Exception exception) {
        exception.printStackTrace();
      }
  }
}
