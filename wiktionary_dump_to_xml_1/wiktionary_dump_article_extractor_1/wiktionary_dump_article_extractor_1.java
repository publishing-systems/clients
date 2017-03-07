/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of wiktionary_dump_article_extractor_1, a submodule of the
 * wiktionary_dump_to_xml_1 package.
 *
 * wiktionary_dump_article_extractor_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * wiktionary_dump_article_extractor_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with wiktionary_dump_article_extractor_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/org.wiktionary/wiktionary_dump_to_xml_1/wiktionary_dump_article_extractor_1/wiktionary_dump_article_extractor_1.java
 * @brief wiktionary.org provides a dump of their articles (in Mediawiki wikitext format) in XML wrapping.
 *     This program extracts the individual articles from the XML wrapping.
 * @author Stephan Kreutzer
 * @since 2016-11-28
 */



import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import java.util.Iterator;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import java.util.Stack;



public class wiktionary_dump_article_extractor_1
{
    public wiktionary_dump_article_extractor_1()
    {
        
    }

    public static void main(String args[])
    {
        System.out.print("wiktionary_dump_article_extractor_1 Copyright (C) 2016-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/refugee-it/clients/ and the project website\n" +
                         "https://www.refugee-it.de.\n\n");

        wiktionary_dump_article_extractor_1 extractor = new wiktionary_dump_article_extractor_1();

        try
        {
            extractor.extract(args);
        }
        catch (ProgramTerminationException ex)
        {
            extractor.handleTermination(ex);
        }

        if (extractor.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(extractor.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wiktionary_dump_article_extractor_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                writer.write("<wiktionary-dump-article-extractor-1-result-information>\n");
                writer.write("  <success>\n");

                writer.write("    <wiktionary-article-files>\n");

                for (int i = 0, max = extractor.getOutputFileNames().size(); i < max; i++)
                {
                    writer.write("      <wiktionary-article-file name=\"" + extractor.getOutputFileNames().get(i) + "\"/>\n");
                }

                writer.write("    </wiktionary-article-files>\n");

                if (extractor.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = extractor.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = extractor.getInfoMessages().get(i);

                        writer.write("      <info-message number=\"" + i + "\">\n");
                        writer.write("        <timestamp>" + infoMessage.getTimestamp() + "</timestamp>\n");

                        String infoMessageText = infoMessage.getMessage();
                        String infoMessageId = infoMessage.getId();
                        String infoMessageBundle = infoMessage.getBundle();
                        Object[] infoMessageArguments = infoMessage.getArguments();

                        if (infoMessageBundle != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageBundle = infoMessageBundle.replaceAll("&", "&amp;");
                            infoMessageBundle = infoMessageBundle.replaceAll("<", "&lt;");
                            infoMessageBundle = infoMessageBundle.replaceAll(">", "&gt;");

                            writer.write("        <id-bundle>" + infoMessageBundle + "</id-bundle>\n");
                        }

                        if (infoMessageId != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageId = infoMessageId.replaceAll("&", "&amp;");
                            infoMessageId = infoMessageId.replaceAll("<", "&lt;");
                            infoMessageId = infoMessageId.replaceAll(">", "&gt;");

                            writer.write("        <id>" + infoMessageId + "</id>\n");
                        }

                        if (infoMessageText != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageText = infoMessageText.replaceAll("&", "&amp;");
                            infoMessageText = infoMessageText.replaceAll("<", "&lt;");
                            infoMessageText = infoMessageText.replaceAll(">", "&gt;");

                            writer.write("        <message>" + infoMessageText + "</message>\n");
                        }

                        if (infoMessageArguments != null)
                        {
                            writer.write("        <arguments>\n");

                            int argumentCount = infoMessageArguments.length;

                            for (int j = 0; j < argumentCount; j++)
                            {
                                if (infoMessageArguments[j] == null)
                                {
                                    writer.write("          <argument number=\"" + j + "\">\n");
                                    writer.write("            <class></class>\n");
                                    writer.write("            <value>null</value>\n");
                                    writer.write("          </argument>\n");

                                    continue;
                                }

                                String className = infoMessageArguments[j].getClass().getName();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                className = className.replaceAll("&", "&amp;");
                                className = className.replaceAll("<", "&lt;");
                                className = className.replaceAll(">", "&gt;");

                                String value = infoMessageArguments[j].toString();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                value = value.replaceAll("&", "&amp;");
                                value = value.replaceAll("<", "&lt;");
                                value = value.replaceAll(">", "&gt;");

                                writer.write("          <argument number=\"" + j + "\">\n");
                                writer.write("            <class>" + className + "</class>\n");
                                writer.write("            <value>" + value + "</value>\n");
                                writer.write("          </argument>\n");
                            }

                            writer.write("        </arguments>\n");
                        }

                        Exception exception = infoMessage.getException();

                        if (exception != null)
                        {
                            writer.write("        <exception>\n");

                            String className = exception.getClass().getName();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            className = className.replaceAll("&", "&amp;");
                            className = className.replaceAll("<", "&lt;");
                            className = className.replaceAll(">", "&gt;");

                            writer.write("          <class>" + className + "</class>\n");

                            StringWriter stringWriter = new StringWriter();
                            PrintWriter printWriter = new PrintWriter(stringWriter);
                            exception.printStackTrace(printWriter);
                            String stackTrace = stringWriter.toString();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            stackTrace = stackTrace.replaceAll("&", "&amp;");
                            stackTrace = stackTrace.replaceAll("<", "&lt;");
                            stackTrace = stackTrace.replaceAll(">", "&gt;");

                            writer.write("          <stack-trace>" + stackTrace + "</stack-trace>\n");
                            writer.write("        </exception>\n");
                        }

                        writer.write("      </info-message>\n");
                    }

                    writer.write("    </info-messages>\n");
                }

                writer.write("  </success>\n");
                writer.write("</wiktionary-dump-article-extractor-1-result-information>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
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
        }
    }

    private static final int XMLTREE_PAGE = 1;
    private static final int XMLTREE_NS = 2;
    private static final int XMLTREE_ID = 2;
    private static final int XMLTREE_TITLE = 2;
    private static final int XMLTREE_REVISION = 2;
    private static final int XMLTREE_TEXT = 3;

    public int extract(String args[])
    {
        this.outputFileNames.clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\twiktionary_dump_article_extractor_1 " + getI10nString("messageParameterList") + "\n");
        }

        File resultInfoFile = new File(args[1]);

        try
        {
            resultInfoFile = resultInfoFile.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageResultInfoFileCantGetCanonicalPath", ex, null, resultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageResultInfoFileCantGetCanonicalPath", ex, null, resultInfoFile.getAbsolutePath());
        }

