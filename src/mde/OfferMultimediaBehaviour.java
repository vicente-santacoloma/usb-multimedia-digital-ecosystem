package mde;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vicente
 * @version 1.0
 * @since   2012-12-03
 */
public class OfferMultimediaBehaviour extends CyclicBehaviour {

  public OfferMultimediaBehaviour() {
  }

  public OfferMultimediaBehaviour(MultimediaSharerAgent multimediaSharerAgent) {
    super(multimediaSharerAgent);
  }

  /**
   * Offer the list of multimedia files associated to the request 
   * performed by an agent.
   */
  @Override
  public void action() {
    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
    ACLMessage msg = myAgent.receive(mt);
    if(msg != null) {
      MultimediaSharerAgent multimediaSharerAgent = (MultimediaSharerAgent) myAgent;
      String multimediaName = msg.getContent().replaceAll("", ".*").toLowerCase();
      ACLMessage reply = msg.createReply();
      System.out.println("Multimedia Name Receive:"+multimediaName);
      File file = null;
      ArrayList<File> files = new ArrayList<>();
      for(int i = 0; i < multimediaSharerAgent.getCatalogue().size(); ++i) {
        if((multimediaSharerAgent.getCatalogue().get(i).exists()) && 
                multimediaSharerAgent.getCatalogue().get(i).getName().
                replaceAll("[^A-Za-z0-9 ]", "").toLowerCase().matches(multimediaName)) {
          files.add(multimediaSharerAgent.getCatalogue().get(i));
        }
      }
      if(!files.isEmpty()) {
        reply.setPerformative(ACLMessage.PROPOSE);
        try {
          reply.setContentObject(files);
        } catch (IOException ex) {
          ex.printStackTrace();
          //Logger.getLogger(OfferMultimediaBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        //reply.setContent("available");
        
      } else {
        reply.setPerformative(ACLMessage.REFUSE);
        //reply.setContent("not-available");
      }
      myAgent.send(reply);
    } else {
      block();
    }
  }

}
