package java.util;

/**
 * A copy implementing missing methods.
 * @author MHGameWork
 */
public class Arrays {

  public static <T extends Object> T[] copyOf(T[] original, int newLength) {
    if (newLength < 0) {
      throw new IllegalArgumentException("Illegal length");
    }
    T[] copy = (T[]) new Object[newLength];
    System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
    return copy;
  }

  public static int[] copyOf(int[] original, int newLength) {
    if (newLength < 0) {
      throw new IllegalArgumentException("Illegal length");
    }
    int[] copy = new int[newLength];
    System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
    return copy;
  }

  /**
   * Copies the specified range of the specified array into a new array.
   * The initial index of the range (<tt>from</tt>) must lie between zero
   * and <tt>original.length</tt>, inclusive.  The value at
   * <tt>original[from]</tt> is placed into the initial element of the copy
   * (unless <tt>from == original.length</tt> or <tt>from == to</tt>).
   * Values from subsequent elements in the original array are placed into
   * subsequent elements in the copy.  The final index of the range
   * (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>,
   * may be greater than <tt>original.length</tt>, in which case
   * <tt>(byte)0</tt> is placed in all elements of the copy whose index is
   * greater than or equal to <tt>original.length - from</tt>.  The length
   * of the returned array will be <tt>to - from</tt>.
   *
   * @param original the array from which a range is to be copied
   * @param from the initial index of the range to be copied, inclusive
   * @param to the final index of the range to be copied, exclusive.
   *     (This index may lie outside the array.)
   * @return a new array containing the specified range from the original array,
   *     truncated or padded with zeros to obtain the required length
   * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt>
   *     or <tt>from &gt; original.length()</tt>
   * @throws IllegalArgumentException if <tt>from &gt; to</tt>
   * @throws NullPointerException if <tt>original</tt> is null
   * @since 1.6
   */
  public static byte[] copyOfRange(byte[] original, int from, int to) {
    int newLength = to - from;
    if (newLength < 0) {
      throw new IllegalArgumentException(from + " > " + to);
    }
    byte[] copy = new byte[newLength];
    System.arraycopy(original, from, copy, 0,
            Math.min(original.length - from, newLength));
    return copy;
  }
}
