package gov.nih.nci.common.util;

import java.util.*;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.lang.reflect.*;
import org.apache.log4j.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
/**
 *
 * @author LeThai
 **/
public class SVGManipulator
{
    /**
    * A string representation of the SVG Diagram
    */
    private String svgString;
    /**
	* The DOM document represented an SVG Diagram
	*/
    private Document svgDiagram;
    /**
	* The DOM document represented an SVG Diagram
	*/
    private Document originalSvg;

    /**
     * The SVG file name of this pathway diagram SVG.
     */
    private String name;

    private String GENE_INFO_STRING;

    private static Logger log = Logger.getLogger(SVGManipulator.class.getName());

     /**
	 * Constructor
	 */
	 public SVGManipulator() throws Exception
	 {
         loadProperties();
	 }

	 /**
	 * Constructor - accepts pathways as an object, use reflection to get its name and svgString through getName and getDiagram methods respectively.
	 * It also converts the svgString into svgDiagram document.
	 * @param pathway Object
	 */
	 public SVGManipulator(Object pathway) throws Exception
	 {

		try
		{
			loadProperties();
		    // Use refection to get information from pathway object and invoke its methods.
			Class cls = pathway.getClass();
			Class[] parameterTypes = new Class[] {};
			Object[] arguments = new Object[] {};

			 if( pathway != null)
			 {
				 Method getDiagram = cls.getMethod("getDiagram", parameterTypes);
				 this.svgString = (String)getDiagram.invoke(pathway, arguments);
				 Method getName = cls.getMethod("getName", parameterTypes);
				 this.name = (String)getName.invoke(pathway, arguments);
				 Document svgDoc = getSvgDiagram();
			 }
		 }
         catch (NoSuchMethodException e)
         {
	          log.error("SVGManipulator - NoSuchMethodException: " + e.getMessage());
	          throw new Exception("SVGManipulator - NoSuchMethodException: " + e.getMessage());

		 }
         catch (IllegalArgumentException e)
         {
			log.error("SVGManipulator - IllegalArgumentException: " + e.getMessage());
			throw new Exception("SVGManipulator - IllegalArgumentException: " + e.getMessage());


		 }
         catch (IllegalAccessException e)
         {
			log.error("SVGManipulator - IllegalAccessException: " + e.getMessage());
			throw new Exception("SVGManipulator - IllegalAccessException: " + e.getMessage());

		 }
		 catch (InvocationTargetException e)
         {
	          //System.out.println("InvocationTargetException: " + e.getMessage());
	          log.error("SVGManipulator - InvocationTargetException: " + e.getMessage());
	          throw new Exception("SVGManipulator - InvocationTargetException: " + e.getMessage());

         }
		 catch(Exception e)
		 {
		     log.error("SVGManipulator - Exception: " + e.getMessage());
		     throw new Exception("SVGManipulator - Exception: " + e.getMessage());

		 }
	 }


	 /**
	  * setGeneInfoLocation - replaces default GENE_INFO_STRING with geneInfoLocation.
	  * By default, the value of GENE_INFO_STRING is populated with value in CORESystem.properties file.
	  * This is the url for Genes within the svg document.
	  * @param geneInfoLocation
	  */
	 public void setGeneInfoLocation(String geneInfoLocation) throws Exception
	 {
 		// check if svg has been changed

		 String svgString = getSvgString();
		 if (svgString==null) return; // nothing to do
		 this.svgString = replaceString(svgString, GENE_INFO_STRING, geneInfoLocation);
		 if (svgDiagram!=null)
         {
			// set the current parsed document to null and reparse the string
			this.svgDiagram=null;
			this.svgDiagram=getSvgDiagram();
        }

	 }

