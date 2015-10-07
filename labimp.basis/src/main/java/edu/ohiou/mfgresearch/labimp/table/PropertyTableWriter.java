package edu.ohiou.mfgresearch.labimp.table;

import java.util.*;

import edu.ohiou.mfgresearch.labimp.basis.ImpXmlWriter;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class PropertyTableWriter extends ImpXmlWriter {
  PropertyTable propertyTable;


  public PropertyTableWriter(PropertyTable table) {
    propertyTable = table;
  }
  public void write(StringBuffer buffer, String tab) {
    /**@todo Implement this edu.ohiou.labimp.basis.ImpXmlWriter abstract method*/

    Hashtable table = propertyTable.getToleranceHashTable();
    Set keySet = table.keySet();
    for (Iterator itr = keySet.iterator(); itr.hasNext(); )
    {
      String key = (String)itr.next();
//      System.out.println("the key value is "+key);
//      double value = ((Double)table.get(key)).doubleValue();
      buffer.append("<Parameter "+key+"=\""+table.get(key)+"\"/>\n");
//      System.out.println("the buffer "+buffer.toString());
    }

  }

}