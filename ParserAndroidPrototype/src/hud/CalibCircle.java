package hud;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class CalibCircle {

	private Path m_Path = null;
	private Paint m_Paint = null;
	private int mRad;
	
	public CalibCircle( int _x, int _y, int _rad )
	{
		mRad = _rad;
		int line = (int) (mRad * 0.1);
		m_Path = new Path();
		m_Path.addCircle(_x, _y, mRad, Path.Direction.CW);
		m_Path.moveTo(_x + mRad + line, _y);
		m_Path.lineTo(_x + mRad - line, _y);
		
		m_Path.moveTo(_x - mRad + line, _y);
		m_Path.lineTo(_x - mRad - line, _y);
		
		m_Path.moveTo(_x, _y + mRad + line);
		m_Path.lineTo(_x, _y + mRad - line);
		
		m_Path.moveTo(_x, _y - mRad + line);
		m_Path.lineTo(_x, _y - mRad - line);
		
		m_Paint = new Paint();
		m_Paint.setColor(Color.GRAY);
		m_Paint.setStyle(Paint.Style.STROKE);
		m_Paint.setStrokeJoin(Paint.Join.ROUND);
		m_Paint.setStrokeCap(Paint.Cap.ROUND);
		m_Paint.setStrokeWidth(3);
	}
	
	public void changePaint()//no arguemnent passed. hack?
	{
		m_Paint = new Paint();
		m_Paint.setColor(Color.RED);
		m_Paint.setStyle(Paint.Style.STROKE);
		m_Paint.setStrokeJoin(Paint.Join.ROUND);
		m_Paint.setStrokeCap(Paint.Cap.ROUND);
		m_Paint.setStrokeWidth(3);
	}
	
	public void update(int newX, int newY)
	{
		m_Path = new Path();//urgh the expense. but circle update is not for main game loop
		m_Path.addCircle(newX, newY, mRad, Path.Direction.CW);
	}
	
	public void draw(Canvas dbimage)
	{
		dbimage.drawPath(m_Path, m_Paint);
	}
	
}
