package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingModel;

public class ShapeToFrontCmd implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int oldIndex;

	public ShapeToFrontCmd(Shape shape, DrawingModel model) {
		this.shape = shape;
		this.model = model;
	}

	@Override
	public void execute() {
		int idx = model.indexOf(shape);
		oldIndex = idx;
		Collections.swap(model.getShapes(), idx, idx + 1);
	}

	@Override
	public void unexecute() {
		Collections.swap(model.getShapes(), oldIndex+1, oldIndex);
	}
	
	@Override
	public String toString() {
		return "ShapeToFrontCmd [oldIndex=" + oldIndex + "]";
	}


}