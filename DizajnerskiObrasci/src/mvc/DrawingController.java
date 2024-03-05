package mvc;

import command.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

import java.io.File;

import java.util.Iterator;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import javax.swing.filechooser.FileNameExtensionFilter;


import adapter.HexagonAdapter;
import drawing.DlgHexagon;
import drawing.DlgCircle;
import drawing.DlgDonut;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import observer.ObservableButtons;
import observer.UpdateButtons;
import strategy.LoadSaveDrawing;
import strategy.LoadSaveLog;
import strategy.LoadSaveStrategy;

public class DrawingController {

	private DrawingModel model;
	private DrawingFrame frame;

	private Shape selectedShape;
	private Point startPoint;
	
	private ArrayList<Command> undoList = new ArrayList<>();
	private ArrayList<Command> redoList = new ArrayList<>();
	
	private ObservableButtons observableButtons = new ObservableButtons();
	private UpdateButtons updateButtons;
	
	private LoadSaveStrategy logIO;
	private LoadSaveStrategy drawingIO;
	
	private Color outlineColor = Color.BLACK;
	private Color innerColor = Color.WHITE;

	public Shape getSelectedShape() {
		Iterator<Shape> iterator = model.getShapes().iterator();
		Shape shape = null;
		while (iterator.hasNext()) {
			Shape s = iterator.next();		
			if (s.isSelected()) {
				shape = s;
			}
		}
		return shape;
	}

	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
		
		this.updateButtons = new UpdateButtons(frame);
		this.observableButtons.addPropertyChangeListener(updateButtons);
		
