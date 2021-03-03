package com.example.opengl3d_animation;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TextureCube {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private float[][] colors = {
            {1.0f, 0.5f, 0.0f, 1.0f},  // 0. orange
            {1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
            {0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
            {0.0f, 0.0f, 1.0f, 1.0f},  // 3. blue
            {1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
            {1.0f, 1.0f, 0.0f, 1.0f}
    };
    static final int COORDS_PER_VERTEX =3;
    static float vertices[]=
            {
                    -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
                    0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
                    -0.2f,  0.2f,  0.2f,  // 2. left-top-front
                    0.2f,  0.2f,  0.2f,  // 3. right-top-front
// BACK
                    0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
                    -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
                    0.2f,  0.2f, -0.2f,  // 7. right-top-back
                    -0.2f,  0.2f, -0.2f,  // 5. left-top-back
// LEFT
                    -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
                    -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
                    -0.2f,  0.2f, -0.2f,  // 5. left-top-back
                    -0.2f,  0.2f,  0.2f,  // 2. left-top-front
// RIGHT
                    0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
                    0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
                    0.2f,  0.2f,  0.2f,  // 3. right-top-front
                    0.2f,  0.2f, -0.2f,  // 7. right-top-back
// TOP
                    -0.2f,  0.2f,  0.2f,  // 2. left-top-front
                    0.2f,  0.2f,  0.2f,  // 3. right-top-front
                    -0.2f,  0.2f, -0.2f,  // 5. left-top-back
                    0.2f,  0.2f, -0.2f,  // 7. right-top-back
// BOTTOM
                    -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
                    0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
                    -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
                    0.2f, -0.2f,  0.2f   // 1. right-bottom-front
            };
    short[] indeces={
            0,1,2,2,1,3,5,4,7,7,4,6,8,9,10,10,9,11,12,13,14,14,13,15,16,17,18,18,17,19,22,23,20,20,23,21
    };

    // Vertex Shader is used to pass graphic code to the rendering pipeline which defines the attributes of the vertex created by us on the screen.
    private final String vertexShaderCode =
        // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    private int vPMatrixHandle;

    //The fragment Shader defines the attributes of the area formed by the vertices passed in the vertex shader.
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int program;
    private int numFaces = 6;
    private int positionHandle;
    private int colorHandle;
    private final int vertexCount = vertices.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    // initialize vertex byte buffer for shape coordinates
    public TextureCube(){
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        // initialize byte buffer for the draw list
        indexBuffer = ByteBuffer.allocateDirect(indeces.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indeces).position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram();
        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader);
        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader);
        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix) { // pass in the calculated transformation matrix
        // Adding program to OpenGL ES environment
        GLES20.glUseProgram(program);
        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");

        for (int face = 0; face < numFaces; face++) {
            // Set the color for each of the faces
            GLES20.glUniform4fv(colorHandle, 1, colors[face], 0);
            indexBuffer.position(face * 6);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        }
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