	 /**
	 * disableAllGenes - go through the svg document, look for genes, and disable them.
	 */
	 public void disableAllGenes() throws Exception
	 {
        Document svg = getSvgDiagram();
        if ( svg == null ) return;
        // go through svg document, look for genes and disable them
        NodeList svgnodes=svg.getDocumentElement().getChildNodes();
        for (int i=0; i<svgnodes.getLength(); i++ )
        {
            // examine g nodes only
            if ( !svgnodes.item(i).getNodeName().equals("g" ))continue;
            // process list of "g" child nodes
            NodeList kids=svgnodes.item(i).getChildNodes();
            for (int j=0; j<kids.getLength(); j++ )
            {
                Node node=kids.item(j);
                // only interested in node if it's an element
                if ( node.getNodeType() == Node.ELEMENT_NODE )
                {
                    String name=node.getNodeName();
                    // get list of nodes for "g" child node
                    NodeList gkids=node.getChildNodes();
                    for ( int k=0; k<gkids.getLength(); k++)
                    {
                        Node gknode=gkids.item(k);
                        if ( gknode.getNodeType() == Node.ELEMENT_NODE )
                        {
                             if ( gknode.hasAttributes() )
                             {
                                  // get node attributes
                                  NamedNodeMap gkattrmap=gknode.getAttributes();
                                  // get cmap object reference
                                  Node objref = gkattrmap.getNamedItemNS(
                                        "http://www.celebration.saic.com/nci_cmap","objectName");
                                  // ignore node, if no objectName ref
                                  if ( objref == null ) continue;
                                  // add diable filter to colorspec
                                  Node styleNode = gkattrmap.getNamedItem("style");
                                  String styleStr=styleNode.getNodeValue();
                                  styleStr=updateFilter(styleStr,"#TransparentFilter");
                                  styleNode.setNodeValue(styleStr);

                             }
                        }
                    } //end for "g" child node list of nodes
                } // end if element node
            } // end for "g" child nodes
        } // end for childnodes
	 }

	 /**
	 * disableAllNodes - disable all nodes within svg document
	 */
	 public void disableAllNodes() throws Exception
	 {
		Document svg = getSvgDiagram();
        if ( svg == null ) return;
        // go through svg document, look for nodes and disable them
        NodeList svgnodes=svg.getDocumentElement().getChildNodes();
        for (int i=0; i<svgnodes.getLength(); i++ )
        {
            // examine g nodes only
           // if ( !svgnodes.item(i).getNodeName().equals("g" ))continue;
            // process list of "g" child nodes
            NodeList kids=svgnodes.item(i).getChildNodes();
            for (int j=0; j<kids.getLength(); j++ )
            {
                Node node=kids.item(j);
                // only interested in node if it's an element
                if ( node.getNodeType() == Node.ELEMENT_NODE )
                {
                    String name=node.getNodeName();
                    // get list of nodes for "g" child node
                    NodeList gkids=node.getChildNodes();
                    for ( int k=0; k<gkids.getLength(); k++)
                    {
                        Node gknode=gkids.item(k);
                        if ( gknode.getNodeType() == Node.ELEMENT_NODE )
                        {
                             if ( gknode.hasAttributes() )
                             {
                                  // get node attributes
                                  NamedNodeMap gkattrmap=gknode.getAttributes();
                                  // add diable filter to colorspec
                                  Node styleNode = gkattrmap.getNamedItem("style");
                                  if (styleNode!=null)
                                  {
                                     String styleStr=styleNode.getNodeValue();
                                     styleStr=updateFilter(styleStr,"#TransparentFilter");
                                     styleNode.setNodeValue(styleStr);
                                     //XMLUtility.setXlinkNameSpace(gknode);
                                  }
                             }
                        }
                    } //end for "g" child node list of nodes
                } // end if element node
            } // end for "g" child nodes
        } // end for childnodes
	 }

