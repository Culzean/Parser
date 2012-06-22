////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//
//Game Engine and game by Daniel Waine and Raphael Chappuis 
//
//for Mobile and Web Games Development
//
//Banner IDs B00226804 - B00218930
//
////////////////////////////////////////////////////////////////////////

package game;

import java.util.*;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import events.PlayerTraj;
import events.RandomNumGen;
import events.Vector2D;
import game.CellType.Cell;
import game.CellType.Platelet;
import game.CellType.RedCell;
import game.CellType.FatCell;
import game.CellType.Virus;
import game.CellType.WhiteCell;
import gameEngine.SoundManager;

public class GameModel {
	
	public static final int UNIT_LIMIT = 50;
	
	static final int ORGAN_RADIUS_MIN = 15;
	static final double DATA_STEP_MIN = 0.5;
	static final double DATA_STEP_MAX = 2;
	
	private float beatSinus[];
	private double dataStep, curData;
	ArrayList<Cell> cells = null;
	ArrayList<FatCell> fatties = null;
	ArrayList<Organ> organs = null;
	ArrayList<OrganGhost> ghosts = null;
	public int cellCount;

	private int organCount, ghostCount, virusCount, fatCount;
	
	private float CalibAxes[] = new float[3];
	
	private PlayerTraj	playerInput = null;
	
	private int cellSounds[] = new int[6];
	RandomNumGen	rand = null;
	
	Resources viewRes;
	AssetManager assetManager;
	SoundManager soundManager = null;
	
	public GameModel(Resources _viewRes, AssetManager assestManagerRef)
	{
		assetManager = assestManagerRef;
		rand = new RandomNumGen();
		rand.Randomize();
		virusCount = 0;
		fatCount = 0;
		beatSinus = getWave();
		dataStep = 0.5;//for experimenting with pace
		curData = 0;
		cells = new ArrayList<Cell>();
		organs = new ArrayList<Organ>();
		ghosts = new ArrayList<OrganGhost>();
		fatties = new ArrayList<FatCell>();
		ghostCount = 0;
		playerInput = null;
		viewRes = _viewRes;
		soundManager = new SoundManager(assetManager);
		LoadSounds();
	}
	public void fasterHeart()
	{
		dataStep *= 1.1;
		if(dataStep > DATA_STEP_MAX)
			dataStep = DATA_STEP_MAX;
	}
	public void slowerHeart()
	{
		dataStep *= 0.9;
		if(dataStep < DATA_STEP_MIN)
			dataStep = DATA_STEP_MIN;
	}
	public float incBeat()
	{
		curData += dataStep;
		if(curData >= beatSinus.length)
			curData = 0;
		return this.getBeat();
	}
	public float getBeat()
	{
		return beatSinus[(int)curData];
	}
	public float getNextBeat()
	{
		//when might datastep change?
		if(curData+dataStep >= beatSinus.length)
			return beatSinus[0];
		else
			return beatSinus[(int) (curData+dataStep)];
	}
	
	public Cell addCell(int startX, int startY, Vector2D start2d, int cellType)
	{
		start2d = start2d.getUnit();
		start2d = PlayerTraj.createSpread( start2d );
		switch(cellType)
		{
			case Entity.REDCELL: cells.add(cellCount, new RedCell(startX, startY, start2d.getVectorScale(2), viewRes, this));
			break;
			case Entity.WHITECELL: cells.add(cellCount,
					new WhiteCell(startX, startY, start2d.getVectorScale(2), viewRes, this));
			break;
			case Entity.PLATELET: cells.add(cellCount,
					new Platelet(startX, startY, start2d.getVectorScale(2), viewRes, this));
			break;
			case Entity.VIRUS: cells.add(cellCount,
					new Virus(startX, startY, start2d.getVectorScale(2), viewRes, this));
			break;
			default:
				return null;
		}
		
		return cells.get(cellCount++);
	}
	
	public void infect()			{		++virusCount;	};
	public void disinfect()			{		--virusCount;	};
	
	
	public void PlayPlop()
	{
		int dice = rand.Random(100);
		int choice = 0;
		if(dice < 20)
			choice = cellSounds[0];
		else if(dice < 30)
			choice = cellSounds[1];
		else if(dice < 70)
			choice = cellSounds[2];
		else
			choice = cellSounds[3];
		
		soundManager.PlaySound(choice);
	}
	public void PlaySlop() { soundManager.PlaySound(cellSounds[4]);	}
	public void PlayHeartBeat()
	{
		soundManager.PlaySound(cellSounds[5], -1, 1.0f);
	}
	
	public void LoadSounds()
	{
		cellSounds[5] = soundManager.LoadSound("sounds/heartbeatloud.ogg");
		cellSounds[0] = soundManager.LoadSound("sounds/cell1.ogg");
		cellSounds[1] = soundManager.LoadSound("sounds/cell2.ogg");
		cellSounds[2] = soundManager.LoadSound("sounds/cell3.ogg");
		cellSounds[3] = soundManager.LoadSound("sounds/cell4.ogg");
		cellSounds[4] = soundManager.LoadSound("sounds/slop1.ogg");
	}
	public int LoadSound(String fname) { return soundManager.LoadSound(fname); }
	public void PlaySound( int SoundID, int loop, float PlyRate ) { soundManager.PlaySound(SoundID, loop, PlyRate); }
	public void PlaySound( int SoundID, int loop ) { soundManager.PlaySound(SoundID, loop); }
	
