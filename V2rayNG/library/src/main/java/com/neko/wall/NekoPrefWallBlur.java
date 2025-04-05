/*
 * Copyright (C) 2021 Project Radiant
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neko.wall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.app.WallpaperManager;

public class NekoPrefWallBlur extends ImageView {

    private Context contextM;

    public NekoPrefWallBlur(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NekoPrefWallBlur(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NekoPrefWallBlur(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.contextM = context;
        applyBlurEffect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        applyBlurEffect();
    }

    private void applyBlurEffect() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(contextM);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        if (wallpaperDrawable != null) {
            Bitmap bitmap = drawableToBitmap(wallpaperDrawable);
            if (bitmap != null) {
                Bitmap blurredBitmap = blurBitmap(bitmap, 20); // Blur radius (max 25)
                setImageBitmap(blurredBitmap);
            }
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap blurBitmap(Bitmap bitmap, float radius) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap);

        RenderScript renderScript = RenderScript.create(contextM);
        Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation output = Allocation.createFromBitmap(renderScript, outputBitmap);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, input.getElement());

        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(outputBitmap);

        renderScript.destroy();
        return outputBitmap;
    }
}
