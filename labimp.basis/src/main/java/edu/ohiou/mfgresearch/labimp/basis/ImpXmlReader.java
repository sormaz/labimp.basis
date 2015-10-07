package edu.ohiou.mfgresearch.labimp.basis;

/**
 * Title:        Generic classes for manufacturing planning
 * Description:  This project implements general classes for intelligent manufacturing planning. These are:
 * ImpObject - umbrella class fro all objects
 * MrgPartModel - general part object data
 * Viewable - interface to display objects in applet
 * GUIApplet - applet that utilizes viewable interface
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Dusan Sormaz
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.*;

//import edu.ohiou.implanner.resources.MfgSystem;

public class ImpXmlReader extends DefaultHandler {
//  private String parserClassName = "com.sun.xml.parser.Parser";
//  private String fname;
  private SAXParser saxParser;
  private Stack handlers;
  private Collection objects = new LinkedList();
  private PrintStream logStream;
  //private SaxFeatureHandler handler;
  static SAXParserFactory spf;
  static {
    spf = SAXParserFactory.newInstance();
    spf.setValidating(false);
  }

  public ImpXmlReader() throws Exception {

    this(null);
  }

  public ImpXmlReader(PrintStream outStream) throws Exception {

    saxParser = spf.newSAXParser();
    handlers = new Stack();
    logStream = outStream;
  }

  public void parseXmlStream(InputSource inStr, ContentHandler handler) {
    this.logParsing("in parse xml stream: start parsing stream " + inStr + " using handler " +
		    handler);
    try {
      XMLReader xmlReader = saxParser.getXMLReader();
      logParsing("input source is " + inStr.toString());
      xmlReader.setContentHandler(handler);
      xmlReader.parse(inStr);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void parseXmlStream(InputSource inStr) {
    parseXmlStream (inStr, new DefaultImpHandler (this));
  }

  public void processingInstruction(String target, String data) throws
      SAXException {
    logParsing("processing instruction, target: " + target + ", data:" + data);
    try {
      if (target.equalsIgnoreCase("javaHandler") ) {
        Class c = Class.forName(data);
        ImpXmlHandler handler = (ImpXmlHandler) c.newInstance();
        XMLReader xmlReader = saxParser.getXMLReader();
        logParsing("handler read from xml file: " + handler.toString());
        xmlReader.setContentHandler(handler);
//        xmlReader.parse(inStr);

      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void startElement(String uri, String localName, String qName,
                           Attributes attributes) throws SAXException {
    XMLReader xmlReader = saxParser.getXMLReader();
     ContentHandler handler = xmlReader.getContentHandler();
     if (handler == this) {
       logParsing("handler was not read from xml file, nothing will be parsed" );
       this.fatalError(new SAXParseException("handler not defined", new LocatorImpl ()));

     }

  }


  XMLReader getParser(String classname) throws Exception,
      IllegalAccessException,
      InstantiationException {
    return org.xml.sax.helpers.XMLReaderFactory.createXMLReader(classname);
  }

  public void initializeHandlerStack() {
    handlers = new Stack();
  }

  public XMLReader getXMLReader() {
    try {
      return saxParser.getXMLReader();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public void addHandler(ContentHandler h) {
    this.logParsing("adding handler " + h);
    handlers.push(h);
  }

  public ContentHandler getNextHandler() {
    ContentHandler h = (ContentHandler) handlers.pop();
    logParsing("passing next handler " + h);
    return h;
  }

  public void addObject(Object o) {
    logParsing("adding object to list: " + o);
    objects.add(o);
  }

  public void clearObjects() {
    logParsing("clearing object list");
    objects.clear();
  }

  public Collection getObjects() {
    return objects;
  }

  public Object getObject() {
    if (objects.size() > 0) {
      return objects.iterator().next();
    }
    else
      return null;
  }

  public void logParsing(String message) {
    if (logStream != null)
      logStream.println(message);
  }

  public static void main(String[] args) {
    try {
//      FileInputStream xmlFile= new FileInputStream ("C:/LSLOT_F5_2_data.xml");
      FileInputStream xmlFile = new FileInputStream(
	  "C:/Documents and Settings/Dusan Sormaz/My Documents/sormaz/testhandler.xml");
      ImpXmlReader converter = new ImpXmlReader(System.out);
      converter.parseXmlStream(new InputSource (xmlFile), new DefaultImpHandler(converter));//, MfgSystem.getSAXHandler (converter, null) );
      System.out.println("objects" + converter.getObjects());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  } // main

public void popNextHandler() throws ImpHandlerException {
	ContentHandler handler = getNextHandler();
	if (handler != null)
	{
	    try
		{
		getXMLReader().setContentHandler(handler);
		return;
	    }
	    catch (Exception ex)
	    {
	    ex.printStackTrace();
	    }
	}
	else {
		throw new ImpHandlerException("Something wrong, handlers and tags are not in sync!");
	}
}
} // class ImpXmlReader
