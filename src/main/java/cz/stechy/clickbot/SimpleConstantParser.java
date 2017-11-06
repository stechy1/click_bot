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

    private static final String ELEMENT_ROOT = "points";
    private static final String ELEMENT_POINT = "point";
    private static final String ATTRIBUTE_POINT_X = "x";
    private static final String ATTRIBUTE_POINT_Y = "y";
    private static final String ATTRIBUTE_NAME = "name";

    // endregion

    // region Variables

    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private final Map<String, Point> map = new HashMap<>();
    private final InputStream inputStream;

    private Document doc;

    // endregion

    public SimpleConstantParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private Point parsePoint(Element point) {
        final int x = Integer.parseInt(point.getAttribute(ATTRIBUTE_POINT_X));
        final int y = Integer.parseInt(point.getAttribute(ATTRIBUTE_POINT_Y));
        return new Point(x, y);
    }

    @Override
    public Map<String, Point> parse() throws Exception {
        final DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(inputStream);
        doc.getDocumentElement().normalize();
        final NodeList list = doc.getElementsByTagName(ELEMENT_POINT);
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

        return map;
    }
}
