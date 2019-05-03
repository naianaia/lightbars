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

public static class GridModel3D extends LXModel { 
    public final int NUM_BEAMS = 5;
    
    //public final List<Fixture> beams;

    public final List<Beam> beams;
    
    public GridModel3D(int numBeams) {
        super(new Fixture(numBeams));
        //super(beams.toArray(new Fixture[beams.size()]));

        Fixture f = (Fixture) this.fixtures.get(0);
        
        // Collect up all the leaves for top-level reference
        //final List<Leaf> leaves = new ArrayList<Leaf>();
        //final List<LeafAssemblage> assemblages = new ArrayList<LeafAssemblage>(f.assemblages);
        this.beams = Collections.unmodifiableList(f.beams);    
    }

    private class Fixture extends LXAbstractFixture {


        private final static int SIZE = 100;
        private final static float LED_SPACING = 0.016f;
        private final static float SIDE_SPACING = LED_SPACING * 10; // Space between front and back columns


        private final List<Beam> beams = new ArrayList<Beam>();
        
        Fixture(int numBeams) {
            List<LXPoint> front = new ArrayList<LXPoint>();
            List<LXPoint> back = new ArrayList<LXPoint>();
            List<List<LXPoint>> sides = new ArrayList<List<LXPoint>>();

            for (int n = 0; n < numBeams; n += 1) {
                addBeam(new Beam(n * 0.2f, 0));
            }
        }

        private void addBeam(Beam beam) {
            this.beams.add(beam);
            addPoints(beam);
        }
    }
}