package edu.ohiou.mfgresearch.labimp.basis;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.StringTokenizer;
import java.lang.reflect.*;

public class DefaultImpHandler extends ImpXmlHandler {
  public DefaultImpHandler(ImpXmlReader reader) {
    super (reader);
  }

  public void processingInstruction (String target, String data)
  {
    reader.logParsing("processing instruction, target: " + target + ", data:" + data);
    if (target.equalsIgnoreCase("handler") ) {
      StringTokenizer tokenizer = new StringTokenizer (data,"=");
      tokenizer.nextToken();
      String handlerName = tokenizer.nextToken();
      String netName = handlerName.substring(1, handlerName.length() -1);
	  reader.logParsing("processing instruction, handlerName: " + handlerName);
	  reader.logParsing("processing instruction, net handlerName: " + netName);
	  try {
	    Class c = Class.forName(netName);
	    Class [] args = {ImpXmlReader.class};
	    Constructor constructor = c.getConstructor(args );
	    Object [] params = {reader};
	    ImpXmlHandler handler = (ImpXmlHandler) constructor.newInstance(params);

	    reader.addHandler(handler);
	    reader.getXMLReader().setContentHandler(handler);
	  }
	  catch (InvocationTargetException ex) {
	    ex.printStackTrace();
	  }
	  catch (IllegalAccessException ex) { ex.printStackTrace();
	  }
	  catch (NoSuchMethodException ex) { ex.printStackTrace();
	  }
	  catch (InstantiationException ex) { ex.printStackTrace();
	  }
	  catch (ClassNotFoundException ex) { ex.printStackTrace();
	  }
    }
  }



  public  void updateObjectChangeHandler() {

  }

public  void updateObjectChangeHandler(String task) {

  }
}
