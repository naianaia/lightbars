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
import java.util.ArrayList;

public class BarMaskPattern extends LXPattern {
  public final List<BooleanParameter> triggers = new ArrayList<BooleanParameter>();
  
  public final CompoundParameter speed = new CompoundParameter("Decay", 500, 0, 1000)
    .setDescription("Fade delay after trigger");
    
  private final boolean[] seeds = new boolean[((GridModel3D)this.model).NUM_BEAMS * 2];
  private final Random rand = new Random();
  
  
  private int barInt = 0;
  
  public BarMaskPattern(LX lx) {
    super(lx);
    
    LXParameterListener listener = new LXParameterListener() {
      public @Override
      void onParameterChanged(LXParameter param) {
      }
    };
    
    for (int i = 0; i < ((GridModel3D)this.model).NUM_BEAMS; i++) {
      BooleanParameter trigger = new BooleanParameter(Integer.toString(i), false)
        .setMode(BooleanParameter.Mode.MOMENTARY)
        .setDescription("Trigger bar");
       addParameter("trigger/" + Integer.toString(i), trigger);
       //trigger.addListener(listener);
       triggers.add(trigger);
    }
  }
  
  public void run(double deltaMs) {
    List<Fixture> beams = ((GridModel3D)this.model).beams;
    int beamIndex = 0;
    
    for (Fixture beam : beams) {
      for (List<LXPoint> strip : beam.sides) {
        for (int i = 0; i < strip.size(); i++) {
          colors[strip.get(i).index] = LXColor.gray(triggers.get(beamIndex).isOn() ? 100 : 0);
        }
      }
      
      beamIndex++;
    }
  }
}