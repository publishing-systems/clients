/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of wiktionary_dump_to_xml_1 workflow, a submodule of the
 * wiktionary_dump_to_xml_1 package.
 *
 * wiktionary_dump_to_xml_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * wiktionary_dump_to_xml_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with wiktionary_dump_to_xml_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/org.wiktionary/wiktionary_dump_to_xml_1/workflows/wiktionary_dump_to_xml_1/wiktionary_dump_to_xml_1.java
 * @brief wiktionary.org provides a dump of their articles (in Mediawiki wikitext format) in XML wrapping.
 *     This workflow converts all Mediawiki wikitext articles of the dump into XML.
 * @author Stephan Kreutzer
 * @since 2017-03-10
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
import java.util.Scanner;



public class wiktionary_dump_to_xml_1
{
    public static void main(String args[])
    {
        System.out.print("wiktionary_dump_to_xml_1 workflow Copyright (C) 2016-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/refugee-it/clients/ and the project website\n" +
                         "https://www.refugee-it.de.\n\n");

        wiktionary_dump_to_xml_1 converter = new wiktionary_dump_to_xml_1();

        try
        {
            converter.convert(args);
        }
        catch (ProgramTerminationException ex)
        {
            converter.handleTermination(ex);
        }

        if (converter.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(converter.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wiktionary_dump_to_xml_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                writer.write("<wiktionary-dump-to-xml-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (converter.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = converter.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = converter.getInfoMessages().get(i);

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

                if (converter.getOutputFileNames().size() > 0)
                {
                    writer.write("    <converted-files>\n");

                    for (int i = 0, max = converter.getOutputFileNames().size(); i < max; i++)
                    {
                        String name = converter.getOutputFileNames().get(i);

                        writer.write("      <converted-file name=\"" + name + "\"/>\n");
                    }

                    writer.write("    </converted-files>\n");
                }

                writer.write("  </success>\n");
                writer.write("</wiktionary-dump-to-xml-1-workflow-result-information>\n");
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

    public int convert(String args[])
    {
        this.outputFileNames.clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\twiktionary_dump_to_xml_1 " + getI10nString("messageParameterList") + "\n");
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
            if (resultInfoFile.isFile() != true)
            {
                throw constructTermination("messageResultInfoPathIsntAFile", null, null, resultInfoFile.getAbsolutePath());
            }

            if (resultInfoFile.canWrite() != true)
            {
                throw constructTermination("messageResultInfoFileIsntWritable", null, null, resultInfoFile.getAbsolutePath());
            }
        }

        wiktionary_dump_to_xml_1.resultInfoFile = resultInfoFile;

        String programPath = wiktionary_dump_to_xml_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try
        {
            programPath = new File(programPath).getCanonicalPath() + File.separator;
            programPath = URLDecoder.decode(programPath, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageCantDetermineProgramPath", ex, null);
        }
        catch (IOException ex)
        {
            throw constructTermination("messageCantDetermineProgramPath", ex, null);
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() == true)
        {
            if (tempDirectory.isDirectory() == true)
            {
                if (tempDirectory.canWrite() != true)
                {
                    throw constructTermination("messageTempDirectoryIsntWritable", null, null, tempDirectory.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageTempPathIsntDirectory", null, null, tempDirectory.getAbsolutePath());
            }
        }
        else
        {
            try
            {
                tempDirectory.mkdirs();
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageTempDirectoryCantCreate", null, null, tempDirectory.getAbsolutePath());
            }
        }

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

        System.out.println("wiktionary_dump_to_xml_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


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

        File wiktionaryDumpArticleExtractor1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "wiktionary_dump_article_extractor_1_resultinfo.xml");

        if (wiktionaryDumpArticleExtractor1ResultInfoFile.exists() == true)
        {
            if (wiktionaryDumpArticleExtractor1ResultInfoFile.isFile() != true)
            {
                throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoPathIsntAFile", null, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
            }

            if (wiktionaryDumpArticleExtractor1ResultInfoFile.canWrite() != true)
            {
                throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoFileIsntWritable", null, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
            }
        }

        File wiktionaryDumpArticleExtractor1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "wiktionary_dump_article_extractor_1_jobfile.xml");

        if (wiktionaryDumpArticleExtractor1JobFile.exists() == true)
        {
            if (wiktionaryDumpArticleExtractor1JobFile.isFile() != true)
            {
                throw constructTermination("messageWiktionaryDumpArticleExtractor1JobPathIsntAFile", null, null, wiktionaryDumpArticleExtractor1JobFile.getAbsolutePath());
            }

            if (wiktionaryDumpArticleExtractor1JobFile.canWrite() != true)
            {
                throw constructTermination("messageWiktionaryDumpArticleExtractor1JobFileIsntWritable", null, null, wiktionaryDumpArticleExtractor1JobFile.getAbsolutePath());
            }
        }

        File tempOutputDirectory = new File(tempDirectory.getAbsolutePath() + File.separator + "output");

        if (tempOutputDirectory.exists() == true)
        {
            if (tempOutputDirectory.isDirectory() == true)
            {
                if (tempOutputDirectory.canWrite() != true)
                {
                    throw constructTermination("messageTempOutputDirectoryIsntWritable", null, null, tempOutputDirectory.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageTempOutputPathIsntDirectory", null, null, tempOutputDirectory.getAbsolutePath());
            }
        }
        else
        {
            try
            {
                tempOutputDirectory.mkdirs();
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageTempOutputDirectoryCantCreate", ex, null, tempOutputDirectory.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(wiktionaryDumpArticleExtractor1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by wiktionary_dump_to_xml_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
            writer.write("<wiktionary-dump-article-extractor-1-job>\n");
            writer.write("  <input-file path=\"" + inputFile.getAbsolutePath() + "\"/>\n");
            writer.write("  <output-directory path=\"" + tempOutputDirectory.getAbsolutePath() + "\"/>\n");
            writer.write("<wiktionary-dump-article-extractor-1-job>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1JobFileErrorWhileWriting", ex, null, wiktionaryDumpArticleExtractor1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1JobFileErrorWhileWriting", ex, null, wiktionaryDumpArticleExtractor1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1JobFileErrorWhileWriting", ex, null, wiktionaryDumpArticleExtractor1JobFile.getAbsolutePath());
        }

        ProcessBuilder builder = new ProcessBuilder("java", "wiktionary_dump_article_extractor_1", wiktionaryDumpArticleExtractor1JobFile.getAbsolutePath(), wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + "wiktionary_dump_article_extractor_1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                System.out.println(scanner.next());
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1ErrorWhileReadingOutput", ex, null);
        }

        if (wiktionaryDumpArticleExtractor1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoFileDoesntExistButShould", null, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
        }

        if (wiktionaryDumpArticleExtractor1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoPathExistsButIsntAFile", null, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
        }

        if (wiktionaryDumpArticleExtractor1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoFileIsntReadable", null, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
        }

        boolean wasSuccess = false;
        List<File> articleFiles = new ArrayList<File>();

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(wiktionaryDumpArticleExtractor1ResultInfoFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("success") == true)
                    {
                        wasSuccess = true;
                    }
                    else if (tagName.equals("wiktionary-article-file") == true)
                    {
                        StartElement articleFileElement = event.asStartElement();
                        Attribute nameAttribute = articleFileElement.getAttributeByName(new QName("name"));

                        if (nameAttribute == null)
                        {
                            throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoFileEntryIsMissingAnAttribute", null, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath(), tagName, "name");
                        }

                        File articleFile = new File(tempOutputDirectory.getAbsolutePath() + File.separator + nameAttribute.getValue());

                        try
                        {
                            articleFile = articleFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageArticleFileCantGetCanonicalPath", ex, null, articleFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageArticleFileCantGetCanonicalPath", ex, null, articleFile.getAbsolutePath());
                        }

                        if (articleFile.exists() != true)
                        {
                            throw constructTermination("messageArticleFileDoesntExist", null, null, articleFile.getAbsolutePath(), wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
                        }

                        if (articleFile.isFile() != true)
                        {
                            throw constructTermination("messageArticlePathIsntAFile", null, null, articleFile.getAbsolutePath());
                        }

                        if (articleFile.canRead() != true)
                        {
                            throw constructTermination("messageArticleFileIsntReadable", null, null, articleFile.getAbsolutePath());
                        }

                        articleFiles.add(articleFile);
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoFileErrorWhileReading", ex, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoFileErrorWhileReading", ex, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1ResultInfoFileErrorWhileReading", ex, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageWiktionaryDumpArticleExtractor1CallWasntSuccessful", null, null, wiktionaryDumpArticleExtractor1ResultInfoFile.getAbsolutePath());

        }

        for (int i = 0; i < articleFiles.size(); i++)
        {
            File articleFile = articleFiles.get(i);

            File wiktionaryArticleToXml1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "wiktionary_article_to_xml_1_resultinfo_" + i + ".xml");

            if (wiktionaryArticleToXml1ResultInfoFile.exists() == true)
            {
                if (wiktionaryArticleToXml1ResultInfoFile.isFile() != true)
                {
                    throw constructTermination("messageWiktionaryArticleToXml1ResultInfoPathIsntAFile", null, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
                }

                if (wiktionaryArticleToXml1ResultInfoFile.canWrite() != true)
                {
                    throw constructTermination("messageWiktionaryArticleToXml1ResultInfoFileIsntWritable", null, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
                }
            }

            File wiktionaryArticleToXml1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "wiktionary_article_to_xml_1_jobfile_" + i + ".xml");

            if (wiktionaryArticleToXml1JobFile.exists() == true)
            {
                if (wiktionaryArticleToXml1JobFile.isFile() != true)
                {
                    throw constructTermination("messageWiktionaryArticleToXml1JobPathIsntAFile", null, null, wiktionaryArticleToXml1JobFile.getAbsolutePath());
                }

                if (wiktionaryArticleToXml1JobFile.canWrite() != true)
                {
                    throw constructTermination("messageWiktionaryArticleToXml1JobFileIsntWritable", null, null, wiktionaryArticleToXml1JobFile.getAbsolutePath());
                }
            }

            File outputFile = new File(outputDirectory.getAbsolutePath() + File.separator + i + ".xml");

            try
            {
                outputFile = outputFile.getCanonicalFile();
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, outputFile.getAbsolutePath(), jobFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, outputFile.getAbsolutePath(), jobFile.getAbsolutePath());
            }

            if (outputFile.exists() == true)
            {
                if (outputFile.isFile() == true)
                {
                    if (outputFile.canWrite() != true)
                    {
                        throw constructTermination("messageOutputFileIsntWritable", null, null, outputFile.getAbsolutePath());
                    }
                }
                else
                {
                    throw constructTermination("messageOutputPathIsntAFile", null, null, outputFile.getAbsolutePath());
                }
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wiktionaryArticleToXml1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wiktionary_dump_to_xml_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                writer.write("<wiktionary-article-to-xml-1-job>\n");
                writer.write("  <input-file path=\"" + articleFile.getAbsolutePath() + "\"/>\n");
                writer.write("  <output-file path=\"" + outputFile.getAbsolutePath() + "\"/>\n");
                writer.write("<wiktionary-article-to-xml-1-job>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                throw constructTermination("messageWiktionaryArticleToXml1JobFileErrorWhileWriting", ex, null, wiktionaryArticleToXml1JobFile.getAbsolutePath());
            }
            catch (UnsupportedEncodingException ex)
            {
                throw constructTermination("messageWiktionaryArticleToXml1JobFileErrorWhileWriting", ex, null, wiktionaryArticleToXml1JobFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageWiktionaryArticleToXml1JobFileErrorWhileWriting", ex, null, wiktionaryArticleToXml1JobFile.getAbsolutePath());
            }

            builder = new ProcessBuilder("java", "wiktionary_article_to_xml_1", wiktionaryArticleToXml1JobFile.getAbsolutePath(), wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + "wiktionary_article_to_xml_1"));
            builder.redirectErrorStream(true);

            try
            {
                Process process = builder.start();
                Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

                while (scanner.hasNext() == true)
                {
                    System.out.println(scanner.next());
                }

                scanner.close();
            }
            catch (IOException ex)
            {
                throw constructTermination("messageWiktionaryArticleToXml1ErrorWhileReadingOutput", ex, null);
            }

            if (wiktionaryArticleToXml1ResultInfoFile.exists() != true)
            {
                throw constructTermination("messageWiktionaryArticleToXml1ResultInfoFileDoesntExistButShould", null, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
            }

            if (wiktionaryArticleToXml1ResultInfoFile.isFile() != true)
            {
                throw constructTermination("messageWiktionaryArticleToXml1ResultInfoPathExistsButIsntAFile", null, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
            }

            if (wiktionaryArticleToXml1ResultInfoFile.canRead() != true)
            {
                throw constructTermination("messageWiktionaryArticleToXml1ResultInfoFileIsntReadable", null, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
            }

            wasSuccess = false;

            {
                try
                {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    InputStream in = new FileInputStream(wiktionaryArticleToXml1ResultInfoFile);
                    XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

                    while (eventReader.hasNext() == true)
                    {
                        XMLEvent event = eventReader.nextEvent();

                        if (event.isStartElement() == true)
                        {
                            String tagName = event.asStartElement().getName().getLocalPart();

                            if (tagName.equals("success") == true)
                            {
                                wasSuccess = true;
                            }
                        }
                    }
                }
                catch (XMLStreamException ex)
                {
                    throw constructTermination("messageWiktionaryArticleToXml1ResultInfoFileErrorWhileReading", ex, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
                }
                catch (SecurityException ex)
                {
                    throw constructTermination("messageWiktionaryArticleToXml1ResultInfoFileErrorWhileReading", ex, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageWiktionaryArticleToXml1ResultInfoFileErrorWhileReading", ex, null, wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath());
                }
            }

            if (wasSuccess == true)
            {
                this.outputFileNames.add(outputFile.getName());
            }
            else
            {
                this.infoMessages.add(constructInfoMessage("messageParsingError", true, null, null, wiktionaryArticleToXml1JobFile.getAbsolutePath(), wiktionaryArticleToXml1ResultInfoFile.getAbsolutePath()));
            }
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
                message = "wiktionary_dump_to_xml_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_dump_to_xml_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "wiktionary_dump_to_xml_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_dump_to_xml_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (wiktionary_dump_to_xml_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wiktionary_dump_to_xml_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wiktionary_dump_to_xml_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                writer.write("<wiktionary-dump-to-xml-1-workflow-result-information>\n");

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

                writer.write("</wiktionary-dump-to-xml-1-workflow-result-information>\n");
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

        wiktionary_dump_to_xml_1.resultInfoFile = null;

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

    private static final String L10N_BUNDLE = "l10n.l10nWiktionaryDumpToXml1WorkflowConsole";
    private ResourceBundle l10nConsole;
}
