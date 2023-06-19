/*******************************************************************************
 * ============LICENSE_START====================================================
 * * org.onap.ccsdk
 * * ===========================================================================
 * * Copyright © 2023 AT&T Intellectual Property. All rights reserved.
 * * ===========================================================================
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 * *
 *  * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * *
 ******************************************************************************/

package org.onap.ccsdk.apps.cadi.wsse.test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.wsse.XEvent;
import org.onap.ccsdk.apps.cadi.wsse.XReader;

public class JU_XReader {

    private final static String TEST_DIR_NAME = "src/test/resources";
    private final static String TEST_XML_NAME = "test.xml";
    private static File testXML;

    private final static String COMMENT = "a comment";
    private final static String OUTER_TAG = "outerTag";
    private final static String INNER_TAG = "innerTag";
    private final static String DATA_TAG = "dataTag";
    private final static String DATA = "some text that represents data";
    private final static String SELF_CLOSING_TAG = "selfClosingTag";
    private final static String PREFIX = "prefix";
    private final static String SUFFIX = "suffix";

    @BeforeClass
    public static void setupOnce() throws IOException {
        testXML = setupXMLFile();
    }

    @AfterClass
    public static void tearDownOnce() {
        testXML.delete();
    }

    @Test
    public void test() throws XMLStreamException, IOException {
        FileInputStream fis = new FileInputStream(TEST_DIR_NAME + '/' + TEST_XML_NAME);
        try {
            XReader xr = new XReader(fis);
            assertThat(xr.hasNext(), is(true));
            XEvent xe;

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.START_DOCUMENT));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.START_ELEMENT));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.COMMENT));
            assertThat(((XEvent.Comment)xe).value, is(COMMENT));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.START_ELEMENT));
            assertThat(xe.asStartElement().getName().toString(), is(OUTER_TAG));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.START_ELEMENT));
            assertThat(xe.asStartElement().getName().toString(), is(INNER_TAG));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.START_ELEMENT));
            assertThat(xe.asStartElement().getName().toString(), is(DATA_TAG));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.CHARACTERS));
            assertThat(xe.asCharacters().getData().toString(), is(DATA));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.END_ELEMENT));
            assertThat(xe.asEndElement().getName().toString(), is(DATA_TAG));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.START_ELEMENT));
            assertThat(xe.asStartElement().getName().toString(), is(SELF_CLOSING_TAG));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.START_ELEMENT));
            assertThat(xe.asStartElement().getName().toString(), is(SUFFIX));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.END_ELEMENT));
            assertThat(xe.asEndElement().getName().toString(), is(INNER_TAG));

            xe = getNextEvent(xr);
            assertThat(xe.getEventType(), is(XMLEvent.END_ELEMENT));
            assertThat(xe.asEndElement().getName().toString(), is(OUTER_TAG));

            assertThat(xr.hasNext(), is(false));

        } finally {
            fis.close();
        }
    }

    private static XEvent getNextEvent(XReader xr) throws XMLStreamException {
        if (xr.hasNext()) {
            return xr.nextEvent();
        }
        return null;
    }

    private static File setupXMLFile() throws IOException {
        File xmlFile = new File(TEST_DIR_NAME, TEST_XML_NAME);
        PrintWriter writer = new PrintWriter(xmlFile);
        writer.println("    ");  // Whitespace before the document - this is for coverage
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<!DOCTYPE xml>");
        writer.println("<!--" + COMMENT + "-->");
        writer.println("<" + OUTER_TAG + ">");
        writer.println("  <" + INNER_TAG + ">");
        writer.println("    <" + DATA_TAG + ">" + DATA + "</" + DATA_TAG + ">");
        writer.println("    <" + SELF_CLOSING_TAG + " withAnAttribute=\"That has nested \\\" marks\" />");
        writer.println("    <" + PREFIX + ":" + SUFFIX + "/>");
        writer.println("  </" + INNER_TAG + ">");
        writer.println("</" + OUTER_TAG + ">");
        writer.flush();
        writer.close();
        return xmlFile;
    }
}
