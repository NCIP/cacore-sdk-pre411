import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class WSTestClient
{

	public static void main(String [] args) throws Exception {

		Service  service = new Service();
		Call     call    = (Call) service.createCall();

		/***************************************************************************************************************/

		QName qnBank = new QName("urn:childwithassociation.inheritance.domain.cacoresdk.nci.nih.gov", "Bank");
		call.registerTypeMapping(Bank.class, qnBank,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(Bank.class, qnBank),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(Bank.class, qnBank));

		/****************************************************************************************************************/

		String url = "http://@WEB_SERVER_HOST_NAME@:@WEB_SERVER_PORT@/@PROJECT_NAME@/ws/@WEBSERVICE_NAME@";

		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("@WEBSERVICE_NAME@", "queryObject"));
		call.addParameter("arg1", org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("arg2", qnBank, ParameterMode.IN);
		call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_ARRAY);

		gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank bank = new gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank();


		try {
			Object[] resultList = (Object[])call.invoke(new Object[] { "gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank", bank });

			System.out.println("Total objects found: " + resultList.length);

			if (resultList.length > 0) {
				for(int resultIndex = 0; resultIndex < resultList.length; resultIndex++) {
					Bank returnedBank = (Bank)resultList[resultIndex];
					System.out.println(
						"\tID " + returnedBank.getId() + "\n" +
						"\tName " + returnedBank.getName() + "\n" +
						"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
