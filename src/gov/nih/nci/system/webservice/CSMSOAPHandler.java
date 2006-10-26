package gov.nih.nci.system.webservice;

import gov.nih.nci.common.util.ClientInfo;
import gov.nih.nci.common.util.ClientInfoThreadVariable;
import gov.nih.nci.common.util.Constant;
import gov.nih.nci.system.server.mgmt.SecurityEnabler;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.SOAPEnvelope;


public class CSMSOAPHandler extends BasicHandler
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static SecurityEnabler securityEnabler = new SecurityEnabler(Constant.APPLICATION_NAME);

	public void invoke(MessageContext messageContext) throws AxisFault
	{
		if (securityEnabler.getSecurityLevel() > 0)
		{
            Message message = messageContext.getRequestMessage();
            SOAPEnvelope  envelope = message.getSOAPEnvelope();
            SOAPHeader header = null;
			try
			{
				header = envelope.getHeader();
			}
			catch (SOAPException e)
			{
				e.printStackTrace();
			}
			String userName = (String)header.getAttribute("username");
			String password = (String)header.getAttribute("password");
	
			ClientInfo clientInfo = new ClientInfo();
			clientInfo.setUserName(userName);
			clientInfo.setPassword(password);		
			ClientInfoThreadVariable.setClientInfo(clientInfo);
			
			return;
		}
	}

}
