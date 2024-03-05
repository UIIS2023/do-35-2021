package mvc;

import java.awt.Graphics;

import javax.swing.JPanel;

public class DrawingView extends JPanel { 
	
	private DrawingModel model = new DrawingModel();

	public void setModel(DrawingModel model) {
		this.model = model;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		model.getShapes().forEach(e -> e.draw(g));
	}

}
