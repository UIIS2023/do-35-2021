package strategy;

public interface LoadSaveStrategy {
	public Object load(String path);
	public void save(Object toSave, String path);
}
