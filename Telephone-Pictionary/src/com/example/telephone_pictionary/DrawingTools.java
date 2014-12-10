package com.example.telephone_pictionary;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet; 

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.graphics.Color;

public class DrawingTools extends View {
	
	
	private Path drawPath;
	private Paint drawPaint, canvasPaint;
	private int paintColor = 0x000000, lastColor;
	private Canvas drawingCanvas;
	private Bitmap drawingBitmap; 
	private float brushSize, lastBrushSize;
	
	public DrawingTools(Context context, AttributeSet attrs){
		super(context, attrs);
		setUpDrawingTools();
	}
	
	private void setUpDrawingTools(){
		drawPath = new Path();
		drawPaint = new Paint();
		
		drawPaint.setColor(paintColor);
		drawPaint.setStrokeWidth(20);
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		//Eclipe describes dither_flag as:
		//"Paint flag that enables dithering when blitting."
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		
		brushSize = getResources().getInteger(R.integer.med);
		lastBrushSize = brushSize;
	
		drawPaint.setStrokeWidth(brushSize);
	}
	
	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
		
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		drawingBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
		drawingCanvas = new Canvas(drawingBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		
		canvas.drawBitmap(drawingBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float xCoord = event.getX();
		float yCoord = event.getY();
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				drawPath.moveTo(xCoord, yCoord);
				break;
			case MotionEvent.ACTION_MOVE:
				drawPath.lineTo(xCoord, yCoord);
				break;
			case MotionEvent.ACTION_UP:
				drawingCanvas.drawPath(drawPath, drawPaint);
				drawPath.reset(); 
				break;
		
			default:
				return false; 		
		}
		invalidate();
		return true; 
	}
	
	public void startNew(){
	//	drawingCanvas.drawColor(0, PorterDuff.Mode.Clear);
		invalidate(); 
	

	}
	
	public void setColor(String color){
		invalidate(); 
		paintColor = Color.parseColor(color);
		drawPaint.setColor(paintColor);  
	}
	
	public void setBrushSize(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
		brushSize = pixelAmount; 
		drawPaint.setStrokeWidth(brushSize); 
	}
	
	public void setLastBrushSize(float lastSize){
		lastBrushSize = lastSize; 
	}
	
	public float getLastBrushSize(){
		return lastBrushSize; 
	}
}