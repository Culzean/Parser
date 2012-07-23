package events;

import game.GameModel;
import game.Entity;

public class CreateCell {

	public int cellType;
	public int rad;
	public int posX;
	public int posY;
	private boolean active = false;
	private GameModel model = null;
	
	public CreateCell( GameModel ref )
	{
		//a command class to make a note of all objects that require creating
		//and then execute creation on next loop
		model = ref;
	}
	
	public void order( int cellOrder, int irad, int iPosX, int iPosY )
	{
		this.active = true;
		model.incrBuildCount();
		this.cellType = cellOrder;
		rad = irad;
		posX = iPosX;
		posY = iPosY;
	}
	
	public boolean addCell( Vector2D inputVec )
	{
		//find cell type and call correct method in model to add cell	
		
		if( cellType == Entity.REDCELL ||
				cellType == Entity.WHITECELL ||
				cellType == Entity.PLATELET ||
				cellType == Entity.VIRUS)
		{
			model.addCell(posX, posY, inputVec, cellType);
		}
		else if( cellType == Entity.FATCELL)
		{
			int startX = (int) (model.rand.Random(rad * 2) - (rad) + posX);
			int startY = (int) (model.rand.Random(rad * 2) - (rad) + posY);
			
			Vector2D heartVec = new Vector2D();
			heartVec.x = (model.heartRef.getX() - posX);
			heartVec.y = (model.heartRef.getY() - posY);
			
			model.addFat(startX, startY, heartVec, rad, 20);
		}
		else if(cellType == Entity.CELLPOP)
		{
			model.addPop(posX, posY, rad, 0);
		}
		else if(cellType == Entity.OXYFLARE)
		{
			model.addFlare(posX, posY, rad, 0);
		}
		setActive(false);
		return true;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
