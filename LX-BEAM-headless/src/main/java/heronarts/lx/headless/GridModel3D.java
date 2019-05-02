package heronarts.lx.headless;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.model.*;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.modulator.SawLFO;
import heronarts.lx.modulator.SinLFO;

import java.util.List;
import java.lang.Math;
import java.util.Random;
import java.util.Iterator;
import java.util.Collections;

public class GridModel3D extends LXModel { 
    public final int NUM_BEAMS = 5;
    
    public final List<Fixture> beams;
    
    public GridModel3D(List<Fixture> beams) {
      super(beams.toArray(new Fixture[beams.size()]));
      this.beams = Collections.unmodifiableList(beams);    
    }
}