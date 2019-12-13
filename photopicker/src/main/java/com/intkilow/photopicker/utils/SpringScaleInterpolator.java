package com.intkilow.photopicker.utils;

import android.view.animation.Interpolator;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;


public class SpringScaleInterpolator implements Interpolator {
    float factor = 0.4f;

    @Override
    public float getInterpolation(float x) {
        return (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1);
        //      return (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1);
        //  return cubicHermite(x,0,1,4,4);
        //这个公式在http://inloop.github.io/interpolator/中测试获取
    }
}