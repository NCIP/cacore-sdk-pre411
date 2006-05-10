<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" exclude-result-prefixes="xlink" version="2.0">
	<xsl:output method="html"/>
	<xsl:variable name="role">simple</xsl:variable>
	<xsl:variable name="recordCounter" select="/xlink:httpQuery/queryResponse/recordCounter"/>
	<xsl:variable name="start" select="/xlink:httpQuery/queryResponse/start"/>
	<xsl:variable name="end" select="/xlink:httpQuery/queryResponse/end"/>
	<xsl:template match="/">
	
	<html>
		<head>  
		   <link rel="stylesheet" type="text/css" href="/@XSLT_PATH@/styleSheet.css"/>		  
		</head>  

		<body>
	
	<!-- nci hdr begins -->
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="hdrBG">
        <tr>
          <td width="283" height="37" align="left">
          <a href="http://www.cancer.gov"><img alt="National Cancer Institute" src="/@XSLT_PATH@/images/logotype.gif" width="283" height="37" border="0"/></a>
          </td>          
          <td width="295" height="37" align="right">
          <a href="http://www.cancer.gov"><img alt="U.S. National Institutes of Health | www.cancer.gov" src="/@XSLT_PATH@/images/tagline.gif" width="295" height="37" border="0"/></a>
          </td>
        </tr>
      </table>
  
  <!-- nci hdr ends -->
  
  <!-- application hdr begins -->
  	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="subhdrBG">
  		<tr>
  		<td><font color="white" size="5"><b><h4>caCORE - HTTP INTERFACE</h4></b></font></td>
  		<td height="50" align="right">
  		<a href="http://cabio.nci.nih.gov/NCICB/infrastructure/cacore_overview">
  		<img src="/@XSLT_PATH@/images/cabio58.gif" alt="Application Logo" height="35" hspace="10" border="0"/></a>
  		<img src="/@XSLT_PATH@/images/cadsr.gif" alt="Application Logo" height="35" hspace="10" border="0"/>			
		<img src="/@XSLT_PATH@/images/evs.gif" alt="Application Logo" height="35" hspace="10" border="0"/>
		<img src="/@XSLT_PATH@/images/csm.gif" alt="Application Logo" height="35" hspace="10" border="0"/>
		</td>  		

  		</tr>
  	</table>
  <!-- application hdr ends -->		

		
			<font color="#737CA1" size="4">
			
			<HR/>
			</font>
			<xsl:apply-templates select="/xlink:httpQuery/queryRequest" mode="req"/>
			<xsl:apply-templates select="/xlink:httpQuery/queryResponse/class" mode="res"/>
			<br>
			<hr></hr>
				<table cellpadding="3" border="2">
					<tr>
						<xsl:apply-templates select="/xlink:httpQuery/queryResponse/previous" mode="previous"/>
						<xsl:apply-templates select="/xlink:httpQuery/queryResponse/next" mode="next"/>
						<xsl:apply-templates select="/xlink:httpQuery/queryResponse/pages" mode="page"/>
					</tr>
				</table>
				<hr></hr>
			</br>
