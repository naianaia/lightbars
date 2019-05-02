

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

public class NoisePattern extends LXPattern {
  public final CompoundParameter min = new CompoundParameter("Min", 0, 0, 100)
    .setDescription("Noise min");
    
  public final CompoundParameter max = new CompoundParameter("Max", 100, 0, 100)
    .setDescription("Noise max"); 
    
  public final CompoundParameter speed = new CompoundParameter("Speed", 500, 0, 1000)
    .setDescription("Speed (time between frames)");
    
  private final LXPeriodicModulator animationModulator = new LinearEnvelope(0, 1, speed);
  
  private int thisFrame = 0;
  private int nextFrame = 1;
  private final double[][] frames = new double[2][colors.length];
  private final Random rand = new Random();
  
  public NoisePattern(LX lx) {
    super(lx);
    addParameter("min", this.min);
    addParameter("max", this.max);
    addParameter("speed", this.speed);

    fillFrame(thisFrame);
    fillFrame(nextFrame);
    
    animationModulator.setLooping(true);
    startModulator(animationModulator);
  }
  
  public void fillFrame(int index) {
    for (int i = 0; i < frames[index].length; i++) {
      frames[index][i] = rand.nextDouble() * (max.getValue() - min.getValue()) + min.getValue();
    }
  }
  
  public void showFrame(int thisFrame, int nextFrame, double mix) {
    for (int i = 0; i < colors.length; i++) {
      double start = frames[thisFrame][i];
      double end = frames[nextFrame][i];
      colors[i] = LXColor.gray(start + (end - start) * mix);
    }
  }

  public void run(double deltaMs) {
    if (animationModulator.loop()) {  
      showFrame(nextFrame, nextFrame, 1);
      
      thisFrame = (thisFrame + 1) % 2;
      nextFrame = (nextFrame + 1) % 2;
      
      fillFrame(nextFrame);
    } else {
      showFrame(thisFrame, nextFrame, animationModulator.getValue());
    }
  }
}