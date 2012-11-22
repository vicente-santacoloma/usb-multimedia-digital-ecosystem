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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 *
 * @author vicente
 */
public class MultimediaSharerAgent extends Agent {
  
  private HashMap catalogue;
  //private ArrayList targetMultimedia;
  private String targetMultimedia;
  private MultimediaSharerGUI myGUI;
  private AID[] sharerAgents;
  
  public String getTargetM(){
      return targetMultimedia;
  }
  
  public void setTargetM(String t){
      targetMultimedia = t;
  }
  
   
  @Override
  protected void setup() {
    
    System.out.println("Hello! Multimedia-Sharer-Agent "+getAID().getName()+" is ready.\n");
    
    catalogue = new HashMap();
    //targetMultimedia = new ArrayList();
    
    myGUI = new MultimediaSharerGUI();
    
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        myGUI.setVisible(true);
      }
    });
    myGUI.setMyAgent(this);
    
    
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
   // Object [] args = getArguments();
    //if(args != null && args.length > 0) {
      targetMultimedia = getTargetM();
      System.out.println("The target multimedia is "+targetMultimedia);
      /*
      for(int i = 0; i < args.length; ++i) {
        targetMultimedia.add((String) args[i]);
        System.out.println("The multimedia "+i+" to request is "+(String) args[i]);
      }
      */
      
      addBehaviour(new OneShotBehaviour() {

        @Override
        public void action() {
          
          System.out.println("Trying to share "+targetMultimedia);
          DFAgentDescription template = new DFAgentDescription();
          ServiceDescription sd = new ServiceDescription();
          sd.setType("multimedia-sharing");
          template.addServices(sd);
          try {
            DFAgentDescription[] result = DFService.search(myAgent, template); 
            System.out.println("Found the following receiver agents:");
            sharerAgents = new AID[result.length];
            for (int i = 0; i < result.length; ++i) {
              sharerAgents[i] = result[i].getName();
              System.out.println(sharerAgents[i].getName());
            }
          }
          catch (FIPAException fe) {
            fe.printStackTrace();
          }

          // Perform the request
          myAgent.addBehaviour(new RequestMultimedia());
        }      
      });
   // }
    addBehaviour(new ShareMultimedia());
    
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
  
  /**
   * This is invoked by the GUI when the user adds a new file
   */
  public void updateCatalogue(final String path, final String name) {
    addBehaviour(new OneShotBehaviour() {
      public void action() {
        catalogue.put(name, path);
        System.out.println(name+" inserted into catalogue.\n");
      }
    } );
  }

}
