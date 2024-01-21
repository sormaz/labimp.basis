/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.basis;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author sormaz
 *
 */
public class IMPFileFilter extends FileFilter {
	String extension;
	String application;

	/**
	 * 
	 */
	public IMPFileFilter(String extension, String application) {
		// TODO Auto-generated constructor stub
		this.extension = extension;
		this.application = application;
	}

    /**
    *
    * @param file
    * @return
    */
   public boolean accept(File file) {

       String fileType = file.getName();
       if(file.isDirectory()){
           return true;
       }
       else if (fileType.endsWith("." + extension)){
           return true;
       }
       return false;
   }
   
   /**
    *
    *
    */
   public String getDescription() {
       return application + " files (." + extension +")";
   }

}