	public FatCell addFat(int x, int y, Vector2D heartPosition,int rad,int idur)
	{
		fatties.add(fatCount, new FatCell(x, y, heartPosition, rad, viewRes, this));
		
		return (FatCell) fatties.get(fatCount++);
	}
	public boolean removeFat(Iterator< FatCell > itr)
	{
		itr.remove();
		--fatCount;
		if(fatties.size() != fatCount)
		{
			System.out.println("fatties does not contain the correct number of elements");
			return false;
		}
		else
			return true;
	}
	public int getFatCount() {		return fatCount;	}
	
	public boolean removeCell(Iterator<Cell> itr)
	{
		itr.remove();
		--cellCount;
		/*if(cells.size() != cellCount)
			System.out.println("cells does not contain the correct number of elements");*/
		
			return true;
	}
	
	public Organ addOrgan(int x, int y, int rad,int idur)
	{
		organs.add(getOrganCount(), new Organ(x, y,  rad, idur, viewRes));
		
		return (Organ) organs.get(organCount++);
	}
	public boolean removeOrgan(Iterator< Organ > itr)
	{
		itr.remove();
		--organCount;
		if(organs.size() != getOrganCount())
			System.out.println("organs does not contain the correct number of elements");
		
		return true;
	}
	
	public int getOrganCount() {		return organCount;	}
	public int getVirusCount() {		return virusCount;	}
	
	public OrganGhost addGhost(int x, int y)
	{
		//sooner or later will have to add what type of organ ghost
		ghosts.add(ghostCount, new OrganGhost(x, y));
		return (OrganGhost) ghosts.get(ghostCount++);
	}
	public OrganGhost getGhost()//not for drawing. But make the signature draw related
	{
		OrganGhost pop = null;
		if(ghostCount > 0)
		{
			pop = ghosts.get(0);
			ghosts.remove(0);
			ghostCount--;
		}

		return pop;
	}
	public OrganGhost getGhost(Canvas g)//not for drawing. But make the signature draw related
	{
		OrganGhost pop = null;
		if(ghostCount > 0)
			pop = ghosts.get(ghostCount -1);

		return pop;
	}
	
	public int getGhostCount()		{	return ghostCount;	};
	
	public PlayerTraj getPlayerIn()
	{
		return playerInput;
	}
	public void setPlayerIn(PlayerTraj newVals)
	{
		playerInput = newVals;		
	}
	
	//manually entered wareform data here
	private float[] getWave()
	{
		float dataSet[] = {-0.165f,	-0.155f, -0.195f, -0.205f, -0.185f, -0.155f, -0.135f, -0.095f,
				-0.075f, -0.065f, -0.065f, -0.125f, -0.125f, -0.125f, -0.115f, -0.125f, -0.165f,
				-0.115f, -0.145f, -0.115f, -0.135f, -0.135f, -0.125f, -0.175f, -0.145f,	-0.125f,
				-0.145f, -0.145f, -0.135f, -0.165f, -0.155f, -0.095f, -0.105f, -0.075f,	-0.085f,
				0.025f,	0.025f,	-0.025f, -0.085f, -0.115f, -0.145f, -0.125f, -0.155f, -0.205f,
				-0.215f, -0.215f, -0.165f, -0.165f, -0.155f, -0.615f, -0.855f, -0.185f, 1.295f,
				2.575f,	2.675f,	2.445f,	0.735f,	-0.415f, -0.295f, -0.285f, -0.345f, -0.365f, -0.345f,
				-0.315f, -0.305f, -0.265f, -0.235f, -0.265f, -0.245f, -0.215f, -0.195f, -0.175f, -0.185f,
				-0.155f, -0.125f, -0.145f, -0.125f, -0.145f, -0.155f, -0.185f, -0.175f,	-0.175f, -0.165f,
				-0.165f, -0.115f, -0.065f, -0.095f,	-0.115f, -0.075f, -0.115f, -0.155f, -0.105f, -0.125f,
				-0.135f, -0.125f, -0.115f, -0.105f,	-0.135f, -0.135f, -0.135f, -0.145f,	-0.125f, -0.115f,
				-0.135f, -0.135f, -0.165f, -0.155f,	-0.115f, -0.085f, -0.055f, -0.055f,	-0.045f, 0.065f,
				0.005f,	-0.015f, -0.055f, -0.115f, -0.135f, -0.135f, -0.175f, -0.205f, -0.225f,	-0.175f,
				-0.175f, -0.155f, -0.255f, -0.875f,	-0.645f, 0.645f, 2.315f, 2.925f, 2.725f, 1.225f,
				-0.185f, -0.275f, -0.275f, -0.285f, -0.285f, -0.335f, -0.305f, -0.285f, -0.275f, -0.275f,
				-0.245f, -0.245f, -0.225f, -0.195f,	-0.205f, -0.165f, -0.125f, -0.135f,	-0.105f, -0.115f,
				-0.115f, -0.095f, -0.155f, -0.165f,	-0.185f, -0.185f, -0.175f, -0.195f };
		return dataSet;
	}
	
	public void calibrateX( float newVal )				{	CalibAxes[0]  = newVal;	};
	public float	getCalibrateX()							{	return CalibAxes[0];	};
	
	public void calibrateY( float newVal )				{	CalibAxes[1]  = newVal;	};
	public float	getCalibrateY()							{	return CalibAxes[1];	};
	
	public void calibrateZ( float newVal )				{	CalibAxes[2]  = newVal;	};
	public float	getCalibrateZ()							{	return CalibAxes[2];	};
	
	public void ShutDown()
	{
		soundManager.ReleaseSounds();
	}
}
