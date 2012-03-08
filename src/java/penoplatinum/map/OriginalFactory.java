package penoplatinum.map;

import java.util.HashMap;
import java.util.Scanner;
import penoplatinum.simulator.tiles.Panel;
import penoplatinum.simulator.tiles.Panels;

/**
 * 
 * @author: Team Platinum
 */
public class OriginalFactory implements MapFactory {
  HashMap<String, Panel> tiles = new HashMap<String, Panel>();

  public OriginalFactory() {
    addTiles();
  }
  
  @Override
  public Map getMap(Scanner sc){
    int width = sc.nextInt();
    int length = sc.nextInt();
    int height = sc.nextInt();
    Map3D map = new Map3D(length, width);
    for(int i = 0; i <length; i++){
      for(int j = 0; j <width; j++){
        String str = sc.next();
        Panel next = tiles.get(str);
        if(next == null){
          next = Panels.NONE;
        }
        map.add(next);
      }
    }
    return map;
  }

  private void addTiles() {
    tiles.put("None.N", Panels.NONE);
    tiles.put("None.E", Panels.NONE);
    tiles.put("None.S", Panels.NONE);
    tiles.put("None.W", Panels.NONE);
    
    tiles.put("RCorner.N", Panels.S_E);
    tiles.put("RCorner.E", Panels.W_S);
    tiles.put("RCorner.S", Panels.N_W);
    tiles.put("RCorner.W", Panels.E_N);
    
    tiles.put("LCorner.N", Panels.S_W);
    tiles.put("LCorner.E", Panels.W_N);
    tiles.put("LCorner.S", Panels.N_E);
    tiles.put("LCorner.W", Panels.E_S);
    
    tiles.put("Straight.N", Panels.S_N);
    tiles.put("Straight.E", Panels.W_E);
    tiles.put("Straight.S", Panels.N_S);
    tiles.put("Straight.W", Panels.E_W);
    
    tiles.put("End.N", Panels.N);
    tiles.put("End.E", Panels.E);
    tiles.put("End.S", Panels.S);
    tiles.put("End.W", Panels.W);
   
    //tiles.put("Cross", Panels.CROSS);
  }
  
}