	 /**
	  * build svg as a document and return it
	  * @return Document
	  */
	public Document getSvgDiagram() throws Exception
	{
		if (this.svgDiagram==null)
        {
			if (this.svgString!=null)
            {
				try
                {
					 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
					 documentBuilderFactory.setNamespaceAware(true);
					 DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
					 StringReader  stringIn = new StringReader(svgString);
					 svgDiagram = documentBuilder.parse(new InputSource(stringIn));

					 if(this.originalSvg == null)
					 {
						 DocumentBuilderFactory documentBuilderFactory0 = DocumentBuilderFactory.newInstance();
						 documentBuilderFactory0.setNamespaceAware(true);
						 DocumentBuilder documentBuilder0 = documentBuilderFactory0.newDocumentBuilder();
						 StringReader  stringIn0 = new StringReader(svgString);
					 	 this.originalSvg = documentBuilder0.parse(new InputSource(stringIn0));
					 }
				}
                catch (Exception e)
                {
					 log.error("getSvgDiagram - Exception: " + e.getMessage());
					 throw new Exception("getSvgDiagram - Exception: " + e.getMessage());
				}
			 }
		}
        return svgDiagram;
	}

	 /**
	  * sets svgDiagram document
	 * @param svg
	 */
	 public void setSvgDiagram(Document svg) throws Exception
	 {
		 String tempString=null;
		 this.svgDiagram = svg;
		 if(originalSvg == null)
		 {
			 try
             {
				StringWriter  stringOut = new StringWriter();
				OutputFormat outformat = new OutputFormat();
				outformat.setIndenting( true );
				XMLSerializer xmlserializer = new XMLSerializer(outformat);
				xmlserializer.setOutputCharStream(stringOut);
				xmlserializer.serialize(svg);
				tempString = stringOut.toString();

				DocumentBuilderFactory documentBuilderFactory0 = DocumentBuilderFactory.newInstance();
				documentBuilderFactory0.setNamespaceAware(true);
				DocumentBuilder documentBuilder0 = documentBuilderFactory0.newDocumentBuilder();
			 	StringReader  stringIn0 = new StringReader(tempString);
				this.originalSvg = documentBuilder0.parse(new InputSource(stringIn0));
			}
             catch (Exception e)
             {
                 log.error("Exception: " + e.getMessage());
                 throw new Exception("setSvgDiagram - Exception: " + e.getMessage());

			 }
		}
	 }

	 /**
	 * Sets the String representing this SVG
	 * @param svgString
	 */
	 public void setSvgString(String svgString)
     {
		 this.svgString=svgString;
		 this.svgDiagram=null;
	 }

	/**
	 * Returns the String representing the SVG Document.
	 * @return the SVG String for the svg document
	 */
	 public String getSvgString() throws Exception
     {
		 // check if svg has been changed
		 if (this.svgDiagram!=null)
         {
			 // set up xml serialization stuff
		   try
           {
				StringWriter  stringOut = new StringWriter();
			 	OutputFormat outformat = new OutputFormat();
			 	outformat.setIndenting( true );
			 	XMLSerializer xmlserializer = new XMLSerializer(outformat);
			 	xmlserializer.setOutputCharStream(stringOut);
			 	xmlserializer.serialize(svgDiagram);
			  	svgString = stringOut.toString();
			} catch (Exception e)
            {
				log.error("getSvgString - Exception: " + e.getMessage());
				throw new Exception("getSvgString - Exception: " + e.getMessage());
			}
		 }
		 return svgString;
    }

