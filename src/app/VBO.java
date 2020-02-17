package app;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;
import java.nio.*;

public class VBO {

	private long window;

	public void go(){

		if( !GLFW.glfwInit() ){
			throw new IllegalStateException("Can't initialize GLFW.");
		}

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL32.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL32.GL_TRUE);

		window = GLFW.glfwCreateWindow(800, 600, "VBO", MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL){
			throw new RuntimeException("No window available.");
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwShowWindow(window);
		GL.createCapabilities();
		GL32.glClearColor(0.0f, 0.396f, 0.643f, 0.0f);

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE ){
				GLFW.glfwSetWindowShouldClose(window, true);
				}
				});

		float[] vertices = {
			-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f,
			-0.5f, 0.5f, 0f
		};

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
		vertexBuffer.put(vertices);
		vertexBuffer.flip();

		int count = vertices.length / 3;

		int vao = GL32.glGenVertexArrays();		
		GL32.glBindVertexArray(vao);

		// Need a new buffer object
		int vbo = GL32.glGenBuffers();
		// Bind it.  Need to tell it type of data and which vbo.
		// Vertex array data or index array data?
		// Almost always Array buffer.
		GL32.glBindBuffer(GL32.GL_ARRAY_BUFFER, vbo);
		// Copy data to it
		// Give a hint about the data.
		// {static, dynamic, stream}_{draw, read, copy}
		// First two are what you expect. Stream is change
		// each frame.  Draw is sent to GPU.  Read means
		// client app will read it.
		// copy means both.  Draw is for vbo.  Others are for
		// pixel bo or frame bo.
		GL32.glBufferData(GL32.GL_ARRAY_BUFFER, vertexBuffer, GL32.GL_STATIC_DRAW);		
		GL32.glVertexAttribPointer(0, 3, GL32.GL_FLOAT, false, 0, 0);
		GL32.glBindBuffer(GL32.GL_ARRAY_BUFFER, 0);

		GL32.glBindVertexArray(0);		
		GL32.glBindVertexArray(vao);
		GL32.glEnableVertexAttribArray(0);
		while( ! GLFW.glfwWindowShouldClose(window) ){
			GLFW.glfwPollEvents();
			GL32.glClear(GL32.GL_COLOR_BUFFER_BIT);
			// Draw the bound buffers.
			GL32.glDrawArrays(GL32.GL_TRIANGLES, 0, count);
			GLFW.glfwSwapBuffers(window);
		}

		GL32.glDisableVertexAttribArray(0);
		GL32.glBindVertexArray(0);
		GL32.glBindBuffer(GL32.GL_ARRAY_BUFFER, 0);	
		GL32.glDeleteBuffers(vbo);
		GL32.glDeleteVertexArrays(vao);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public static void main(String[] args){

		VBO vbo = new VBO();
		vbo.go();
	}


}
