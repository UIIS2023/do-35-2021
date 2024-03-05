package command;

import geometry.Shape;

import mvc.DrawingModel;

public class DeselectShapeCmd implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int index;

	public DeselectShapeCmd(Shape shape, DrawingModel model) {
		this.shape = shape;
		this.model=model;
	}

	@Override
	public void execute() {
		index = model.indexOf(shape);
		shape.setSelected(false);
	}

	@Override
	public void unexecute() {
		shape.setSelected(true);
	}
	
	@Override
	public String toString() {
		return "DeselectShapeCmd [index=" + index + "]";
	}

}