	/**
	 * Get the current color of the specified gene within the SVG diagram.
	 * With current cabio domain model, to get bcid for a given gene object:
	 * <li> call getGeneAliasCollection method of the Gene object
	 * <li>	iterate through this collection (GeneAlias object), call getType() method, check its value, if it matches “BioCarta” then call getName() method to get bcid.
	 *			Break through the loop when this occurs.<br>
	 * When bcid value is found, go through svg document, look for the bcid found, if there is a match, return the color for that, if not, return null.
	 *
	 * @param gene The gene to get the color for
	 * @return the color of the gene as a comma seperated list of rgb values (eg "10,20,255")
	 */
	public String getSvgColor(Object gene) throws Exception
    {
		// return the current color of the object mapped to the gene identifier passed in
		// get gene bcid's. We are going to assume a major hack here to simplify matters.
		// The hack is to only take the first BCID we get from the gene. This way we only return
		// a single color. This has a major shortcoming in that it is *possible* to have more than one
		// BCID per gene. Apparaently, most(all?) of the CVG diagrams assume a 1-1 mapping, so this
		// assumption should handle all cases for now...

		Document svg = getSvgDiagram();
		if ( svg == null ) return null;
		String aliasType, bcid=null;
		Collection c=null;
		// use reflection to get gene information and invoke it method
		// to get GeneAliasCollection. From GeneAlias, get BCID
		try
        {
			Class cls = gene.getClass();
			Class[] parameterTypes = new Class[] {};
			String className = cls.getName();
			Object[] arguments = new Object[] {};

			if(gene != null)
			{
				 Method getAlias = cls.getMethod("getGeneAliasCollection", parameterTypes);
				 c = (Collection)getAlias.invoke(gene, arguments);
			}

			for(Iterator itr = c.iterator(); itr.hasNext();)
			{
			    Object geneAlias = itr.next();
				Class clsGeneAlias = geneAlias.getClass();
				Method getType = clsGeneAlias.getMethod("getType", parameterTypes);
				aliasType = (String)getType.invoke(geneAlias, arguments);
				if(aliasType.equals(Constant.BIOCARTA_STRING))
				{
					Method getName = clsGeneAlias.getMethod("getName", parameterTypes);
					bcid = (String)getName.invoke(geneAlias, arguments);
					//System.out.println("bcid for gene id: + gene.getId() +  is " + bcid);
					break; // found the first bcid
				}
			}
		 } catch (NoSuchMethodException e)
         {
			log.error("NoSuchMethodException: " + e.getMessage());
			throw new Exception("getSvgColor - NoSuchMethodException: " + e.getMessage());


		 } catch (IllegalArgumentException e)
         {
			// TODO Auto-generated catch block
			log.error("getSvgColor - IllegalArgumentException: " + e.getMessage());
			throw new Exception("getSvgColor - IllegalArgumentException: " + e.getMessage());


		 } catch (IllegalAccessException e)
         {
			log.error("getSvgColor - IllegalAccessException: " + e.getMessage());
			throw new Exception("getSvgColor - IllegalAccessException: " + e.getMessage());


		 } catch (InvocationTargetException e)
         {
			log.error("getSvgColor - InvocationTargetException: " + e.getMessage());
			throw new Exception("getSvgColor - InvocationTargetException: " + e.getMessage());


         } catch(Exception e)
         {
             log.error("getSvgColor - Exception: " + e.getMessage());
             throw new Exception("getSvgColor - Exception: " + e.getMessage());


         }

		// go through svg document, return color for the specified bcid
		String color=null;
		NodeList svgnodes=svg.getDocumentElement().getChildNodes();
		for (int i=0; i<svgnodes.getLength(); i++ )
        {
			// examine g nodes only
			if ( !svgnodes.item(i).getNodeName().equals("g" ))continue;
			// process list of "g" child nodes
			NodeList kids=svgnodes.item(i).getChildNodes();
			for (int j=0; j<kids.getLength(); j++ )
            {
				Node node=kids.item(j);
				// only interested in node if it's an element
				if ( node.getNodeType() == Node.ELEMENT_NODE )
                {
					String name=node.getNodeName();
					// get list of nodes for "g" child node
					NodeList gkids=node.getChildNodes();
					for ( int k=0; k<gkids.getLength(); k++)
                    {
						Node gknode=gkids.item(k);
						if ( gknode.getNodeType() == Node.ELEMENT_NODE )
                        {
							 if ( gknode.hasAttributes() )
                             {
								  // get node attributes
								  NamedNodeMap gkattrmap=gknode.getAttributes();
								  // get cmap object reference
								  Node objref = gkattrmap.getNamedItemNS(
										"http://www.celebration.saic.com/nci_cmap","objectName");
								  // ignore node, if no objectName ref
								  if ( objref == null ) continue;
								  String obname=objref.getNodeValue();
								  if ( obname.equals(bcid) )
                                  {
									  Node colref = gkattrmap.getNamedItem("style");
									  String colspec=colref.getNodeValue();
									  int rgbstart=colspec.indexOf("rgb(");
									  String t=colspec.substring(rgbstart+4);
									  int reststart=t.indexOf(')');
									  color=t.substring(0,reststart);
								  }
							 }
						}
					} //end for "g" child node list of nodes
				} // end if element node
			} // end for "g" child nodes
		} // end for childnodes
		return color;
    }

