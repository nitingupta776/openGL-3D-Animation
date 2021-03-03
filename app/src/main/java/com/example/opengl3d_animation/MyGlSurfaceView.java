package com.example.opengl3d_animation;

import android.content.Context;
import android.opengl.GLSurfaceView;

// used this class to set the renderer for our graphic object
class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;
    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3);
        renderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}
