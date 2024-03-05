package command;

import geometry.Shape;
import mvc.DrawingModel;

public class SelectShapeCmd implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int index;

	public SelectShapeCmd(Shape shape, DrawingModel model) {
		this.shape = shape;
		this.model=model;
	}

	@Override
	public void execute() {
		index = model.indexOf(shape);
		shape.setSelected(true);
	}

	@Override
	public void unexecute() {
		shape.setSelected(false);
	}
	
	@Override
	public String toString() {
		return "SelectShapeCmd [index=" + index + "]";
	}

}