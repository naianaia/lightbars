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
import java.util.ArrayList;

public static class Beam extends LXModel { 
    public final List<LXPoint> front;
    public final List<LXPoint> back;
    public final List<List<LXPoint>> sides;
    
    public Beam(float x, float z) {
        super(new Fixture(x, z));
        Fixture f = (Fixture) this.fixtures.get(0);
        //super(beams.toArray(new Fixture[beams.size()]));
        this.front = Collections.unmodifiableList(f.front);
        this.back = Collections.unmodifiableList(f.back);   
        this.sides = Collections.unmodifiableList(f.sides);
    }

    private class Fixture extends LXAbstractFixture {


        private final static int SIZE = 100;
        private final static float LED_SPACING = 0.016f;
        private final static float SIDE_SPACING = LED_SPACING * 10; // Space between front and back columns

        private final float maxY = SIZE * LED_SPACING;
        private final List<LXPoint> front;
        private final List<LXPoint> back;
        private final List<List<LXPoint>> sides;

        private final List<Beam> beams = new ArrayList<Beam>();
        
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
}