        if (resultInfoFile.exists() == true)
        {
            if (resultInfoFile.isDirectory() == true)
            {
                throw constructTermination("messageResultInfoPathIsADirectory", null, null, resultInfoFile.getAbsolutePath());
            }

            if (resultInfoFile.canWrite() != true)
            {
                throw constructTermination("messageResultInfoFileIsntWritable", null, null, resultInfoFile.getAbsolutePath());
            }
        }

        wiktionary_dump_article_extractor_1.resultInfoFile = resultInfoFile;

        File jobFile = new File(args[0]);

        try
        {
            jobFile = jobFile.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageJobFileCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJobFileCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath());
        }

        if (jobFile.exists() != true)
        {
            throw constructTermination("messageJobFileDoesntExist", null, null, jobFile.getAbsolutePath());
        }

        if (jobFile.isFile() != true)
        {
            throw constructTermination("messageJobPathIsntAFile", null, null, jobFile.getAbsolutePath());
        }

        if (jobFile.canRead() != true)
        {
            throw constructTermination("messageJobFileIsntReadable", null, null, jobFile.getAbsolutePath());
        }

        System.out.println("wiktionary_dump_article_extractor_1: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        File inputFile = null;
        File outputDirectory = null;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(jobFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("input-file") == true)
                    {
                        StartElement inputFileElement = event.asStartElement();
                        Attribute pathAttribute = inputFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (inputFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        inputFile = new File(pathAttribute.getValue());

                        if (inputFile.isAbsolute() != true)
                        {
                            inputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

                        try
                        {
                            inputFile = inputFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile.exists() != true)
                        {
                            throw constructTermination("messageInputFileDoesntExist", null, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile.isFile() != true)
                        {
                            throw constructTermination("messageInputPathIsntAFile", null, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile.canRead() != true)
                        {
                            throw constructTermination("messageInputFileIsntReadable", null, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile != null &&
                            outputDirectory != null)
                        {
                            break;
                        }
                    }
                    else if (tagName.equals("output-directory") == true)
                    {
                        StartElement outputDirectoryElement = event.asStartElement();
                        Attribute pathAttribute = outputDirectoryElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (outputDirectory != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        outputDirectory = new File(pathAttribute.getValue());

                        if (outputDirectory.isAbsolute() != true)
                        {
                            outputDirectory = new File(jobFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

                        try
                        {
                            outputDirectory = outputDirectory.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageOutputDirectoryCantGetCanonicalPath", ex, null, outputDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageOutputDirectoryCantGetCanonicalPath", ex, null, outputDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (outputDirectory.exists() == true)
                        {
                            if (outputDirectory.isDirectory() == true)
                            {
                                if (outputDirectory.canWrite() != true)
                                {
                                    throw constructTermination("messageOutputDirectoryIsntWritable", null, null, outputDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                                }
                            }
                            else
                            {
                                throw constructTermination("messageOutputPathIsntDirectory", null, null, outputDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                            }
                        }
                        else
                        {
                            try
                            {
                                outputDirectory.mkdirs();
                            }
                            catch (SecurityException ex)
                            {
                                throw constructTermination("messageOutputDirectoryCantCreate", ex, null, outputDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                            }
                        }

                        if (inputFile != null &&
                            outputDirectory != null)
                        {
                            break;
                        }
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageJobFileErrorWhileReading", ex, null, jobFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageJobFileErrorWhileReading", ex, null, jobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJobFileErrorWhileReading", ex, null, jobFile.getAbsolutePath());
        }

        if (inputFile == null)
        {
            throw constructTermination("messageJobFileInputFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "input-file");
        }

        if (outputDirectory == null)
        {
            throw constructTermination("messageJobFileOutputDirectoryIsntConfigured", null, null, jobFile.getAbsolutePath(), "output-directory");
        }


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(inputFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            Stack<String> elementStack = new Stack<String>();

            StringBuilder sbNs = null;
            StringBuilder sbTitle = null;
            StringBuilder sbId = null;
            StringBuilder sbText = null;

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    QName elementName = event.asStartElement().getName();
                    String fullElementName = elementName.getLocalPart();

                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }

                    if (fullElementName.equals("ns") == true)
                    {
                        if (elementStack.size() - 1 == XMLTREE_PAGE)
                        {
                            if (elementStack.get(XMLTREE_PAGE).equals("page") == true)
                            {
                                sbNs = new StringBuilder();
                            }
                        }
                    }
                    else if (fullElementName.equals("id") == true)
                    {
                        if (elementStack.size() - 1 == XMLTREE_PAGE)
                        {
                            if (elementStack.get(XMLTREE_PAGE).equals("page") == true)
                            {
                                if (sbId != null)
                                {
                                    throw constructTermination("messagePageIdFoundMoreThanOnce", null, null, inputFile.getAbsolutePath());
                                }

                                sbId = new StringBuilder();
                            }
                        }
                    }
                    else if (fullElementName.equals("title") == true)
                    {
                        if (elementStack.size() - 1 == XMLTREE_PAGE)
                        {
                            if (elementStack.get(XMLTREE_PAGE).equals("page") == true)
                            {
                                sbTitle = new StringBuilder();
                            }
                        }
                    }
                    else if (fullElementName.equals("text") == true)
                    {
                        if (elementStack.size() - 1 == XMLTREE_REVISION)
                        {
                            if (elementStack.get(XMLTREE_PAGE).equals("page") == true &&
                                elementStack.get(XMLTREE_REVISION).equals("revision") == true)
                            {
                                sbText = new StringBuilder();
                            }
                        }  
                    }

                    elementStack.push(fullElementName);
                }
                else if (event.isEndElement() == true)
                {
                    QName elementName = event.asEndElement().getName();
                    String fullElementName = elementName.getLocalPart();

                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }

                    elementStack.pop();

                    if (fullElementName.equals("page") == true)
                    {
                        if (sbNs != null)
                        {
                            if (sbNs.toString().equals("0") == true)
                            {
                                if (sbId != null)
                                {
                                    if (sbText != null)
                                    {
                                        String outputFileName = sbId.toString() + ".wikitext";
                                        File outFile = new File(outputDirectory.getAbsolutePath() + File.separator + outputFileName);

                                        try
                                        {
                                            BufferedWriter writer = new BufferedWriter(
                                                                    new OutputStreamWriter(
                                                                    new FileOutputStream(outFile),
                                                                    "UTF-8"));

                                            writer.write(sbText.toString());
                                            writer.flush();
                                            writer.close();
                                        }
                                        catch (FileNotFoundException ex)
                                        {
                                            throw constructTermination("messageOutputFileErrorWhileWriting", ex, null, outFile.getAbsolutePath());
                                        }
                                        catch (UnsupportedEncodingException ex)
                                        {
                                            throw constructTermination("messageOutputFileErrorWhileWriting", ex, null, outFile.getAbsolutePath());
                                        }
                                        catch (IOException ex)
                                        {
                                            throw constructTermination("messageOutputFileErrorWhileWriting", ex, null, outFile.getAbsolutePath());
                                        }

                                        this.outputFileNames.add(outputFileName);
                                    }
                                }
                            }
                        }

                        sbNs = null;
                        sbId = null;
                        sbTitle = null;
                        sbText = null;
                    }
                }
                else if (event.isCharacters() == true)
                {
                    /* Print current path.
                    Iterator<String> iter = elementStack.iterator();

                    while (iter.hasNext())
                    {
                        System.out.print("/");
                        System.out.print(iter.next());
                    }

                    System.out.print("\n");
                    */

                    if (elementStack.size() - 1 == XMLTREE_NS)
                    {
                        if (elementStack.get(XMLTREE_PAGE).equals("page") == true &&
                            elementStack.get(XMLTREE_NS).equals("ns") == true)
                        {
                            if (sbNs != null)
                            {
                                sbNs.append(event.asCharacters().getData());
                            }
                        }
                    }

                    if (elementStack.size() - 1 == XMLTREE_ID)
                    {
                        if (elementStack.get(XMLTREE_PAGE).equals("page") == true &&
                            elementStack.get(XMLTREE_ID).equals("id") == true)
                        {
                            if (sbId != null)
                            {
                                sbId.append(event.asCharacters().getData());
                            }
                        }
                    }

                    if (elementStack.size() - 1 == XMLTREE_TITLE)
                    {
                        if (elementStack.get(XMLTREE_PAGE).equals("page") == true &&
                            elementStack.get(XMLTREE_TITLE).equals("title") == true)
                        {
                            if (sbTitle != null)
                            {
                                sbTitle.append(event.asCharacters().getData());
                            }
                        }
                    }

                    if (elementStack.size() - 1 == XMLTREE_TEXT)
                    {
                        if (elementStack.get(XMLTREE_PAGE).equals("page") == true &&
                            elementStack.get(XMLTREE_REVISION).equals("revision") == true &&
                            elementStack.get(XMLTREE_TEXT).equals("text") == true)
                        {
                            if (sbText != null)
                            {
                                sbText.append(event.asCharacters().getData());
                            }
                        }
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
        }

        return 0;
    }

    public InfoMessage constructInfoMessage(String id,
                                            boolean outputToConsole,
                                            Exception exception,
                                            String message,
                                            Object ... arguments)
    {
        if (message == null)
        {
            if (arguments == null)
            {
                message = "wiktionary_dump_article_extractor_1: " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_dump_article_extractor_1: " + getI10nStringFormatted(id, arguments);
            }
        }

        if (outputToConsole == true)
        {
            System.out.println(message);

            if (exception != null)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        return new InfoMessage(id, exception, message, L10N_BUNDLE, arguments);
    }

    public ProgramTerminationException constructTermination(String id, Exception cause, String message, Object ... arguments)
    {
        if (message == null)
        {
            if (arguments == null)
            {
                message = "wiktionary_dump_article_extractor_1: " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_dump_article_extractor_1: " + getI10nStringFormatted(id, arguments);
            }
        }

        return new ProgramTerminationException(id, cause, message, L10N_BUNDLE, arguments);
    }

    public int handleTermination(ProgramTerminationException ex)
    {
        String message = ex.getMessage();
        String id = ex.getId();
        String bundle = ex.getBundle();
        Object[] arguments = ex.getArguments();
        boolean normalTermination = ex.isNormalTermination();

        if (message != null)
        {
            System.err.println(message);
        }

        Throwable innerException = ex.getCause();

        if (innerException != null)
        {
            System.out.println(innerException.getMessage());
            innerException.printStackTrace();
        }

        if (wiktionary_dump_article_extractor_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wiktionary_dump_article_extractor_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wiktionary_dump_article_extractor_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                writer.write("<wiktionary-dump-article-extractor-1-result-information>\n");

                if (normalTermination == false)
                {
                    writer.write("  <failure>\n");
                }
                else
                {
                    writer.write("  <success>\n");
                }

                writer.write("    <timestamp>" + ex.getTimestamp() + "</timestamp>\n");

                if (bundle != null)
                {
                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    bundle = bundle.replaceAll("&", "&amp;");
                    bundle = bundle.replaceAll("<", "&lt;");
                    bundle = bundle.replaceAll(">", "&gt;");

                    writer.write("    <id-bundle>" + bundle + "</id-bundle>\n");
                }

                if (id != null)
                {
                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    id = id.replaceAll("&", "&amp;");
                    id = id.replaceAll("<", "&lt;");
                    id = id.replaceAll(">", "&gt;");

                    writer.write("    <id>" + id + "</id>\n");
                }

                if (message != null)
                {
                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    message = message.replaceAll("&", "&amp;");
                    message = message.replaceAll("<", "&lt;");
                    message = message.replaceAll(">", "&gt;");

                    writer.write("    <message>" + message + "</message>\n");
                }

                if (arguments != null)
                {
                    writer.write("    <arguments>\n");

                    int argumentCount = arguments.length;

                    for (int i = 0; i < argumentCount; i++)
                    {
                        if (arguments[i] == null)
                        {
                            writer.write("      <argument number=\"" + i + "\">\n");
                            writer.write("        <class></class>\n");
                            writer.write("        <value>null</value>\n");
                            writer.write("      </argument>\n");

                            continue;
                        }

                        String className = arguments[i].getClass().getName();

                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        className = className.replaceAll("&", "&amp;");
                        className = className.replaceAll("<", "&lt;");
                        className = className.replaceAll(">", "&gt;");

                        String value = arguments[i].toString();

                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        value = value.replaceAll("&", "&amp;");
                        value = value.replaceAll("<", "&lt;");
                        value = value.replaceAll(">", "&gt;");

                        writer.write("      <argument number=\"" + i + "\">\n");
                        writer.write("        <class>" + className + "</class>\n");
                        writer.write("        <value>" + value + "</value>\n");
                        writer.write("      </argument>\n");
                    }

                    writer.write("    </arguments>\n");
                }

                if (innerException != null)
                {
                    writer.write("    <exception>\n");

                    String className = innerException.getClass().getName();

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    className = className.replaceAll("&", "&amp;");
                    className = className.replaceAll("<", "&lt;");
                    className = className.replaceAll(">", "&gt;");

                    writer.write("      <class>" + className + "</class>\n");

                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    innerException.printStackTrace(printWriter);
                    String stackTrace = stringWriter.toString();

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    stackTrace = stackTrace.replaceAll("&", "&amp;");
                    stackTrace = stackTrace.replaceAll("<", "&lt;");
                    stackTrace = stackTrace.replaceAll(">", "&gt;");

                    writer.write("      <stack-trace>" + stackTrace + "</stack-trace>\n");
                    writer.write("    </exception>\n");
                }

                if (this.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = this.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = this.getInfoMessages().get(i);

                        writer.write("      <info-message>\n");
                        writer.write("        <timestamp>" + infoMessage.getTimestamp() + "</timestamp>\n");

                        String infoMessageText = infoMessage.getMessage();
                        String infoMessageId = ex.getId();
                        String infoMessageBundle = ex.getBundle();
                        Object[] infoMessageArguments = ex.getArguments();

                        if (infoMessageBundle != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageBundle = infoMessageBundle.replaceAll("&", "&amp;");
                            infoMessageBundle = infoMessageBundle.replaceAll("<", "&lt;");
                            infoMessageBundle = infoMessageBundle.replaceAll(">", "&gt;");

                            writer.write("        <id-bundle>" + infoMessageBundle + "</id-bundle>\n");
                        }

                        if (infoMessageId != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageId = infoMessageId.replaceAll("&", "&amp;");
                            infoMessageId = infoMessageId.replaceAll("<", "&lt;");
                            infoMessageId = infoMessageId.replaceAll(">", "&gt;");

                            writer.write("        <id>" + infoMessageId + "</id>\n");
                        }

                        if (infoMessageText != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageText = infoMessageText.replaceAll("&", "&amp;");
                            infoMessageText = infoMessageText.replaceAll("<", "&lt;");
                            infoMessageText = infoMessageText.replaceAll(">", "&gt;");

                            writer.write("        <message>" + infoMessageText + "</message>\n");
                        }

                        if (infoMessageArguments != null)
                        {
                            writer.write("        <arguments>\n");

                            int argumentCount = infoMessageArguments.length;

                            for (int j = 0; j < argumentCount; j++)
                            {
                                if (infoMessageArguments[j] == null)
                                {
                                    writer.write("          <argument number=\"" + j + "\">\n");
                                    writer.write("            <class></class>\n");
                                    writer.write("            <value>null</value>\n");
                                    writer.write("          </argument>\n");

                                    continue;
                                }

                                String className = infoMessageArguments[j].getClass().getName();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                className = className.replaceAll("&", "&amp;");
                                className = className.replaceAll("<", "&lt;");
                                className = className.replaceAll(">", "&gt;");

                                String value = infoMessageArguments[j].toString();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                value = value.replaceAll("&", "&amp;");
                                value = value.replaceAll("<", "&lt;");
                                value = value.replaceAll(">", "&gt;");

                                writer.write("          <argument number=\"" + j + "\">\n");
                                writer.write("            <class>" + className + "</class>\n");
                                writer.write("            <value>" + value + "</value>\n");
                                writer.write("          </argument>\n");
                            }

                            writer.write("        </arguments>\n");
                        }

                        writer.write("      </info-message>\n");
                    }

                    writer.write("    </info-messages>\n");
                }

                if (normalTermination == false)
                {
                    writer.write("  </failure>\n");
                }
                else
                {
                    writer.write("  </success>\n");
                }

                writer.write("</wiktionary-dump-article-extractor-1-result-information>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex2)
            {
                ex2.printStackTrace();
            }
            catch (UnsupportedEncodingException ex2)
            {
                ex2.printStackTrace();
            }
            catch (IOException ex2)
            {
                ex2.printStackTrace();
            }
        }

        wiktionary_dump_article_extractor_1.resultInfoFile = null;

        System.exit(-1);
        return -1;
    }

    public List<String> getOutputFileNames()
    {
        return this.outputFileNames;
    }

    public List<InfoMessage> getInfoMessages()
    {
        return this.infoMessages;
    }

    public Locale getLocale()
    {
        return Locale.getDefault();
    }

    /**
     * @brief This method interprets l10n strings from a .properties file as encoded in UTF-8.
     */
    private String getI10nString(String key)
    {
        if (this.l10nConsole == null)
        {
            this.l10nConsole = ResourceBundle.getBundle(L10N_BUNDLE, this.getLocale());
        }

        try
        {
            return new String(this.l10nConsole.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return this.l10nConsole.getString(key);
        }
    }

    private String getI10nStringFormatted(String i10nStringName, Object ... arguments)
    {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(this.getLocale());

        formatter.applyPattern(getI10nString(i10nStringName));
        return formatter.format(arguments);
    }

    protected List<String> outputFileNames = new ArrayList<String>();

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nWiktionaryDumpArticleExtractor1Console";
    private ResourceBundle l10nConsole;
}
