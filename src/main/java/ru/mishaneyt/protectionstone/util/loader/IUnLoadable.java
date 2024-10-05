package ru.mishaneyt.protectionstone.util.loader;

public interface IUnLoadable extends ILoadable {

  /**
   * Unload object
   * @throws Exception Error
   */
  void unload() throws Exception;
}
