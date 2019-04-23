package edu.ohiou.mfgresearch.labimp.basis;

/**
 * Title:        Generic classes for manufacturing planning
 * Description:  Thsi project implements general classes for intelligent manufacturing planning. These are:
 * ImpObject - umbrella class fro all objects
 * MrgPartModel - general part object data
 * Viewable - interface to display objects in applet
 * GUIApplet - applet that utilizes viewable interface
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Dusan Sormaz
 * @version 1.0
 */
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class ImpHandler is top level class for handling XML
 * for a given object and using given ImpXmlReader
 */
abstract public class ImpXmlHandler
    extends DefaultHandler {

  protected ImpXmlReader reader;

  public ImpXmlHandler(ImpXmlReader inReader) {
    reader = inReader;
  }

  public abstract void updateObjectChangeHandler();

  public abstract void updateObjectChangeHandler(String task);

  /**
   *
   * @param localName
   * @param qName
   * @return
   */
  public String verifyName(String localName, String qName) {
    if (localName == null || localName.equalsIgnoreCase(""))
      return qName;
    else
      return localName;
  }

}
