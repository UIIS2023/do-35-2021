package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Drawing extends JPanel {
	public Drawing() {
		setBackground(Color.WHITE);
	}
	
	public static void main(String[] args) {

		JFrame frame = new JFrame("Drawing");
		frame.setSize(800, 600);
		Drawing drawing = new Drawing();
		frame.getContentPane().add(drawing);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	public void paint(Graphics g) {
		
		Point p = new Point(50, 65, false, Color.RED);
		
		Point p1 = new Point(70, 80, true, Color.BLACK);
		
		Line l = new Line(new Point(110, 110), new Point(200, 200), false, Color.RED);
		
		Line l1 = new Line(new Point(240, 240), new Point(340, 340), true, Color.BLACK);
		
		Rectangle r = new Rectangle(new Point(360, 360), 100, 150, true, Color.BLACK, Color.YELLOW);
		
		Circle c = new Circle(new Point(500, 120), 50, true, Color.RED, Color.YELLOW);
		
		
		Donut d = new Donut(new Point(600, 400), 50, 20, true, Color.BLACK, Color.GREEN);
		
		
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		shapes.add(p); 
		shapes.add(l);
		shapes.add(c);
		shapes.add(r);
		shapes.add(d);
		
		Iterator<Shape> it = shapes.iterator();
		while(it.hasNext()) {
			System.out.println("Selected: " + it.next().isSelected());
		} 
		
		shapes.get(2).draw(g);
		
		shapes.get(shapes.size() - 1).draw(g);
		
		shapes.get(3).draw(g);
		
		Line newLine = new Line(new Point(150, 150), new Point(350, 350));
		shapes.add(3, newLine);
		
		shapes.remove(1);

		while (it.hasNext()) {
			it.next().setSelected(true);
		}


		while (it.hasNext()) {
			it.next().draw(g);
		}

		while (it.hasNext()) {
			if (it.next() instanceof SurfaceShape) {
				it.next().draw(g);
			}
		}
		
	}

}
