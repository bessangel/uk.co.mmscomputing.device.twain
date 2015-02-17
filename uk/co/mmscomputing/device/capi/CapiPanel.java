package uk.co.mmscomputing.device.capi;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import uk.co.mmscomputing.util.metadata.*;
import uk.co.mmscomputing.device.capi.exception.*;

public class CapiPanel extends JPanel{

  private JPanel         profile;

  public CapiPanel(Metadata md)throws CapiInformation{
    setLayout(new BorderLayout());

    profile=new JPanel(new BorderLayout());

    JPanel p=new JPanel(new GridLayout(0,2));
    p.add(createCtrlGroup(md));
    p.add(profile);

    add(p,BorderLayout.NORTH);
    add(profile,BorderLayout.CENTER);
  }

  private JPanel createCtrlGroup(Metadata md)throws CapiInformation{
    int noofctrls=CapiEnumerator.getNoOfControllers();

    JPanel p=new JPanel();
    p.setLayout(new GridLayout(0,1));
    p.setBorder(new EtchedBorder());
   
    if(noofctrls>0){
      JRadioButton   rb;
      ButtonGroup    bg            = new ButtonGroup();
      JRadioButton[] rbsController = new JRadioButton[noofctrls];

      for(int i=0;i<noofctrls;i++){
        CapiController ctrl=CapiEnumerator.getController(i+1);

        JTree tree = new JTree(ctrl.getProfile().toTree());
        JScrollPane treeView = new JScrollPane(tree);

        rb=new JRadioButton(getRBAction(treeView,md,ctrl));
        bg.add(rb); p.add(rb);
        rbsController[i]=rb;
      }
      try{
        int ctrlid=md.getInt(CapiConstants.capiControllerID,1);
        rbsController[ctrlid-1].doClick();
      }catch(ArrayIndexOutOfBoundsException aiobe){
        rbsController[0].doClick();
      }
    }
    return p;
  }

  private Action getRBAction(final JScrollPane pane,final Metadata md, final CapiController ctrl){
    return new AbstractAction(ctrl.getName()){
      public void actionPerformed(ActionEvent ev){
        ctrl.update(md);

        profile.removeAll();
        profile.add(pane);
        profile.validate();
        profile.repaint();
      }
    };
  }
}