	 /**
	  * This method goes through the svg document, set the color given for each bcid found in each genes array.
	  * For genes[0], set colors[0], etc. To find the bcid for the specified gene,
	  * use the same logic as in getSvgColor(Object gene) method.
	  * @param genes
	  * @param colors
	 */
	public void setSvgColors(Object[] genes, String[] colors) throws Exception
	 {
		Document svg = getSvgDiagram();
        if ( svg == null ) return;
        // next build a hashmap of BCID and colors
        Hashtable colortab=new Hashtable();

        try
        {
			Class[] parameterTypes = new Class[] {};
			Object[] arguments = new Object[] {};
            for (int i=0; i<genes.length; i++ )
            {
                String[] bcids=null, tempbcids=null;
                String aliasType=null, bcid=null;
                int n=0;
                Class cls = genes[i].getClass();

                Method getAlias = cls.getMethod("getGeneAliasCollection", parameterTypes);
				Collection c = (Collection)getAlias.invoke(genes[i], arguments);
                tempbcids = new String[c.size()];

                for(Iterator itr = c.iterator(); itr.hasNext();)
				{
					Object geneAlias = itr.next();
					Class clsGeneAlias = geneAlias.getClass();
					Method getType = clsGeneAlias.getMethod("getType", parameterTypes);

					aliasType = (String)getType.invoke(geneAlias, arguments);
					if(aliasType.equals(Constant.BIOCARTA_STRING))
					{
						Method getName = clsGeneAlias.getMethod("getName", parameterTypes);
						bcid = (String)getName.invoke(geneAlias, arguments);
                        tempbcids[n++] = bcid;

					}
				}
                if(tempbcids != null && tempbcids.length > 0)
                {
                    bcids = new String[tempbcids.length - 1];
                    for (int k=0; k<n; k++)
                    {
                        bcids[k] = tempbcids[k];
                    }

                    for (int j=0; j<bcids.length; j++)
                    {
                        colortab.put(bcids[j],colors[i]);
                    }
                }
            }
         } catch (NoSuchMethodException e)
         {
			//System.out.println(e);
			log.error("setSvgColors - NoSuchMethodException: " + e.getMessage());
			throw new Exception("setSvgColors - NoSuchMethodException: " + e.getMessage());

		 } catch (IllegalArgumentException e)
         {
			// TODO Auto-generated catch block

			log.error("IllegalArgumentException: " + e.getMessage());
			throw new Exception("setSvgColors - IllegalArgumentException: " + e.getMessage());

		 } catch (IllegalAccessException e)
         {
			// TODO Auto-generated catch block
			log.error("IllegalAccessException: " + e.getMessage());
			throw new Exception("setSvgColors - IllegalAccessException: " + e.getMessage());


		 } catch (InvocationTargetException e)
         {
		     //System.out.println(e);
		     log.error("InvocationTargetException: " + e.getMessage());
		     throw new Exception("setSvgColors - InvocationTargetException: " + e.getMessage());


         } catch (Exception e)
         {
			//System.out.println("exception here" + e.getMessage());
			log.error("Exception: " + e.getMessage());
			throw new Exception("setSvgColors - Exception: " + e.getMessage());

            //return;
         }

        // now go through svg document, change colors for those
        // components that map to the BCID
        NodeList svgnodes=svg.getDocumentElement().getChildNodes();
        for (int i=0; i<svgnodes.getLength(); i++ )
        {
            // examine g nodes only
            if ( !svgnodes.item(i).getNodeName().equals("g" ))continue;
            // process list of "g" child nodes
            NodeList kids=svgnodes.item(i).getChildNodes();
            for (int j=0; j<kids.getLength(); j++ )
            {
                Node node=kids.item(j);
                // only interested in node if it's an element
                if ( node.getNodeType() == Node.ELEMENT_NODE )
                {
                    String name=node.getNodeName();
                    // get list of nodes for "g" child node
                    NodeList gkids=node.getChildNodes();
                    for ( int k=0; k<gkids.getLength(); k++)
                    {
                        Node gknode=gkids.item(k);
                        if ( gknode.getNodeType() == Node.ELEMENT_NODE )
                        {
                             if ( gknode.hasAttributes() ){
                                  // get node attributes
                                  NamedNodeMap gkattrmap=gknode.getAttributes();
                                  // get cmap object reference
                                  Node objref = gkattrmap.getNamedItemNS(
                                        "http://www.celebration.saic.com/nci_cmap","objectName");
                                  // ignore node, if no objectName ref
                                  if ( objref == null ) continue;
                                  String obname=objref.getNodeValue();
                                  // get existing color then update to new color
                                  Node colref = gkattrmap.getNamedItem("style");
                                  if (colortab.containsKey(obname))
                                  {
                                     String newcolor=colorMap(colref.getNodeValue(),(String)colortab.get(obname));
                                     newcolor=updateFilter(newcolor,"#OpaqueFilter");
                                     colref.setNodeValue(newcolor);
                                  }
                             }
                        }
                    } //end for "g" child node list of nodes
                } // end if element node
            } // end for "g" child nodes
        } // end for childnodes

	 }



