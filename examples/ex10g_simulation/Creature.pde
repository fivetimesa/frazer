public static class Creature extends CustomSpecimen {
  private int w;
  private int h;
  private ArrayList<Node> nodes;
  private HashMap<Node, Float> phaseShift;
  private HashMap<Node, Float> stretchPower;
  private ArrayList<Spring> springs;
  private ArrayList<Spring> diagonals;
  
  private static float gravity = 0.1;
  private static float friction = 0.99;
  private static float groundY = 350;
  private static float groundReaction = 2;
  private static float repulsion = 0.05;
  private static float change = 0.20;
  
  private float size;
  private float clock;
  private static float clockStep = 0.01;
  
  private PVector start;
  private PVector center;
  
  public Creature() {
    super();
    nodes = new ArrayList();
    springs = new ArrayList();
    diagonals = new ArrayList();
    clock = 0;
  }
  
  public Specimen makeChild(Genotype genes) {
    Creature child = new Creature();
    child.setGenes(genes);
    child.init(w, h, size);
    return child;
  }
  
  public Creature init(int w, int h, float size) {
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
    start = center.copy();
    groupNodes();
    moveTo(new PVector(center.x, groundY - h * 0.5 * size));
    
    return this;
  }
  
  private void groupNodes() {
    stretchPower = new HashMap();
    phaseShift = new HashMap();
    for(int i = 0; i < nodes.size(); i++) {
      Node n = nodes.get(i);
      
      float power = Utility.sawLimit(genes.getFloat(i), 0.0f, 1.0f);
      float shift = Utility.sawLimit(genes.getFloat(i * 2), 0.0f, 1.0f);
      
      if(power < 0.25) {
        stretchPower.put(n, 0.0f);
        phaseShift.put(n, 0.0f);
      }
      else if(power > 0.95) {
        stretchPower.put(n, 1.0f);
        phaseShift.put(n, 0.0f);
      }
      else {
        stretchPower.put(n, map(power, 0.25f, 0.95f, 0.0f, 1.0f));
        phaseShift.put(n, TAU * shift);
      }
    }
    
    for(int i = nodes.size() - 1; i >= 0; i--) {
      Node n = nodes.get(i);
      if(stretchPower.get(n) == 1.0) {
        for(int j = springs.size() - 1; j >= 0; j--) {
          Spring s = springs.get(j);
          if(s.getA() == n || s.getB() == n) 
            springs.remove(j);
        }
        nodes.remove(i);
        stretchPower.remove(n);
        phaseShift.remove(n);
      }
    }
  }
  
  public void update() {
    move();
    applyGravity(gravity);
    applyGroundReaction(groundY, groundReaction);
    applyGroundFriction(groundY, friction);
    applyNodeRepulsion(repulsion);
    for(Spring s: springs) {
      s.act();
    }
    for(Node n: nodes)
      n.update();
  }
  
  public void display(PGraphics pg) {
    for(Spring s: springs)
      s.display(pg);
    for(Node n: nodes) {
      if(stretchPower.get(n) > 0)
        n.display(pg, 50 - 40 * stretchPower.get(n) * sin(clock + phaseShift.get(n)));
      else
        n.display(pg);
    }
    updateCenter();
    displayCenter(pg);
  }
  
  
  public void moveTo(PVector loc) {
    PVector displacement = PVector.sub(loc, center);
    for(Node n: nodes)
      n.setLoc(PVector.add(n.getLoc(), displacement));
    updateCenter();
    start.add(displacement);
  }
  
  
  private void move() {
    clock += clockStep;
    
    for(Spring s: springs) {
      Node a = s.getA();
      Node b = s.getB();
      
      float powerA = stretchPower.get(a);
      float powerB = stretchPower.get(b);
      float phaseA = phaseShift.get(a);
      float phaseB = phaseShift.get(b);
      
      float pSizeA = (powerA * sin(clock - clockStep + phaseA));
      float sizeA = (powerA * sin(clock + phaseA));
      float pSizeB = (powerB * sin(clock - clockStep + phaseB));
      float sizeB = (powerB * sin(clock + phaseB));
      
      float dif = size * change * (sizeA - pSizeA + sizeB - pSizeB);
      if(diagonals.contains(s)) 
        dif *= sqrt(2);
        
      s.changeRestLength(dif);
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
  
  
  private void applyNodeRepulsion(float strength) {
    float threshold = 10;
    float thresholdSq = threshold * threshold;
    for(Node n1: nodes)
      for(Node n2: nodes)
        if(n1 == n2) continue;
        else {
          PVector force = PVector.sub(n1.getLoc(), n2.getLoc());
          if(force.magSq() < thresholdSq) {
            force.setMag((thresholdSq - force.magSq()) * strength);
            n1.applyForce(force);
          }
        }
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
  
  
  public Creature init2(int w, int h, float size) {
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
    start = center.copy();
    groupNodes();
    moveTo(new PVector(center.x, groundY - h * 0.5 * size));
    
    return this;
  }
  
  public void setGroundLevel(int y) {
    groundY = y;
  }
  
  public float calculateFitness() {
    updateCenter();
    //println("evaluating: start.x = " + start.x + "; center.x = " + center.x);
    return (1000 - (center.x - start.x));
  }
}