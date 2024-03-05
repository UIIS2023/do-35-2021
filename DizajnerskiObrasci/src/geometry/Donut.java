package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Donut extends Circle {

	private int innerRadius;
	
	public Donut() {
		
	}
	
	public Donut(Point center, int radius, int innerRadius) {
		super(center, radius);
		this.innerRadius = innerRadius;
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected) {
		this(center, radius, innerRadius);
		setSelected(selected);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color) { 
		this(center, radius, innerRadius, selected);
		setColor(color);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color, Color innerColor) { 
		this(center, radius, innerRadius, selected, color);
		setInnerColor(innerColor);
	}
	
	public void draw(Graphics g) {
		this.fill(g);
		
		g.setColor(getColor());
		
		g.drawOval(getCenter().getX() - getRadius(), 
				getCenter().getY() - getRadius(), 
				getRadius() * 2, 
				getRadius() * 2);
		g.drawOval(getCenter().getX() - this.innerRadius, 
				getCenter().getY() - this.innerRadius, 
				this.innerRadius * 2, 
				this.innerRadius * 2);
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - getRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() + getRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - getRadius() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + getRadius() - 3, 6, 6);
		}
		
		
	}
	
	public void fill(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Ellipse2D outerCircle = new Ellipse2D.Double(getCenter().getX() - getRadius(), 
				getCenter().getY() - getRadius(), 
				2 * getRadius(), 
				2 * getRadius());
		Ellipse2D innerCircle = new Ellipse2D.Double(getCenter().getX() - innerRadius,
				getCenter().getY() - innerRadius,
				2 * innerRadius,
				2 * innerRadius);
		
		Area outerArea = new Area(outerCircle);
        Area innerArea = new Area(innerCircle);
        outerArea.subtract(innerArea);
        
		g2d.setColor(getInnerColor());
		g2d.fill(outerArea);
		g2d.dispose();
	}
	
	public int compareTo(Object o) {
		if (o instanceof Donut) {
			return (int) (this.area() - ((Donut) o).area());
		}
		return 0;
	}
	
	public double area() {
		return super.area() - innerRadius * innerRadius * Math.PI;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Donut) {
			Donut d = (Donut) obj;
			if (this.getCenter().equals(d.getCenter()) &&
					this.getRadius() == d.getRadius() &&
					this.innerRadius == d.getInnerRadius()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean contains(int x, int y) {
		double dFromCenter = this.getCenter().distance(x, y);
		return super.contains(x, y) && dFromCenter > innerRadius;
	}
	
	public boolean contains(Point p) {
		double dFromCenter = this.getCenter().distance(p.getX(), p.getY());
		return super.contains(p.getX(), p.getY()) && dFromCenter > innerRadius;
	}
	
	public int getInnerRadius() {
		return this.innerRadius;
	}
	
	public void setInnerRadius(int innerRadius) {
		this.innerRadius = innerRadius;
	}
	
	@Override
	public String toString() {
		return "Donut [innerRadius=" + innerRadius + ", getCenter()={" + getCenter() + "}, getRadius()=" + getRadius()
		+ ", getInnerColor()={" + getInnerColor() + "}, getColor()={" + getColor() + "}]";
	}
	
	public Donut clone(Donut donut) {
		super.clone(donut);
		this.setInnerRadius(donut.getInnerRadius());
		return this;
	}
}
