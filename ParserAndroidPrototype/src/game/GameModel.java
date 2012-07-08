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
import events.CreateCell;
import events.PlayerTraj;
import events.RandomNumGen;
import events.Vector2D;
import game.CellType.Cell;
import game.CellType.CellPop;
import game.CellType.Platelet;
import game.CellType.RedCell;
import game.CellType.FatCell;
import game.CellType.Virus;
import game.CellType.WhiteCell;
import gameEngine.GameObject;
import gameEngine.SoundManager;

public class GameModel {
	
	public static final int UNIT_LIMIT = 50;
	
	static final int ORGAN_RADIUS_MIN = 15;
	static final int ORGAN_RADIUS_MAX = 24;
	static final int FAT_RADIUS_DEF = 18;
	static final double DATA_STEP_MIN = 0.04;
	static final double DATA_STEP_MAX = 0.2;
	
	private float beatSinus[];
	private double dataStep, curData;
	public ArrayList<GameObject> cells = null;
	public ArrayList<Organ> organs = null;
	public ArrayList<OrganGhost> ghosts = null;
	public LinkedList<CreateCell> buildList = new LinkedList<CreateCell>();
	public final int MAX_BUILD_LIST = 12;
	public Heart heartRef = null;
	public int cellCount;

	private int organCount, ghostCount, virusCount, buildCount;
	
	private float CalibAxes[] = new float[3];
	
	public PlayerTraj	playerInput = null;
	public  GameScore	scoreCal = null;
	
	private int cellSounds[] = new int[6];
	public RandomNumGen	rand = null;
	
	private int gameWidth;
	private int gameHeight;
	
	Resources viewRes;
	AssetManager assetManager;
	SoundManager soundManager = null;
	
