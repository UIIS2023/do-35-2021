package strategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import mvc.DrawingController;

public class LoadSaveLog implements LoadSaveStrategy {
	
	private DrawingController controller;
	
	public LoadSaveLog(DrawingController controller) {
		this.controller = controller;
	}

	@Override
	public String load(String path) {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String str = "";
			String line;
			while ((line = reader.readLine()) != null) {
				int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to execute this command?\n" + line,
						"Execute command", JOptionPane.YES_NO_OPTION);
				if (selectedOption == JOptionPane.YES_OPTION) {
					controller.executeCommandString(line);
				} else {
					return str;
				}
				str += line + '\n';
			}
			return str;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public void save(Object toSave, String path) {
		try (BufferedWriter bwriter = new BufferedWriter(new FileWriter(path))) {
			bwriter.write((String) toSave);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
