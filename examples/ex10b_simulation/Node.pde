public static class Node {
  protected static float dampen = 0.05f;
  private PVector loc;
  private PVector vel;
  private PVector acc;
  private ArrayList<Spring> connections;
  
  public Node(PVector loc) {
    this.loc = loc.copy();
    vel = new PVector();
    acc = new PVector();
    connections = new ArrayList();
  }
  
  public void update() {
    vel.add(acc);
    loc.add(vel);
    acc.mult(0f);
    vel.mult(1.0f - dampen);
  }
  
  public void connect(Spring s) {
    connections.add(s);
  }
  
  public ArrayList<Spring> getConnections() {
    return connections;
  }
  
  public void applyForce(PVector force) {
    acc.add(force);
  }
  
  public PVector getLoc() {
    return loc.copy();
  }
  
  public PVector getVel() {
    return vel.copy();
  }
  
  public void setLoc(PVector loc) {
    this.loc = loc;
  }
  
  public void display(PGraphics pg) {
    pg.pushStyle();
    pg.stroke(0);
    pg.fill(255);
    pg.ellipse(loc.x, loc.y, 10, 10);
    pg.popStyle();
  }
}