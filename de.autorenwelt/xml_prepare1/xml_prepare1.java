/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of xml_prepare1.
 *
 * xml_prepare1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * xml_prepare1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with xml_prepare1. If not, see <http://www.gnu.org/licenses/>.
 */



import java.io.File;
import javax.xml.stream.XMLInputFactory;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.Comment;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.namespace.QName;
import java.util.Iterator;
import javax.xml.stream.events.Attribute;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.text.Normalizer;
import java.net.URLDecoder;



public class xml_prepare1
{
    public static void main(String args[])
    {
        System.out.print("xml_prepare1 Copyright (C) 2015 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/publishing-systems/clients/\n" +
                         "and the project website http://www.publishing-systems.org.\n\n");

        if (args.length < 2)
        {
            System.out.println("Usage:\n" +
                               "\txml_prepare1 in-file out-file\n\n");
            System.exit(1);
        }

        String programPath = xml_prepare1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try
        {
            programPath = new File(programPath).getCanonicalPath() + File.separator;
            programPath = URLDecoder.decode(programPath, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        File inFile = new File(args[0]);

        if (inFile.exists() != true)
        {
            System.out.println("xml_prepare1: Input file '" + inFile.getAbsolutePath() + "' doesn't exist.");
            System.exit(-1);
        }

        if (inFile.isFile() != true)
        {
            System.out.println("xml_prepare1: Input path '" + inFile.getAbsolutePath() + "' isn't a file.");
            System.exit(-1);
        }

        if (inFile.canRead() != true)
        {
            System.out.println("xml_prepare1: Input file '" + inFile.getAbsolutePath() + "' isn't readable.");
            System.exit(-1);
        }

        File outFile = new File(args[1]);

        if (outFile.exists() == true)
        {
            if (outFile.isFile() == true)
            {
                if (outFile.canWrite() != true)
                {
                    System.out.println("xml_prepare1: Output file '" + outFile.getAbsolutePath() + "' isn't writable.");
                    System.exit(-1);
                }
            }
            else
            {
                System.out.println("xml_prepare1: Output path '" + outFile.getAbsolutePath() + "' does already exist, but isn't a file.");
                System.exit(-1);
            }
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() != true)
        {
            if (tempDirectory.mkdir() != true)
            {
                System.out.print("xml_prepare1: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (tempDirectory.isDirectory() != true)
            {
                System.out.print("xml_prepare1: Temp directory path '" + tempDirectory.getAbsolutePath() + "' exists, but isn't a directory.\n");
                System.exit(-1);
            }
        }

        File tempFile = new File(tempDirectory.getAbsolutePath() + File.separator + "temp.xml");

        if (tempFile.exists() == true)
        {
            if (tempFile.isFile() == true)
            {
                if (tempFile.canWrite() != true)
                {
                    System.out.println("xml_prepare1: Temporary file '" + tempFile.getAbsolutePath() + "' isn't writable.");
                    System.exit(-1);
                }
            }
            else
            {
                System.out.println("xml_prepare1: Temporary path '" + inFile.getAbsolutePath() + "' does already exist, but isn't a file.");
                System.exit(-1);
            }
        }


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(inFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF-8");

            XMLEvent event = null;

            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(tempFile),
                                    "UTF-8"));

            StringBuilder textTitel = null;

            boolean element = false;
            boolean titel = false;

            while (eventReader.hasNext() == true)
            {
                event = eventReader.nextEvent();

                if (event.isStartDocument() == true)
                {
                    StartDocument startDocument = (StartDocument)event;

                    writer.write("<?xml version=\"" + startDocument.getVersion() + "\"");

                    if (startDocument.encodingSet() == true)
                    {
                        writer.write(" encoding=\"" + startDocument.getCharacterEncodingScheme() + "\"");
                    }

                    if (startDocument.standaloneSet() == true)
                    {
                        writer.write(" standalone=\"");

                        if (startDocument.isStandalone() == true)
                        {
                            writer.write("yes");
                        }
                        else
                        {
                            writer.write("no");
                        }

                        writer.write("\"");
                    }

                    writer.write("?>\n");
                }
                else if (event.isStartElement() == true)
                {
                    QName elementName = event.asStartElement().getName();
                    String fullElementName = elementName.getLocalPart();

                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }

                    if (fullElementName.equals("element") == true)
                    {
                        if (element == true)
                        {
                            System.out.println("xml_prepare1: Nested tag '" + fullElementName + "' was found in input file '" + inFile.getAbsolutePath() + "'.");
                            System.exit(-1);
                        }

                        element = true;
                    }
                    else if (element == true &&
                             fullElementName.equals("Titel") == true)
                    {
                        if (titel == true)
                        {
                            System.out.println("xml_prepare1: Nested tag '" + fullElementName + "' was found in input file '" + inFile.getAbsolutePath() + "'.");
                            System.exit(-1);
                        }

                        textTitel = new StringBuilder();
                        titel = true;
                    }

                    writer.write("<" + fullElementName);

                    // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                    @SuppressWarnings("unchecked")
                    Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();

                    while (attributes.hasNext() == true)
                    {
                        Attribute attribute = attributes.next();
                        QName attributeName = attribute.getName();
                        String fullAttributeName = attributeName.getLocalPart();

                        if (attributeName.getPrefix().length() > 0)
                        {
                            fullAttributeName = attributeName.getPrefix() + ":" + fullAttributeName;
                        }

                        String attributeValue = attribute.getValue();

                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        attributeValue = attributeValue.replaceAll("&", "&amp;");
                        attributeValue = attributeValue.replaceAll("\"", "&quot;");
                        attributeValue = attributeValue.replaceAll("'", "&apos;");
                        attributeValue = attributeValue.replaceAll("<", "&lt;");
                        attributeValue = attributeValue.replaceAll(">", "&gt;");

                        writer.write(" " + fullAttributeName + "=\"" + attributeValue + "\"");
                    }

                    writer.write(">");
                }
                else if (event.isEndElement() == true)
                {
                    boolean output = true;

                    QName elementName = event.asEndElement().getName();
                    String fullElementName = elementName.getLocalPart();

                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }

                    if (fullElementName.equals("element") == true)
                    {
                        if (element == false)
                        {
                            System.out.println("xml_prepare1: End tag '" + fullElementName + "' found without corresponding start tag.");
                            System.exit(-1);
                        }

                        if (titel == true)
                        {
                            System.out.println("xml_prepare1: End tag 'Titel' is missing.");
                            System.exit(-1);
                        }

                        element = false;
                    }
                    else if (element == true &&
                             fullElementName.equals("Titel") == true)
                    {
                        if (titel == false)
                        {
                            System.out.println("xml_prepare1: End tag '" + fullElementName + "' found without corresponding start tag.");
                            System.exit(-1);
                        }

                        output = false;
                        writer.write("</" + fullElementName + ">");

                        String handle = textTitel.toString();
                        handle = Normalizer.normalize(handle, Normalizer.Form.NFKC);

                        handle = handle.replace(" ", "-");
                        handle = handle.replace("	", "-");
                        handle = handle.replace("_", "-");
                        handle = handle.toLowerCase();
                        // Always build with javac option "-encoding UTF-8"!
                        handle = handle.replace("ä", "ae");
                        handle = handle.replace("ö", "oe");
                        handle = handle.replace("ü", "ue");
                        handle = handle.replace("ß", "ss");

                        while (handle.contains("--") == true)
                        {
                            handle = handle.replace("--", "-");
                        }

                        handle = handle.replaceAll("[^a-z0-9-]","");

                        writer.write("\n            <Handle>" + handle + "</Handle>");

                        titel = false;
                        textTitel = null;
                    }

                    if (output == true)
                    {
                        writer.write("</" + fullElementName + ">");
                    }
                }
                else if (event.isCharacters() == true)
                {
                    event.writeAsEncodedUnicode(writer);

                    if (titel == true)
                    {
                        if (textTitel == null)
                        {
                            System.out.println("xml_prepare1: No StringBuilder present for text content of tag 'Titel'.");
                            System.exit(-1);
                        }

                        textTitel.append(event.asCharacters().getData());
                    }
                }
                else if (event.getEventType() == XMLStreamConstants.COMMENT)
                {
                    writer.write("<!--" + ((Comment)event).getText() + "-->");
                }
                else if (event.isEndDocument() == true)
                {

                }
                else
                {
                    System.out.println("xml_prepare1: Unsupported XML event type " + event.getEventType() + " (see javax.xml.stream.XMLStreamConstants for more details). The output file will be incomplete.");
                }
            }

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (XMLStreamException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        if (CopyFileBinary(tempFile, outFile) != 0)
        {
            System.exit(-1);
        }

        if (tempFile.delete() != true)
        {
            System.out.println("xml_prepare1: Can't delete temporary file '" + tempFile.getAbsolutePath() + "'.");
        }
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("xml_prepare1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }

        if (from.isFile() != true)
        {
            System.out.println("xml_prepare1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }

        if (from.canRead() != true)
        {
            System.out.println("xml_prepare1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
            return -3;
        }

        if (to.exists() == true)
        {
            if (to.isFile() == true)
            {
                if (to.canWrite() != true)
                {
                    System.out.println("xml_prepare1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + to.getAbsolutePath() + "' does already exist, but isn't writable.");
                    return -4;
                }
            }
            else
            {
                System.out.println("xml_prepare1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + to.getAbsolutePath() + "' does already exist, but isn't a file.");
                return -5;
            }
        }


        byte[] buffer = new byte[1024];

        try
        {
            if (to.exists() != true)
            {
                to.createNewFile();
            }

            FileInputStream reader = new FileInputStream(from);
            FileOutputStream writer = new FileOutputStream(to);

            int bytesRead = reader.read(buffer, 0, buffer.length);

            while (bytesRead > 0)
            {
                writer.write(buffer, 0, bytesRead);
                bytesRead = reader.read(buffer, 0, buffer.length);
            }

            writer.close();
            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(-1);
        }

        return 0;
    }
}
