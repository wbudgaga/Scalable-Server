package cs455.scaling.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public  class TestFileWriter {
	private  Writer output;

	public TestFileWriter(String file){
		File testFile = new File(file);
		try {
			openFile(testFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openFile(File aFile)              throws FileNotFoundException, IOException {
	    if (aFile == null) {
	      throw new IllegalArgumentException("File should not be null.");
	    }
	    if (!aFile.exists()) {
	      throw new FileNotFoundException ("File does not exist: " + aFile);
	    }
	    if (!aFile.isFile()) {
	      throw new IllegalArgumentException("Should not be a directory: " + aFile);
	    }
	    if (!aFile.canWrite()) {
	      throw new IllegalArgumentException("File cannot be written: " + aFile);
	    }

	    //use buffering
	    output = new BufferedWriter(new FileWriter(aFile));
	  }
	public void add(String text){
      try {
		output.write( text );
      } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
	} 

}

