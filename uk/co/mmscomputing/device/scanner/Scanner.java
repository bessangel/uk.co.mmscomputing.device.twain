package uk.co.mmscomputing.device.scanner;

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.JLabel;

import java.lang.reflect.*;

//import uk.co.mmscomputing.device.twain.*;
//import uk.co.mmscomputing.device.sane.*;

abstract public class Scanner{

  static protected boolean installed=false;
  
  public abstract boolean isAPIInstalled();

  protected Vector              listeners=new Vector();           // list of scanner event listeners
  protected ScannerIOMetadata   metadata=null;                    // information structure

  public abstract void       select()throws ScannerIOException;
  public abstract String[]   getDeviceNames()throws ScannerIOException;
  public abstract void       select(String name)throws ScannerIOException;
  public abstract void       acquire()throws ScannerIOException;
  public abstract void       setCancel(boolean c)throws ScannerIOException;

  public void addListener(ScannerListener listener){
    listeners.add(listener);
  }

  public void removeListener(ScannerListener listener){
    listeners.remove(listener);
  }

  public void fireExceptionUpdate(Exception e){
    metadata.setException(e);
    fireListenerUpdate(metadata.EXCEPTION);
  }

  public void fireListenerUpdate(ScannerIOMetadata.Type type){
    for(Enumeration e = listeners.elements(); e.hasMoreElements() ;){
      ScannerListener listener=(ScannerListener)e.nextElement();
      listener.update(type,metadata);
    }
  }

  public JComponent getScanGUI(){
    return new JLabel("Dummy Scanner GUI");
  }

  public JComponent getScanGUI(int mode){
    return new JLabel("Dummy Scanner GUI");
  }

  static public Scanner getDevice(){
    String osname    = System.getProperty("os.name");
    String cn;
    if(osname.startsWith("Linux")){
      cn = "uk.co.mmscomputing.device.sane.SaneScanner";
    }else if(osname.startsWith("Windows")){
      cn = "uk.co.mmscomputing.device.twain.TwainScanner";
//    }else if(osname.startsWith("Mac")){
    }else{
      return null;
    }                                             // use reflection here, because allows jar files with/out
    try{                                          // twain and/or sane files
      Scanner scanner   = (Scanner)Class.forName(cn).newInstance();  
      if(scanner.isAPIInstalled()){return scanner;}
    }catch(IllegalAccessException iae){
      iae.printStackTrace();
    }catch(InstantiationException ie){
      ie.printStackTrace();
    }catch(ClassNotFoundException cnfe){
      cnfe.printStackTrace();
    }
    return null;
  }
}


