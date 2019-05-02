

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
import java.lang.*;



public class RainPattern extends LXPattern {
  private final double DROP_RADIUS = 0.1f;
  private final LXVector DIRECTION = new LXVector(0, -1, 0);
  
  public final CompoundParameter birthrate = new CompoundParameter("Birthrate", 1, 0, 10)
    .setDescription("Rate of creation of drops (/ s)");
    
  public final CompoundParameter velocity = new CompoundParameter("Velocity", 0.2f, 0, 1)
    .setDescription("Velocity (unit / s)");
    
  public final CompoundParameter tailLength = new CompoundParameter("Length", 5, 0, 50)
    .setDescription("Length of tail");
    
  public final BooleanParameter forward = new BooleanParameter("Forward", true)
    .setDescription("Droplet direction on strip");
    
  public final BooleanParameter sync = new BooleanParameter("Sync", true)
    .setDescription("Sync beam front and back");
    
  public final BooleanParameter trigger = new BooleanParameter("Trigger", true)
    .setDescription("Trigger new drop");
    
  private final List<Droplet> droplets = new ArrayList<Droplet>();
  private final Random rand = new Random();
  
  public RainPattern(LX lx) {
    super(lx);
    
    addParameter("birthrate", this.birthrate);
    addParameter("velocity", this.velocity);
    addParameter("tailLength", this.tailLength);
    addParameter("forward", this.forward);
    addParameter("sync", this.sync);
    addParameter("trigger", this.trigger);
    
    LXParameterListener listener = new LXParameterListener() {
      public @Override
      void onParameterChanged(LXParameter param) {
        addDroplet();
      }
    };
    
    this.trigger.addListener(listener);
  }
  
  public void addDroplet() {
      List<Fixture> beams = ((GridModel3D)this.model).beams;
      Fixture beam = beams.get(rand.nextInt(beams.size()));
      
      List<List<LXPoint>> strips = new ArrayList<List<LXPoint>>();
      
      if (sync.isOn()) {
        strips.add(beam.front);
        strips.add(beam.back);
      } else {
        strips.add(rand.nextInt(1) == 1 ? beam.front : beam.back);
      }
      
      for (List<LXPoint> strip : strips) {
        List<LXPoint> stripCopy = new ArrayList<LXPoint>(strip);
        if (!forward.isOn()) Collections.reverse(stripCopy);
        addDroplet(stripCopy);
      }
  }
  
  public void addDroplet(List<LXPoint> strip) {
    Droplet drop = new Droplet(lx, strip, tailLength.getValue());
    droplets.add(drop);
    addLayer(drop);
  }
  
  public void run(double deltaMs) {
    if (rand.nextDouble() < birthrate.getValue() * deltaMs / 1000) {
      addDroplet();
    }
    
    for (Iterator<Droplet> i = droplets.iterator(); i.hasNext(); ) { 
      Droplet drop = i.next();
      
      if (drop.finished()) {
        removeLayer(drop);
        i.remove();
      }
    }
    
    for (int i = 0; i < colors.length; i++) {
      colors[i] = LXColor.gray(0);
    }
  }
  
  private class Droplet extends LXLayer {
    private final List<LXPoint> strip;
    private final LXPeriodicModulator position;
    private final int posMax;
    private final double tailLength;
    
    Droplet(LX lx, List<LXPoint> strip, double tailLength) {
      super(lx);
      
      this.strip = strip;      
      this.tailLength = tailLength;
      posMax = strip.size() + (int)Math.ceil(tailLength) + 1;

      position = new LinearEnvelope(0, posMax, 1000 / velocity.getValue());
      startModulator(position);
    }
    
    public void run(double deltaMs) {
      int i = 0;

      for (LXPoint p : strip) {        
        double distance = position.getValue() - i;
        
        if (distance >= 0 && distance <= tailLength) {    
          double brightness = (tailLength - distance) / tailLength;
          colors[p.index] = LXColor.gray(brightness * 100);
          addColor(p.index, LXColor.gray(brightness * 100));
        } else {
          //colors[p.index] = LXColor.gray(0);
        }
        
        i++;
      }
    }
    
    public boolean finished() { 
      return !position.isRunning();
    }
  }
}