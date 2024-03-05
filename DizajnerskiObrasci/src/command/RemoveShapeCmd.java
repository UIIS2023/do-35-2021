package command;

import geometry.Shape;
import mvc.DrawingModel;

public class RemoveShapeCmd implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int originalIndex;
	
	public RemoveShapeCmd(Shape shape, DrawingModel model) {
		this.shape = shape;
		this.model = model;
	}

	@Override
	public void execute() {
		originalIndex = model.indexOf(shape);
		model.remove(shape);

	}

	@Override
	public void unexecute() {
		model.add(originalIndex, shape);
	

	}
	
	@Override
	public String toString() {
		return "RemoveShapeCmd [originalIndex=" + originalIndex + "]";
	}

}