package uk.co.mmscomputing.device.sane.option;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;

import uk.co.mmscomputing.device.sane.*;
import uk.co.mmscomputing.device.sane.gui.*;

public class DescriptorPanel extends JPanel implements SaneConstants, ActionListener, ChangeListener{

  static public String descstr      = jsane.getResource("option.DescriptorPanel.descstr");
  static public String valstr       = jsane.getResource("option.DescriptorPanel.valstr");

  protected Descriptor od           = null;

  public DescriptorPanel(Descriptor od){
    this.od=od;
    setLayout(new BorderLayout());
    add(newDescriptionPanel(od.desc+"\n\n"+od.toString()),BorderLayout.CENTER);
  }

  private JComponent newDescriptionPanel(String desc){
    JTextArea ta=new JTextArea(desc);
    ta.setLineWrap(true);
    ta.setWrapStyleWord(true);
    JScrollPane sp=new JScrollPane(ta);
    JPanel p=new JPanel();
    p.setLayout(new BorderLayout());
    p.setBorder(new TitledBorder(descstr));
    p.add(sp);
    return p;
  }

  protected void addValuePanel(JComponent component){
    String valstr=this.valstr;
    JPanel p=new JPanel();
    p.setLayout(new BorderLayout());
    p.add(component,BorderLayout.CENTER);
    if(od.unit!=0){ valstr+=" in ["+SANE_UNIT[od.unit]+"]";}
    p.setBorder(new TitledBorder(valstr));
    add(p,BorderLayout.NORTH);
  }

  // call from Descriptor : PreviewPanel value has changed

  void signalNewValue(){
    System.err.println(getClass().getName()+".signalNewValue()\n    I shouldn't be here!");
  }

  void reloadOptions(){
    Container p=getParent();
    while(p!=null){
      if(p instanceof SaneAcquirePanel){
        ((SaneAcquirePanel)p).buildTab();
        break;
      }
      p=p.getParent();
    }
  }

  public void actionPerformed(ActionEvent e){}
  public void stateChanged(ChangeEvent e){}
  
  protected void checkCapabilities(JComponent cmp){
    if((od.cap&SANE_CAP_INACTIVE)==SANE_CAP_INACTIVE){
      cmp.setEnabled(false);
    }else{
      if((od.cap&SANE_CAP_SOFT_SELECT)==SANE_CAP_SOFT_SELECT){
        cmp.setEnabled(true);
      }else{
        cmp.setEnabled(false);
      }
    }
  }
}
