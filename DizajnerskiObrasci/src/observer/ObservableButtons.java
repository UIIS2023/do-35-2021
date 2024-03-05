package observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableButtons {
	
	private boolean modifyEnabled;
	private boolean deleteEnabled;
	private boolean toFrontEnabled;
	private boolean bringToFrontEnabled;
	private boolean toBackEnabled;
	private boolean bringToBackEnabled;
	private boolean undoEnabled;
	private boolean redoEnabled;
	
	private PropertyChangeSupport propertyChangeSupport;

	public ObservableButtons() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.removePropertyChangeListener(pcl);
	}
	
	public void setEditEnabled(boolean editEnabled) {
		propertyChangeSupport.firePropertyChange("btnEdit", this.modifyEnabled, editEnabled);
		this.modifyEnabled = editEnabled;
	}
	
	public void setDeleteEnabled(boolean deleteEnabled) {
		propertyChangeSupport.firePropertyChange("btnDelete", this.deleteEnabled, deleteEnabled);
		this.deleteEnabled = deleteEnabled;
	}

	public void setToFrontEnabled(boolean toFrontEnabled) {
		propertyChangeSupport.firePropertyChange("btnToFront", this.toFrontEnabled, toFrontEnabled);
		this.toFrontEnabled = toFrontEnabled;
	}

	public void setBringToFrontEnabled(boolean bringToFrontEnabled) {
		propertyChangeSupport.firePropertyChange("btnBringToFront", this.bringToFrontEnabled, bringToFrontEnabled);
		this.bringToFrontEnabled = bringToFrontEnabled;
	}
	
	public void setToBackEnabled(boolean toBackEnabled) {
		propertyChangeSupport.firePropertyChange("btnToBack", this.toBackEnabled, toBackEnabled);
		this.toBackEnabled = toBackEnabled;
	}

	public void setBringToBackEnabled(boolean bringToBackEnabled) {
		propertyChangeSupport.firePropertyChange("btnBringToBack", this.bringToBackEnabled, bringToBackEnabled);
		this.bringToBackEnabled = bringToBackEnabled;
	}
	
	public void setUndoEnabled(boolean undoEnabled) {
		propertyChangeSupport.firePropertyChange("btnUndo", this.undoEnabled, undoEnabled);
		this.undoEnabled = undoEnabled;
	}

	public void setRedoEnabled(boolean redoEnabled) {
		propertyChangeSupport.firePropertyChange("btnRedo", this.redoEnabled, redoEnabled);
		this.redoEnabled = redoEnabled;
	}
}
