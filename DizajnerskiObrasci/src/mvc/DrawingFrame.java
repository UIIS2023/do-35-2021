package mvc;

import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;

import drawing.*;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.FlowLayout;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class DrawingFrame extends JFrame { 

	private JPanel contentPane;
	private DrawingView view = new DrawingView();
	private DrawingController controller;
	
	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public DrawingView getView() {
		return view;
	}

	private JToggleButton tglbtnPoint = new JToggleButton("Point");
	private JToggleButton tglbtnLine = new JToggleButton("Line");
	private JToggleButton tglbtnRectangle = new JToggleButton("Rectangle");
	private JToggleButton tglbtnCircle = new JToggleButton("Circle");
	private JToggleButton tglbtnDonut = new JToggleButton("Donut");

	private JToggleButton tglbtnSelect = new JToggleButton("Select");

	private final JButton btnUndo = new JButton("Undo");
	private final JButton btnRedo = new JButton("Redo");
	private final JToggleButton tglbtnHexagon = new JToggleButton("Hexagon");
	
	private JButton btnModify;
	private JButton btnDelete;
	private final JButton btnToFront = new JButton("To Front");
	private final JButton btnBringToFront = new JButton("Bring To Front");
	private final JButton btnToBack = new JButton("To Back");
	private final JButton btnBringToBack = new JButton("Bring To Back");
	
	
	private final JScrollPane scrollPane = new JScrollPane();

	
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnLog = new JMenu("Log");
	private final JMenuItem mntmLoad = new JMenuItem("Load");
	private final JMenuItem mntmSave = new JMenuItem("Save");
	private final JMenu mnDrawing = new JMenu("Drawing");
	private final JMenuItem mntmLoad_1 = new JMenuItem("Load");
	private final JMenuItem mntmSave_1 = new JMenuItem("Save");
	
	private final JButton btnOutlineColor = new JButton("Outline Color");
	private final JButton btnInnerColor = new JButton("Inner Color");
	private final JPanel panel = new JPanel();
	private final JTextArea logTextArea = new JTextArea();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrawingModel model = new DrawingModel();
					DrawingFrame frame = new DrawingFrame(); 
					frame.getView().setModel(model); 
					frame.setController(new DrawingController(model, frame)); 
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public DrawingFrame() {

		setTitle("IT-35/2021 Lazar Acimovic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 600);
		setResizable(true); 
		
		setJMenuBar(menuBar);
		
		menuBar.add(mnLog);
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.loadLog();
			}
		});
		
		mnLog.add(mntmLoad);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveLog();
			}
		});
		
		mnLog.add(mntmSave);
		
		menuBar.add(mnDrawing);
		mntmLoad_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.loadDrawing();
			}
		});
		
		mnDrawing.add(mntmLoad_1);
		mntmSave_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveDrawing();
			}
		});
		
		mnDrawing.add(mntmSave_1);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		view.setBackground(Color.WHITE);
		contentPane.add(view, BorderLayout.CENTER);
		view.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controller.mouseClicked(me);
			}
		});

		JPanel pnlNorth = new JPanel();
		pnlNorth.setBackground(Color.GREEN);
		contentPane.add(pnlNorth, BorderLayout.NORTH);
		
		pnlNorth.add(tglbtnSelect);

		pnlNorth.add(tglbtnPoint);
		pnlNorth.add(tglbtnLine);
		pnlNorth.add(tglbtnCircle);
		pnlNorth.add(tglbtnDonut);
		pnlNorth.add(tglbtnRectangle);

		ButtonGroup btnGroup = new ButtonGroup();

		btnGroup.add(tglbtnPoint);
		btnGroup.add(tglbtnLine);
		btnGroup.add(tglbtnCircle);
		btnGroup.add(tglbtnDonut);
		btnGroup.add(tglbtnRectangle);
		btnGroup.add(tglbtnHexagon);
		
		pnlNorth.add(tglbtnHexagon);
		
		btnOutlineColor.setBackground(Color.BLACK);
		btnOutlineColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setOutlineColor();
			}
		});
		
		pnlNorth.add(btnOutlineColor);
		btnInnerColor.setForeground(Color.BLACK);
		btnInnerColor.setBackground(Color.WHITE);
		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setInnerColor();
			}
		});
		
		pnlNorth.add(btnInnerColor);
		

		JPanel pnlSouth = new JPanel();
		contentPane.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setBackground(Color.GREEN);
		
		

		btnModify = new JButton("Edit");
		
		btnModify.setEnabled(false);
		
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (controller.getSelectedShape() != null) { 
					controller.modify();
				

				} else {
					JOptionPane.showMessageDialog(null, "Please, select shape!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnlSouth.add(btnModify);
		btnGroup.add(btnModify);

		btnDelete = new JButton("Delete");
		
		btnDelete.setEnabled(false);
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.delete();
				tglbtnSelect.setSelected(false);
			}
		});
		pnlSouth.add(btnDelete);
		btnGroup.add(btnDelete);
		
		btnUndo.setEnabled(false);
		
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		
		pnlSouth.add(btnUndo);
		
		btnRedo.setEnabled(false);
		
		
		
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		
		pnlSouth.add(btnRedo);
		
		btnToFront.setEnabled(false);
		
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toFront();
			}
		});
		
		pnlSouth.add(btnToFront);
		
		btnBringToFront.setEnabled(false);
		
		
		
		
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToFront();
			}
		});
		
		pnlSouth.add(btnBringToFront);
		
		btnToBack.setEnabled(false);
		
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.toBack();
			}
		});
		
		pnlSouth.add(btnToBack);
		
		btnBringToBack.setEnabled(false);
		
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToBack();
			}
		});
		
		pnlSouth.add(btnBringToBack);
		
		
		
		
		
		scrollPane.setPreferredSize(new Dimension(200, 100));

		contentPane.add(scrollPane, BorderLayout.WEST);
		scrollPane.setViewportView(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		panel.add(logTextArea);
		
		logTextArea.setEditable(false);

		view.repaint();
	}

	public DrawingView getPnlDrawing() {
		return view;
	}

	public void setPnlDrawing(DrawingView view) {
		this.view = view;
	}

	public JToggleButton getTglbtnPoint() {
		return tglbtnPoint;
	}

	public void setTglbtnPoint(JToggleButton tglbtnPoint) {
		this.tglbtnPoint = tglbtnPoint;
	}

	public JToggleButton getTglbtnLine() {
		return tglbtnLine;
	}

	public void setTglbtnLine(JToggleButton tglbtnLine) {
		this.tglbtnLine = tglbtnLine;
	}

	public JToggleButton getTglbtnRectangle() {
		return tglbtnRectangle;
	}

	public void setTglbtnRectangle(JToggleButton tglbtnRectangle) {
		this.tglbtnRectangle = tglbtnRectangle;
	}

	public JToggleButton getTglbtnCircle() {
		return tglbtnCircle;
	}

	public void setTglbtnCircle(JToggleButton tglbtnCircle) {
		this.tglbtnCircle = tglbtnCircle;
	}

	public JToggleButton getTglbtnDonut() {
		return tglbtnDonut;
	}

	public void setTglbtnDonut(JToggleButton tglbtnDonut) {
		this.tglbtnDonut = tglbtnDonut;
	}

	public JToggleButton getTglbtnSelect() {
		return tglbtnSelect;
	}

	public void setTglbtnSelect(JToggleButton tglbtnSelect) {
		this.tglbtnSelect = tglbtnSelect;
	}
	
	public JToggleButton getTglbtnHexagon() {
		return tglbtnHexagon;
	}
	
	public JButton getBtnModify() {
		return btnModify;
	}
	public JButton getBtnRedo() {
		return btnRedo;
	}
	public JButton getBtnDelete() {
		return btnDelete;
	}
	public JButton getBtnUndo() {
		return btnUndo;
	}
	public JButton getBtnBringToBack() {
		return btnBringToBack;
	}
	public JButton getBtnToBack() {
		return btnToBack;
	}
	public JButton getBtnBringToFront() {
		return btnBringToFront;
	}
	public JButton getBtnToFront() {
		return btnToFront;
	}
	
	public JTextArea getLogTextArea() {
		return logTextArea;
	}
	
	public JButton getBtnInnerColor() {
		return btnInnerColor;
	}
	public JButton getBtnOutlineColor() {
		return btnOutlineColor;
	}

}
