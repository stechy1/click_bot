package cz.stechy.clickbot;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimpleConstantParser implements IConstantParser {

    // region Constants


    private static final String NODE_POINTS = "points";
    private static final String ELEMENT_POINT = "point";
    private static final String ATTRIBUTE_POINT_X = "x";
    private static final String ATTRIBUTE_POINT_Y = "y";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String NODE_IMAGES = "images";
    private static final String ELEMENT_IMAGE = "image";

    // endregion

    // region Variables

    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private final Map<String, Object> map = new HashMap<>();
    private final InputStream inputStream;

    private Document doc;

    // endregion

    public SimpleConstantParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private void parsePoints(Element parent) {
        final NodeList list = parent.getChildNodes();
        final int count = list.getLength();
        for (int i = 0; i < count; i++) {
            final Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            final Element element = (Element) node;
            String name = element.getAttribute(ATTRIBUTE_NAME);

            map.put(name, parsePoint(element));
        }
    }

    private void parseImages(Element parent) {
        final NodeList list = parent.getChildNodes();
        final int count = list.getLength();
        for (int i = 0; i < count; i++) {
            final Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            final Element element = (Element) node;
            String name = element.getAttribute(ATTRIBUTE_NAME);
            String path = element.getTextContent();
            map.put(name, path);
        }
    }

    private Point parsePoint(Element point) {
        final int x = Integer.parseInt(point.getAttribute(ATTRIBUTE_POINT_X));
        final int y = Integer.parseInt(point.getAttribute(ATTRIBUTE_POINT_Y));
        return new Point(x, y);
    }

    @Override
    public Map<String, Object> parse() throws Exception {
        final DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(inputStream);
        doc.getDocumentElement().normalize();
        final Node rootNode = doc.getDocumentElement();
        final NodeList list = rootNode.getChildNodes();
        final int count = list.getLength();

        for (int i = 0; i < count; i++) {
            final Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            final Element element = (Element) node;
            final String nodeName = element.getNodeName();
            switch (nodeName) {
                case NODE_POINTS:
                    parsePoints(element);
                    break;
                case NODE_IMAGES:
                    parseImages(element);
                    break;
                default:
                    throw new IllegalStateException("Tohle by nikdy nemelo nastat: " + nodeName);
            }
        }

//        for (int i = 0; i < count; i++) {
//            final Node node = list.item(i);
//            if (node.getNodeType() != Node.ELEMENT_NODE) {
//                continue;
//            }
//
//            final Element element = (Element) node;
//            String name = element.getAttribute(ATTRIBUTE_NAME);
//
//            map.put(name, parsePoint(element));
//        }

        return map;
    }


}
