package command;

import geometry.Rectangle;
import geometry.Point;

public class UpdateRectangleCmd implements Command {
	
	private Rectangle rect;
	private Rectangle newState;
	private Rectangle original = new Rectangle(new Point(0,0), 0, 0);

	public UpdateRectangleCmd(Rectangle rectangle, Rectangle newState) {
		this.rect = rectangle;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original.clone(rect);
		rect.clone(newState);
	}

	@Override
	public void unexecute() {
		rect.clone(original);
	}
	
	@Override
	public String toString() {
		return "UpdateRectangleCmd [rect={" + rect + "}, newState={" + newState + "}]";
	}

}
