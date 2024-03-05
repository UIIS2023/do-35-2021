package command;

import geometry.Line;
import geometry.Point;


public class UpdateLineCmd implements Command {
	
	private Line line;
	private Line newState;
	private Line original = new Line(new Point(0,0), new Point(0,0));


	public UpdateLineCmd(Line line, Line newState) {
		this.line = line;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original.clone(line);
		line.clone(newState);
	}

	@Override
	public void unexecute() {
		line.clone(original);
	}

	@Override
	public String toString() {
		return "UpdateLineCmd [line={" + line + "}, newState={" + newState + "}]";
	}
}
