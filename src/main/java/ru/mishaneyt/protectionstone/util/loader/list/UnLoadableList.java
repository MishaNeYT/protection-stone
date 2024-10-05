package ru.mishaneyt.protectionstone.util.loader.list;

import org.jetbrains.annotations.NotNull;
import ru.mishaneyt.protectionstone.util.loader.IUnLoadable;

import java.util.ArrayList;
import java.util.List;

public abstract class UnLoadableList extends LoadableList implements IUnLoadable {
  protected List<IUnLoadable> unLoadableList;

  protected UnLoadableList() {
    unLoadableList = new ArrayList<>();
  }

  public void addAllUnLoadable(final @NotNull List<IUnLoadable> unLoadable) {
    unLoadableList.addAll(unLoadable);
  }

  public void unload() {
    for (var unLoadable : unLoadableList)
      try {
        unLoadable.unload();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
  }
}
