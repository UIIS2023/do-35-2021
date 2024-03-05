package command;

import geometry.Point;


import geometry.Circle;

public class UpdateCircleCmd implements Command {
	
	private Circle circle;
	private Circle newState;
	private Circle original = new Circle(new Point(0,0), 0);

	public UpdateCircleCmd(Circle circle, Circle newState) {
		this.circle = circle;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original.clone(circle);
		circle.clone(newState);
	}

	@Override
	public void unexecute() {
		circle.clone(original);
	}
	
	@Override
	public String toString() {
		return "UpdateCircleCmd [circle={" + circle + "}, newState={" + newState + "}]";
	}

}
