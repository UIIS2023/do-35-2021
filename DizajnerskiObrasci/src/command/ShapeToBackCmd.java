package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingModel;

public class ShapeToBackCmd implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int oldIndex;

	public ShapeToBackCmd(Shape shape, DrawingModel model) {
		this.shape = shape;
		this.model = model;
	}

	@Override
	public void execute() {
		int idx = model.indexOf(shape);
		oldIndex = idx;
		Collections.swap(model.getShapes(), idx, idx - 1);
	}

	@Override
	public void unexecute() {
		Collections.swap(model.getShapes(), oldIndex, oldIndex - 1);
	}
	
	@Override
	public String toString() {
		return "ShapeToBackCmd [oldIndex=" + oldIndex + "]";
	}

}