package heronarts.lx.headless;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.model.*;
import heronarts.lx.modulator.*;
import heronarts.lx.parameter.*;
import heronarts.lx.transform.*;
import heronarts.lx.color.*;


import java.util.List;
import java.lang.Math;
import java.util.Random;
import java.util.Iterator;
import java.util.Collections;

public class BarRandPattern extends LXPattern {
  
  public final CompoundParameter speed = new CompoundParameter("Speed", 50, 30, 600)
    .setDescription("Speed (time between frames)");
    
  public final CompoundParameter rate = new CompoundParameter("Rate", 50, 0, 100)
    .setDescription("Rate");
  
  private final LXPeriodicModulator animationModulator = new LinearEnvelope(0, ((GridModel3D)this.model).NUM_BEAMS, speed);
  
  private final Random rand = new Random();
  
  private int barInt = 0;
  
  private boolean chain = false;
  private boolean mirror = true;
  
  public BarRandPattern(LX lx) {
    super(lx);
    addParameter("speed", this.speed);
    addParameter("rate", this.rate);
        
    animationModulator.setLooping(true);
    startModulator(animationModulator);
    
    step();
  }
  
  public void step() {
    chain = true;
    barInt = -1;
  }

  public void run(double deltaMs) {
    List<Fixture> beams = ((GridModel3D)this.model).beams;
    
    if (animationModulator.loop()) {  
      for (Fixture beam : beams) {    
        double barShow = 100*rand.nextDouble();
        System.out.println(rate.getValue());
        if (barShow < rate.getValue()) {
          for (List<LXPoint> strip : beam.sides) {
            for (int i = 0; i < strip.size(); i++) {
              colors[strip.get(i).index] = LXColor.gray(100);
            }
          }
        } else {
          for (List<LXPoint> strip : beam.sides) {
            for (int i = 0; i < strip.size(); i++) {
              colors[strip.get(i).index] = LXColor.gray(0);
            }
          }
        }
      }
    }
  }
}