package cz.stechy.clickbot;

import cz.stechy.clickbot.actions.CheckScreen;
import cz.stechy.clickbot.actions.Cycle;
import cz.stechy.clickbot.actions.Delay;
import cz.stechy.clickbot.actions.LeftClick;
import cz.stechy.clickbot.actions.RightClick;
import cz.stechy.clickbot.actions.SmoothMove;
import cz.stechy.clickbot.actions.Writer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class SimpleActionParser implements IActionParser {

    // region Constants

    private static final String ELEMENT_ROOT = "actions";
    private static final String ATTRIBUTE_DELAY = "delay";
    private static final String ATTRIBUTE_REPEAT = "repeat";
    private static final String ELEMENT_ACTION = "action";
    private static final String ATTRIBUTE_ACTION_TYPE = "type";
    private static final String VALUE_ACTION_TYPE_CLICK = "click";
    private static final String ATTRIBUTE_ACTION_TYPE_MOUSE_BUTTON = "button";
    private static final String VALUE_ACTION_TYPE_MOUSE_BUTTON_LEFT = "left";
    private static final String VALUE_ACTION_TYPE_MOUSE_BUTTON_RIGHT = "right";
    private static final String ATTRIBUTE_ACTION_TYPE_MOVE = "move";
    private static final String ATTRIBUTE_ACTION_TYPE_WRITE = "write";
    private static final String ATTRIBUTE_ACTION_TYPE_DELAY = "delay";
    private static final String ELEMENT_POINT = "point";
    private static final String ATTRIBUTE_POINT_X = "x";
    private static final String ATTRIBUTE_POINT_Y = "y";
    private static final String ATTRIBUTE_POINT_CONSTANT = "constant";
    private static final String ATTRIBUTE_ACTION_TYPE_CYCLE = "cycle";
    private static final String ATTRIBUTE_ACTION_TYPE_CHECK_IMAGE = "check_image";
    private static final String ATTRIBUTE_CHECK_IMAGE_WIDTH = "width";
    private static final String ATTRIBUTE_CHECK_IMAGE_HEIGHT = "height";
    private static final String ATTRIBUTE_CHECK_IMAGE_IMAGE = "image";
    private static final String VALUE_POINT_CONSTANT_CONFIGURATION_1 = "configuration1";
    private static final String VALUE_POINT_CONSTANT_CONFIGURATION_2 = "configuration2";
    private static final String VALUE_POINT_CONSTANT_COAL_BAG = "coal_bag";
    private static final String VALUE_POINT_CONSTANT_FURNACE_FROM_BANK = "furnace_from_bank";
    private static final String VALUE_POINT_CONSTANT_BANK_FROM_FURNACE = "bank_from_furnace";
    private static final String VALUE_POINT_CONSTANT_ANVIL_FROM_FURNACE = "anvil_from_furnace";
    private static final String VALUE_POINT_CONSTANT_BANK_FROM_ANVIL = "bank_from_anvil";

    private static final String ELEMENT_STEP = "step";
    private static final String ELEMENT_TEXT = "text";

    // endregion

    // region Variables

    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private final InputStream inputStream;
    private final IConstantProvider constantProvider;

    private Document doc;
    private int delay;
    private int iterations;

    // endregion

    // region Constructors

    public SimpleActionParser(InputStream inputStream, IConstantProvider constantProvider) {
        this.inputStream = inputStream;
        this.constantProvider = constantProvider;
    }

    // endregion

    // region Private methods

    /**
     * Hlavni parsovací metoda
     *
     * @param element {@link Element}
     * @return {@link Consumer<IRobotController>}
     */
    private Consumer<IRobotController> parseMainElement(Element element) {

        final String actionType = element.getAttribute(ATTRIBUTE_ACTION_TYPE);
        switch (actionType) {
            case VALUE_ACTION_TYPE_CLICK:
                final String mouseButton = element.getAttribute(ATTRIBUTE_ACTION_TYPE_MOUSE_BUTTON);
                switch (mouseButton) {
                    case VALUE_ACTION_TYPE_MOUSE_BUTTON_LEFT:
                        return parseLeftClickAction(element);
                    case VALUE_ACTION_TYPE_MOUSE_BUTTON_RIGHT:
                        return parseRightClickAction(element);
                    default:
                        throw new IllegalStateException("Narazil jsem na neočekávaný token");
                }
            case ATTRIBUTE_ACTION_TYPE_MOVE:
                return parseMoveAction(element);
            case ATTRIBUTE_ACTION_TYPE_WRITE:
                return parseWriteAction(element);
            case ATTRIBUTE_ACTION_TYPE_DELAY:
                return parseDelay(element);
            case ATTRIBUTE_ACTION_TYPE_CYCLE:
                return parseCycle(element);
            case ATTRIBUTE_ACTION_TYPE_CHECK_IMAGE:
                return parseCheckScreen(element);
            default:
                throw new IllegalStateException("Narazil jsem na neočekávaný token");
        }
    }

    /**
     * Pomocná metoda, která naparsuje souřadnice bodu z XML elementu
     * <point x="10" y="15"/>
     *
     * @param element {@link Element}
     * @return {@link Point}
     */
    private Point parsePoint(Element element) {
        final Element point = (Element) element.getElementsByTagName(ELEMENT_POINT).item(0);
        if (point == null) {
            return new Point(-1, -1);
        }
        if (point.hasAttribute(ATTRIBUTE_POINT_CONSTANT)) {
            String constant = point.getAttribute(ATTRIBUTE_POINT_CONSTANT);
            return constantProvider.valueOf(constant);
        }

        final int x = Integer.parseInt(point.getAttribute(ATTRIBUTE_POINT_X));
        final int y = Integer.parseInt(point.getAttribute(ATTRIBUTE_POINT_Y));
        return new Point(x, y);
    }

    /**
     * Pomocná metoda, která vyparsuje počet kroků pro nastavení plynulého pohybu
     * <step>5</step>
     *
     * @param element {@link Element}
     * @return {@link Integer}
     */
    private int parseStep(Element element) {
        final Element point = (Element) element.getElementsByTagName(ELEMENT_STEP).item(0);
        return Integer.parseInt(point.getTextContent().trim());
    }

    private void parseDefaultProperties(Element element) {
        this.delay = Integer.parseInt(element.getAttribute(ATTRIBUTE_DELAY));
        this.iterations = Integer.parseInt(element.getAttribute(ATTRIBUTE_REPEAT));
    }

    private Consumer<IRobotController> parseLeftClickAction(Element element) {
        return new LeftClick(parsePoint(element));
    }

    private Consumer<IRobotController> parseRightClickAction(Element element) {
        return new RightClick(parsePoint(element));
    }

    private Consumer<IRobotController> parseMoveAction(Element element) {
        return new SmoothMove(parsePoint(element), parseStep(element));
    }

    private Consumer<IRobotController> parseWriteAction(Element element) {
        return new Writer(element.getTextContent());
    }

    private Consumer<IRobotController> parseDelay(Element element) {
        return new Delay(Integer.parseInt(element.getTextContent().trim()));
    }

    private Consumer<IRobotController> parseCycle(Element parent) {
        final int iterations = Integer.parseInt(parent.getAttribute(ATTRIBUTE_REPEAT));
        final List<Consumer<IRobotController>> list = new ArrayList<>();

        final NodeList childNodes = parent.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            final Element element = (Element) node;
            list.add(parseMainElement(element));
        }

        return new Cycle(iterations, list);
    }

    private Consumer<IRobotController> parseCheckScreen(Element element) {
        final Point coordinates = parsePoint(element);
        final int width = Integer.parseInt(element.getAttribute(ATTRIBUTE_CHECK_IMAGE_WIDTH));
        final int height = Integer.parseInt(element.getAttribute(ATTRIBUTE_CHECK_IMAGE_HEIGHT));
        final String image = element.getAttribute(ATTRIBUTE_CHECK_IMAGE_IMAGE);
        final String imagePath = constantProvider.valueOf(image);
        return new CheckScreen(imagePath, coordinates, width, height);
    }

    // endregion

    // region Public methods

    @Override
    public List<Consumer<IRobotController>> parse() throws Exception {
        final DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(inputStream);
        doc.getDocumentElement().normalize();
        final Node rootNode = doc.getDocumentElement();
        parseDefaultProperties((Element) rootNode);
        final NodeList list = rootNode.getChildNodes();
        final int count = list.getLength();
        final List<Consumer<IRobotController>> actionList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            final Element element = (Element) node;
            actionList.add(parseMainElement(element));
        }

        return actionList;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public int getIterationCount() {
        return iterations;
    }

    // endregion
}
