package penoplatinum.pacman;

/**
 * GhostAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Ghost
 * 
 * @author: Team Platinum
 */


import penoplatinum.Color;
import penoplatinum.grid.Agent;
import penoplatinum.grid.MovingAgent;

public class GhostAgent extends MovingAgent {
  private Color color = new Color(255,255,255);

  public GhostAgent(String name) { super(name); }
  public GhostAgent(String name, Color color) {
    super(name);
    this.color = color;
  }
  
  public Color getColor() { return this.color; }

  @Override
  public Agent copyAgent() {
    GhostAgent ret = new GhostAgent(this.getName(), this.getColor());
    return ret;
  }

  
  
}