		this.logIO = new LoadSaveLog(this);
		this.drawingIO =  new LoadSaveDrawing();
	}
	
	private void updateButtonState() {
		
		int selectedShapesAmount = 0;
		Iterator<Shape> iterator = model.getShapes().iterator();
		while(iterator.hasNext()) {
			if(iterator.next().isSelected()) {
				selectedShapesAmount++;
			}
		}
		if (selectedShapesAmount > 0) {
			observableButtons.setDeleteEnabled(true);
			if (selectedShapesAmount == 1) {
				observableButtons.setEditEnabled(true);
				if (model.indexOf(getSelectedShape()) != 0) {
					observableButtons.setToBackEnabled(true);
					observableButtons.setBringToBackEnabled(true);
				} else {
					observableButtons.setToBackEnabled(false);
					observableButtons.setBringToBackEnabled(false);
				}
				if (model.indexOf(getSelectedShape()) != model.getShapes().size() - 1) {
					observableButtons.setToFrontEnabled(true);
					observableButtons.setBringToFrontEnabled(true);
				} else {
					observableButtons.setToFrontEnabled(false);
					observableButtons.setBringToFrontEnabled(false);
				}
			} else {
				observableButtons.setEditEnabled(false);
				observableButtons.setToFrontEnabled(false);
				observableButtons.setBringToFrontEnabled(false);
				observableButtons.setToBackEnabled(false);
				observableButtons.setBringToBackEnabled(false);
			}
		} else {
			observableButtons.setDeleteEnabled(false);
			observableButtons.setEditEnabled(false);
			observableButtons.setToFrontEnabled(false);
			observableButtons.setBringToFrontEnabled(false);
			observableButtons.setToBackEnabled(false);
			observableButtons.setBringToBackEnabled(false);
		}
		if (undoList.size() > 0) {
			observableButtons.setUndoEnabled(true);
		} else {
			observableButtons.setUndoEnabled(false);
		}
		if (redoList.size() > 0) {
			observableButtons.setRedoEnabled(true);
		} else {
			observableButtons.setRedoEnabled(false);
		}
		
	}
	
	private void execute(Command command) {
		command.execute();
		undoList.add(command);
		redoList.clear();
		frame.repaint();
		
		frame.getLogTextArea().append(command.toString() +  '\n');
		
		updateButtonState();
	}
	
	public void undo() {
		int index = undoList.size() - 1;
		if(index < 0) {
			JOptionPane.showMessageDialog(frame, "Nothing to undo!", "error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Command command = undoList.get(index);
		command.unexecute();
		undoList.remove(index);
		redoList.add(command);
		frame.repaint();
		frame.getLogTextArea().append("Undo - " + command +  '\n');
		
		updateButtonState();
	}
	
	public void redo() {
		int index = redoList.size() - 1;
		if(index < 0) {
			JOptionPane.showMessageDialog(frame, "Nothing to redo!", "error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Command command = redoList.get(index);
		command.execute();
		redoList.remove(index);
		undoList.add(command);
		frame.repaint();
		
		frame.getLogTextArea().append("Redo - " + command +  '\n');
		updateButtonState();
	}
	
	

	public void mouseClicked(MouseEvent me) {
		Shape newShape = null;
		Point click = new Point(me.getX(), me.getY()); 

		if (frame.getTglbtnSelect().isSelected()) {
			selectedShape = null;
			Iterator<Shape> iterator = model.getShapes().iterator();

			while (iterator.hasNext()) {
				Shape shape = iterator.next();					
				
				if (shape.contains(click.getX(), click.getY())) {
					selectedShape = shape;
					
				}
				

			}

			if (selectedShape != null) {
				if(selectedShape.isSelected()) {
					DeselectShapeCmd cmd = new DeselectShapeCmd(selectedShape,model);
					execute(cmd);
					selectedShape = null;
				} else {
					SelectShapeCmd cmd = new SelectShapeCmd(selectedShape,model);
					execute(cmd);
				}
			} else {
				Iterator<Shape> iterator1 = model.getShapes().iterator();

				while (iterator1.hasNext()) {
					Shape shape = iterator1.next();		
					if (shape.isSelected()) {
						DeselectShapeCmd cmd = new DeselectShapeCmd(shape,model);
						execute(cmd);
					}
				}
			}
				

		} else if (frame.getTglbtnPoint().isSelected()) {

			newShape = new Point(click.getX(), click.getY(), false, outlineColor);

		} else if (frame.getTglbtnLine().isSelected()) {

			if (startPoint == null)
				startPoint = click;
			else {
				newShape = new Line(startPoint, new Point(me.getX(), me.getY()), false, outlineColor);
				startPoint = null;
			}

		} else if (frame.getTglbtnCircle().isSelected()) {

			DlgCircle dialog = new DlgCircle();

			dialog.getTxtX().setText("" + Integer.toString(click.getX()));
			dialog.getTxtX().setEditable(false);  			
			dialog.getTxtY().setText("" + Integer.toString(click.getY()));
			dialog.getTxtY().setEditable(false);
			
			dialog.getBtnOutlineColor().setBackground(outlineColor);
			dialog.getBtnInnerColor().setBackground(innerColor);
			dialog.setVisible(true);

			if (dialog.isConfirm()) {
				newShape = dialog.getCircle();
			}

		} else if (frame.getTglbtnDonut().isSelected()) {

			DlgDonut dialog = new DlgDonut();
			dialog.setModal(true);
			dialog.getTxtX().setText("" + Integer.toString(click.getX()));
			dialog.getTxtX().setEditable(false);
			dialog.getTxtY().setText("" + Integer.toString(click.getY()));
			dialog.getTxtY().setEditable(false);
			
			dialog.getBtnOutlineColor().setBackground(outlineColor);
			dialog.getBtnInnerColor().setBackground(innerColor);
			dialog.setVisible(true);

			if (dialog.isConfirm()) {

				newShape = dialog.getDonut();
			}
		} else if (frame.getTglbtnRectangle().isSelected()) {

			DlgRectangle dialog = new DlgRectangle();
			dialog.setModal(true);
			dialog.getTxtX().setText("" + Integer.toString(me.getX()));
			dialog.getTxtX().setEditable(false);
			dialog.getTxtY().setText("" + Integer.toString(me.getY()));
			dialog.getTxtY().setEditable(false);
			
			dialog.getBtnOutlineColor().setBackground(outlineColor);
			dialog.getBtnInnerColor().setBackground(innerColor);
			dialog.setVisible(true);

			if (!dialog.isConfirm())
				return;

			try {
				newShape = dialog.getRect();
			} catch (Exception ex) {

				JOptionPane.showMessageDialog(frame, "Wrong data type", "error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (frame.getTglbtnHexagon().isSelected()) {
			
			DlgHexagon dialog = new DlgHexagon();

			dialog.getTxtX().setText("" + Integer.toString(click.getX()));
			dialog.getTxtX().setEditable(false);
			dialog.getTxtY().setText("" + Integer.toString(click.getY()));
			dialog.getTxtY().setEditable(false);
			
			dialog.getBtnOutlineColor().setBackground(outlineColor);
			dialog.getBtnInnerColor().setBackground(innerColor);
			
			dialog.setVisible(true);
			
			if (dialog.isConfirm()) {
				newShape = dialog.getHexagon();
			}
		
		}
		if (newShape != null) {
			// model.add(newShape);
			AddShapeCmd add = new AddShapeCmd(newShape, model);
			execute(add);
		}

		frame.repaint();
	}

	public void modify() {
		
		selectedShape = getSelectedShape();
		if (selectedShape != null) {
			
			Command cmd = null;

			if (selectedShape instanceof Point) {

				Point p = (Point) selectedShape; 
				DlgPoint dialog = new DlgPoint();

				dialog.getTxtX().setText("" + Integer .toString(p.getX()));
				dialog.getTxtY().setText("" + Integer.toString(p.getY()));
				dialog.getBtnColor().setBackground(p.getColor());
				dialog.setVisible(true); 

				if (dialog.isConfirm()) {
					cmd = new UpdatePointCmd(p, dialog.getP());
				}

			} else if (selectedShape instanceof Donut) {

				Donut donut = (Donut) selectedShape;
				DlgDonut dialogd = new DlgDonut();

				dialogd.getTxtX().setText("" + Integer.toString(donut.getCenter().getX()));
				dialogd.getTxtY().setText("" + Integer.toString(donut.getCenter().getY()));
				dialogd.getTxtR().setText("" + Integer.toString(donut.getRadius()));
				dialogd.getTxtInnerR().setText("" + Integer.toString(donut.getInnerRadius()));
				dialogd.getBtnInnerColor().setBackground(donut.getInnerColor());
				dialogd.getBtnOutlineColor().setBackground(donut.getColor());
				dialogd.setModal(true);
				dialogd.setVisible(true);

				if (dialogd.isConfirm()) {
					cmd = new UpdateDonutCmd(donut, dialogd.getDonut());
				}
			} else if (selectedShape instanceof Circle && (selectedShape instanceof Donut) == false) {

				Circle circle = (Circle) selectedShape;
				DlgCircle dialog = new DlgCircle();

				dialog.getTxtX().setText("" + Integer.toString(circle.getCenter().getX()));
				dialog.getTxtY().setText("" + Integer.toString(circle.getCenter().getY()));
				dialog.getTxtR().setText("" + Integer.toString(circle.getRadius()));
				dialog.getBtnInnerColor().setBackground(circle.getInnerColor());
				dialog.getBtnOutlineColor().setBackground(circle.getColor());

				dialog.setVisible(true);
				dialog.setModal(true);

				if (dialog.isConfirm()) {
					cmd = new UpdateCircleCmd(circle, dialog.getCircle());
				}

			} else if (selectedShape instanceof Line) {

				Line line = (Line) selectedShape;
				DlgLine dialog = new DlgLine();

				dialog.getTxtSPX().setText("" + Integer.toString(line.getStartPoint().getX()));
				dialog.getTxtSPY().setText("" + Integer.toString(line.getStartPoint().getY()));
				dialog.getTxtEPX().setText("" + Integer.toString(line.getEndPoint().getX()));
				dialog.getTxtEPY().setText("" + Integer.toString(line.getEndPoint().getY()));
				dialog.getBtnOutlineColor().setBackground(line.getColor());

				dialog.setVisible(true);

				if (dialog.isConfirm()) {
					cmd = new UpdateLineCmd(line, dialog.getLine());
				}

			} else if (selectedShape instanceof Rectangle) {

				Rectangle rect = (Rectangle) selectedShape;
				DlgRectangle dialog = new DlgRectangle();

				dialog.getTxtX().setText("" + Integer.toString(rect.getUpperLeftPoint().getX()));
				dialog.getTxtY().setText("" + Integer.toString(rect.getUpperLeftPoint().getY()));
				dialog.getTxtHeight().setText("" + Integer.toString(rect.getHeight()));
				dialog.getTxtWidth().setText("" + Integer.toString(rect.getWidth()));
				dialog.getBtnInnerColor().setBackground(rect.getInnerColor());
				dialog.getBtnOutlineColor().setBackground(rect.getColor());
				dialog.setModal(true);
				dialog.setVisible(true);

				if (dialog.isConfirm()) {
					cmd = new UpdateRectangleCmd(rect, dialog.getRect());

				}
			} else if (selectedShape instanceof HexagonAdapter) {

				HexagonAdapter hexagon = (HexagonAdapter) selectedShape;
				DlgHexagon dialogh = new DlgHexagon();

				dialogh.getTxtX().setText("" + Integer.toString(hexagon.getX()));
				dialogh.getTxtY().setText("" + Integer.toString(hexagon.getY()));
				dialogh.getTxtR().setText("" + Integer.toString(hexagon.getR()));
				dialogh.getBtnInnerColor().setBackground(hexagon.getInnerColor());
				dialogh.getBtnOutlineColor().setBackground(hexagon.getColor());
				dialogh.setModal(true);
				dialogh.setVisible(true);

				if (dialogh.isConfirm()) {
					cmd = new UpdateHexagonCmd(hexagon, dialogh.getHexagon());
				}
			}

			if(cmd != null) {
				execute(cmd);
				frame.repaint();
			}

		}
	}

	public void delete() {
		ArrayList<Shape> selectedShapes = new ArrayList<Shape>();
		Iterator<Shape> iterator = model.getShapes().iterator();
		while(iterator.hasNext()) {
			Shape shape = iterator.next();
			
			if(shape.isSelected()) {
				selectedShapes.add(shape);
			}
		}
		if (selectedShape != null) {
			int selectedOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?",
					"Warning message", JOptionPane.YES_NO_OPTION);
			if (selectedOption == JOptionPane.YES_OPTION) {
				Iterator<Shape> iterator1 = selectedShapes.iterator();
				while(iterator1.hasNext()) {
					RemoveShapeCmd remove = new RemoveShapeCmd(iterator1.next(), model); 
					execute(remove);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "You haven't selected any shape!", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
		selectedShape = null;
		frame.repaint();
	}
	
	public void toFront() {
		ShapeToFrontCmd cmd = new ShapeToFrontCmd(getSelectedShape(), model);
		execute(cmd);
	}
	public void toBack() {
		ShapeToBackCmd cmd = new ShapeToBackCmd(getSelectedShape(), model);
		execute(cmd);
	}
	public void bringToFront() {
		BringShapeToFrontCmd cmd = new BringShapeToFrontCmd(getSelectedShape(), model);
		execute(cmd);
	}
	public void bringToBack() {
		BringShapeToBackCmd cmd = new BringShapeToBackCmd(getSelectedShape(), model);
		execute(cmd);
	}
	
	public void loadLog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open log");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(frame);
		fileChooser.setVisible(true);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			model.getShapes().clear();
			frame.getLogTextArea().setText("");
			frame.getLogTextArea().setText((String) logIO.load(path));
		}
	}
	public void saveLog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save as");
		int result = fileChooser.showSaveDialog(frame);
		fileChooser.setVisible(true);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			logIO.save(frame.getLogTextArea().getText(), path + ".txt");
		}
	}
	public void loadDrawing() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open drawing");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary Files", "bin");
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(frame);
		fileChooser.setVisible(true);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			model.getShapes().clear();
			frame.getLogTextArea().setText("");
			model.setShapes((ArrayList<Shape>) drawingIO.load(path));
			undoList.clear();
			redoList.clear();
			frame.repaint();
			updateButtonState();
		}
	}
	public void saveDrawing() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save as");
		int result = fileChooser.showSaveDialog(frame);
		fileChooser.setVisible(true);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			drawingIO.save(model.getShapes(), path + ".bin");
		}
	}
	
	public void setOutlineColor() {
		Color color = JColorChooser.showDialog(null, "Choose outline color", outlineColor);
		if (color != null) {
			outlineColor = color;
			frame.getBtnOutlineColor().setBackground(outlineColor);
		}
	}
	
	public void setInnerColor() {
		Color color = JColorChooser.showDialog(null, "Choose inner color", innerColor);
		if (color != null) {
			innerColor = color;
			frame.getBtnInnerColor().setBackground(innerColor);
		}
	}
	
	private Color parseColor(String colorStr) {
		if(colorStr.equals("null")) {
			return null;
		}
		String[] split = colorStr.split("=");
		int r = Integer.parseInt(split[1].split(",")[0]);
		int g = Integer.parseInt(split[2].split(",")[0]);
		int b = Integer.parseInt(split[3].split("]")[0]);
		return new Color(r,g,b);
	}
	
	private Point parsePoint(String pointStr) {
		Point point = new Point();
		String[] split = pointStr.split("=");
		point.setX(Integer.parseInt(split[1].split(",")[0]));
		point.setY(Integer.parseInt(split[2].split(",")[0]));
		int index = pointStr.indexOf("getColor()=") + "getColor()=".length();
		point.setColor(parseColor(pointStr.substring(index).split("\\]")[0]));
		return point;
	}
	
	private Shape parseShape(String shapeStr) {
		if (shapeStr.startsWith("Point")) {
			return parsePoint(shapeStr);
		} else if (shapeStr.startsWith("Line")) {
			Line line = new Line();
			String[] split = shapeStr.split("\\{");
			
			line.setStartPoint(parsePoint(split[1].split("\\}")[0]));
			line.setEndPoint(parsePoint(split[2].split("\\}")[0]));
			int index = shapeStr.indexOf("getColor()=") + "getColor()=".length();
			line.setColor(parseColor(shapeStr.substring(index).split("\\]")[0]));
			
			return line;
		} else if (shapeStr.startsWith("Rectangle")) {
			Rectangle rect = new Rectangle();
			int index = 0;
			
			index = shapeStr.indexOf("upperLeftPoint={") + "upperLeftPoint={".length();
			String substr = shapeStr.substring(index);
			rect.setUpperLeftPoint(parsePoint(substr.split("\\}")[0]));
			
			index = substr.indexOf("height=") + "height=".length();
			substr = substr.substring(index);
			rect.setHeight(Integer.parseInt(substr.split(",")[0]));
			
			index = substr.indexOf("width=") + "width=".length();
			substr = substr.substring(index);
			rect.setWidth(Integer.parseInt(substr.split(",")[0]));
			
			index = substr.indexOf("getInnerColor()={") + "getInnerColor()={".length();
			substr = substr.substring(index);
			rect.setInnerColor(parseColor(substr.split("\\}")[0]));
			
			index = substr.indexOf("getColor()={") + "getColor()={".length();
			substr = substr.substring(index);
			rect.setColor(parseColor(substr.split("\\}")[0]));
			
			return rect;
		} else if (shapeStr.startsWith("Circle")) {
			Circle circle = new Circle();
			int index = 0;
			
			index = shapeStr.indexOf("center={") + "center={".length();
			String substr = shapeStr.substring(index);
			circle.setCenter(parsePoint(substr.split("\\}")[0]));
			
			index = substr.indexOf("radius=") + "radius=".length();
			substr = substr.substring(index);
			circle.setRadius(Integer.parseInt(substr.split(",")[0]));
			
			index = substr.indexOf("getInnerColor()={") + "getInnerColor()={".length();
			substr = substr.substring(index);
			circle.setInnerColor(parseColor(substr.split("\\}")[0]));
			
			index = substr.indexOf("getColor()={") + "getColor()={".length();
			substr = substr.substring(index);
			circle.setColor(parseColor(substr.split("\\}")[0]));
			
			return circle;
		} else if (shapeStr.startsWith("Donut")) {
			Donut donut = new Donut();
			int index = 0;
			
			index = shapeStr.indexOf("innerRadius=") + "innerRadius=".length();
			String substr = shapeStr.substring(index);
			donut.setInnerRadius(Integer.parseInt(substr.split(",")[0]));
			
			index = substr.indexOf("getCenter()={") + "getCenter()={".length();
			substr = substr.substring(index);
			donut.setCenter(parsePoint(substr.split("\\}")[0]));
			
			index = substr.indexOf("getRadius()=") + "getRadius()=".length();
			substr = substr.substring(index);
			donut.setRadius(Integer.parseInt(substr.split(",")[0]));
			
			index = substr.indexOf("getInnerColor()={") + "getInnerColor()={".length();
			substr = substr.substring(index);
			donut.setInnerColor(parseColor(substr.split("\\}")[0]));
			
			index = substr.indexOf("getColor()={") + "getColor()={".length();
			substr = substr.substring(index);
			donut.setColor(parseColor(substr.split("\\}")[0]));
			
			return donut;
		} else if (shapeStr.startsWith("HexagonAdapter")) {
			String[] split = shapeStr.split("=");
			
			int x = Integer.parseInt(split[1].split(",")[0]);
			int y = Integer.parseInt(split[2].split(",")[0]);
			int r = Integer.parseInt(split[3].split(",")[0]);
			HexagonAdapter hexagon = new HexagonAdapter(new Point(x,y),r);
			
			int index = shapeStr.indexOf("outlineColor={") + "outlineColor={".length();
			String substr = shapeStr.substring(index);
			hexagon.setColor(parseColor(substr.split("\\}")[0]));
			
			index = substr.indexOf("innerColor={") + "innerColor={".length();
			substr = substr.substring(index);
			hexagon.setInnerColor(parseColor(substr.split("\\}")[0]));
			return hexagon;
		}
		return null;
	}
	//Point [x=198, y=124, getColor()=java.awt.Color[r=0,g=0,b=0]]
	//AddShapeCmd [shape={}]

	public void executeCommandString(String cmd) {
		if (cmd.startsWith("AddShapeCmd")) {
			int index = cmd.indexOf("{");
			int last = cmd.lastIndexOf("}");
			String shapeStr = cmd.substring(index + 1, last);
			AddShapeCmd command = new AddShapeCmd(parseShape(shapeStr), model);
			execute(command);
		} else if (cmd.startsWith("RemoveShapeCmd")) {
			String[] split = cmd.split("=");
			String[] split2 = split[1].split("\\]");
			RemoveShapeCmd command = new RemoveShapeCmd(model.get(Integer.parseInt(split2[0])), model);
			execute(command);
		} else if (cmd.startsWith("SelectShapeCmd")) {
			String[] split = cmd.split("=");
			String[] split2 = split[1].split("\\]");
			SelectShapeCmd command = new SelectShapeCmd(model.get(Integer.parseInt(split2[0])), model);
			execute(command);
		} else if (cmd.startsWith("DeselectShapeCmd")) {
			String[] split = cmd.split("=");
			String[] split2 = split[1].split("\\]");
			DeselectShapeCmd command = new DeselectShapeCmd(model.get(Integer.parseInt(split2[0])), model);
			execute(command);
		} else if (cmd.startsWith("UpdatePointCmd")) {
			int index = cmd.indexOf("newState={") + "newState={".length();
			int last = cmd.lastIndexOf("}");
			String substr = cmd.substring(index, last);
			Point p = (Point) parseShape(substr);
			UpdatePointCmd command = new UpdatePointCmd((Point) getSelectedShape(), p);
			execute(command);
		} else if (cmd.startsWith("UpdateLineCmd")) {
			int index = cmd.indexOf("newState={") + "newState={".length();
			int last = cmd.lastIndexOf("}");
			String substr = cmd.substring(index, last);
			Line l = (Line) parseShape(substr);
			UpdateLineCmd command = new UpdateLineCmd((Line) getSelectedShape(), l);
			execute(command);
		} else if (cmd.startsWith("UpdateRectangleCmd")) {
			int index = cmd.indexOf("newState={") + "newState={".length();
			int last = cmd.lastIndexOf("}");
			String substr = cmd.substring(index, last);
			Rectangle r = (Rectangle) parseShape(substr);
			UpdateRectangleCmd command = new UpdateRectangleCmd((Rectangle) getSelectedShape(), r);
			execute(command);
		} else if (cmd.startsWith("UpdateCircleCmd")) {
			int index = cmd.indexOf("newState={") + "newState={".length();
			int last = cmd.lastIndexOf("}");
			String substr = cmd.substring(index, last);
			Circle c = (Circle) parseShape(substr);
			UpdateCircleCmd command = new UpdateCircleCmd((Circle) getSelectedShape(), c);
			execute(command);
		} else if (cmd.startsWith("UpdateDonutCmd")) {
			int index = cmd.indexOf("newState={") + "newState={".length();
			int last = cmd.lastIndexOf("}");
			String substr = cmd.substring(index, last);
			Donut d = (Donut) parseShape(substr);
			UpdateDonutCmd command = new UpdateDonutCmd((Donut) getSelectedShape(), d);
			execute(command);
		} else if (cmd.startsWith("UpdateHexagonCmd")) {
			int index = cmd.indexOf("newState={") + "newState={".length();
			int last = cmd.lastIndexOf("}");
			String substr = cmd.substring(index, last);
			HexagonAdapter h = (HexagonAdapter) parseShape(substr);
			UpdateHexagonCmd command = new UpdateHexagonCmd((HexagonAdapter) getSelectedShape(), h);
			execute(command);
		}
		else if (cmd.startsWith("ShapeToBackCmd")) {
			this.toBack();
		} else if (cmd.startsWith("ShapeToFrontCmd")) {
			this.toFront();
		} else if (cmd.startsWith("BringShapeToBackCmd")) {
			this.bringToBack();
		} else if (cmd.startsWith("BringShapeToFrontCmd")) {
			this.bringToFront();
		} else if (cmd.startsWith("Undo")) {
			this.undo();
		} else if (cmd.startsWith("Redo")) {
			this.redo();
		} 
		
	}
	
	

	
	

	
}
