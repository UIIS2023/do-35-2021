package mvc;

import java.util.ArrayList;

import geometry.Shape;

public class DrawingModel {
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	public void add(Shape shape) {
		shapes.add(shape);
	}
	
	public void remove(Shape shape) {
		shapes.remove(shape);
	}
	
	public Shape get(int index) {
		return shapes.get(index);
	}

	public ArrayList<Shape> getShapes() {
		return shapes;
	}
	
	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}
	
	public int indexOf(Shape shape) {
		return shapes.indexOf(shape);
	
	}
	
	public void add(int index, Shape shape) {
		shapes.add(index, shape);
	}

}
