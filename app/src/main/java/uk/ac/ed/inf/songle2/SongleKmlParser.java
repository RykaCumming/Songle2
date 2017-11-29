package uk.ac.ed.inf.songle2;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s1540547 on 27/10/17.
 */

public class SongleKmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public ArrayList<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    private ArrayList readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Entry> entries = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "kml");
        while (parser.next() != XmlPullParser.END_TAG) { //while parse has not reached </kml> WRONG
            if (parser.getEventType() != XmlPullParser.START_TAG) { //??
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag

            if (name.equals("Placemark")) {
                entries.add(readEntry(parser));
            }
            else if (name.equals("Document")) {
                continue;
            }
            else {
                skip(parser);
            }
        }
        return entries;
    }

    public static class Point {
        public final String coordinate;

        private Point(String coordinate) {this.coordinate=coordinate;}
        public String getCoordinate()
        {
            return coordinate;
        }
    }
    public static class Entry {
        public final String name;
        public final String description;
        public final String styleUrl;
        public final String coordinate;

        private Entry(String name,String description,String styleUrl, String coordinate) {
            this.name = name;
            this.description = description;
            this.styleUrl = styleUrl;
            this.coordinate = coordinate;
        }

        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
        public String getStyleUrl() {
            return styleUrl;
        }
        public String getCoordinate() {
            return coordinate;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");
        String name = null;
        String description = null;
        String styleUrl = null;
        String coordinate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String thename = parser.getName();
            if (thename.equals("name")) {
                name = readName(parser);
            } else if (thename.equals("description")) {
                description = readDescription(parser);
            } else if (thename.equals("styleUrl")) {
                styleUrl = readstyleUrl(parser);
            } else if (thename.equals("Point")) {
                continue;
            }
            else if (thename.equals("coordinates")) {
                coordinate = readCoordinate(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Entry(name,description,styleUrl,coordinate);
    }
    // Processes title tags in the feed.
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
    private String readstyleUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "styleUrl");
        String styleUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "styleUrl");
        return styleUrl;
    }
    private Point readPoint(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Point");
        String coordinates = null;
        String thename = parser.getName();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (thename.equals("coordinates")) {
                coordinates = readCoordinate(parser);
            } else {
                skip(parser);
            }
        }
//        parser.require(XmlPullParser.END_TAG, ns, "point");
        return new Point(coordinates);
    }

    private String readCoordinate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String coordinate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        return coordinate;
    }



    // Processes link tags in the feed.



    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}




