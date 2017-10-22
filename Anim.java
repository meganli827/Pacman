package myextension.given;

public interface Anim {
	
	/**
	 * Draw itself on the screen, however it likes
	 */
	public void draw();
	
	/**
	 * Tell me whether it is finished, and should be removed from my
	 *     list of things I'm animating.
	 * @return whether this Anim is done
	 */
	public boolean isDone();

}
