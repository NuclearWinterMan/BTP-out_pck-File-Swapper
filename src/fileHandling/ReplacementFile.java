package fileHandling;

public class ReplacementFile extends AssetFile {

	private String name;
	
	public ReplacementFile(byte[] contents) {
		super(contents);
	}
	
	public ReplacementFile(byte[] contents, String name) {
		super(contents);
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