	/**
	 * This is an overloaded method instead the parameter contain a Map with key as bcid and value is color.
	 * Use this method when bcid(s) are known.
	 * Hastable key contain BCID, value contain color value
	 * @param colors
	 */
	 public void setSvgColors(Map geneColors) throws Exception
	 {
		 Document svg = getSvgDiagram();
         if ( svg == null ) return;

		 NodeList svgnodes=svg.getDocumentElement().getChildNodes();
		 for (int i=0; i<svgnodes.getLength(); i++ )
         {
			 // examine g nodes only
			 if ( !svgnodes.item(i).getNodeName().equals("g" ))continue;
			 // process list of "g" child nodes
			 NodeList kids=svgnodes.item(i).getChildNodes();
			 for (int j=0; j<kids.getLength(); j++ )
             {
				 Node node=kids.item(j);
				 // only interested in node if it's an element
				 if ( node.getNodeType() == Node.ELEMENT_NODE )
                 {
					 String name=node.getNodeName();
					 // get list of nodes for "g" child node
					 NodeList gkids=node.getChildNodes();
					 for ( int k=0; k<gkids.getLength(); k++)
                     {
						 Node gknode=gkids.item(k);
						 if ( gknode.getNodeType() == Node.ELEMENT_NODE )
                         {
							  //System.out.println("     gkNode=" + gknode.getNodeName() +
							  //                   " val=" + gknode.getNodeValue() +
							  //                   " attr=" + gknode.hasAttributes());
							  if ( gknode.hasAttributes() )
                              {
								   // get node attributes
								   NamedNodeMap gkattrmap=gknode.getAttributes();
								   // get cmap object reference
								   Node objref = gkattrmap.getNamedItemNS(
										 "http://www.celebration.saic.com/nci_cmap","objectName");
								   // ignore node, if no objectName ref
								   if ( objref == null ) continue;
								   String obname=objref.getNodeValue();
								   // get existing color then update to new color
								   Node colref = gkattrmap.getNamedItem("style");
								   //System.out.println(" svg=" + svgnodes.item(i).getNodeName()+
								   //                   " kid=" + name+
								   //                   " knode=" + gknode.getNodeName()+
								   //                    " New color f="+colref.getNodeValue()+
								   //                    "t=" + colorMap(colref.getNodeValue(),
								   //                                       (String)geneColors.get(obname)));
								   if (geneColors.containsKey(obname))
                                   {
									  String newcolor=colorMap(colref.getNodeValue(),(String)geneColors.get(obname));
									  newcolor=updateFilter(newcolor,"#OpaqueFilter");
									  colref.setNodeValue(newcolor);
                                   }
							  }
						 }
					 } //end for "g" child node list of nodes
				 } // end if element node
			 } // end for "g" child nodes
		 } // end for childnodes

	 }

