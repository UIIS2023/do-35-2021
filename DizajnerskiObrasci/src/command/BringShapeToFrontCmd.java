package command;

import geometry.Shape;
import mvc.DrawingModel;

public class BringShapeToFrontCmd implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int originalIndex;

	public BringShapeToFrontCmd(Shape shape, DrawingModel model) {
		this.shape = shape;
		this.model = model;
	}

	@Override
	public void execute() {
		originalIndex = model.indexOf(shape);
		model.remove(shape);
		model.add(shape);
	}

	@Override
	public void unexecute() {
		model.remove(shape);
		model.add(originalIndex, shape);
	}
	
	@Override
	public String toString() {
		return "BringShapeToFrontCmd [originalIndex=" + originalIndex + "]";
	}

}