package edu.ohiou.mfgresearch.labimp.basis;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ImpXmlHandlerException extends SAXParseException {

  public ImpXmlHandlerException() {
    this ("Handler not defined");
  }

  public ImpXmlHandlerException(String message) {
    super(message, new LocatorImpl());
  }

}
