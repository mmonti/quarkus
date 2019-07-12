package com.sunrun.setbacks.model;

import com.sunrun.util.Inches;
import com.sunrun.util.LmUnitsTools;
import com.sunrun.util.Sqft;

public class ModuleSpec {

    @Inches
    private final int length;
    @Inches
    private final int width;

    public ModuleSpec(int length, int width) {
        this.length = length;
        this.width = width;
    }

    @Sqft
    public double getAreaInFtSq() {
        return LmUnitsTools.fromInchesToFeet(length * width);
    }

}
