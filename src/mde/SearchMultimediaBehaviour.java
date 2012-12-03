/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mde;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 *
 * @author vicente
 */
  public class SearchMultimediaBehaviour extends Behaviour {

    private MultimediaRequestGUI multimediaRequestGUI;
    private MessageTemplate mt;
    private int step = 0;
    private int repliesCnt = 0;

    public SearchMultimediaBehaviour() {
    }

    public SearchMultimediaBehaviour(MultimediaSharerAgent multimediaSharerAgent) {
      super(multimediaSharerAgent);
    }

    public SearchMultimediaBehaviour(MultimediaRequestGUI multimediaRequestGUI, 
            MultimediaSharerAgent multimediaSharerAgent) {
      super(multimediaSharerAgent);
      this.multimediaRequestGUI = multimediaRequestGUI;
    }

    @Override
    public void action() {
      
      MultimediaSharerAgent multimediaSharerAgent = (MultimediaSharerAgent) myAgent;
      switch(step) {
        case 0:
          // Update the list of sharer agents
          DFAgentDescription template = new DFAgentDescription();
          ServiceDescription sd = new ServiceDescription();
          sd.setType("multimedia-sharing");
          template.addServices(sd);
          try {
            DFAgentDescription[] result = DFService.search(myAgent, template); 
            System.out.println("Found the following agents:");
            System.out.println("result length: " + result.length);
            AID sharerAgents [] = new AID[result.length - 1];
            int k = 0;
            for(int i = 0; i < result.length; ++i) {
              if(!myAgent.getAID().equals(result[i].getName())) {
                sharerAgents[k] = result[i].getName();
                System.out.println(sharerAgents[k].getName());
                ++k;
              }
            }
            multimediaSharerAgent.setSharerAgents(sharerAgents);
          }
          catch (FIPAException fe) {
            fe.printStackTrace();
          }
          ++step;
          break;
        case 1:
          // Send the cfp to all sellers
          ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
          AID sharerAgents [] = multimediaSharerAgent.getSharerAgents();
          for(int i = 0; i < sharerAgents.length; ++i) {
            cfp.addReceiver(sharerAgents[i]);
          }
          cfp.setContent(multimediaSharerAgent.getTargetMultimedia()); // Put hear the file name to request
          cfp.setConversationId("multimedia-trade");
          cfp.setReplyWith("cfp"+System.currentTimeMillis());
          myAgent.send(cfp);
          mt = MessageTemplate.and(MessageTemplate.MatchConversationId("multimedia-trade"),
				   MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
          ++step;
          break;
        case 2:
          // Receive all proposals/refusals from seller agents
	  ACLMessage reply = myAgent.receive(mt);
          if(reply != null) {
            // Reply received
            //System.out.println(reply.getContent());
            if(reply.getPerformative() == ACLMessage.PROPOSE) {
              //multimediaSharerAgent.getRequestAgents().add(reply.getSender());
              multimediaSharerAgent.getRequestAgents().put(reply.getSender().getName(), reply.getSender());
              try {
                multimediaSharerAgent.getRequestCatalogue().put(reply.getSender().getName(), reply.getContentObject());
              } catch (UnreadableException ex) {
                ex.printStackTrace();
                //Logger.getLogger(SearchMultimediaBehaviour.class.getName()).log(Level.SEVERE, null, ex);
              }
            }
            ++repliesCnt;
            if(repliesCnt >= multimediaSharerAgent.getSharerAgents().length) {
              ++step;
              multimediaRequestGUI.drawAgents(multimediaSharerAgent);
              //multimediaSharerAgent
            }
          } else {
            block();
          }
          break;
      }
    }

    @Override
    public boolean done() {
      return (step == 3);
    }

  }
