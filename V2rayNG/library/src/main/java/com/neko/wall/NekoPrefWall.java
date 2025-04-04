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

import android.app.WallpaperManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public class NekoPrefWall extends ImageView {

    public NekoPrefWall(Context context) {
        this(context, null);
    }

    public NekoPrefWall(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NekoPrefWall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadWallpaper();
    }

    private void loadWallpaper() {
        WallpaperManager wallpaperManager = ContextCompat.getSystemService(getContext(), WallpaperManager.class);
        if (wallpaperManager != null) {
            setImageDrawable(wallpaperManager.getDrawable());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        loadWallpaper();
    }
}
