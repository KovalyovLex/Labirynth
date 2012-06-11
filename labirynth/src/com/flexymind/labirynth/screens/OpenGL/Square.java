/**
 * 
 */
package com.flexymind.labirynth.screens.OpenGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.flexymind.labirynth.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;

/**
 * @author impaler
 * 
 */
public class Square {

	private FloatBuffer vertexBuffer; // buffer holding the vertices
	private float vertices[] = { 
			-1.0f,	-1.0f,	0.0f, // V1 - bottom left
			-1.0f,	1.0f,	0.0f, // V2 - top left
			1.0f,	-1.0f,	0.0f, // V3 - bottom right
			1.0f,	1.0f,	0.0f // V4 - top right
	};

	private FloatBuffer textureBuffer; // buffer holding the texture coordinates
	private float texture[] = {
			// Mapping coordinates for the vertices
			0.0f, 1.0f, // top left (V2)
			0.0f, 0.0f, // bottom left (V1)
			1.0f, 1.0f, // top right (V4)
			1.0f, 0.0f // bottom right (V3)
	};

	/** The texture pointer */
	private int[] textures = new int[1];

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

	public void moveTo(PointF vec){
		vertices[0] += vec.x;
		vertices[1] += vec.y;
		vertices[3] += vec.x;
		vertices[4] += vec.y;
		vertices[6] += vec.x;
		vertices[7] += vec.y;
		vertices[9] += vec.x;
		vertices[10] += vec.y;
		
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());

		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();

		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);

		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
	}
	
	/**
	 * Load the texture for the square
	 * 
	 * @param gl
	 * @param context
	 */
	public void loadGLTexture(GL10 gl, Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.finish);
		
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		ByteBuffer imageBuffer = ByteBuffer.allocateDirect(bitmap.getHeight() * bitmap.getWidth() * 4);
		imageBuffer.order(ByteOrder.nativeOrder());
		byte buffer[] = new byte[4];
		for(int i = 0; i < bitmap.getHeight(); i++)
		{
			for(int j = 0; j < bitmap.getWidth(); j++)
			{
				int color = bitmap.getPixel(j, i);
				buffer[0] = (byte)Color.red(color);
				buffer[1] = (byte)Color.green(color);
				buffer[2] = (byte)Color.blue(color);
				buffer[3] = (byte)Color.alpha(color);
				imageBuffer.put(buffer);
			}
		}
		imageBuffer.position(0);
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, imageBuffer);

		// create nearest filtered texture
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
		//		GL10.GL_NEAREST);
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
		//		GL10.GL_LINEAR);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
		// GL10.GL_REPEAT);
		// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
		// GL10.GL_REPEAT);

		// Use Android GLUtils to specify a two-dimensional texture image from
		// our bitmap
		//GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap1, 0);

		//GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		// Clean up
		bitmap.recycle();
	}

	/** The draw method for the square with the GL context */
	public void draw(GL10 gl) {
		// bind the previously generated texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Set the face rotation
		gl.glFrontFace(GL10.GL_CW);

		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		// Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
