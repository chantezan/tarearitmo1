import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class GestorMemoriaSecundaria {
	int pageSize;
	long blocksUsed;
	
	public GestorMemoriaSecundaria() {
		blocksUsed = 0;
		pageSize = getDefaultSize();
	}
	
	public int getDefaultSize(){
		return 0;
	}
	
	public void deleteFile(String diskFilePath, long blockNumber){
		String num_s;
		num_s = Objects.toString(blockNumber, null);			
		Path diskFilePath2 = Paths.get(diskFilePath + "_"+num_s);
		try {
		    Files.delete(diskFilePath2);
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", diskFilePath2);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", diskFilePath2);
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		}
	}
	
	public void writeToDisk(String diskFilePath, long blockNumber, RTree tree){
		
		
		String num_s;
		num_s = Objects.toString(blockNumber, null);			
		String diskFilePath2 = diskFilePath + "_"+num_s;

		try(  PrintWriter out = new PrintWriter( diskFilePath2 )  ){
		    out.println( tree.toString() );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public RTree getFromDisk(String diskFilePath, long blockNumber){
		RTree tree = new RTree();
		
		
		String num_s;
		num_s = Objects.toString(blockNumber, null);			
		String diskFilePath2 = diskFilePath + "_"+num_s;
		try {
		    BufferedReader in = new BufferedReader(new FileReader("infilename"));
		    String str = in.readLine();
		    tree.convertFromString(str);
		    /*while ((str = in.readLine()) != null)
		        process(str);*/
		    in.close();
		} catch (IOException e) {
		}
		
		return tree;
	}

}
