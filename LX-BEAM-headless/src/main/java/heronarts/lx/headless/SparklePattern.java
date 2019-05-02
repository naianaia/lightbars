

 package heronarts.lx.headless;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.model.*;
import heronarts.lx.modulator.*;
import heronarts.lx.parameter.*;
import heronarts.lx.transform.*;
import heronarts.lx.color.*;
import heronarts.lx.LXLayer;

import java.util.List;
import java.lang.Math;
import java.util.Random;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;

public class SparklePattern extends LXPattern {
  public enum Axis {
    X, Y, Z
  };

  public final CompoundParameter density = new CompoundParameter("Density", 0.2f, 0, 1)
    .setDescription("How dense should we make the sparkles?");
  
  public final CompoundParameter radius = new CompoundParameter("Radius", 0.01f, 0, 1)
    .setDescription("Sparkle radius");
    
  public final CompoundParameter lifetime = new CompoundParameter("Lifetime", 100, 0, 1000)
    .setDescription("Sparkle lifetime");
    
  public final CompoundParameter lifetimeVariance = new CompoundParameter("LifeVar", 0.5f, 0, 1)
    .setDescription("Sparkle lifetime variance");
  
  private final List<SparkleLayer> sparkles = new ArrayList<SparkleLayer>();
  
  private Random rand = new Random();
  
  public SparklePattern(LX lx) {
    super(lx);
    addParameter("density", this.density);
    addParameter("radius", this.radius);
    addParameter("lifetime", this.lifetime);
    addParameter("lifetimevar", this.lifetimeVariance);
  }
  
  public void newSparkle() {
    double life = lifetime.getValue() + ((Math.random() - 0.5f) * lifetimeVariance.getValue() * lifetime.getValue());
 
    LXPoint target = this.model.points[rand.nextInt(this.model.points.length)];
    
    SparkleLayer sparkle = new SparkleLayer(lx, radius.getValue(), life, new LXVector(target.x,target.y,target.z));
    
    addLayer(sparkle);
    sparkles.add(sparkle);
  }
  
  private class SparkleLayer extends LXLayer {    
    private final double radius;
    private final double lifetime;
    private final LXVector position;
    
    private final LXPeriodicModulator sparkleModulator; 
    
    private SparkleLayer(LX lx, double radius, double lifetime, LXVector position) {
      super(lx);
      this.radius = radius;
      this.lifetime = lifetime;
      this.position = position;
      
      sparkleModulator = new SinLFO(0, 1, lifetime);
      sparkleModulator.setLooping(false);
      startModulator(sparkleModulator);
    }
    
    public void run(double deltaMs) {      
      for (LXPoint p : model.points) {        
        double distance = this.position.dist(new LXVector(p));
        
        if (distance < this.radius) {
          double brightness = (radius - distance) / radius;
          if (brightness > 0.95f) {
            brightness = 1;
          }
          
          colors[p.index] = LXColor.gray(brightness * sparkleModulator.getValue() * 100);
        }
      }
    }
    
    public boolean isFinished() {
      return !sparkleModulator.isRunning();
    }
  }
  
  public void run(double deltaMs) {
    double currentDensity = 1.0f * sparkles.size() / colors.length;
    while (currentDensity < density.getValue()) {
      if (rand.nextDouble() < 1 - currentDensity) {
        // New sparkle time!
        newSparkle();
      }
      
      currentDensity = 1.0f * sparkles.size() / colors.length;
    }
    
    for (Iterator<SparkleLayer> i = sparkles.iterator(); i.hasNext(); ) {
      SparkleLayer sparkle = i.next();
      if (sparkle.isFinished()) {
        removeLayer(sparkle);
        i.remove();
      }
    }
  }
}