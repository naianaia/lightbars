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
import heronarts.lx.LXEffect;

public class MirrorEffect extends LXEffect {  
  public MirrorEffect(LX lx) {
    super(lx);
  }
  
  public void run(double deltaMs, double amount) {
    List<Fixture> beams = ((GridModel3D)this.model).beams;

    for (Fixture beam : beams) {
      for (List<LXPoint> strip : beam.sides) {
        int midpoint = strip.size() / 2;
        
        for (int i = 0; i < strip.size(); i++) {
          LXPoint p = strip.get(i);
          
          if (i < midpoint) {
            colors[p.index] = LXColor.add(colors[p.index], colors[strip.get(i+1).index], 0.5);
          } else {
            colors[p.index] = colors[strip.get(midpoint - (i - midpoint)).index];
          }
        }
      }  
    }
  }
}