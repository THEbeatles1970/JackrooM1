package engine.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.GameManager;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.IllegalSwapException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;


public class Board implements BoardManager  {
    private final ArrayList<Cell> track;
    private final ArrayList<SafeZone> safeZones;
	private final GameManager gameManager;
    private int splitDistance;

    public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
        this.track = new ArrayList<>();
        this.safeZones = new ArrayList<>();
        this.gameManager = gameManager;
        
        for (int i = 0; i < 100; i++) {
            this.track.add(new Cell(CellType.NORMAL));
            
            if (i % 25 == 0) 
                this.track.get(i).setCellType(CellType.BASE);
            
            else if ((i+2) % 25 == 0) 
                this.track.get(i).setCellType(CellType.ENTRY);
        }

        for(int i = 0; i < 8; i++)
            this.assignTrapCell();

        for (int i = 0; i < 4; i++)
            this.safeZones.add(new SafeZone(colourOrder.get(i)));

        splitDistance = 3;
    }

    public ArrayList<Cell> getTrack() {
        return this.track;
    }

    public ArrayList<SafeZone> getSafeZones() {
        return this.safeZones;
    }
    
    @Override
    public int getSplitDistance() {
        return this.splitDistance;
    }

    public void setSplitDistance(int splitDistance) {
        this.splitDistance = splitDistance;
    }
   
    private void assignTrapCell() {
        int randIndex = -1;
        
        do
            randIndex = (int)(Math.random() * 100); 
        while(this.track.get(randIndex).getCellType() != CellType.NORMAL || this.track.get(randIndex).isTrap());
        
        this.track.get(randIndex).setTrap(true);
    }
    private ArrayList<Cell> getSafeZone(Colour colour){
        if (safeZones != null) {
        	for (int i = 0 ;  i < safeZones.size() ;i++ ){
        		SafeZone currentsafezone = safeZones.get(i);
                if (currentsafezone.getColour().equals(colour)) {
                    return currentsafezone.getCells()  ;
                }
            }
        }
        return null;
        
    }
    
    private int getPositionInPath(ArrayList<Cell> path, Marble marble){
    	for (int i = 0 ; i< path.size() ; i++    ){
    		Cell cell = path.get(i) ;
    		if (cell.getMarble() == marble ){return i ;  }
    	}
    	return -1 ; 
    	
    }
    private int getBasePosition(Colour colour) {
    	if (safeZones.get(0).getColour() == (colour) ){return 0 ; }
    	if (safeZones.get(1).getColour() == (colour) ){return 25 ; }
    	if (safeZones.get(2).getColour() == (colour) ){return 50 ; }
    	if (safeZones.get(3).getColour() == (colour) ){return 75 ; }
return -1 ;}
    
    private int getEntryPosition(Colour colour) {
    	if (safeZones.get(0).getColour() == (colour) ){return 98 ; }
    	if (safeZones.get(1).getColour() == (colour) ){return 23 ; }
    	if (safeZones.get(2).getColour() == (colour) ){return 48 ; }
    	if (safeZones.get(3).getColour() == (colour) ){return 73 ; }
return -1 ; 
    	
    }
   
    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
        Colour marbleColour = marble.getColour();
        ArrayList<Cell> safeZone = getSafeZone(marbleColour);
        ArrayList<Cell> fullPath = new ArrayList<>();

        int posInTrack = getPositionInPath(track, marble);
        int posInSafeZone = getPositionInPath(safeZone, marble) ;

        if (posInTrack == -1 && posInSafeZone == -1) {
            throw new IllegalMovementException("Marble not found on board.");
        }

        int entryPos = getEntryPosition(marbleColour);

        if (posInTrack != -1) {
            
            	
                int stepsToSafeZone = entryPos - posInTrack;
                int remainingSteps = steps - stepsToSafeZone;
                if (steps > Math.abs(entryPos - posInTrack)+4){
            		throw new IllegalMovementException("too much steps that the marble entered the safezone and there were still steps ");
            	}
                
                fullPath.add(track.get(posInTrack));
                
                if (steps > 0 && entryPos < posInTrack){
                	System.out.println("forward 1  posintrack = "+ posInTrack + " steps =  "+steps);
                	int posintrack1=posInTrack ; 
                	for ( int i  = 0  ; i < steps ; i++ ){
                		if (posintrack1 == 100 ){posintrack1 = 0; System.out.print("correction");}
                		System.out.println((posintrack1 ) );
                		fullPath.add(track.get( posintrack1++ ));
                	}
                return fullPath;}
               
                if (steps>0  && entryPos >= posInTrack ){
                	System.out.println("forward 2  posintrack = "+ posInTrack + " steps =  "+steps);

                	int a = 1 ;int  b =  0 ;
                	while (a<=steps){
	                	if (posInTrack + a  <= entryPos ){
	                		fullPath.add(track.get((posInTrack+ a)%track.size() ));
	                		}
	                	else if (marbleColour != gameManager.getActivePlayerColour() ){
	                			fullPath.add(track.get((posInTrack+ a)%track.size() ));
	                		}
	                	else {fullPath.add(safeZone.get(b));
	                			 b++ ; 
	                		}
	                		
                	a++ ; }
                	System.out.println("end of forward 2 ");
                	return fullPath ;
                }
                if (steps < 0 && posInTrack < entryPos ){
                	int posInTrack1= posInTrack;
                	for(int i = 1  ; i<(-steps)   ; i ++ ){
                		
                		if ((posInTrack1-i  )<0){
                			posInTrack1 = track.size()-1  ; 
                		}
                		
                		fullPath.add(track.get((posInTrack1 - 1 )%track.size() ));
                		posInTrack1--;
                	}
                	return fullPath ; 
                }
                if (steps < 0 && posInTrack >= entryPos ){
                	System.out.println("backward 2  " );
                	int  b =  0 ;int a =1 ;
                	while (a<=(-steps)){
                	
	                	if (posInTrack - a  >= entryPos ){fullPath.add(track.get((posInTrack + (steps) + a)%track.size() ));}
	                	else{
	                		if (marbleColour != gameManager.getActivePlayerColour() ){
	                			fullPath.add(track.get((posInTrack- a)%track.size() ));
	                		}
	                		else {fullPath.add(safeZone.get(b));
	                			 b++ ; 
	                		}
	                		a++ ; }
	                	}
                return fullPath; }
                
        }
        if (posInSafeZone!= -1){
        	if (steps <0 ){throw new IllegalMovementException ("cannot move backward in safezone "); }
        	if (posInSafeZone + steps > 3 ){throw new IllegalMovementException ("index out of range "); }
        	fullPath.add(safeZone.get(posInSafeZone));
            for (int i = 0 ; i < steps ; i++ ){
            	fullPath.add(safeZone.get(posInSafeZone + i ));
            }
        }

    return  fullPath ; }

    
   private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalMovementException {
    	int no_Of_mymarbles = 0 ;
    	int no_Of_othersmarbles = 0 ;
        for (int i = 1 ; i < fullPath.size(); i++) {
        	Cell current = fullPath.get(i); 
        	System.out.println(current.getCellType() + "  " + destroy + "  " + gameManager.getActivePlayerColour() + " " + marble.getColour());
        	System.out.println(" 1 ");
	        if (current.getMarble() != null ){
	        	System.out.println(" 1");
		       if (destroy == false && current.getCellType() == CellType.ENTRY && fullPath.get(i).getMarble() != null ){
		    	   System.out.println(" inside 1  " );
		    	   if (i + 1 < fullPath.size() && fullPath.get(i + 1).getCellType() == CellType.SAFE) {
						throw new IllegalMovementException("Cannot land on or bypass a marble in the Safe Zone entry.");
					}
		        		
		        		}
		       
		       
		        else if (current.getCellType() == CellType.BASE  && Isbaseoccupied(current.getMarble())){
		        	System.out.println(" inside 2 ");
		        		throw new IllegalMovementException("The given path is invalid because it contains a cell with a marble belonging to the active player") ; 
		        		}
	        	else if (destroy == true &&  current.getCellType() == CellType.SAFE   && current.getMarble()!= null){
		        		throw new IllegalMovementException("can't pass on safezone marble ") ; 
		        		}
	        	else if (current.getMarble().getColour() == gameManager.getActivePlayerColour()){
		        		no_Of_mymarbles ++ ; 
		        		}
	        	else if (current.getMarble().getColour() != gameManager.getActivePlayerColour()){
		        		no_Of_othersmarbles ++ ; 
		        	}
		        }
        }
        	if (no_Of_mymarbles > 0 && destroy == false ){throw new IllegalMovementException("cannot pass your own marble ") ; }
        	if (no_Of_othersmarbles > 1 && destroy == false ){throw new IllegalMovementException("cannot pass upon multiple marbles ") ; }
        }    
   
    public boolean Isbaseoccupied(Marble marble ){
    	int pos = getPositionInPath(track, marble); 
    	int marblebaseindex = getBasePosition(marble.getColour());
    	if (marblebaseindex == pos  ){return true ;}
    	
    return false ; } 
    
    
    private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy)throws IllegalDestroyException {
    	if (fullPath.isEmpty() ){throw new IllegalDestroyException("Path is Empty ");}
    	else {
    		{
    			
    			Cell current_cell = fullPath.get(0) ; 
    			current_cell.setMarble(null);
    			Cell Target= fullPath.get(fullPath.size() -1 ) ;
    			if (destroy == true){
	    			for (int i = 1 ; i  < fullPath.size()-1 ;i++ ){
	    				Cell cell = fullPath.get(i) ; 
	    				if (cell.getMarble() != null  ){
	    					destroyMarble(cell.getMarble());}
	    				}
    			}
    			if (Target.getMarble()!= null  ){
    				destroyMarble(Target.getMarble());
    				Target.setMarble(marble);
    			}
    			Target.setMarble(marble);
    			if (Target.isTrap()){Target.setMarble(null);
    								 Target.setTrap(false);
    								 assignTrapCell();}
    		}
    	}
    }
   

    private void validateDestroy(int positionInPath) throws IllegalDestroyException{
    	System.out.println("1 ");
    	if (positionInPath < 0 ){
    		System.out.println("2 ");
    		throw new IllegalDestroyException("not on tarck ") ; }
    	System.out.println("4  ");
    	Cell currentcell = track.get(positionInPath); 
    	System.out.println(" 4.1 ");
    	
    	if (currentcell.getMarble()!= null && Isbaseoccupied(currentcell.getMarble()) ){
    		System.out.println(" 4.1 ");
    		throw new IllegalDestroyException("there is a marble in the base cell ") ;
    	}
    }
    private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException {
    	Marble currentmarble = occupiedBaseCell.getMarble() ; 
    	Colour marblecolour = currentmarble.getColour() ; 
    	if (occupiedBaseCell.getMarble() != null && gameManager.getActivePlayerColour() == marblecolour ){throw new CannotFieldException("Base cell is not empty  ");}
    
    }
    private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException{
    	System.out.println("saving 1  posintrack = "+ positionOnTrack  + " positionInSafeZone =  "+positionInSafeZone);
    	
    	if (positionOnTrack < 0 ||positionOnTrack >= track.size()  ) {
            throw new InvalidMarbleException("Marble is not on the track.");
        }
    	if (positionInSafeZone>=0 && positionInSafeZone<safeZones.size()){
    		throw new InvalidMarbleException("Marble is in the safezone");
    	}
    	/*System.out.println("continued 1 ");
    	Cell tmpcell = track.get(positionOnTrack);
    	System.out.println("continued 1.1 ");
    	if(tmpcell.getMarble() == null ){
    		System.out.println("continued 2 ");
    		throw new InvalidMarbleException("Marble is not found.");}
    	System.out.println("continued 2 out ");
    	ArrayList<Cell> tmpsafezone = getSafeZone(gameManager.getActivePlayerColour());
    	System.out.println("saving 1  tmpcell.getMarble() = "+ gameManager.getActivePlayerColour()+" " +tmpcell.getMarble().getColour()  + " tmpsafezone.get(positionInSafeZone).getMarble() =  "+tmpsafezone.get(positionInSafeZone).getMarble());
    	if (tmpsafezone.get(positionInSafeZone).getMarble() == tmpcell.getMarble()){
    		System.out.println("continued 3 ");
    		
    		throw new InvalidMarbleException("Marble is already in the Safe Zone.") ; 
    		}
    	System.out.println("continued 3 out ");
    	/*if (tmpsafezone.get(positionInSafeZone).getMarble() != tmpcell.getMarble()){
    		System.out.println("continued 3 ");
    		throw new InvalidMarbleException("Marble isnt  in the Safe Zone.") ; 
    		}
    	*/
    	
    	System.out.println(" end of validate saving   " );
    }
  
    public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException{
    	ArrayList<Cell> fullPath = validateSteps(marble, steps);
    	validatePath(marble, fullPath, destroy);
		move(marble, fullPath, destroy);
    }
    public void swap(Marble marble_1, Marble marble_2) throws IllegalSwapException{
    	validateSwap(marble_1, marble_2);
		Cell Cell_of_Marble_1 = null;
		Cell Cell_of_Marble_2 = null ; 
    	int pos_Of_Marble1 = getPositionInPath(track, marble_1) ;
    	/*if (pos_Of_Marble1 != -1){ */Cell_of_Marble_1 = track.get(pos_Of_Marble1); /*}
    	else {
    	    for (SafeZone zone : safeZones) {
    	        int safePos = getPositionInPath(zone.getCells(), marble_1);
    	        if (safePos != -1) {
    	            Cell_of_Marble_1 = zone.getCells().get(safePos);
    	            break;
    	        	}
    	        }
    	    }*/
    	int pos_Of_Marble2 = getPositionInPath(track, marble_2) ;
    	/*if (pos_Of_Marble2 != -1){ */Cell_of_Marble_2 = track.get(pos_Of_Marble2) ; /*}
    	else {
    	    for (SafeZone zone : safeZones) {
    	        int safePos = getPositionInPath(zone.getCells(), marble_2);
    	        if (safePos != -1) {
    	            Cell_of_Marble_2 = zone.getCells().get(safePos);
    	            break;
    	        	}
    	        }
    	    
    	}*/
    Cell_of_Marble_1.setMarble(marble_2);
    Cell_of_Marble_2.setMarble(marble_1);
    	
    }
    private void validateSwap(Marble marble_1, Marble marble_2)throws IllegalSwapException {
		int pos_1 = getPositionInPath(track, marble_1);
		int pos_2 = getPositionInPath(track, marble_2);
		if (pos_1 == -1 || pos_2 == -1 ){
			throw new IllegalSwapException("a marbles are not on the track  "); 
		}
		if ( (gameManager.getActivePlayerColour() == marble_1.getColour() )&&  (Isbaseoccupied(marble_2)) ){
			throw new IllegalSwapException("marble 2 is in the base cell ");
		}
		if ( (gameManager.getActivePlayerColour() == marble_2.getColour() )&&  (Isbaseoccupied(marble_1)) ){
			throw new IllegalSwapException("marble 1 is in the base cell ");
		}
		
	}

	public void destroyMarble(Marble marble) throws IllegalDestroyException {
        int marbleindex = getPositionInPath(track, marble) ; 
        validateDestroy(marbleindex);
        Cell current = track.get(marbleindex);
        gameManager.sendHome(marble);
        current.setMarble(null);
        
       
    }

    public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {
    	Colour marblecolor = marble.getColour();
    	
        int basePos = getBasePosition(marblecolor );
       
       // Cell tmp = track.get(getPositionInPath(track, marble)) ; 
        Cell baseCell = track.get(basePos);
        
        Marble existingMarble = baseCell.getMarble();
       
        if (existingMarble != null) {
        	/*if (existingMarble.getColour().equals(marblecolor) ){
        		throw new CannotFieldException("there is a marble on thebasecell with the same colour  ") ; 
        		}*/
        	validateFielding(baseCell);
        	destroyMarble(baseCell.getMarble());
        }
        //validateDestroy(basePos); 
       // destroyMarble(existingMarble);
        //tmp.setMarble(null);
        baseCell.setMarble(marble);
    }


    public void sendToSafe(Marble marble) throws InvalidMarbleException{
    	ArrayList<Cell> safezone_of_marble  =getSafeZone(marble.getColour()) ; 
    	int Track_Index = getPositionInPath(track, marble); 
    	int index_2 = 0 ; 
    	boolean flag = false ; 
    	validateSaving(  getPositionInPath(safezone_of_marble,marble) , Track_Index);

    	while (flag == false){
	    	int randomindex = (int)(Math.random()*safeZones.size());
	    	if (safezone_of_marble.contains(marble) || Track_Index == -1 ){
	    		
	    		throw new InvalidMarbleException("the selected marble was already in the Safe Zone or if it wasn’t on the track.");
	    	}
	    	
	    	if (safezone_of_marble.get(randomindex).getMarble() == null){
	    		
	            flag =true ; 
	           
	            index_2 = randomindex ; 
	            
	    	}
    	}
    	System.out.print("index 2 is "+index_2);
    	
    	safezone_of_marble.get(index_2).setMarble(marble);
    	
    	track.get(Track_Index).setMarble(null);
    	
    }
  
    public ArrayList<Marble> getActionableMarbles(){
    	ArrayList<Marble> marbles = new ArrayList<Marble>(); 
    	Colour colour_of_marble = gameManager.getActivePlayerColour() ; 
    	for(int i = 0 ; i < track.size() ; i++ ){
    		Cell cell = track.get(i);
    		Marble marble_player = cell.getMarble();
    		if (marble_player.getColour().equals(colour_of_marble)&& marble_player != null ){
    			marbles.add(marble_player) ; 
    		}
    	}
    	ArrayList<Cell> Safezone_Of_player = getSafeZone(colour_of_marble) ; 
    	for(int i = 0 ; i < Safezone_Of_player.size() ; i++ ){
    		Cell Safe_cell = Safezone_Of_player.get(i);
    		Marble marble_player = Safe_cell.getMarble();
    		if (marble_player.getColour().equals(colour_of_marble)&& marble_player != null ){
    			marbles.add(marble_player) ; 
    		}
    	}
    	
    	return marbles ; 
    }
    
}


