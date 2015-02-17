package uk.co.mmscomputing.device.capi;

import java.io.*;
import uk.co.mmscomputing.device.capi.exception.*;

public class CapiEnumerator{

  static private int nobc=0;               //  number of b-channels
  static private CapiController[] ctrls=new CapiController[0];

  static public int getNoOfControllers(){ return ctrls.length;}
  static public int getNoOfBChannels(){ return nobc;}

  static public CapiController getController(int i)throws CapiInformation{ 
    try{
      return ctrls[i-1];
    }catch(ArrayIndexOutOfBoundsException aioobe){
      throw new CapiInformation(CapiEnumerator.class.getName()+".getController(int i):\n\tNo Capi Controller available.\n\tIndex = "+aioobe.getMessage());
    }
  }

  static public CapiController getController(CapiChannel channel)throws CapiInformation{ 
    return getController(channel.getCtrlId());
  }

  public CapiEnumerator(){
  }

  public String toString(){
    String s="";
    s+="CapiEnumerator.getNoOfControllers() = "+ctrls.length+"\n\n\n";
    for(int i=0;i<ctrls.length;i++){
      s+="CapiEnumerator.getController("+i+"):\n\n"+ctrls[i].toString()+"\n\n";
    }
    return s;
  }

  static{
    try{
      jcapi.checkInstalled();
      nobc=0;
      int noc=jcapi.getNoOfControllers();
      ctrls=new CapiController[noc];
      for(int i=0; i<noc; i++){
        ctrls[i]=new CapiController(i+1);         // capi starts counting at 1
        nobc+=ctrls[i].getNoOfBChannels();
      }
    }catch(Exception e){
      System.err.println(CapiEnumerator.class.getName()+"\n\t"+e.getMessage());
    }
  }
}