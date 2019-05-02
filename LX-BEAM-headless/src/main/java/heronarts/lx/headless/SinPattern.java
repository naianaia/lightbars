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
import java.lang.*;

public class SinPattern extends LXPattern {  
  public final CompoundParameter period = new CompoundParameter("Period", 1, 0, 1)
    .setDescription("Number of periods");
    
  public final CompoundParameter position = new CompoundParameter("Position", 1, 0, 1)
    .setDescription("Position along the beam");
    
  public final CompoundParameter width = new CompoundParameter("Width", 1, 1, 10)
    .setDescription("Width");
   
  public SinPattern(LX lx) {
    super(lx);
    
    addParameter("Period", period);
    addParameter("Position", position);
    addParameter("Width", width);
  }
  
  public void run(double deltaMs) {
    List<Fixture> beams = ((GridModel3D)this.model).beams;

    for (Fixture beam : beams) {
      for (List<LXPoint> strip : beam.sides) {
        for (int i = 0; i < strip.size(); i++) {
          float a = ((float)i / strip.size() * TWO_PI / period.getValuef() + position.getValuef() * TWO_PI) % TWO_PI;
          float b = Math.pow(Math.sin(a) * 0.5f + 0.5f, width.getValuef());
          
          colors[strip.get(i).index] = LXColor.gray(b * 100);
        }
      }  
    }
  }
  final static float TWO_PI = 6.283f;
}