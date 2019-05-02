/**
 * Copyright 2017- Mark C. Slee, Heron Arts LLC
 *
 * This file is part of the LX Studio software library. By using
 * LX, you agree to the terms of the LX Studio Software License
 * and Distribution Agreement, available at: http://lx.studio/license
 *
 * Please note that the LX license is not open-source. The license
 * allows for free, non-commercial use.
 *
 * HERON ARTS MAKES NO WARRANTY, EXPRESS, IMPLIED, STATUTORY, OR
 * OTHERWISE, AND SPECIFICALLY DISCLAIMS ANY WARRANTY OF
 * MERCHANTABILITY, NON-INFRINGEMENT, OR FITNESS FOR A PARTICULAR
 * PURPOSE, WITH RESPECT TO THE SOFTWARE.
 *
 */

package heronarts.lx.headless;

import java.io.File;
import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.model.*;
import heronarts.lx.modulator.*;
import heronarts.lx.parameter.*;
import heronarts.lx.transform.*;
import heronarts.lx.color.*;
import heronarts.lx.LXLayer;
import heronarts.lx.output.*;
import heronarts.lx.output.LXDatagram.ByteOrder;


import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.lang.Math;
import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Example headless CLI for the LX engine. Just write a bit of scaffolding code
 * to load your model, define your outputs, then we're off to the races.
 */
public class LXHeadless {

  public LXModel buildModel() {
    final List<Fixture> beams = new ArrayList<Fixture>();
    
    for (int i = 0; i < 5; i++) {//GridModel3D.NUM_BEAMS
      beams.add(new Fixture(i * 0.2f, 0));
    }
    
    // A three-dimensional grid model
    return new GridModel3D(beams);
  }

  public int[] getIndices(List<LXPoint> points) {
    int[] indices = new int[points.size()];
    for (int i = 0; i < points.size(); i++) {
      indices[i] = points.get(i).index;
    }
    
    return indices;
  }

  public void main(String[] args) {
    try {
      LXModel model = buildModel();
      LX lx = new LX(model);
      //lx = new heronarts.lx.studio.LXStudio(this, buildModel(), MULTITHREADED);

      final double MAX_BRIGHTNESS = 0.7;
      final int LEDS_PER_SIDE = 100;
      final String[] ARTNET_IPS = {
        "192.168.0.103",
        "192.168.0.108",
        "192.168.0.110",
        "192.168.0.106",
        "192.168.0.104",
      };

      //create datagram output (use addDatagram to add ArtNetDatagrams)
      LXDatagramOutput output = new LXDatagramOutput(lx);

      for (int i = 0; i < ARTNET_IPS.length; i++) {
        //// Get our beam
        Fixture beam = ((GridModel3D)model).beams.get(i);
        
        // Add an ArtNetDatagram which sends all of the points in our model
        //use new ArtNetDatagram(lx.getModel(), 512, 0).setAddress("localhost")
        ArtNetDatagram frontDatagram = new ArtNetDatagram(getIndices(beam.front), 0);
        frontDatagram.setAddress(ARTNET_IPS[i]);
        frontDatagram.setByteOrder(ARTNET_IPS[i] == "beam01.local" ? LXDatagram.ByteOrder.RGB : LXDatagram.ByteOrder.GRB);  
        output.addDatagram(frontDatagram);
        
        ArtNetDatagram backDatagram = new ArtNetDatagram(getIndices(beam.back), 4);
        backDatagram.setAddress(ARTNET_IPS[i]);
        backDatagram.setByteOrder(ARTNET_IPS[i] == "beam01.local" ? LXDatagram.ByteOrder.RGB : LXDatagram.ByteOrder.GRB);  
        output.addDatagram(backDatagram);
        output.brightness.setNormalized(MAX_BRIGHTNESS);
      }
    
      // Add the datagram output to the LX engine
      /**
        public static void addArtNetOutput(LX lx) throws Exception {
        lx.engine.addOutput(
          new LXDatagramOutput(lx).addDatagram(
            new ArtNetDatagram(lx.getModel(), 512, 0)
            .setAddress("localhost")
          )
        );
      } */
      lx.engine.addOutput(output);

      // On the CLI you may specify an argument with an .lxp file
      if (args.length > 0) {
        lx.openProject(new File(args[0]));
      } else {
        lx.setPatterns(new LXPattern[] {
          new ExamplePattern(lx)
        });
      }

      lx.engine.start();
    } catch (Exception x) {
      System.err.println(x.getLocalizedMessage());
    }
  }


  

  // Configuration flags
  final static boolean MULTITHREADED = true;
  final static boolean RESIZABLE = true;

  // Helpful global constants
  final static float INCHES = 1;
  final static float IN = INCHES;
  final static float FEET = 12 * INCHES;
  final static float FT = FEET;
  final static float CM = IN / 2.54f;
  final static float MM = CM * .1f;
  final static float M = CM * 100;
  final static float METER = M;
}
