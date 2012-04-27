package penoplatinum.util;

/**
 * This class represents a translation + rotation + translation transformation
 *
 * @author Team Platinum
 */
public class TransformationTRT implements Transformation {

  public static TransformationTRT Identity;

  static {
    Identity = new TransformationTRT();
    Identity.setTransformation(0, 0, Rotation.NONE, 0, 0);
  }
  private int translationAX;
  private int translationAY;
  private int translationBX;
  private int translationBY;
  private Rotation rotation;

  public Rotation getRotation() {
    return this.rotation;
  }

  public TransformationTRT setTransformation(int tAX, int tAY,
          Rotation rotation,
          int tBX, int tBY) {
    this.rotation = rotation;
    this.translationAX = tAX;
    this.translationAY = tAY;
    this.translationBX = tBX;
    this.translationBY = tBY;

    return this;
  }

  public void transform(Point point) {
    // translate to A XY
    point.translate(this.translationAX, this.translationAY);
    // rotate
    point.rotate(rotation);
    // translate to B XY
    point.translate(this.translationBX, this.translationBY);
  }

  public void inverseTransform(Point point) {
    
    // translate to B XY
    point.translate(-this.translationBX, -this.translationBY);
    
    // rotate
    point.rotate(rotation.invert());
    
    // translate to A XY
    point.translate(-this.translationAX, -this.translationAY);
    
  }

  public String toString() {
    return "Transformation: " + this.translationAX + "," + this.translationAY
            + ":" + this.rotation + ":" + this.translationBX
            + "," + this.translationBY;
  }

  @Deprecated
  public Point transform(int x, int y) {
    Point point = new Point(x, y);
    this.transform(point);
    return point;
  }

  public static TransformationTRT fromRotation(Rotation r) {
    return new TransformationTRT().setTransformation(0, 0, r, 0, 0);
  }
}