<hr></hr>
<hr></hr>
<!-- footer begins -->
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="ftrTable">
        <tr>
          <td valign="top">
            <div align="center">
              <a href="http://www.cancer.gov/"><img src="/@XSLT_PATH@/images/footer_nci.gif" width="63" height="31" alt="National Cancer Institute" border="0"/></a>
              <a href="http://www.dhhs.gov/"><img src="/@XSLT_PATH@/images/footer_hhs.gif" width="39" height="31" alt="Department of Health and Human Services" border="0"/></a>
              <a href="http://www.nih.gov/"><img src="/@XSLT_PATH@/images/footer_nih.gif" width="46" height="31" alt="National Institutes of Health" border="0"/></a>
              <a href="http://www.firstgov.gov/"><img src="/@XSLT_PATH@/images/footer_firstgov.gif" width="91" height="31" alt="FirstGov.gov" border="0"/></a>
            </div>
          </td>
        </tr>
      </table>
  <!-- footer ends -->

		</body>
		</html>
	</xsl:template>
	<xsl:template name="request" match="/xlink:httpQuery/queryRequest" mode="req">
		<!--  -#003333 #99C68E #FDEEF4 -->
		<font color="#737CA1" size="4">
			<xsl:for-each select="/xlink:httpQuery/queryRequest">
				<table bgcolor="#98B7B7">
					<tr>
						<td>
							<font color="#003333" size="4">
		Criteria : 	<xsl:value-of select="/xlink:httpQuery/queryRequest/criteria"/>
								<br/>
							</font>
						</td>
					</tr>
					<tr>
						<td>
							<font color="#003333" size="4">
		Result class name: <xsl:value-of select="/xlink:httpQuery/queryRequest/query/class"/>
								<br/>
							</font>
						</td>
					</tr>
				</table>
				<hr size="2"/>
				<xsl:choose>
					<xsl:when test="$recordCounter > 0">
						<b>
							<u>Results:  <xsl:value-of select="$recordCounter"/> records found</u>
						</b>
						<b> (<xsl:value-of select="$start"/> - <xsl:value-of select="$end"/> records per page.)</b>
					</xsl:when>
					<xsl:otherwise>
						<b>
							<u>Results:  zero records found</u>
						</b>
					</xsl:otherwise>
				</xsl:choose>
				<br/>
				<br/>
			</xsl:for-each>
		</font>
	</xsl:template>
	<xsl:template name="response" match="/xlink:httpQuery/queryResponse/class" mode="res">
	
		<table border="2" style="table-layout:fixed" cellpadding="0" cellspacing="0" bgcolor="#FAF8CC">
<!--
<table class="dataTable" border="1">
-->

			<tbody>
				<xsl:call-template name="cacore">
					<xsl:with-param name="counter" select="@recordNumber"/>
					<xsl:with-param name="rec" select="@recordNumber"/>
				</xsl:call-template>
			</tbody>
		</table>
	</xsl:template>
	<xsl:template name="cacore">
		<xsl:param name="counter"/>
		<xsl:param name="rec"/>
		<xsl:choose>
			<xsl:when test="$counter = $rec">
				<xsl:if test="$counter = @recordNumber">
					<xsl:if test="@recordNumber = $start">
						<!--	<tr bgcolor="#ECD872">  -->
						<tr bgcolor="#E3E4FA">
							<xsl:for-each select="field">
								<th width="200">
									<xsl:value-of select="@name"/>
								</th>
							</xsl:for-each>
						</tr>
					</xsl:if>
					<tr align="left" valign="top">
						<xsl:for-each select="field">
							<td width="200" nowrap="off">
								<xsl:choose>
									<xsl:when test="$role = @xlink:type">
										<a href="{@xlink:href}">
											<xsl:value-of select="."/>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="."/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</xsl:for-each>
					</tr>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$counter > @recordNumber">
				</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="cacore">
							<xsl:with-param name="counter" select="$counter + 1"/>
							<xsl:with-param name="rec" select="following::node()/@recordNumber"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="previous" match="/xlink:httpQuery/queryResponse/previous" mode="previous">
		<xsl:for-each select="/xlink:httpQuery/queryResponse/previous">
			<td bgcolor="#E0FFFF">
				<font color="#25587E">
					<a href="{@xlink:href}">
						<xsl:value-of select="."/>
					</a>
				</font>
			</td>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="next" match="/xlink:httpQuery/queryResponse/next" mode="next">
		<xsl:for-each select="/xlink:httpQuery/queryResponse/next">
			<td bgcolor="#E0FFFF">
				<font color="#25587E">
					<a href="{@xlink:href}">
						<xsl:value-of select="."/>
					</a>
				</font>
			</td>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="page" match="/xlink:httpQuery/queryResponse/pages" mode="page">
		<xsl:choose>
			<xsl:when test="@count > 1">
				<xsl:for-each select="/xlink:httpQuery/queryResponse/pages/page">
					<td bgcolor="#E0FFFF">
						<font color="#25587E">
							<a href="{@xlink:href}">
								<xsl:value-of select="."/>
							</a>
						</font>
					</td>
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
