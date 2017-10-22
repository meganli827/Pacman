package myextension.given;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sedgewick.StdDraw;

/**
 * 
 * IN CYTRON WE TRUST
 * 
 * @author limeng FanHaoyang
 *
 * the vertex class needs an additional x & y info
 * actually equivalent to the existing id tho (id%20, id/20)
 * 
 */

public class Pacman {

	public static void main(String[] args) {
		final int showPauseTime = 20; // milliseconds
		final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
		int[] pacmanstartpos = {6,9};
		int[] monsterstartpos = {3,9};//{16,16};
		int pacmanspd = 10;
		int monsterspd = 15;
		int movepacmancount = 0;
		int movemonstercount = 0;
		boolean monstermove = false;
		int monstermovetimer = 0;
		int pacmandir = 0;
		
		//
		// Want to be able to continually iterate through a list of
		//    things to draw on the screen.  Each of those things
		//    should be able to draw itself, and also tell me when
		//    it is done.
		//
		List<Anim> scene1 = new LinkedList<Anim>();
		Playground thePlayground = new Playground(pacmanstartpos,monsterstartpos);
		scene1.add(thePlayground);

		thePlayground.playgroundconstruct();
		thePlayground.playgroundgraphgen();
		//thePlayground.graphtester();
		
		mainloop:
		while (!scene1.isEmpty()) {
			
			// check end game
			//
			if(thePlayground.CheckEndGame()){
				break mainloop; 
			}	
			
			// check pacman move
			//
			if(StdDraw.hasNextKeyTyped())
			{
				int k=StdDraw.nextKeyTyped();
				if(k == 'a')pacmandir = LEFT;
				else if(k == 'd')pacmandir = RIGHT;
				else if(k == 's')pacmandir = DOWN;
				else if(k == 'w')pacmandir = UP;
			} 
			
			// update game state
			//
			if(movepacmancount>pacmanspd){
				thePlayground.movepacman(pacmandir);
				movepacmancount=0;
			}
			else movepacmancount++;
			
			if(movemonstercount>monsterspd){
				int decision = thePlayground.decidemonsterdir();
				thePlayground.movemonster(decision);
				movemonstercount=0;
			}
			else movemonstercount++;
			
			StdDraw.clear();
			// draw everything
			//
			for (Anim a : scene1) {
				a.draw();
			}
			
			//
			// Delete what is done, the safe way
			//
			Iterator<Anim> iter = scene1.iterator();
			while (iter.hasNext()) {
				Anim a = iter.next();
				if (a.isDone()) {
					iter.remove();   // removes the current object safely
				}
			}
			StdDraw.show(showPauseTime);
		}
		
		//
		// clear the screen
		//
		StdDraw.clear();
		StdDraw.show(showPauseTime);
		
	}
	
}
