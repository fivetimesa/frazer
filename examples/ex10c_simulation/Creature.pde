public static class Creature {
  private static int w;
  private static int h;
  private ArrayList<Node> nodes;
  private ArrayList<Spring> springs;
  private ArrayList<Spring> diagonals;
  private ArrayList<Node> group1;
  private ArrayList<Node> group2;
  
  private static float gravity = 0.1;
  private static float friction = 0.99;
  private static float groundY = 350;
  private static float groundReaction = 2;
  private static float change = 0.20;
  
  private float size;
  private float clock;
  private static float clockStep = 0.01;
  
  private PVector center;
  
  public Creature() {
    nodes = new ArrayList();
    springs = new ArrayList();
    group1 = new ArrayList();
    group2 = new ArrayList();
    diagonals = new ArrayList();
    clock = 0;
  }
  
  public void init(int w, int h, float size) {
    this.w = w;
    this.h = h;
    this.size = size;
    
    for(int y = 0; y < h; y++) {
      for(int x = 0; x < w; x++) {
        nodes.add(new Node(new PVector(x * size, y * size)));
      }
    }
    
    int currentNode = 0;
    for(int y = 0; y < h; y++) {
      for(int x = 0; x < w; x++) {
        if(y > 0) 
          springs.add(new Spring(
            nodes.get(currentNode - w),
            nodes.get(currentNode),
            size));
        if(x > 0) 
          springs.add(new Spring(
            nodes.get(currentNode - 1),
            nodes.get(currentNode),
            size));
        if(x > 0 && y > 0) {
          Spring d = new Spring(
            nodes.get(currentNode - w - 1),
            nodes.get(currentNode));
          springs.add(d);
          diagonals.add(d);
        }
        if(x < w - 1 && y > 0) {
          Spring d = new Spring(
            nodes.get(currentNode - w + 1),
            nodes.get(currentNode));
          springs.add(d);
          diagonals.add(d);
        }
        currentNode++;
      }
    }
    updateCenter();
    groupNodes();
  }
  
  private void groupNodes() {
    for(int i = nodes.size() - 1; i >= 0; i--) {
      double r = Math.random();
      if(r < 0.2) group1.add(nodes.get(i));
      else if(r < 0.4) group2.add(nodes.get(i));
      else if(r > 0.95) {
        Node n = nodes.get(i);
        for(int j = springs.size() - 1; j >= 0; j--) {
          Spring s = springs.get(j);
          if(s.getA() == n || s.getB() == n) 
            springs.remove(j);
        }
        nodes.remove(i);
      }
    }
  }
  
  public void update() {
    move();
    applyGravity(gravity);
    applyGroundReaction(groundY, groundReaction);
    applyGroundFriction(groundY, friction);
    for(Spring s: springs)
      s.act();
    for(Node n: nodes)
      n.update();
  }
  
  public void display(PGraphics pg) {
    for(Spring s: springs)
      s.display(pg);
    for(Node n: nodes)
      n.display(pg);
    updateCenter();
    displayCenter(pg);
  }
  
  
  public void moveTo(PVector loc) {
    PVector displacement = PVector.sub(loc, center);
    for(Node n: nodes)
      n.setLoc(PVector.add(n.getLoc(), displacement));
    updateCenter();
  }
  
  
  private void move() {
    float pSize1 = size * (1 + change * sin(clock));
    float pSize2 = size * (1 + change * cos(clock));
    
    float pDiagonalSize1 = size * (1 + change * sin(clock) * sqrt(2));
    float pDiagonalSize2 = size * (1 + change * cos(clock) * sqrt(2));
    
    clock += clockStep;
    float size1 = size * (1 + change * sin(clock));
    float size2 = size * (1 + change * cos(clock));
    
    float diagonalSize1 = size * (1 + change * sin(clock) * sqrt(2));
    float diagonalSize2 = size * (1 + change * cos(clock) * sqrt(2));
    
    float ds1 = size1 - pSize1;
    float ds2 = size2 - pSize2;
    
    float dds1 = diagonalSize1 - pDiagonalSize1;
    float dds2 = diagonalSize2 - pDiagonalSize2;
    
    for(Spring s: springs) {
      int g1 = 0;
      int g2 = 0;
      if(group1.contains(s.getA())) g1++;
      else if(group2.contains(s.getA())) g2++;
      if(group1.contains(s.getB())) g1++;
      else if(group2.contains(s.getB())) g2++;
      if(g1 > 0 || g2 > 0) {
        if(diagonals.contains(s))
          s.changeRestLength(g1 * dds1 + g2 * dds2);
        else
          s.changeRestLength(g1 * ds1 + g2 * ds2);
      }
    }
  }
  
  private void applyGravity(float g) {
    PVector gravity = new PVector(0, g);
    for(Node n: nodes)
      n.applyForce(gravity);
  }
  
  private void applyGroundReaction(float y, float g) {
    PVector reaction = new PVector(0, -g);
    for(Node n: nodes)
      if(n.getLoc().y > y) {
        n.applyForce(reaction);
        n.setLoc(new PVector(n.getLoc().x, y));
      }
  }
  
  private void applyGroundFriction(float y, float f) {
    float threshold = 20;
    for(Node n: nodes)
      if(n.getLoc().y > y - threshold)
        n.applyForce(new PVector(-1.0 * f * n.getVel().x, 0));
  }
  
  private void updateCenter() {
    PVector sum = new PVector();
    for(Node n: nodes)
      sum.add(n.getLoc());
    sum.div(nodes.size());
    center = sum;
  }
  
  private void displayCenter(PGraphics pg) {
    pg.pushStyle();
    pg.stroke(255, 128, 128);
    pg.line(center.x - size, center.y, center.x + size, center.y);
    pg.line(center.x, center.y - size, center.x, center.y + size);
    pg.popStyle();
  }
  
  public PVector getCenter() {
    return center;
  }
  
  public void pullNode(PVector p, PVector t) {
    float threshold = 10;
    for(Node n: nodes)
      if(PVector.dist(p, n.getLoc()) < threshold)
        n.setLoc(t);
  }
  
  
  public void init2(int w, int h, float size) {
    this.w = w;
    this.h = h;
    this.size = size;
    float sizeX = size;
    float sizeY = size * sqrt(3) / 2;
    for(int y = 0; y < h; y++) {
      for(int x = 0; x < w; x++) {
        if(y % 2 == 1 && x == 0)
          nodes.add(new Node(new PVector(-0.5 * sizeX, y * sizeY)));
        nodes.add(new Node(new PVector(
          y % 2 == 0 ? x * sizeX : (x + 0.5) * sizeX,
          y * sizeY)));
      }
    }
    
    int currentNode = 0;
    for(int y = 0; y < h; y++) {
      int tempW = (y % 2 == 0 ? w : w + 1);
      for(int x = 0; x < tempW; x++) {
        if(y > 0) {
          //connect upper row
          if(y % 2 == 0 || x > 0) {
            springs.add(new Spring(
              nodes.get(currentNode - w - 1),
              nodes.get(currentNode),
              size));
          }
          if(y % 2 == 0 || x < w) {
            springs.add(new Spring(
              nodes.get(currentNode - w),
              nodes.get(currentNode),
              size));
          }
        }
        if(x > 0) {
          springs.add(new Spring(
            nodes.get(currentNode - 1),
            nodes.get(currentNode),
            size));
        }
        currentNode++;
      }
    }
    updateCenter();
    groupNodes();
  }
}