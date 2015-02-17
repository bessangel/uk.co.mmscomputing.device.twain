package uk.co.mmscomputing.device.sane.option;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;

import uk.co.mmscomputing.device.sane.*;

// not tested this, hp oj doesn't use this type of option

public class FixedListPanel extends DescriptorPanel{

  JComboBox[] combos=null;

  public FixedListPanel(FixedList od){
    super(od);

    String[] list=new String[od.list.length];
    for(int i=0;i<list.length;i++){
      list[i]=od.unfixstr(od.list[i]);
    }

    int len=od.size>>2;
    JPanel p=new JPanel();
    p.setLayout(new GridLayout(len,1));
    combos=new JComboBox[len];
    for(int i=0;i<len;i++){
      p.add(createPanel(i, list));
    }
    addValuePanel(new JScrollPane(p));

/*
    String valstr="Value";
    if(od.unit!=0){ valstr+=" in ["+SANE_UNIT[od.unit]+"]";}
    p.setBorder(new TitledBorder(valstr));
    add(new JScrollPane(p),BorderLayout.NORTH);
*/
  }

  private JPanel createPanel(int i, String[] list){
    JComboBox combo=new JComboBox(list);
    combo.setMinimumSize(new Dimension(200,100));
    combo.setMaximumRowCount(5);
    combo.addActionListener(this);
    checkCapabilities(combo);

    JPanel p=new JPanel();
    p.setLayout(new BorderLayout());
    p.add(combo);     

    combo.setSelectedItem(od.getStringValue(i));       

    combos[i]=combo;

    return p;
  }

  public void actionPerformed(ActionEvent e){
    JComboBox combo=(JComboBox)e.getSource();
    for(int i=0;i<combos.length;i++){
      if(combos[i]==combo){
        try{
          String str=combo.getSelectedItem().toString();
          str=od.setStringValue(i,str);
          combo.setSelectedItem(str);
        }catch(SaneIOException sioe){
          sioe.printStackTrace();
        }
      }
    }
  }
}
