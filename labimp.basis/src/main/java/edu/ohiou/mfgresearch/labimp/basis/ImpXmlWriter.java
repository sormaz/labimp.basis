package edu.ohiou.mfgresearch.labimp.basis;

/**
 * <p>Title: Generic classes for manufacturing planning</p>
 * <p>Description: Thsi project implements general classes for intelligent manufacturing planning. These are:
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: Ohio University</p>
 * @author Dusan Sormaz
 * @version 1.0
 */
import java.io.*;

abstract public class ImpXmlWriter {

  public ImpXmlWriter() {
  }

	abstract public void  write (StringBuffer buffer, String indent);

	/**
 * Write the header for xml files that specifies xml version
 * @param buffer - buffer in which the header is to be written
 */
	public void writeXMLHeader (StringBuffer buffer) {
		buffer.append (
						"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
		buffer.append ("\n");
	}

	/**
	 * writes an error message into XML file telling that subclass needs
	 * to implement this method
	 * @param buffer - StringBuffer which accpets XML string
	 * @param indent - increment for indentation of XML string
	 */
//	public void write (StringBuffer buffer, String indent) {
//		buffer.append ("\n<error>handler " + this.toString() +
//									 "should implement writeXML method</error>");
//	}

	/**
	 *
	 * @param file
	 * @throws IOException
	 */
	public void saveXML (File file) throws IOException {

		saveXML(new BufferedWriter(new FileWriter(file)));
	}

	/**
	 *
	 * @param writer
	 * @throws IOException
	 */
	public void saveXML (BufferedWriter writer) throws IOException {
		StringBuffer buffer = new StringBuffer();
		this.writeXMLHeader(buffer);
		write(buffer, new String(""));
		writer.write(buffer.toString());
		writer.flush();
		writer.close();
	}
	public void saveXML (StringBuffer buffer) throws IOException {
		write(buffer, new String(""));
	}
}