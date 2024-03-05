package adapter;

import java.awt.Color;
import java.awt.Graphics;

import geometry.Point;
import geometry.SurfaceShape;
import hexagon.Hexagon;

public class HexagonAdapter extends SurfaceShape {
	
	private Hexagon hexagon;

	public HexagonAdapter() {

	}
	public HexagonAdapter(Point center, int r) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), r);
	}
	public HexagonAdapter(Point center, int r, boolean selected,Color outlineColor, Color innerColor) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), r);
		hexagon.setBorderColor(outlineColor);
		hexagon.setAreaColor(innerColor);
		hexagon.setSelected(selected);
	}

	@Override
	public void moveBy(int x, int y) {
		hexagon.setX(hexagon.getX() + x);
		hexagon.setY(hexagon.getY() + y);
	}

	@Override
	public int compareTo(Object obj) {
		if (obj instanceof HexagonAdapter) {
			HexagonAdapter hexagonAdapter = (HexagonAdapter)obj;
			return hexagon.getR() - hexagonAdapter.hexagon.getR();
		}
		return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
	}

	@Override
	public void fill(Graphics g) {
		
	}
	@Override
	public double area() {
		return 0;
	}
	
	public int getX() {
		return hexagon.getX();
	}
	public int getY() {
		return hexagon.getY();
	}
	public void setX(int x) {
		hexagon.setX(x);
	}
	public void setY(int y) {
		hexagon.setY(y);
	}
	
	public int getR() {
		return hexagon.getR();
	}
	
	public void setR(int r) {
		hexagon.setR(r);
	}
	
	@Override
	public void setSelected(boolean selected) {
		hexagon.setSelected(selected);
	}
	
	@Override
	public boolean isSelected() {
		return hexagon.isSelected();
	}

	@Override
	public void setColor(Color color) {
		hexagon.setBorderColor(color);
	}
	
	@Override
	public Color getColor() {
		return hexagon.getBorderColor();
	}
	
	@Override
	public Color getInnerColor() {
		return hexagon.getAreaColor();
	}
	
	@Override
	public void setInnerColor(Color color) {
		hexagon.setAreaColor(color);
	}
	
	@Override
	public String toString() {
		return "HexagonAdapter [x=" + hexagon.getX() + ", y=" + hexagon.getY() + ", r=" + 
				hexagon.getR() + ", outlineColor={" + hexagon.getBorderColor() +
				"}, innerColor={" + hexagon.getAreaColor() + "}]";
	}
	
	public HexagonAdapter clone(HexagonAdapter newState) {
		this.setX(newState.getX());
		this.setY(newState.getY());
		this.setR(newState.getR());
		this.setColor(newState.getColor());
		this.setInnerColor(newState.getInnerColor());
		return this;
	}
}