	 /**
	 * This method returns pathway diagrame name
	 * @return pathway diagram name
	 */
	 public String getName()
	 {
		 return this.name;
	 }

	 /**
	 * @param name
	 */
	 public void setName(String name)
	 {
		 this.name = name;
	 }

	 /**
	  *
	  */
	 public String toString()
	 {
		 String tempString=null;
		 if (svgDiagram!=null)
         {
			 try
             {
				StringWriter  stringOut = new StringWriter();
				OutputFormat outformat = new OutputFormat();
				outformat.setIndenting( true );
				XMLSerializer xmlserializer = new XMLSerializer(outformat);
				xmlserializer.setOutputCharStream(stringOut);
				xmlserializer.serialize(svgDiagram);
				tempString = stringOut.toString();
			 } catch (Exception e)
             {
				log.error("toString - Exception: " + e.getMessage());
				System.out.println("toString - Exception: " + e.getMessage());
			 }
		 }
		 StringBuffer buf = new StringBuffer();
         //StringBuilder buf = new StringBuilder();
	   	 buf.append("PathwayDiagram:\r\n");
	     buf.append(" Name: [").append(name).append("]\r\n");
	     buf.append(" svg: [").append(tempString).append("]\r\n");
         return buf.toString();
	 }

	 /**
	  * This method reset the svg document and return the original svg document.
	  * @return The original SVG document
	  */
	 public Document reset() throws Exception
	 {
		 this.svgDiagram =null;

		 String tempString=null;

		 if(originalSvg != null)
		 {
			 try
             {
				StringWriter  stringOut = new StringWriter();
				OutputFormat outformat = new OutputFormat();
				outformat.setIndenting( true );
				XMLSerializer xmlserializer = new XMLSerializer(outformat);
				xmlserializer.setOutputCharStream(stringOut);
				xmlserializer.serialize(this.originalSvg);
				tempString = stringOut.toString();

				DocumentBuilderFactory documentBuilderFactory0 = DocumentBuilderFactory.newInstance();
				documentBuilderFactory0.setNamespaceAware(true);
				DocumentBuilder documentBuilder0 = documentBuilderFactory0.newDocumentBuilder();
				StringReader  stringIn0 = new StringReader(tempString);
				this.svgDiagram = documentBuilder0.parse(new InputSource(stringIn0));

             } catch (Exception e)
             {
				log.error("reset - Exception: " + e.getMessage());
				throw new Exception("reset - Exception: " + e.getMessage());
             }
		}
		return this.svgDiagram;
	 }
	 /**
	  * save the document in the specified filename.
	  * @param fileName
	  * @param doc
	  * @return true - document is saved, false otherwise
	 */
	public boolean saveXMLDoc(String fileName, Document doc)
	{
		 File xmlOutputFile = new File(fileName);
		 FileOutputStream fos;
		 Transformer transformer=null;
		 try
         {
			 fos = new FileOutputStream(xmlOutputFile);
		 }
		 catch (FileNotFoundException e)
         {
			 //System.out.println("FileNotFoundException: " + e.getMessage());
			 log.error("saveXMLDoc - FileNotFoundException: " + e.getMessage());
			 return false;
		 }
		 // Use a Transformer for output
		 TransformerFactory transformerFactory = TransformerFactory.newInstance();
		 try
         {
		     transformer = transformerFactory.newTransformer();
		 }
		 catch (TransformerConfigurationException e)
         {
		     //System.out.println("Transformer configuration error: " + e.getMessage());
		     log.error("saveXMLDoc - TransformerConfigurationException: " + e.getMessage());
		     return false;
		 }

		 DOMSource source = new DOMSource(doc);
		 StreamResult result = new StreamResult(fos);
		 try
         {
		     transformer.transform(source, result);
		 }
		 catch (TransformerException e)
         {
		     //System.out.println("Error transform: " + e.getMessage());
		     log.error("saveXMLDoc - TransformerException: " + e.getMessage());
		     return false;
		 }
		 //System.out.println("XML file saved.");
		 return true;
	}

	 /* Private methods */

