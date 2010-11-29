/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Majid
 */
public class DataParser
{

    private Document dom;
    private HashMap<String, State> states = new HashMap<String, State>();
    private ArrayList invalidMessages = new ArrayList();

    // default constructor
    public DataParser()
    {

        // Load the XML file and parse it
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("src/chatbot/data.xml");

            // Load states from XML file
            loadConfiguration();
            loadStates();
        } 
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        } 
        catch (SAXException se)
        {
            se.printStackTrace();
        } 
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    // Load states from XML file
    private void loadStates()
    {

        // get elements
        Element docEle = dom.getDocumentElement();

        // get all State node names
        NodeList nl = docEle.getElementsByTagName("State");

        // if node is not null and has children
        if (nl != null && nl.getLength() > 0)
        {

            // loop through all children
            for (int i = 0; i < nl.getLength(); i++)
            {

                // get state element
                Element el = (Element) nl.item(i);

                // get state id, message and keywords
                String id = el.getAttribute("id");
                String message = getTextValue(el, "message");
                ArrayList keywords = getKeywords(el);

                // construct a new State object
                State state = new State(id, message, keywords);

                // add the state to the states hashmap
                states.put(id, state);
            }
        }
    }

    // get state object by id
    public State getState(String id)
    {
        return states.get(id);
    }


    // get all keywords in an State tag
    public ArrayList getKeywords(Element ele) 
    {
        // construct keywords arraylist
        ArrayList keywords = new ArrayList();

        // get all nodes by keyword tag name
        NodeList nl = ele.getElementsByTagName("keyword");

        // if the tag is not null and has children
        if (nl != null && nl.getLength() > 0)
        {

            // loop through all the children
            for (int i = 0; i < nl.getLength(); i++) {

                //get the keyword element
                Element el = (Element) nl.item(i);

                // find the keyword title and target state
                String word = el.getFirstChild().getNodeValue();
                String target = el.getAttribute("target");

                // construct a new keyword
                Keyword keyword = new Keyword(word, target);

                // add the keyword to keywords array list
                keywords.add(keyword);
            }
        }

        // return all the keywords in the given tag
        return keywords;
    }

    // get a value of a tag inside given element by tag name
    private String getTextValue(Element ele, String tagName)
    {
        String textVal = null;
        
        // find the tag
        NodeList nl = ele.getElementsByTagName(tagName);

        // if the tag is not null and has children
        if (nl != null && nl.getLength() > 0)
        {
            // get first element and its node value
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        // return the string in the tag
        return textVal;
    }

    private void loadConfiguration()
    {
        // get elements
        Element docEle = dom.getDocumentElement();

        // get all State node names
        NodeList nl = docEle.getElementsByTagName("InvalidMessages");

        // if node is not null and has children
        if (nl != null && nl.getLength() > 0)
        {

            // loop through all children
            for (int i = 0; i < nl.getLength(); i++)
            {

                // get state element
                Element el = (Element) nl.item(i);

                // get state id, message and keywords
                String message = getTextValue(el, "message");
                invalidMessages.add(message);
                System.out.println(message);

            }
        }
    }
}