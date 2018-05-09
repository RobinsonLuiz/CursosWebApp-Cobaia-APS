package cobaia.mvc;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ListingPackages {
	private Map<String, FileList> files = null;
    private ArrayList<String> classes = new ArrayList<>();
      
    public void printPathFileList(String initialPath) {
      	File file = new File(initialPath);
        generatePathFileList("", file);
        if (this.files != null) {
          	Iterator<String> it = this.files.keySet().iterator();
            while (it.hasNext()) {
              	String folderName = (String) it.next();
                Iterator<?> fileNamesIt = ((FileList) this.files.get(folderName)).getFilesNames().iterator();
                while (fileNamesIt.hasNext()) {
                  	String fileName = (String) fileNamesIt.next();
                   	getClasses().add(fileName);
                }
            }
        }
    }

    private void generatePathFileList(String folder, File file) {
    	String[] f = file.list();
        for (int x = 0; x < f.length; x++) {
        	File vfile = new File(file.getAbsolutePath() + "/" + f[x]);
            if (vfile.isDirectory()) {
            	generatePathFileList((folder.equals("") ? "" : 
            		folder.concat(".")) + f[x], vfile);
            } else {
            // is file
            	if (this.files == null) {
            		this.files = new TreeMap<String, FileList>();
                }
                if (this.files.get(folder) != null) {
                	((FileList) this.files.get(folder)).addFileName(f[x]);
                } else {
                	this.files.put(folder, new FileList(f[x]));
                }
            }
        }
    }

    public ArrayList<String> getClasses() {
    	return this.classes;
	}
}

 

