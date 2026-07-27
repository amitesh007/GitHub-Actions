package com.finastra.integrationapi.tool.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLJSONParsingUtils {
	private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(XMLJSONParsingUtils.class);

	public static final XMLJSONParsingUtils Utils = new XMLJSONParsingUtils();

	

	public static void trimWhitespace(Node node) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				child.setTextContent(child.getTextContent().trim());
			}
			trimWhitespace(child);
		}
	}

	public Map convertXMLAPI2Map(String xmlString) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			this.setDocumentBuilderSecurityFeature(dbFactory);
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException("Conversion Error: " + e.getMessage());
		}
		doc.getDocumentElement().normalize();
		LinkedHashMap map = new LinkedHashMap();
		NodeList resultNode = doc.getChildNodes();
		getAPINodes(resultNode, map);
		// println map;
		return map;
	}
	
	public void setDocumentBuilderSecurityFeature(DocumentBuilderFactory documentBuilderFactory) {
		try {
			documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities",false);
			documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Error while setting DocumentBuilderFactory feature", e);
		}
	}

	private void getAPINodes(NodeList aNode, Map map) {

		for (int index = 0; index < aNode.getLength(); index++) {
			Node tempNode = aNode.item(index);
			if (tempNode.getNodeType() == Node.CDATA_SECTION_NODE) {
				// JSON cannot contain CRs in text value..
				map.put("#text", tempNode.getNodeValue().replaceAll("[\n\r]", "")); // TODO:
				// Check if it impairs CRs in cdata fields							
			}
			// TODO: Optimize plain text only nodes to emit plain string entry /
			// array entries .. tad complex..
			if (tempNode.getNodeType() == Node.TEXT_NODE && tempNode.getNodeValue() != null
					&& !tempNode.getNodeValue().trim().isEmpty()) {
				map.put(tempNode.getNodeName(), tempNode.getNodeValue().replaceAll("[\n\r]", ""));
			}
			LinkedHashMap mp = new LinkedHashMap();
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if("messages".equals(tempNode.getNodeName())){
					addMessageNode(tempNode, map);
					return;
				}
				NamedNodeMap attrs = tempNode.getAttributes();
				for (int idx = 0; idx < attrs.getLength(); idx++) {
					mp.put(attrs.item(idx).getNodeName(), attrs.item(idx).getNodeValue());
				}
				Object exists;
				boolean testTextNodeOnly = tempNode.getChildNodes().getLength() == 1
						&& tempNode.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE
						&& tempNode.getChildNodes().item(0).getNodeValue() != null
						&& !tempNode.getChildNodes().item(0).getNodeValue().trim().isEmpty();
				if ((exists = map.get(tempNode.getNodeName())) == null)
					map.put(tempNode.getNodeName(), mp);
				else {
					ArrayList arr = new ArrayList();
					if (exists instanceof ArrayList)
						arr.addAll((Collection) exists);
					else
						arr.add(exists);
					if (testTextNodeOnly)
						arr.add(tempNode.getChildNodes().item(0).getNodeValue());
					else
						arr.add(mp);
					map.put(tempNode.getNodeName(), arr);
				}
				if (testTextNodeOnly && exists == null) {
					map.put(tempNode.getNodeName(), tempNode.getChildNodes().item(0).getNodeValue());
				} else
					getAPINodes(tempNode.getChildNodes(), mp);
			}
		}
	}

	private void addMessageNode(Node tempNode , Map map){
		ArrayList arr = new ArrayList();
		map.put(tempNode.getNodeName(), arr);
		NodeList aNode = tempNode.getChildNodes();
		for (int index = 0; index < aNode.getLength(); index++) {
			Node childNode = aNode.item(index);
			if(childNode.getNodeType() == Node.ELEMENT_NODE){
				LinkedHashMap mp = new LinkedHashMap();
				NamedNodeMap attrs = childNode.getAttributes();
				for (int idx = 0; idx < attrs.getLength(); idx++) {
					mp.put(attrs.item(idx).getNodeName(), attrs.item(idx).getNodeValue());
				}
				// Business Object Identifiers
				NodeList boNode = childNode.getChildNodes();
				for (int i = 0; i < boNode.getLength(); i++) {
					Node boChild = boNode.item(i);
					if(boChild.getNodeType() == Node.ELEMENT_NODE){
						NamedNodeMap battrs = boChild.getAttributes();
						for (int idx = 0; idx < battrs.getLength(); idx++) {
							if(!"version".equals(battrs.item(idx).getNodeName()))
								mp.put(battrs.item(idx).getNodeName(), battrs.item(idx).getNodeValue());
						}
					}
				}
				arr.add(mp);
			}
		}
	}
	

}

