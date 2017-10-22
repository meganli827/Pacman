package myextension.given;

import static org.junit.Assert.*;

public class Test {

	@org.junit.Test
	public void test() {
		int[] pacmanstartpos = {9,4};
		int[] monsterstartpos = {10,10};//{16,16};
		Playground thePlayground = new Playground(pacmanstartpos,monsterstartpos);
		thePlayground.playgroundconstruct();
		thePlayground.playgroundgraphgen();
		
		assertEquals("wrong direction!", 0, thePlayground.decidemonsterdir());
	}

}