	public GameModel(Resources _viewRes, AssetManager assestManagerRef)
	{
		assetManager = assestManagerRef;
		playerInput = null;
		viewRes = _viewRes;
		soundManager = new SoundManager(assetManager);
		LoadSounds();
		InitGame();
	}
	public void InitGame()
	{
		virusCount = 0;
		buildCount = 0;
		setSinus(0);
		dataStep = 0.05;//for experimenting with pace
		curData = 0;
		ghostCount = 0;
		cells = new ArrayList<GameObject>();
		organs = new ArrayList<Organ>();
		ghosts = new ArrayList<OrganGhost>();
		//fatties = new ArrayList<FatCell>();
		scoreCal = new GameScore(this);
		
		for(int i=0; i<MAX_BUILD_LIST; ++i)
			buildList.add(new CreateCell(this));
		
		rand = new RandomNumGen();
		rand.Randomize();
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
	public float incBeat(long dt)
	{
		curData += (dataStep * dt);
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
	
	public void setSinus(int rhythm)
	{
		//maybe factor this data structure into it's own class
		switch(rhythm)
		{
		case 0:
			beatSinus = this.getSinusWave();
			break;
		case 1:
			beatSinus = this.getFailureWave();
			break;
			
			default:
				
				break;
		}
	}
	
	public Cell addCell(int startX, int startY, Vector2D start2d, int cellType)
	{
		start2d = start2d.getUnit();
		start2d = PlayerTraj.createSpread( start2d );
		switch(cellType)
		{
			case Entity.REDCELL: cells.add(cellCount, new RedCell(startX, startY, start2d, viewRes, this));
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
				--cellCount;
				return null;
		}
		++cellCount;
		return ( (Cell) cells.get(cellCount-1) );
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
	public void PlayHeartBeat( float vol )
	{
		soundManager.PlaySound(cellSounds[5], vol, vol, -1, 1.0f);
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
	
	public Cell addFat(int x, int y, Vector2D heartPosition,int rad,int idur)
	{
		cells.add(cellCount, new FatCell(x, y, heartPosition, rad, viewRes, this));
		
		return (Cell) cells.get(cellCount++);
	}
	public Cell addPop(int x, int y,int rad,int idur)
	{
		cells.add(cellCount, new CellPop(x, y,0,0, rad, viewRes, this));
		++cellCount;
		return (Cell) cells.get(cellCount-1);
	}
	public boolean removeFat(Iterator< Cell > itr)
	{
		itr.remove();
		--cellCount;
		if(cells.size() != cellCount)
		{
			System.out.println("fatties does not contain the correct number of elements");
			return false;
		}
		else
			return true;
	}
	public boolean removeCell(Iterator<GameObject> cell_itr)
	{
		cell_itr.remove();
		--cellCount;
		/*if(cells.size() != cellCount)
			System.out.println("cells does not contain the correct number of elements");*/
		
			return true;
	}
	
	public Organ addOrgan(int x, int y, int rad,int idur)
	{
		organs.add(getOrganCount(), new Organ(x, y,  rad, idur, viewRes, this));
		
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
	private float[] getSinusWave()
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
	
	private float[] getFailureWave()
	{
		float dataSet[] = {-0.640f,	0.680f,	0.630f,	0.570f,	0.535f,	0.510f,	0.515f,	0.520f,	0.525f,	0.495f,
				0.430f,	0.275f,	0.195f,	0.200f,	0.260f,	0.355f,	0.370f,	0.315f,	0.155f,	-0.035f, -0.170f,
				-0.255f, -0.265f, -0.290f, 	-0.300f, -0.335f,-0.365f, -0.385f, -0.355f,	-0.355f, -0.360f, -0.340f,
				-0.360f, -0.370f, -0.350f, -0.365f, -0.345f, -0.365f, -0.325f, -0.335f, -0.345f, -0.350f, -0.385f,
				-0.355f, -0.365f, -0.350f, -0.350f, -0.340f, -0.360f, -0.360f, -0.385f, -0.385f, -0.390f, -0.400f,
				-0.405f, -0.375f, -0.400f, -0.400f, -0.410f, -0.420f, -0.420f, -0.430f, -0.435f, -0.425f, -0.440f,
				-0.455f, -0.440f, -0.440f, -0.465f, -0.455f, -0.445f, -0.445f, -0.490f, -0.450f, -0.450f, -0.460f,
				-0.445f, -0.475f, -0.465f, -0.445f, -0.445f, -0.440f, -0.435f, -0.420f, -0.375f, -0.405f, -0.385f,
				-0.390f, -0.350f, -0.345f, -0.310f, -0.300f, -0.305f, -0.275f, -0.280f, -0.275f, -0.270f, -0.260f,
				-0.255f, -0.260f, -0.210f, -0.190f, -0.210f, -0.235f, -0.235f, -0.245f, -0.255f, -0.235f, -0.235f,
				-0.245f, -0.255f, -0.225f, -0.240f, -0.230f, -0.235f, -0.260f, -0.285f, -0.300f, -0.265f, -0.295f,
				-0.290f, -0.300f, -0.285f, -0.290f, -0.305f, -0.280f, -0.285f, -0.300f, -0.305f, -0.305f, -0.310f,
				-0.315f, -0.285f, -0.290f, -0.320f, -0.300f, -0.310f, -0.285f, -0.280f, -0.265f, -0.305f, -0.300f,
				-0.320f, -0.295f, -0.290f, -0.300f, -0.295f, -0.300f, -0.310f, -0.305f, -0.305f, -0.260f, -0.255f,
				-0.215f, -0.200f, -0.200f, -0.195f, -0.170f, -0.160f, -0.160f, -0.140f, -0.130f, -0.140f, -0.135f,
				-0.145f, -0.145f, -0.165f, -0.165f, -0.185f, -0.235f, -0.255f, -0.290f, -0.260f, -0.255f, -0.245f,
				-0.240f, -0.285f, -0.285f, -0.310f, -0.345f, -0.340f, -0.350f, -0.345f, -0.360f, -0.375f, -0.360f,
				-0.395f, -0.385f, -0.385f, -0.380f, -0.365f, -0.355f, -0.350f, -0.365f, -0.365f, -0.365f, -0.350f,
				-0.350f, -0.355f, -0.375f, -0.380f, -0.355f, -0.335f, -0.315f, -0.280f, -0.215f, -0.145f, 0.010f,
				0.195f,	0.405f,	0.575f,	0.685f, 0.725f,	0.765f,	0.750f,	0.685f,	0.615f,	0.560f,	0.515f,	0.510f,	0.500f,
				0.515f,	0.495f,	0.430f,	0.330f,	0.230f,	0.145f,	0.120f,	0.160f,	0.255f,	0.285f,	0.240f,	0.070f,	-0.105f,
				-0.240f,-0.285f,-0.295f,-0.295f,-0.295f, -0.370f,-0.395f, -0.415f, -0.420f,	-0.415f, -0.415f, -0.390f,
				-0.395f, -0.370f, -0.375f, -0.370f, -0.375f,-0.380f, -0.395f, -0.420f, -0.405f, -0.390f, -0.390f, -0.380f,
				-0.415f, -0.410f, -0.410f, -0.420f, -0.420f, -0.420f, -0.395f, -0.410f, -0.395f, -0.395f, -0.420f, -0.420f,
				-0.440f, -0.485f, -0.490f, -0.480f, -0.455f, -0.455f, -0.470f, -0.480f, -0.500f, -0.480f, -0.495f, -0.495f,
				-0.485f, -0.480f, -0.485f, -0.495f, -0.490f, -0.490f, -0.480f, -0.455f, -0.450f, -0.460f, -0.425f, -0.410f,
				-0.420f, -0.420f, -0.425f, -0.410f, -0.390f, -0.415f, -0.415f, -0.390f, -0.360f, -0.360f, -0.335f, -0.315f,
				-0.315f, -0.295f, -0.310f, -0.280f, -0.275f, -0.265f, -0.260f, -0.255f, -0.230f, -0.245f, -0.265f, -0.215f,
				-0.230f, -0.220f, -0.235f, -0.235f, -0.255f, -0.255f, -0.285f, -0.270f, -0.280f, -0.265f, -0.235f, -0.240f,
				-0.260f, -0.265f, -0.280f, -0.280f, -0.295f, -0.300f, -0.315f, -0.280f, -0.290f, -0.295f, -0.295f, -0.310f,
				-0.300f, -0.315f, -0.305f, -0.300f, -0.295f, -0.290f, -0.315f, -0.320f, -0.305f, -0.305f, -0.285f, -0.300f,
				-0.275f, -0.245f, -0.275f, -0.265f, -0.270f, -0.265f, -0.280f, -0.285f, -0.270f, -0.255f, -0.225f, -0.225f,
				-0.205f, -0.200f, -0.210f, -0.190f, -0.200f, -0.180f, -0.150f, -0.135f, -0.100f, -0.090f, -0.085f, -0.060f,
				-0.075f, -0.095f, -0.100f, -0.110f, -0.110f, -0.130f, -0.160f, -0.160f, -0.195f, -0.190f, -0.190f, -0.180f,
				-0.180f, -0.185f, -0.195f, -0.215f, -0.240f, -0.275f, -0.310f, -0.320f, -0.305f, -0.295f, -0.300f, -0.290f,
				-0.265f, -0.280f, -0.290f, -0.305f, -0.320f, -0.305f, -0.295f, -0.295f, -0.300f, -0.285f, -0.285f, -0.280f,
				-0.280f, -0.275f, -0.275f, -0.250f, -0.205f, -0.140f, -0.105f, -0.055f, 0.040f, 0.220f, 0.390f, 0.600f,
				0.740f,	0.830f,	0.875f,	0.885f,	0.835f, 0.750f,	0.665f,	0.615f,	0.600f,	0.575f,	0.595f,	0.590f,	0.530f,	0.405f,
				0.295f,	0.170f,	0.155f,	0.225f,	0.315f,	0.345f,	0.290f,	0.150f,	-0.040f,-0.175f,-0.260f, -0.235f, -0.240f,-0.250f,
				-0.275f, -0.315f, -0.380f, -0.375f, -0.375f, -0.350f, -0.370f, -0.360f, -0.345f, -0.350f, -0.340f, -0.325f, -0.335f,
				-0.330f, -0.335f, -0.330f, -0.365f, -0.375f, -0.370f, -0.345f, -0.360f, -0.340f, -0.335f, -0.365f, -0.365f,
				-0.390f, -0.390f, -0.375f, -0.375f, -0.370f, -0.405f, -0.425f, -0.445f, -0.440f, -0.435f, -0.450f, -0.455f,
				-0.445f, -0.445f, -0.435f, -0.435f, -0.425f, -0.455f, -0.475f, -0.490f, -0.455f, -0.480f, -0.480f, -0.455f,
				-0.475f, -0.475f, -0.475f, -0.495f, -0.465f, -0.465f, -0.445f, -0.445f, -0.435f, -0.445f, -0.470f, -0.425f,
				-0.440f, -0.420f, -0.390f, -0.385f, -0.355f, -0.340f, -0.335f, -0.315f, -0.310f, -0.320f, -0.310f, -0.290f,
				-0.260f, -0.255f, -0.225f, -0.240f, -0.240f, -0.245f, -0.230f, -0.235f, -0.240f, -0.235f, -0.230f, -0.240f,
				-0.215f, -0.225f, -0.260f, -0.255f, -0.265f, -0.255f, -0.250f, -0.245f, -0.245f, -0.220f, -0.245f, -0.260f,
				-0.280f, -0.300f, -0.295f, -0.290f, -0.295f, -0.290f, -0.275f, -0.270f, -0.290f, -0.295f, -0.290f, -0.295f,
				-0.305f, -0.285f, -0.265f, -0.260f, -0.280f, -0.270f, -0.290f, -0.280f, -0.285f, -0.280f, -0.290f, -0.290f,
				-0.275f, -0.260f, -0.270f, -0.270f, -0.280f, -0.280f, -0.270f, -0.245f, -0.215f, -0.195f, -0.185f, -0.175f,
				-0.140f, -0.140f, -0.125f, -0.100f, -0.090f, -0.055f, -0.015f, -0.040f, -0.015f, -0.075f, -0.115f, -0.145f,
				-0.165f, -0.155f, -0.140f,-0.145f,  -0.165f, -0.180f,	-0.165f,  -0.215f, -0.220f,	-0.260f, -0.275f,
				-0.300f, -0.295f, -0.315f, -0.320f, -0.310f, -0.320f, -0.320f, -0.340f, -0.330f, -0.325f, -0.320f, -0.320f,
				-0.300f, -0.325f, -0.355f, -0.345f, -0.340f, -0.320f, -0.315f, -0.300f, -0.300f, -0.300f, -0.300f, -0.285f,
				-0.275f, -0.215f, -0.150f, -0.035f, 0.145f, 0.350f, 0.590f,	0.825f, 0.990f,	1.105f,	1.135f,	1.135f,	1.095f,
				1.025f,	0.915f,	0.880f,	0.860f,	0.855f,	0.835f,	0.820f,	0.770f,	0.700f,	0.565f,	0.455f,	0.385f,	0.420f,	0.525f,
				0.575f,	0.545f,	0.405f,	0.165f,	-0.010f,	-0.160f,-0.185f,-0.210f,-0.205f,	-0.285f,-0.340f,-0.405f,-0.385f,
				-0.410f,-0.420f,-0.410f,-0.380f,-0.390f,-0.375f,-0.350f,-0.365f,-0.380f,-0.365f,-0.385f,	-0.365f,-0.350f,-0.370f,
				-0.370f,-0.390f,-0.380f,-0.390f,-0.390f,-0.380f,-0.395f,-0.375f,-0.355f,-0.385f,-0.415f,-0.435f,-0.455f,-0.425f,-0.445f,
				-0.400f,-0.430f,-0.440f,-0.450f,-0.470f,-0.495f,-0.505f,-0.520f,-0.495f,-0.490f,-0.470f,-0.480f,-0.485f,-0.505f,-0.515f,
				-0.510f,-0.515f,-0.515f,-0.500f,-0.490f,-0.485f,-0.490f,-0.500f,-0.490f,-0.480f,-0.460f,	-0.475f,-0.445f,-0.435f,-0.415f,
				-0.425f,-0.405f,-0.375f,-0.350f,-0.325f,-0.300f,-0.305f,-0.285f,-0.290f,-0.260f,-0.275f,-0.255f,-0.265f,-0.255f,-0.240f,-0.220f,
				-0.210f,-0.220f,-0.225f,-0.225f,-0.215f,-0.230f,-0.240f,-0.270f,-0.285f,-0.260f,-0.260f,-0.285f,-0.295f,
				-0.285f,-0.270f,-0.275f,-0.265f,-0.240f,-0.265f,-0.285f,-0.290f,-0.330f,-0.325f,-0.330f,-0.320f,-0.315f,
				-0.305f,-0.320f,-0.315f,-0.315f,-0.320f,-0.305f,-0.310f,-0.295f,-0.300f,-0.300f,-0.325f,-0.320f,-0.305f,
				-0.330f,-0.325f,-0.320f,-0.320f,-0.300f,-0.285f,-0.285f,-0.285f,-0.280f,-0.255f,-0.225f,-0.190f,-0.185f,
				-0.180f,-0.160f,-0.150f,-0.105f,-0.055f,-0.075f,-0.110f,-0.135f,-0.105f,-0.085f,-0.075f,-0.100f,-0.125f,
				-0.150f,-0.180f,-0.165f,-0.180f,-0.190f,-0.175f,-0.185f,-0.190f,-0.190f,-0.205f,-0.240f,-0.280f,-0.325f,
				-0.355f,-0.355f,-0.370f,-0.355f,-0.375f,-0.355f,-0.360f,-0.360f,-0.370f,-0.370f,-0.375f,-0.375f,-0.335f,
				-0.365f,-0.375f,-0.370f,-0.380f,-0.380f,-0.360f,-0.365f,-0.330f,-0.320f,-0.320f,-0.310f,-0.270f,
				-0.185f,
				-0.045f };
		return dataSet;
	}
	
	public void calibrateX( float newVal )				{	CalibAxes[0]  = newVal;	};
	public float	getCalibrateX()							{	return CalibAxes[0];	};
	
	public void calibrateY( float newVal )				{	CalibAxes[1]  = newVal;	};
	public float	getCalibrateY()							{	return CalibAxes[1];	};
	
	public void calibrateZ( float newVal )				{	CalibAxes[2]  = newVal;	};
	public float	getCalibrateZ()							{	return CalibAxes[2];	};
	
	public void incrBuildCount()	{	++buildCount;	}
	public void resetBuildCount()	{	buildCount = 0;	}
	public CreateCell orderCell()
	{		
		return buildList.get(buildCount);
	}
	
	public void ShutDown()
	{
		soundManager.ReleaseSounds();
	}
	
	public boolean gameOver()
	{
		//clear sounds
		this.ShutDown();
		//delete game objext
		clearCells();
		scoreCal = null;
		setHeartRef(null);
		return true;
	}
	
	public void clearCells()
	{
		cells.removeAll(cells);
		organs.removeAll(organs);
		ghosts.removeAll(ghosts);
		buildList.removeAll(buildList);
		organCount = 0;
		ghostCount = 0;
		cellCount = 0;
		buildCount = 0;
	}
	public Heart getHeartRef()						{		return heartRef;	}
	public void setHeartRef(Heart heartRef) 		{		this.heartRef = heartRef;	}
	public int getGameWidth() {
		return gameWidth;
	}
	public void setGameWidth(int gameWidth) {
		this.gameWidth = gameWidth;
	}
	public int getGameHeight() {
		return gameHeight;
	}
	public void setGameHeight(int gameHeight) {
		this.gameHeight = gameHeight;
	}

}
