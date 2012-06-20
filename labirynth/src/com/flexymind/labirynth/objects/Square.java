/**
 * 
 */
package com.flexymind.labirynth.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.flexymind.labirynth.storage.Settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * @author impaler
 * 
 */
public class Square {
	
	private final PointF zeroPoint = new PointF(-Settings.getCurrentXRes() / 2, Settings.getCurrentYRes() / 2);
	
	/** прозрачность квадрата */
	private float opacity = 1f;
	
	private static final float standartVert[] = { 
			-1.0f,	-1.0f,	0.0f, // V1 - bottom left
			-1.0f,	1.0f,	0.0f, // V2 - top left
			1.0f,	-1.0f,	0.0f, // V3 - bottom right
			1.0f,	1.0f,	0.0f  // V4 - top right
	};
	
	private float vertices[] = {
			-1.0f,	-1.0f,	0.0f, // V1 - bottom left
			-1.0f,	1.0f,	0.0f, // V2 - top left
			1.0f,	-1.0f,	0.0f, // V3 - bottom right
			1.0f,	1.0f,	0.0f  // V4 - top right
	};

	private FloatBuffer vertexBuffer; // buffer holding the vertices
	
	private FloatBuffer textureBuffer; // buffer holding the texture coordinates
	
	private float texture[] = {
			// Mapping coordinates for the vertices
			0.0f, 1.0f, // top left (V2)
			0.0f, 0.0f, // bottom left (V1)
			1.0f, 1.0f, // top right (V4)
			1.0f, 0.0f // bottom right (V3)
	};

	/** The texture pointer */
	private static int[] textures = new int[99];
	
	private static int count = 0;
	
	private int thiscount = 0;
	
	private GL10 openGL = null;
	
	public Square() {
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());

		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();

		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);

		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);

		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}

	public int getWidth(){
		return (int)(vertices[6] - vertices[0]);
	}
	
	public int getHeight(){
		return (int)(vertices[4] - vertices[1]);
	}
	
	public void moveTo(PointF vec){
		vertices[0] += vec.x;
		vertices[1] += vec.y;
		vertices[3] += vec.x;
		vertices[4] += vec.y;
		vertices[6] += vec.x;
		vertices[7] += vec.y;
		vertices[9] += vec.x;
		vertices[10] += vec.y;

		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);

		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
	}
	
	/**
	 * Задаёт положение левого верхнего угла
	 * @param vec - положение
	 */
	public void setLeftTop(PointF vec){
		PointF shift = new PointF();
		
		shift.x = vec.x - vertices[3] + zeroPoint.x;
		shift.y = -vec.y - vertices[4] + zeroPoint.y;
		
		// moving figure
		moveTo(shift);
	}
	
	/**
	 * Set size of square to new, and change position to (0, 0)
	 * @param height - new Height
	 * @param width - new Width
	 */
	public void setSize(float height, float width){
		// set squre size to bitmap size
		vertices[0] = standartVert[0] * width / 2f;
		vertices[1] = standartVert[1] * height / 2f;
		vertices[2] = standartVert[2];
		vertices[3] = standartVert[3] * width / 2f;
		vertices[4] = standartVert[4] * height / 2f;
		vertices[5] = standartVert[5];
		vertices[6] = standartVert[6] * width / 2f;
		vertices[7] = standartVert[7] * height / 2f;
		vertices[8] = standartVert[8];
		vertices[9] = standartVert[9] * width / 2f;
		vertices[10] = standartVert[10] * height / 2f;
		vertices[11] = standartVert[11];
		
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);

		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
	}
	
	/**
	 * set opacity of square
	 * @param newOpc - opacity value in 0..1
	 */
	public void setOpacity(float newOpc){
		if (newOpc > 1f || newOpc < 0f){
			return;
		}
		opacity = newOpc;
	}
	
	/**
	 * Load the texture for the square
	 * 
	 * @param gl - GL object
	 * @param context - context of application
	 * @param drawableId - id of resource for draw
	 */
	public void loadGLTexture(GL10 gl, Context context, int drawableId) {
		openGL = gl;
		
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				drawableId);
		
		thiscount = count++;
		
		gl.glGenTextures(1, textures, thiscount);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[thiscount]);
		
		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);
		
		// Use Android GLUtils to specify a two-dimensional texture image from
		// our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		int err = gl.glGetError();
        if (err != 0) {
            Log.e("OpenGL error", GLU.gluErrorString(err));
        }
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		
		// set squre size to bitmap size
		vertices[0] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[1] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;
		vertices[3] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[4] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;
		vertices[6] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[7] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;
		vertices[9] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[10] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;
		
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);

		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		// Clean up
		bitmap.recycle();
	}

	/**
	 * Load the texture for the square
	 * 
	 * @param gl - GL object
	 * @param drawableId - drawable image for draw
	 */
	public void loadGLTexture(GL10 gl, Drawable drawable) {
		openGL = gl;
		
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		
		thiscount = count++;
		
		gl.glGenTextures(1, textures, thiscount);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[thiscount]);
		
		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);
		
		// Use Android GLUtils to specify a two-dimensional texture image from
		// our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		int err = gl.glGetError();
        if (err != 0) {
            Log.e("OpenGL error", GLU.gluErrorString(err));
        }
		
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

		// set squre size to bitmap size
		vertices[0] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[1] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;
		vertices[3] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[4] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;
		vertices[6] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[7] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;
		vertices[9] *= bitmap.getWidth() * Settings.getScaleFactorX() / 2f;
		vertices[10] *= bitmap.getHeight() * Settings.getScaleFactorY() / 2f;

		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);

		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		// Clean up
		bitmap.recycle();
	}
	
	/** The draw method for the square with the GL context */
	public void draw(GL10 gl) {
		
		// bind the previously generated texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[thiscount]);
		
		// Set the face rotation
		gl.glFrontFace(GL10.GL_CW);

		gl.glColor4f(1.0f, 1.0f, 1.0f, opacity);
		
		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		
		int err = gl.glGetError();
        if (err != 0) {
            Log.e("OpenGL error", GLU.gluErrorString(err));
        }
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
	
	protected void onDestroy()
    {
    	if (openGL != null){
    		openGL.glDeleteTextures(1, textures, thiscount);
    		textures[thiscount] = 0;
    		openGL.glFlush();
    	}
    }
	
	protected static void resetCount(){
		count = 0;
	}
}
