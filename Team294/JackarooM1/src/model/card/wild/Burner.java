package model.card.wild;

import java.util.ArrayList;

import model.player.Marble;
import model.player.Player;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Burner extends Wild {

    public Burner(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager); 
    }
   public boolean validateMarbleColours(ArrayList<Marble> marbles){
	   for (int i = 0 ; i < marbles.size() ; i++ ){
		   if (marbles.get(0).getColour() == gameManager.getActivePlayerColour()){
			   return false ; }
    	}

return  true ; }
    public  void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException{
    	
    	int index = (int)(Math.random() * marbles.size() ); 
    	
    	Marble randmarble = marbles.get(index);
    	
    	gameManager.sendHome(randmarble);
    	boardManager.destroyMarble(randmarble);
}
    
   
}