	 /**
	  * Update the filter attribute within an SVG style parameter list.
	  * ex, convert curr="visibility:inherit;fill:rgb(0,128,0);filter:url(#OpaqueFilter)"
	  *               to="visibility:inherit;fill:rgb(0,128,0);filter:url(#TransparentFilter)
	  *       given the filterSpec of "TransparentFilter"
	  *
	  * @param source
	  * @param filterSpec
	  *
	  * @return The updated string with the new filter:url specified.
	  */
	 private String updateFilter(String source, String filterSpec)
     {
		 StringBuffer resultbuff=new StringBuffer();
         //StringBuilder resultbuff=new StringBuilder();
		 if ( source == null )return "filter:url(" + filterSpec + ')';
		 try
         {
			 // remove OpaqueFilter and Transparent if they exist
			 replaceString(source, "filter:url(#OpaqueFilter)", "");
			 replaceString(source, "filter:url(#TransparentFilter)", "");
			 if (filterSpec.equals("#TransparentFilter"))
             {
				 resultbuff.append(source).append(";filter:url(#TransparentFilter)");
			 } else if (filterSpec.equals("#OpaqueFilter"))
             {
				 resultbuff.append(source).append(";filter:url(#OpaqueFilter)");
			 }
		 }catch (Exception e)
         {
			 resultbuff.append(source);
			 log.error("Exception: " + e.getMessage());
		 }
		 return resultbuff.toString();
    }
	/**
    * Replace the color specifier in a string. The input is assumed to contain
    * text of "rgb(val,val,val)". There may be other words, chars in the string.
    * This method replaces the rgb values with those in the passed in new string.
    * <p>
    * ex curr="visibility:inherit;fill:rgb(0,128,0)"  newcolor="255,0,255"
    * returns "visibility:inherit;fill:rgb(255,0,255)"
    *
    * @param currColor The source string
    * @param newColor The new rgb values to insert in the rgb specifier of the source string
    *
    * @return The updated string with the RGB value modified, or the original string if a
    *          substitution was unable to be made.
    */
	 private String colorMap(String currColor, String newColor)
	 {
		 // return appropriate color directive for the passed in bcid id using the
		 // passed in currcolor string as the template.
		 // ie it could be : visibility:inherit;fill:rgb(0,128,0)
		 //             or : rgb(0,128,0)
		 // the important part seems to be "rbg(x,x,x)"
		 String result=null;
		 if (newColor == null || newColor.equals("null") ) return currColor;
		 try
         {
			 int rgbstart=currColor.indexOf("rgb(");
			 result = currColor.substring(0,rgbstart+4) + newColor;
			 String t=currColor.substring(rgbstart+1);
			 int reststart=t.indexOf(')');
			 result += t.substring(reststart);
		 }catch (Exception e)
         {
			 result=currColor;
			 log.error("Exception: " + e.getMessage());
		 }
        return result;
	 }

	 private String replaceString(String str, String sep, String rep)
	 {
         //StringBuilder retVal = new StringBuilder();
         StringBuffer retVal = new StringBuffer();
		 int idx = 0;
		 int jdx = str.indexOf(sep);
		 while (jdx >= 0)
         {
			retVal.append(str.substring(idx, jdx));
			retVal.append(rep);
			idx = jdx + sep.length();
			jdx = str.indexOf(sep, idx);
		 }
		 retVal.append(str.substring(idx));
         return retVal.toString();
	 }

     private void loadProperties() throws Exception
     {
        if(GENE_INFO_STRING != null) return;
        try{
            Properties _properties = new Properties();

            _properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("CORESystem.properties"));

            GENE_INFO_STRING = _properties.getProperty("GENE_INFO_STRING");

        }catch(IOException e)
        {
            log.error("loadProperties - IOException: " + e.getMessage());
            throw new IOException("loadProperties - IOException: " + e.getMessage());
            //System.out.println("IOException occured: "+e.getMessage());
        }
        catch(Exception ex){
            log.error("loadProperties - Exception: " + ex.getMessage());
            throw new Exception("loadProperties - Exception: " + ex.getMessage());
            //System.out.println("Exception - "+ ex.getMessage());
        }
    }


}