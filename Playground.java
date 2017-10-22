package myextension.given;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import sedgewick.StdDraw;

import spath.ShortestPaths;
import spath.graphs.*;

public class Playground implements Anim{
	
	final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
	// the playground
	public final int unitcount = 20;
	public final double gridsize = 0.025;
	public final double ratio = 0.05; //gridsize/((double)size);
	
	public boolean [][] playground = new boolean [unitcount][unitcount];
	public Vertex [][] vertices = new Vertex [unitcount][unitcount];
	public DirectedGraph graph = new DirectedGraph();
	public Map<Edge, Integer> weights;
	public LinkedList<Edge> outputPath;
	
	public int[] pacmancoord = new int[2];
	public int[] monstercoord = new int[2];
	
	public Playground(int[] pacmancoord, int[] monstercoord) {
		//super();
		this.weights = new HashMap<Edge, Integer>();
		
		// playground vertices?
		
		this.pacmancoord = pacmancoord;
		this.monstercoord = monstercoord;
	}

	public boolean CheckEndGame(){
		return false;
	}
	
	/*
	 *  Decide where the monster should go
	 *  in order to hunt down the pacman
	 *  
	 */
	public int decidemonsterdir(){
		
		// Decide where the monster should go
		// Using shortest path
		// FIXME
		//
		
		int diffx = pacmancoord[0]- monstercoord[0];
		int diffy = pacmancoord[1]- monstercoord[1];
		if(Math.abs(diffx)>Math.abs(diffy)){
			if(diffx > 0)return RIGHT;
			else return LEFT;
		}
		else{
			if(diffy > 0) return UP;
			else return DOWN;
		}
	}
	
	
	public void movemonster(int dir){
		int tempmonster0 = monstercoord[0] + (1-dir)%2;
		int tempmonster1 = monstercoord[1] + (2-dir)%2;
		if (!(dir < 0)){
			if(!playground[tempmonster0][monstercoord[1]]) monstercoord[0] += (1-dir)%2;
			if(!playground[monstercoord[0]][tempmonster1]) monstercoord[1] += (2-dir)%2;
		}
	}
	
	public void movepacman(int dir){
		
		int temppacman0 = pacmancoord[0] + (1-dir)%2;
		if(temppacman0<0) temppacman0=unitcount-1; // boundary
		else if(temppacman0>unitcount-1) temppacman0=0;
		if(!playground[temppacman0][pacmancoord[1]]){ // block
			pacmancoord[0] = temppacman0;
		}
		
		int temppacman1 = pacmancoord[1] + (2-dir)%2;
		if(temppacman1<0) temppacman1=unitcount-1;
		else if(temppacman1>unitcount-1)temppacman1=0;
		if(!playground[pacmancoord[0]][temppacman1]){
			pacmancoord[1] = temppacman1;
		}
	}
	
	public void playgroundconstruct(){
		
		// border
		for (int j=0;j<unitcount;j++){
			playground[j][0] = true;
			playground[j][unitcount-1] = true;
		}
		for (int i=0;i<unitcount;i++){
			playground[0][i] = true;
			playground[unitcount-1][i] = true;
		}
		
		// the tunnel
		/*
		playground[0][9] = false;
		playground[0][10] = false;
		playground[unitcount-1][9] = false;
		playground[unitcount-1][10] = false;
		*/
		
		// center
		for (int j=4;j<unitcount-4;j++) // x
		{
			playground[j][2] = true;
			playground[j][6] = true;
			playground[j][8] = true;
			playground[j][11] = true;
			playground[j][13] = true;
			playground[j][17] = true;
		}
		
		for (int j=0;j<7;j++) // x
		{
			playground[j+2][4] = true;
			playground[unitcount-j-2-1][4] = true;
			playground[j+2][15] = true;
			playground[unitcount-j-2-1][15] = true;
		}
		
		for (int j=0;j<5;j++) // x
		{
			playground[2][j+2] = true;
			playground[2][unitcount -j-2-1] = true;
			playground[17][j+2] = true;
			playground[17][unitcount -j-2-1] = true;
		}
		//playground[6][5] = true;
		//playground[14][16] = true;
	}
	
