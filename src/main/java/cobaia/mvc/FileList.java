package cobaia.mvc;
import java.util.HashSet;
import java.util.Set;


public class FileList {
	private Set<String> filesNames = new HashSet<String>();

	public FileList(String f) {
		this.filesNames.add(f);
	}
	
	public void addFileName(String f) {
		this.filesNames.add(f);
	}

	public Set<String> getFilesNames() {
		return filesNames;
    }
}
        