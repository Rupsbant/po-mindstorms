package penoplatinum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntegerMap<Value> {

  private int size = 0;
  private int[] keys = new int[10];
  private Value[] values = (Value[]) new Object[10];

  public Value get(int key) {
    int index = indexOf(key);
    if (index == -1) {
      return null;
    }
    return values[index];
  }

  private int indexOf(int key) {
    for (int i = 0; i < size; i++) {
      if (keys[i] == key) {
        return i;
      }
    }
    return -1;
  }

  public void put(int key, Value value) {
    int index = indexOf(key);
    if (index == -1) {
      ensureCapacity();
      keys[size] = key;
      values[size] = value;
      size++;
      return;
    }
    values[index] = value;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public List<Value> values() {
    List<Value> out = new ArrayList<Value>(size);
    for (int i = 0; i < size; i++) {
      out.add(values[i]);
    }
    return out;
  }

  public int findKey(Value g) {
    for (int i = 0; i < size; i++) {
      if (g.equals(values[i])) {
        return keys[i];
      }
    }
    return -1;
  }

  private void ensureCapacity() {
    if (size < keys.length) {
      return;
    }
    keys = Arrays.copyOf(keys, keys.length * 3 / 2);
    values = Arrays.copyOf(values, keys.length);
  }
}
