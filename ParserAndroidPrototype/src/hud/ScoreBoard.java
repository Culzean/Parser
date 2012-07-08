package hud;

import java.util.Observable;
import java.util.Observer;

import events.ScoreObserver;
import game.GameScore;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ScoreBoard extends DisplayObject implements ScoreObserver{
	
	private GameScore observable;
	private float score;
	private float scoreVel;
	private float prevVel;
	private float scoreGrad;
	private String scorePrint;
	private Paint texCol;
	private float SCORE_INDENT;
	
	public ScoreBoard( int posX, int posY, int _width, int _height, GameScore observe )
	{
		super( posX,  posY,  _width,  _height);
		this.observable = observe;
		observable.addObserver(this);
		SCORE_INDENT = (float) (_width * 0.08);
		init();
	}
	
	private void init()
	{
		texCol = new Paint();
		texCol.setColor(Color.BLACK);
		texCol.setStyle(Paint.Style.STROKE);
		texCol.setStrokeJoin(Paint.Join.BEVEL);
		texCol.setStrokeCap(Paint.Cap.BUTT);
		texCol.setStrokeWidth(3);
		texCol.setTextSize(22);
	}	
	
	
	public void draw( Canvas dbimage )
	{
		dbimage.drawRect(posX, posY, posX+getWidth(), posY+getHeight(), color);
		dbimage.drawText("Score: ", posX+SCORE_INDENT, posY+SCORE_INDENT * 2, texCol);
		dbimage.drawText(""+(int)score, posX+SCORE_INDENT, posY+SCORE_INDENT * 5, texCol);
		dbimage.drawText(""+scoreVel, posX+SCORE_INDENT, posY+SCORE_INDENT * 10, texCol);
		dbimage.drawText(""+scorePrint, posX+SCORE_INDENT, posY+SCORE_INDENT * 8, texCol);
	}
	

	@Override
	public void UpdateScore(float newScore, float newVel) {
		// TODO Auto-generated method stub
		this.score = newScore;
		this.scoreVel = newVel;
		if(scoreVel > 0.8)
			texCol.setColor(Color.WHITE);
		else if(scoreVel < 0.6)
			texCol.setColor(Color.MAGENTA);
		else
			texCol.setColor(Color.BLACK);
		
		scoreGrad = (float) ((scoreGrad + (scoreVel - prevVel) ) * 0.5);
		if(scoreGrad > 0)
			scorePrint = "+";
		else if(scoreGrad < 0)
			scorePrint = "-";
		else
			scorePrint = ":S";
		
		prevVel = scoreVel;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	
}
