//package com.albertoventurini.graphino.graph.graphml;
//
//import com.albertoventurini.graphino.graph.xml.Element;
//import com.albertoventurini.graphino.graph.xml.Tag;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//import java.util.regex.Pattern;
//
//public class XmlParser {
//
//    private static class ParseInner {
//        private final String filePath;
//        private final Set<Node> nodes;
//        private final Set<Edge> edges;
//
//        private static final Pattern TAG_PATTERN = Pattern.compile("<(\\w+)\\s+([^>]*)/?>");
//        private static final Pattern CLOSE_TAG_PATTERN = Pattern.compile("</(\\w+)\\s*>");
//
//        ParseInner(final String filePath) {
//            this.filePath = filePath;
//            nodes = new HashSet<>();
//            edges = new HashSet<>();
//        }
//
//        GraphMLParseResult parse() throws IOException {
//            final String fileString = Files.readString(Path.of(filePath));
//            final ParseContext ctx = new ParseContext(fileString);
//
//            while (ctx.hasNext()) {
//                if (ctx.peek() == '<') {
//                    parseElement(ctx);
//                }
//                ctx.advance();
//            }
//
//            return new GraphMLParseResult(nodes, edges);
//        }
//
//        private Element<?> parseElement(final ParseContext ctx) {
//            final Tag openingTag = parseTag(ctx);
//        }
//
//        private Tag parseTag(final ParseContext ctx) {
//            ctx.consume('<');
//
//            Tag.Type type = null;
//            if (ctx.peek() == '/') {
//                type = Tag.Type.END_TAG;
//                ctx.advance();
//            }
//
//            final String name = parseToken(ctx);
//            skipWhitespaces(ctx);
//            final Map<String, String> attributes = parseTagAttributes(ctx);
//
//            if (ctx.peek() == '/') {
//                type = Tag.Type.EMPTY_ELEMENT_TAG;
//                ctx.advance();
//            } else if (type != null) {
//                type = Tag.Type.START_TAG;
//            }
//
//            ctx.consume('>');
//
//            return new Tag(name, attributes, type);
//        }
//
//        private Map<String, String> parseTagAttributes(final ParseContext ctx) {
//            final Map<String, String> attributes = new TreeMap<>();
//
//            while (ctx.hasNext()) {
//                final char c = ctx.next();
//                if (c == '/' || c == '>') {
//                    break;
//                }
//
//                final var attribute = parseTagAttribute(ctx);
//                attributes.put(attribute.getKey(), attribute.getValue());
//
//                skipWhitespaces(ctx);
//            }
//
//            return attributes;
//        }
//
//        private Map.Entry<String, String> parseTagAttribute(final ParseContext ctx) {
//            final String name = parseToken(ctx);
//            ctx.consume('=');
//            if (ctx.peek() == '"' || ctx.peek() == '\'') {
//                ctx.advance();
//            }
//            final String value = parseToken(ctx);
//            if (ctx.peek() == '"' || ctx.peek() == '\'') {
//                ctx.advance();
//            }
//
//            return Map.entry(name, value);
//        }
//
//        private String parseToken(final ParseContext ctx) {
//            final int start = ctx.getCursor();
//
//            while (ctx.hasNext()) {
//                final char c = ctx.next();
//                if (!Character.isLetterOrDigit(c)) {
//                    break;
//                }
//            }
//
//            return ctx.substring(start);
//        }
//
//        private void skipWhitespaces(final ParseContext ctx) {
//            while (ctx.hasNext()) {
//                final char c = ctx.peek();
//                if (!Character.isWhitespace(c)) {
//                    break;
//                }
//                ctx.advance();
//            }
//        }
//    }
//
//    public GraphMLParseResult parse(final String filePath) throws IOException {
//        return new ParseInner(filePath).parse();
//    }
//
//
//
//    private void parseTag(final ParseContext ctx) {
//        ctx.consume('<');
//
//        if (ctx.matches("node")) {
//            parseNode(ctx, nodes);
//        } else if (ctx.matches("edge")) {
//            parseEdge(ctx, edges);
//        } else {
//            parseUnknownTag(ctx);
//        }
//    }
//
//    private void parseNode(final ParseContext ctx, final Set<Node> nodes) {
//
//    }
//
//    private void parseEdge(final ParseContext ctx, final Set<Edge> edges) {
//
//    }
//
//    private void parseUnknownTag(final ParseContext ctx) {
//        int openTags = 1;
//        while (ctx.hasNext() && openTags > 0) {
//            final char c = ctx.next();
//            if (c == '<') {
//                openTags++;
//            } else if (c == '/' && ctx.next() == '>') {
//                openTags--;
//            }
//        }
//    }
//
//}
