public static class Spring {
  private static float springK = 0.1;
  private float currLength;
  private float restLength;
  private Node a;
  private Node b;
  
  public Spring(Node a, Node b, float restLength) {
    this.a = a;
    this.b = b;
    this.restLength = restLength;
    currLength = PVector.dist(a.getLoc(), b.getLoc());
  }
  
  public Spring(Node a, Node b) {
    this.a = a;
    this.b = b;
    this.restLength = PVector.dist(a.getLoc(), b.getLoc());
    currLength = restLength;
  }
  
  
  public void act() {
    PVector force = PVector.sub(a.getLoc(), b.getLoc());
    currLength = force.mag();
    
    float forceMag = (restLength - currLength) * springK;
    force.setMag(forceMag);
    
    a.applyForce(force);
    force.mult(-1);
    b.applyForce(force);
  }
  
  
  public void forceMinDist(float threshold, float strength) {
    PVector force = PVector.sub(a.getLoc(), b.getLoc());
    currLength = force.mag();
    
    if(currLength >=  threshold) return;
    float forceMag = (threshold - currLength) * strength;
    force.setMag(forceMag);
    
    a.applyForce(force);
    force.mult(-1);
    b.applyForce(force);
  }
  
  public void display(PGraphics pg) {
    pg.pushStyle();
    pg.strokeWeight(1);
    pg.stroke(
      map(currLength, 0.5 * restLength, 1.5 * restLength, 255, 0),
      128,
      map(currLength, 0.5 * restLength, 1.5 * restLength, 0, 255));
    PVector aLoc = a.getLoc();
    PVector bLoc = b.getLoc();
    pg.line(aLoc.x, aLoc.y, bLoc.x, bLoc.y);
    pg.popStyle();
  }
  
  public void setRestLength(float restLength) {
    this.restLength = restLength;
  }
  
  public void changeRestLength(float change) {
    restLength = restLength + change;
  }
  
  public Node getA() {
    return a;
  }
 
  public Node getB() {
    return b;
  }
}