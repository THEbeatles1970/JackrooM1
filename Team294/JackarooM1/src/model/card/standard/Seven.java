package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Seven extends Standard {

    public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 7, suit, boardManager, gameManager);
    }
    
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
    	int size = marbles.size() ; 
    	if (size == 1  || size == 2  ){return true ; }
    	return false;
    }
    public boolean validateMarbleColours(ArrayList<Marble> marbles){
    	Colour players_colour = gameManager.getActivePlayerColour(); 
    	for (int i =0 ; i < marbles.size() ;i++ ){
    		if ( players_colour !=  marbles.get(i).getColour()){
    			return false  ; 
    			}
    		}
    	return true ; 
    	}
    public  void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException{
    	if (marbles.size()== 2 ){
			
			Marble first = marbles.get(0);
			Marble second  = marbles.get(1);
			
			int index = boardManager.getSplitDistance()  ; 
			boardManager.moveBy(first,  index , false);
			boardManager.moveBy(second , 7-index , false);
		 
		}
    	
    	if (marbles.size() == 1 ){
    		super.act(marbles);
    	}
    else if (marbles.isEmpty()){    		throw new InvalidMarbleException("you cannot select this card  ");
}
    }
  
}
