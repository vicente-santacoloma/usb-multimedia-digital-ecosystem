package mde;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  vicente
 * @version 1.0
 * @since   2012-12-03
 */
public class MultimediaSharerAgent extends Agent {
  
  private ArrayList<File> catalogue;
  private String targetMultimedia;
  private MultimediaSharerGUI myGUI;
  private HashMap requestAgents;
  private HashMap requestCatalogue;
  private AID [] sharerAgents;
  
  /**
   * Initializes the agent.
   * 
   */
  @Override
  protected void setup() {
    
    System.out.println("Hello! Multimedia-Sharer-Agent " + getAID().getName()+" is ready.\n");
    catalogue = new ArrayList<>();
    requestAgents = new HashMap();
    requestCatalogue = new HashMap();

    MultimediaSharerGUI.main(this);
        
    DFAgentDescription dfd = new DFAgentDescription();
    dfd.setName(getAID());
    ServiceDescription sd = new ServiceDescription();
    sd.setType("multimedia-sharing");
    sd.setName("JADE-multimedia-sharing");
    dfd.addServices(sd);
    try {
      DFService.register(this, dfd);
    } catch (FIPAException fe) {
      fe.printStackTrace();
      //Logger.getLogger(MultimediaSharerAgent.class.getName()).log(Level.SEVERE, null, fe);
    }
    
    addBehaviour(new OfferMultimediaBehaviour(this));
    
    addBehaviour(new ShareMultimediaBehaviour(this));
    
  }
  
  /**
   * Deregister the agent from the DFService.
   * 
   */
  @Override
  protected void takeDown() {
    try {
      System.out.println("Taking Down! Multimedia-Sharer-Agent " + getAID().getName()+"\n");
      DFService.deregister(this);
    } catch (FIPAException ex) {
      ex.printStackTrace();
      //Logger.getLogger(MultimediaSharerAgent.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Add a file to the agent catalog.
   * 
   * @param file the file to add.
   * @return true if the file is added. 
   *          false otherwise.
   */
  public boolean addFileCatalogue(File file) { 
    
    File f = null;
    
    for(int i = 0; i < catalogue.size(); ++i) {
      if(file.getName().equals(catalogue.get(i).getName())) {
        f = (File) catalogue.get(i);
      }
    }

    if(f != null) {
      if(!f.getAbsolutePath().equals(file.getAbsolutePath())) {
        catalogue.add(file);
        return true;
      }
    } else {
      catalogue.add(file);
      return true;
    }
    return false;
  }
      
  // Getters and Setters

  public ArrayList<File> getCatalogue() {
    return catalogue;
  }

  public void setCatalogue(ArrayList<File> catalogue) {
    this.catalogue = catalogue;
  }

  public String getTargetMultimedia() {
    return targetMultimedia;
  }

  public void setTargetMultimedia(String targetMultimedia) {
    this.targetMultimedia = targetMultimedia;
  }

  public MultimediaSharerGUI getMyGUI() {
    return myGUI;
  }

  public void setMyGUI(MultimediaSharerGUI myGUI) {
    this.myGUI = myGUI;
  }

  public HashMap getRequestAgents() {
    return requestAgents;
  }

  public void setRequestAgents(HashMap requestAgents) {
    this.requestAgents = requestAgents;
  }
  
  public HashMap getRequestCatalogue() {
    return requestCatalogue;
  }

  public void setRequestCatalogue(HashMap requestCatalogue) {
    this.requestCatalogue = requestCatalogue;
  }

  public AID[] getSharerAgents() {
    return sharerAgents;
  }

  public void setSharerAgents(AID[] sharerAgents) {
    this.sharerAgents = sharerAgents;
  }

}
