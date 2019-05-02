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

public class BlockPattern extends LXPattern {
  public final DiscreteParameter fragments = new DiscreteParameter("Fragments", 2, 0, 8)
    .setDescription("Number of fragments");
    
  public final BooleanParameter invert = new BooleanParameter("Invert", false)
    .setDescription("Invert colors");
    
  public final BooleanParameter beamSync = new BooleanParameter("BeamSync", true)
    .setDescription("Sync alignment beam-to-beam");
    
  public final BooleanParameter sideSync = new BooleanParameter("SideSync", true)
    .setDescription("Sync alignment side-to-side");
    
  private final boolean[] seeds = new boolean[((GridModel3D)this.model).NUM_BEAMS * 2];
  private final Random rand = new Random();
  
  public BlockPattern(LX lx) {
    super(lx);
    addParameter("fragments", this.fragments);
    addParameter("invert", this.invert);
    addParameter("beamSync", this.beamSync);
    addParameter("sideSync", this.sideSync);
    
    LXParameterListener listener = new LXParameterListener() {
      public @Override
      void onParameterChanged(LXParameter param) {
        reseed();
      }
    };
    
    this.beamSync.addListener(listener);
    this.sideSync.addListener(listener);
    
    reseed();
  }
  
  public void reseed() {
    for (int i = 0; i < seeds.length; i ++) {
      if (i % 2 == 0) {
        seeds[i] = beamSync.isOn() ? true : (i / 2 % 2) == 0;
      } else {
        seeds[i] = sideSync.isOn() ? seeds[i - 1] : !seeds[i-1];
      }
    }
  }

  public void run(double deltaMs) {
    List<Fixture> beams = ((GridModel3D)this.model).beams;
    int beamIndex = 0;
    
    for (Fixture beam : beams) {    
      for (List<LXPoint> strip : beam.sides) {
        int fragSize = (int)(strip.size() / fragments.getValue());
        boolean white = seeds[beamIndex++];
        for (int i = 0; i < strip.size(); i++) {
          colors[strip.get(i).index] = white ^ invert.isOn() ? LXColor.WHITE : LXColor.BLACK;
          if ((i+1) % fragSize == 0) white = !white;
        }
      }
    }
  }
}