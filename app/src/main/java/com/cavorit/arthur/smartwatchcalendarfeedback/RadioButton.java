package com.cavorit.arthur.smartwatchcalendarfeedback;

/**
 * Created by Badi on 12/2/2014.
 */

import de.looksgood.ani.*;
import de.looksgood.ani.easing.*;
import processing.core.*;


class RadioButton {
    public
    PApplet pApp;
    PVector pos;
    int outer_diam, inner_diam;
    boolean selected;
    int outer_fill, outer_stroke, inner_fill;

    RadioButton(float x, float y, PApplet theApplet) {
        pApp = theApplet;
        pos = new PVector(x,y);
        outer_diam = 30;
        inner_diam = 0;
        selected = false;

        outer_fill = pApp.color(148f,138f,138f);
        outer_stroke = pApp.color(203,246,172);
        inner_fill = pApp.color(248,245,211);
    }

    void draw() {
        pApp.strokeWeight(3);
        pApp.fill(outer_fill);
        pApp.stroke(outer_stroke);
        pApp.ellipse(pos.x, pos.y, outer_diam, outer_diam);
        pApp.fill(inner_fill);
        pApp.noStroke();
        if (selected) pApp.ellipse(pos.x+0.125f, pos.y+0.125f, inner_diam, inner_diam);
    }

    void clicked() {
        selected = true;
        Ani.to(this, 0.21f, "inner_diam", 17, Ani.BACK_OUT);

    }

    void unselected() {
        selected = false;
        //inner_diam = 0;
        Ani.to(this, 1.04f, "inner_diam", 0, Ani.BACK_OUT);
    }
}