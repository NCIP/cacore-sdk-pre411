<%@page contentType="text/html"%>
<HTML>
<HEAD>
<title>Criteria Page</title>
<link href="styleSheet.css" type="text/css" rel="stylesheet" />
</HEAD>
<BODY>
<!-- begin the logo for the application --> 
<table width="100%">
	<tr valign="top" align="center">
		<td>
 			<img src="images/sdk.gif" name="caCORE SDK" width="180" height="170" border="0">
 		</td>
 	</tr>
</table>
<!-- end of logo --> 

<%@ page import="gov.nih.nci.common.util.*,
				 java.lang.reflect.*,
				 java.util.*" %>
<br>
<font size="4" color="#737CA1"><center>Click on the class name link on the bottom left menu to start your search.
<br>For each search criteria, at least one field is required for the search.
<br>For date attribute, please use this syntax: <b>mm-dd-yyyy</b></center></font>
<br><br>
<% JSPUtils jspUtils= null;
List fieldNames = null, domainNames=new ArrayList();
String message = null, selectedSearchDomain=null;
String className = request.getParameter("klassName");
session.setAttribute("selectedDomain", className);

if(className != null)
{
	try
	{	
		jspUtils = JSPUtils.getJSPUtils(config);
		fieldNames = jspUtils.getAllFields(className);
		domainNames = jspUtils.getDomainNames();
		
	}
	catch(Exception ex){
		message=ex.getMessage();
	}
	%>
<!-- bgcolor="#FAF8CC" -->	
	<form method=post action="Result.jsp" target = "_blank" name=form1>
		<table align="center" border =0 cellspacing=2 cellpadding=2 bgcolor="#FAF8CC">
		<tr align="left" valign="top">
			<td>Selected Object:  </td>
			<td><%=className%></td>
		</tr>
			
		<% if(fieldNames != null)
		{%>
		<% for(int i=0; i<fieldNames.size(); i++)
		   {%>
			<%String attrName = (String)fieldNames.get(i);%>
		<tr align="left" valign="top">
			<td><%=attrName%></td>
			<td><input type=text name=<%=attrName%> > </td>
		</tr>
		  <%}%>
		<tr align="left" valign="top">
			<td>Search Object: </td>
			<td><SELECT NAME=searchObj >
			<% if(domainNames != null)
			   { if(!((String)domainNames.get(0)).equals("Please choose")) domainNames.add(0, "Please choose");
			   %>
			   		<%for(int i=0; i<domainNames.size(); i++)
			   		{%>
			   		<OPTION<% selectedSearchDomain = request.getParameter("searchObj");
			   				   if((selectedSearchDomain != null) && (domainNames.get(i).equals(selectedSearchDomain))) 
			   					{%> SELECTED <% } %> ><%=domainNames.get(i)%></OPTION>
			   		<%}%>
			   <%}%></SELECT></td>
		</tr>
		<tr><td></td>
			<td><INPUT TYPE=SUBMIT NAME=BtnSearch VALUE=Submit ></td>
		</tr>
		<%}// end if statement%>
		
		</table>		
		</form>
		
<%}%>



</BODY>
</HTML>