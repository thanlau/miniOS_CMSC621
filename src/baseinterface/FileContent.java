package baseinterface;

public class FileContent {
	private static final long serialVersionUID = 4466L;
	private String data;
	private String fileName;
	
	public FileContent(String filename){
		this.fileName = filename;
		data = new String();
	}

	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
