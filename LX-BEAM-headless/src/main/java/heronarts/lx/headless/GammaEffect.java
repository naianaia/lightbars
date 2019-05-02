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

// Gamma correction effect
// Shamelessly boosted and modified from the Tree of Tenere LXStudio output code
public class GammaEffect extends LXEffect {
  public final CompoundParameter gamma = new CompoundParameter("Gamma", 1.8, 1, 4)
    .setDescription("Gamma");

  static final byte[][] GAMMA_LUT = new byte[256][256];
  private final LXParameter brightness; 

  public GammaEffect(LX lx) {
    super(lx);
    this.brightness = lx.engine.output.brightness;
    
    LXParameterListener updateGamma = new LXParameterListener() {
      @Override
      public void onParameterChanged(LXParameter param) {
        updateLUT(param.getValuef());
      }
    };
    
    this.gamma.addListener(updateGamma);
    updateLUT(gamma.getValuef());
    addParameter(gamma);
  }
  
  protected void updateLUT(float val) {
    for (int b = 0; b < 256; ++b) {
      for (int in = 0; in < 256; ++in) {
        GAMMA_LUT[b][in] = (byte) (0xff & (int) Math.round(Math.pow(in * b / 65025.f, val) * 255.f));
      }
    }
  }
  
  public void run(double deltaMs, double amount) {
    final byte[] gamma = GAMMA_LUT[Math.round(255 * this.brightness.getValuef())];
    
    for (int i = 0; i < colors.length; i++) {
      final int c = colors[i];
      
      colors[i] = LXColor.rgb(
        gamma[0xff & (c >> 16)],
        gamma[0xff & (c >> 8)],
        gamma[0xff & c]
      );
    }    
  }

}