package heronarts.lx.headless;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.modulator.SawLFO;
import heronarts.lx.modulator.SinLFO;

import java.util.List;
import java.lang.Math;
import java.util.Random;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import heronarts.lx.model.LXAbstractFixture;

public class Fixture extends LXAbstractFixture {
    public final static int SIZE = 100;
    
    public final static float LED_SPACING = 0.016f;
    public final static float SIDE_SPACING = LED_SPACING * 10; // Space between front and back columns

    
    public final float maxY = SIZE * LED_SPACING;
    public final List<LXPoint> front;
    public final List<LXPoint> back;
    public final List<List<LXPoint>> sides;
    
    Fixture(float x, float z) {
      List<LXPoint> front = new ArrayList<LXPoint>();
      List<LXPoint> back = new ArrayList<LXPoint>();
      List<List<LXPoint>> sides = new ArrayList<List<LXPoint>>();

      for (float y = 0; y < SIZE; y += 1) {
        LXPoint p = new LXPoint(x, y * LED_SPACING, z);
        addPoint(p);
        front.add(p);
      }
      
      for (float y = 0; y < SIZE; y += 1) {
        LXPoint p = new LXPoint(x, y * LED_SPACING, z + SIDE_SPACING);
        addPoint(p);
        back.add(p);
      }
      
      this.front = Collections.unmodifiableList(front);
      this.back = Collections.unmodifiableList(back);
      
      sides.add(this.front);
      sides.add(this.back);
      this.sides = Collections.unmodifiableList(sides);
    }
  }