/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mde;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.File;

/**
 *
 * @author vicente
 */
public class OfferMultimediaBehaviour extends CyclicBehaviour {

  public OfferMultimediaBehaviour() {
  }

  public OfferMultimediaBehaviour(MultimediaSharerAgent multimediaSharerAgent) {
    super(multimediaSharerAgent);
  }

  @Override
  public void action() {
    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
    ACLMessage msg = myAgent.receive(mt);
    if(msg != null) {
      MultimediaSharerAgent multimediaSharerAgent = (MultimediaSharerAgent) myAgent;
      String multimediaName = msg.getContent();
      ACLMessage reply = msg.createReply();
      System.out.println("Multimedia Name Receive:"+multimediaName);
      File file = (File) multimediaSharerAgent.getCatalogue().get(multimediaName);
      if(file != null && file.exists()) {
        reply.setPerformative(ACLMessage.PROPOSE);
        reply.setContent("available"); //Aqui se puede mandar el tamano del archivo
      } else {
        reply.setPerformative(ACLMessage.REFUSE);
        reply.setContent("not-available");
      }
      myAgent.send(reply);
    } else {
      block();
    }
  }

}