	public void playgroundgraphgen(){
		
		// only inner part of the playground!
		for (int i=1;i<unitcount-1;i++) // y
		{
			for (int j=1;j<unitcount-1;j++) // x
			{
				if(playground[j][i] == false){	
					vertices[j][i] = new Vertex();
					vertices[j][i].coord[0] = j;
					vertices[j][i].coord[1] = i;
					graph.addVertex(vertices[j][i]);
				}
			}
		}
		
		for (int i=1;i<unitcount-1;i++) // y
		{
			for (int j=1;j<unitcount-1;j++) // x
			{
				//System.out.println(j + " "+ i);
				
				if(!playground[j][i]){
					
					if (!playground[j+1][i]){		
						Edge e1 = new Edge(getvertex(j,i), getvertex(j+1,i));
						graph.addEdge(e1);
						weights.put(e1, 1);
					}
					if (!playground[j-1][i]){			
						Edge e2 = new Edge(getvertex(j,i), getvertex(j-1,i));
						graph.addEdge(e2);
						weights.put(e2, 1);
					}
					if (!playground[j][i+1]){
						Edge e3 = new Edge(getvertex(j,i), getvertex(j,i+1));
						graph.addEdge(e3);
						weights.put(e3, 1);
					}
					if (!playground[j][i-1]){
						Edge e4 = new Edge(getvertex(j,i), getvertex(j,i-1));
						graph.addEdge(e4);
						weights.put(e4, 1);
					}	
				}
			}
		}
		
	}
	
	public void graphtester(){
		
		Vertex v = new Vertex();
		
		System.out.println(graph);
		
		System.out.println("\nEdges from each vertex");
		for (int i=0;i<unitcount;i++) {
			v = vertices[i][2];
			System.out.println("From vertex " + v);
			for (Edge e : v.edgesFrom()) {
				System.out.println("   Edge " + e);
			}
		}
		
		System.out.println("\nEdges to each vertex");
		for (int i=0;i<unitcount;i++) {
			v = vertices[i][2];
			System.out.println("To vertex " + v);
			for (Edge e : v.edgesTo()) {
				System.out.println("   Edge " + e);
			}
		}
	}
	
	private Vertex getvertex(int x, int y){
		Iterator<Vertex> iv = graph.vertices().iterator();
		for (int i=0; i < unitcount*unitcount; i++) {
			Vertex v = iv.next();
			
			//System.out.println(v);
			
			if ((v.coord[0] == x) && (v.coord[1] == y)) {
				return v;
			}
		}
		return null;
	}
	
	@Override
	public void draw() {
		double tempx,tempy;
		
		// draw the playground
		StdDraw.setPenColor(Color.BLACK );
		StdDraw.filledSquare(0, 0, 1);
		for (int i = 0;i<unitcount;i++) // y
		{
			for (int j=0;j<unitcount;j++) // x
			{
				if(playground[j][i] == true){
					tempx = ((double)j) * ratio;
					tempy = ((double)i) * ratio;
					StdDraw.setPenColor(Color.BLUE );
					StdDraw.filledSquare(tempx, tempy, gridsize);
				}
				else{
					tempx = ((double)j) * ratio;
					tempy = ((double)i) * ratio;
					StdDraw.setPenColor(Color.BLACK );
					StdDraw.square(tempx, tempy, gridsize);
				}
			}
		}
		
		// draw the pacman
		tempx = ((double)pacmancoord[0]) * ratio;
		tempy = ((double)pacmancoord[1]) * ratio;
		StdDraw.setPenColor(Color.YELLOW );
		StdDraw.filledCircle(tempx, tempy, gridsize);
		StdDraw.setPenColor(Color.BLACK );
		StdDraw.circle(tempx, tempy, gridsize);
		
		// draw the monster
		tempx = ((double)monstercoord[0]) * ratio;
		tempy = ((double)monstercoord[1]) * ratio;
		StdDraw.setPenColor(Color.red );
		StdDraw.filledCircle(tempx, tempy, gridsize); 
		
		StdDraw.text(1, 1, "" + pacmancoord[0] + " " + pacmancoord[1]);
		
	}

	@Override
	public boolean isDone() {
		return false;
	}
}
