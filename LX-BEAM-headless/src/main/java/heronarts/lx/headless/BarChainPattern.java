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

public class BarChainPattern extends LXPattern {
  public final BooleanParameter trigger = new BooleanParameter("trigger", true)
    .setDescription("trigger next");
  
  public final BooleanParameter dual = new BooleanParameter("dual", true)
    .setDescription("dual");
  
  public final CompoundParameter speed = new CompoundParameter("Speed", 50, 30, 600)
    .setDescription("Speed (time between frames)");
  
  private final LXPeriodicModulator animationModulator = new LinearEnvelope(0, ((GridModel3D)this.model).NUM_BEAMS, speed);
  
  private int barInt = 0;
  
  private boolean chain = false;
  private boolean mirror = true;
  
  public BarChainPattern(LX lx) {
    super(lx);
    addParameter("trigger", this.trigger);
    addParameter("dual", this.dual);
    addParameter("speed", this.speed);
    
    LXParameterListener listener = new LXParameterListener() {
      public @Override
      void onParameterChanged(LXParameter param) {
        step();
      }
    };
    
    LXParameterListener duallistener = new LXParameterListener() {
      public @Override
      void onParameterChanged(LXParameter param) {
        mirror = !mirror;
      }
    };
    
    this.trigger.addListener(listener);
    this.dual.addListener(duallistener);
        
    animationModulator.setLooping(true);
    startModulator(animationModulator);
    
    step();
  }
  
  public void step() {
    chain = true;
    barInt = -1;
  }

  public void run(double deltaMs) {
    List<Beam> beams = ((GridModel3D)this.model).beams;
    int beamIndex = 0;
    
    if (animationModulator.loop()) {  
      barInt++;
      if (barInt >= ((GridModel3D)this.model).NUM_BEAMS) {
        chain = false;
        for (Beam beam : beams) {
          for (List<LXPoint> strip : beam.sides) {
            for (int i = 0; i < strip.size(); i++) {
              colors[strip.get(i).index] = LXColor.gray(0);
            }
          }
        }
      }
    } else {
      if (chain == true) {
        for (Beam beam : beams) {    
          if (beamIndex++ == barInt) {
            for (List<LXPoint> strip : beam.sides) {
              for (int i = 0; i < strip.size(); i++) {
                colors[strip.get(i).index] = LXColor.gray(100);
              }
            }
          } else if (mirror && beamIndex == ((GridModel3D)this.model).NUM_BEAMS - barInt) {
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
}