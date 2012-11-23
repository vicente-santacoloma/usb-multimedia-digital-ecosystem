/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mde;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vicente
 */
public class MultimediaSharerAgent extends Agent {
  
  private HashMap catalogue;
  private String targetMultimedia;
  private MultimediaSharerGUI myGUI;
  private ArrayList<AID> requestAgents;
  private AID [] sharerAgents;
  
  @Override
  protected void setup() {
    
    System.out.println("Hello! Multimedia-Sharer-Agent " + getAID().getName()+" is ready.\n");
    
    catalogue = new HashMap();
    requestAgents = new ArrayList<>();

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
    }
    
    addBehaviour(new OfferMultimediaBehaviour(this));
    
  }
  
  @Override
  protected void takeDown() {
    try {
      System.out.println("Taking Down! Multimedia-Sharer-Agent " + getAID().getName()+"\n");
      DFService.deregister(this);
    } catch (FIPAException ex) {
      ex.printStackTrace();
      Logger.getLogger(MultimediaSharerAgent.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private class RequestMultimedia extends Behaviour {

    private MessageTemplate mt;
    private int step = 0;
    //private boolean receive = false;
    
    @Override
    public void action() {
      
      switch(step) {
        
        case 0:
          ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
          for(int i = 0; i < sharerAgents.length; ++i) {
            cfp.addReceiver(sharerAgents[i]);
          }
          File file = new File("./Source/"+targetMultimedia);
          byte [] multimediaContent = null;
          try {
            FileInputStream fileInput = new FileInputStream(file);
            multimediaContent = new byte[(int)file.length()];
            fileInput.read(multimediaContent);
          } catch (IOException ex) {
            Logger.getLogger(MultimediaSharerAgent.class.getName()).log(Level.SEVERE, null, ex);
          }
          cfp.setByteSequenceContent(multimediaContent);
          cfp.addUserDefinedParameter("multimedia-name", targetMultimedia);
          System.out.println("I'm going to send the multimedia content");
          myAgent.send(cfp);
          mt = MessageTemplate.and(MessageTemplate.MatchConversationId("multimedia-sharing"),
				   MessageTemplate.MatchInReplyTo(cfp.getReplyWith())); 
          step = 1;
          break;
        case 1:
          ACLMessage reply = myAgent.receive(mt);
          if(reply != null) {
            
            if(reply.getPerformative() == ACLMessage.PROPOSE) {
              if("Multimedia-received".equals(reply.getContent())) {
                System.out.println("Multimedia successfully shared");
              }
            }
          }
          step = 2;
          break;
      }
    }

    @Override
    public boolean done() {
      return (step == 2);
    }
    
  }
  
  private class ShareMultimedia extends CyclicBehaviour {

    @Override
    public void action() {
      
      String fileName;
      byte [] multimediaContent;
      MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
      ACLMessage msg = myAgent.receive(mt);
      if (msg != null) {
        // CFP Message received. Process it
        fileName = msg.getUserDefinedParameter("multimedia-name");
        multimediaContent = msg.getByteSequenceContent();
        //int x = (Integer) msg.getContent();
        ACLMessage reply = msg.createReply();

        if (!"".equals(fileName)) {
          // The requested book is available for sale. Reply with the price
          try {

          FileOutputStream fileOutput = new FileOutputStream("./Destination/"+fileName);
          fileOutput.write(multimediaContent);
          } catch (Exception e) {
            e.printStackTrace();
          }
          reply.setPerformative(ACLMessage.PROPOSE);
          reply.setContent("Multimedia-received");
          System.out.println("Multimedia-received "+fileName);
        }
        else {
          // The requested book is NOT available for sale.
          reply.setPerformative(ACLMessage.REFUSE);
          reply.setContent("Multimedia-not-received");
          System.out.println("Multimedia-not-received");
        }
        System.out.println("I'm going to send a reply message");
        myAgent.send(reply);
      } else {
        block();
      }
    }
    
  }

  public boolean addFileCatalogue(File file) { 
    
    File f = (File) catalogue.get(file.getName());
    
    if(f != null) {
      if(!f.getAbsolutePath().equals(file.getAbsolutePath())) {
        catalogue.put(file.getName(), file);
        return true;
      }
    } else {
      catalogue.put(file.getName(), file);
      return true;
    }
    return false;
  }
  
  public void removeFileCatalogue(File file) { 
    //catalogue.put(file, file.getName());
  }
    
  // Getters and Setters

  public HashMap getCatalogue() {
    return catalogue;
  }

  public void setCatalogue(HashMap catalogue) {
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

  public ArrayList<AID> getRequestAgents() {
    return requestAgents;
  }

  public void setRequestAgents(ArrayList<AID> requestAgents) {
    this.requestAgents = requestAgents;
  }

  public AID[] getSharerAgents() {
    return sharerAgents;
  }

  public void setSharerAgents(AID[] sharerAgents) {
    this.sharerAgents = sharerAgents;
  }

}
