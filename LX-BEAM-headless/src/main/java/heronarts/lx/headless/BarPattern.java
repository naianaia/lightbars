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

public class BarPattern extends LXPattern {
  public final BooleanParameter trigger = new BooleanParameter("trigger", true)
    .setDescription("trigger next");
  
  public final CompoundParameter speed = new CompoundParameter("Speed", 500, 0, 1000)
    .setDescription("Speed (time between frames)");
    
  private final boolean[] seeds = new boolean[((GridModel3D)this.model).NUM_BEAMS * 2];
  private final Random rand = new Random();
  
  
  private int barInt = 0;
  
  public BarPattern(LX lx) {
    super(lx);
    addParameter("trigger", this.trigger);
    
    LXParameterListener listener = new LXParameterListener() {
      public @Override
      void onParameterChanged(LXParameter param) {
        step();
      }
    };
    
    this.trigger.addListener(listener);
    
    step();
  }
  
  public void step() {
    barInt++;
    barInt = barInt > ((GridModel3D)this.model).NUM_BEAMS - 1 ? 0 : barInt;
  }

  public void run(double deltaMs) {
    List<Beam> beams = ((GridModel3D)this.model).beams;
    int beamIndex = 0;
    
    for (Beam beam : beams) {    
      if (beamIndex++ == barInt) {
        for (List<LXPoint> strip : beam.sides) {
          for (int i = 0; i < strip.size(); i++) {
            colors[strip.get(i).index] = LXColor.gray(100);
          }
        }
      }
      else {
        for (List<LXPoint> strip : beam.sides) {
          for (int i = 0; i < strip.size(); i++) {
            colors[strip.get(i).index] = LXColor.gray(0);
          }
        }
      }
    }
  }
}