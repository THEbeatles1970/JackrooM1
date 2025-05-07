package model.card.wild;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Saver extends Wild {

    public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }
   public boolean validateMarbleColours(ArrayList<Marble> marbles){
	   Colour players_colour = gameManager.getActivePlayerColour(); 
	   for (int i = 0 ; i < marbles.size() ; i++ ){
		   if (marbles.get(0).getColour() == gameManager.getActivePlayerColour()){
			   return true ; }
    	}
   	return false ; }
    	
    	
    public  void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException{
    	if (marbles.isEmpty()){throw new InvalidMarbleException("marbles are empty ");}
    	int index = (int)(Math.random() * marbles.size() ); 
    	Marble randmarble = marbles.get(index);
    	boardManager.sendToSafe(randmarble);
    	
    }
}
