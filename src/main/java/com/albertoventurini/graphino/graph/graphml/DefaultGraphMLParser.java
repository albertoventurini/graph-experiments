//package com.albertoventurini.graphino.graph.graphml;
//
//import org.w3c.dom.Document;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.events.XMLEvent;
//import java.io.File;
//import java.io.FileInputStream;
//import java.nio.file.Path;
//import java.util.HashSet;
//import java.util.Set;
//
//public class DefaultGraphMLParser implements GraphMLParser {
//
//    @Override
//    public GraphMLParseResult parse(final String filePath) {
//
//        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
//        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath));
//
//        while (reader.hasNext()) {
//            XMLEvent nextEvent = reader.nextEvent();
//        }
//
//        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//
//        try {
//            final File file = Path.of(filePath).toFile();
//
//            final DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
//            final Document d = documentBuilder.parse(file);
//
//            return buildParseResult(d);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private GraphMLParseResult buildParseResult(final Document d) {
//        final Set<Node> nodes = new HashSet<>();
//        d.getElementsByTagName("node").
//    }
//
//    private final Set<Node> buildNodes(final Document d) {
//        final var nodeList = d.getElementsByTagName("node");
//        for (int i = 0; i < nodeList.getLength(); i++) {
//
//        }
//    }
//}
