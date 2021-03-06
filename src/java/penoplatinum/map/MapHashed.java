package penoplatinum.map;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import penoplatinum.util.Point;
import penoplatinum.simulator.tiles.Tile;

public class MapHashed implements Map {

  @SuppressWarnings("unchecked")
  private HashMap<Point, Tile> map = new HashMap();
  private int minX = Integer.MAX_VALUE;
  private int maxX = Integer.MIN_VALUE;
  private int minY = Integer.MAX_VALUE;
  private int maxY = Integer.MIN_VALUE;
  
  List<Point> ghosts = new ArrayList<Point>();
  Point pacman;

  @Override
  public Map add(Tile t) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Boolean exists(int left, int top) {
    return map.containsKey(new Point(left+minX-1, top+minY-1));
  }

  @Override
  public Tile get(int left, int top) {
    return map.get(new Point(left+minX-1, top+minY-1));
  }
  
  public Tile getRaw(int left, int top) {
    return map.get(new Point(left, top));
  }

  @Override
  public Tile getFirst() {
    return map.values().toArray(new Tile[1])[0];
  }

  @Override
  public int getHeight() {
    return maxY - minY+1;
  }

  @Override
  public int getWidth() {
    return maxX - minX+1;
  }

  @Override
  public int getTileCount() {
    return map.size();
  }

  @Override
  public Map put(Tile tile, int left, int top) {
    if (top > maxY) {
      maxY = top;
    }
    if (top < minY) {
      minY = top;
    }
    if (left > maxX) {
      maxX = left;
    }
    if (left < minX) {
      minX = left;
    }
    map.put(new Point(left, top), tile);
    return this;
  }

  private Point transformPosition(Point position) {
    return new Point(position.getX(), this.getHeight() - position.getY() - 1);
  }
  
  public MapHashed addGhostPosition(Point position) {
    this.ghosts.add(this.transformPosition(position));
    return this;
  }
  
  public List<Point> getGhostPositions() {
    return ghosts;
  }

  public MapHashed setPacmanPosition(Point position) {
    this.pacman = this.transformPosition(position);
    return this;
  }
  
  // returns the position where the pacman needs to be positioned
  public Point getPacmanPosition() {
    return this.pacman;
  }
}
