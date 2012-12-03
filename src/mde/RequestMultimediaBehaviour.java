package mde;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vicente
 * @version 1.0
 * @since   2012-12-03
 */
public class RequestMultimediaBehaviour extends Behaviour {
  
  private AID targetAgent;
  private File targetFile;
  private MessageTemplate mt;
  private int step = 0;

  public RequestMultimediaBehaviour() {
  }
  
  public RequestMultimediaBehaviour(Agent a) {
    super(a);
  }

  public RequestMultimediaBehaviour(AID targetAgent, File targetFile, Agent a) {
    super(a);
    this.targetAgent = targetAgent;
    this.targetFile = targetFile;
  }

  /**
   * Performed a request to the choice file and agent.
   * 
   * First it send a message containing the multimedia target file. Second it 
   * receive the multimedia content.
   */
  @Override
  public void action() {
    
    MultimediaSharerAgent multimediaSharerAgent = (MultimediaSharerAgent) myAgent;
    switch(step) {
      case 0:
        ACLMessage request = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        request.addReceiver(targetAgent);
        //request.setContent(multimediaSharerAgent.getTargetMultimedia());
        request.setContent(targetFile.getName());
        System.out.println("Request Multimedia Content: "+request.getContent());
        request.setConversationId("multimedia-trade");
        request.setReplyWith("request" + System.currentTimeMillis());
        myAgent.send(request);
        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("multimedia-trade"),
			          MessageTemplate.MatchInReplyTo(request.getReplyWith()));
        ++step;
      case 1:
        ACLMessage reply = myAgent.receive(mt);
        if(reply != null) {
          
          if(reply.getPerformative() == ACLMessage.INFORM) {
            try {
              byte [] multimediaContent = reply.getByteSequenceContent();
              FileOutputStream fileOutput = 
                      new FileOutputStream("./MDE Downloads/" + targetFile.getName());
              fileOutput.write(multimediaContent);
            } catch (IOException ex) {
              ex.printStackTrace();
              //Logger.getLogger(RequestMultimediaBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
          } else {
            System.out.println("Attempt failed: " + reply.getContent());
          }
          ++step;  
        }
    }
  }

  /**
   * Evaluate the status of the request behavior.
   * 
   * @return true if the agent has finished executing the request behaviour. 
   *          false otherwise.
   */
  @Override
  public boolean done() {
    return (step == 2);
  }
  
  //Getters and Setters

  public AID getTargetAgent() {
    return targetAgent;
  }

  public void setTargetAgent(AID targetAgent) {
    this.targetAgent = targetAgent;
  }

  public File getTargetFile() {
    return targetFile;
  }

  public void setTargetFile(File targetFile) {
    this.targetFile = targetFile;
  }

  public Agent getMyAgent() {
    return myAgent;
  }

  public void setMyAgent(Agent myAgent) {
    this.myAgent = myAgent;
  }
   
}
