package com.example.opengl3d_animation;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MyGLSurfaceView gLsurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLsurfaceView=new MyGLSurfaceView(this);
        setContentView(gLsurfaceView);
    }
}