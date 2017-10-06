/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of twitch_channel_id_obtainer_1 workflow of clients for the
 * automated_digital_publishing and digital_publishing_workflow_tools packages.
 *
 * twitch_channel_id_obtainer_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * twitch_channel_id_obtainer_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with twitch_channel_id_obtainer_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/tv.twitch/twitch_video_uploader_1/workflows/ttwitch_channel_id_obtainer/twitch_channel_id_obtainer_1/twitch_channel_id_obtainer_1.java
 * @brief Will obtain the ID of a Twitch channel by name.
 * @author Stephan Kreutzer
 * @since 2017-10-05
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
import java.util.Scanner;
import java.util.Stack;



public class twitch_channel_id_obtainer_1
{
    public static void main(String args[])
    {
        System.out.print("twitch_channel_id_obtainer_1 workflow Copyright (C) 2016-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/clients/ and the project website\n" +
                         "http://www.publishing-systems.org.\n\n");

        twitch_channel_id_obtainer_1 obtainer = new twitch_channel_id_obtainer_1();

        try
        {
            obtainer.obtain(args);
        }
        catch (ProgramTerminationException ex)
        {
            obtainer.handleTermination(ex);
        }

        if (obtainer.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(obtainer.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by twitch_channel_id_obtainer_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<twitch-channel-id-obtainer-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (obtainer.channelId != null)
                {
                    writer.write("    <channel id=\"" + obtainer.channelId + "\"/>\n");
                }

                if (obtainer.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = obtainer.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = obtainer.getInfoMessages().get(i);

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
                writer.write("</twitch-channel-id-obtainer-1-workflow-result-information>\n");
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

    public int obtain(String args[])
    {
        this.channelId = null;

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\ttwitch_channel_id_obtainer_1 " + getI10nString("messageParameterList") + "\n");
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

        twitch_channel_id_obtainer_1.resultInfoFile = resultInfoFile;

        String programPath = twitch_channel_id_obtainer_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("twitch_channel_id_obtainer_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        String channelName = null;

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

                    if (tagName.equals("channel") == true)
                    {
                        StartElement channelElement = event.asStartElement();
                        Attribute attributeName = channelElement.getAttributeByName(new QName("name"));

                        if (attributeName == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "name");
                        }

                        if (channelName != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        channelName = attributeName.getValue();
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

        if (channelName == null)
        {
            throw constructTermination("messageJobFileElementIsntConfigured", null, null, jobFile.getAbsolutePath(), "channel");
        }

        File httpsClient1JobFile = new File(tempDirectory + File.separator + "jobfile_https_client_1.xml");

        if (httpsClient1JobFile.exists() == true)
        {
            if (httpsClient1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = httpsClient1JobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (httpsClient1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageHttpsClient1JobFileExistsButIsntWritable", null, null, httpsClient1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageHttpsClient1JobPathExistsButIsntAFile", null, null, httpsClient1JobFile.getAbsolutePath());
            }
        }

        File responseFileJson = new File(tempDirectory.getAbsolutePath() + File.separator + "response.json");

        if (responseFileJson.exists() == true)
        {
            if (responseFileJson.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = responseFileJson.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (responseFileJson.canWrite() != true)
                    {
                        throw constructTermination("messageHttpsClient1ResponseFileExistsButIsntWritable", null, null, responseFileJson.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageHttpsClient1ResponsePathExistsButIsntAFile",  null, null, responseFileJson.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(httpsClient1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by twitch_channel_id_obtainer_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
            writer.write("<https-client-1-jobfile>\n");
            writer.write("  <request url=\"https://api.twitch.tv/kraken/users?login=" + escapeURL(channelName) + "\" method=\"GET\">\n");
            writer.write("    <header>\n");
            writer.write("      <field name=\"Accept\" value=\"application/vnd.twitchtv.v5+json\"/>\n");
            writer.write("      <field name=\"Client-ID\" value=\"feuq0hqseoxyg38p5inirhw2rstqce\"/>\n");
            writer.write("      <field name=\"charset\" value=\"utf-8\"/>\n");
            writer.write("    </header>\n");
            writer.write("  </request>\n");
            writer.write("  <response destination=\"" + responseFileJson.getAbsolutePath() + "\"/>\n");
            writer.write("</https-client-1-jobfile>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageHttpsClient1JobFileWritingError", ex, null, httpsClient1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageHttpsClient1JobFileWritingError", ex, null, httpsClient1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageHttpsClient1JobFileWritingError", ex, null, httpsClient1JobFile.getAbsolutePath());
        }

        File httpsClient1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_https_client_1.xml");

        if (httpsClient1ResultInfoFile.exists() == true)
        {
            if (httpsClient1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = httpsClient1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (httpsClient1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageHttpsClient1ResultInfoFileExistsButIsntWritable", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageHttpsClient1ResultInfoPathExistsButIsntAFile", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
            }
        }

        ProcessBuilder builder = new ProcessBuilder("java", "https_client_1", httpsClient1JobFile.getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "digital_publishing_workflow_tools" + File.separator + "https_client" + File.separator + "https_client_1"));
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
            throw constructTermination("messageHttpsClient1ErrorWhileReadingOutput", ex, null, httpsClient1JobFile.getAbsolutePath());
        }

        if (httpsClient1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageHttpsClient1ResultInfoFileDoesntExistButShould", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
        }

        if (httpsClient1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageHttpsClient1ResultInfoPathExistsButIsntAFile", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
        }

        if (httpsClient1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageHttpsClient1ResultInfoFileIsntReadable", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
        }

        boolean wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(httpsClient1ResultInfoFile);
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
                        break;
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageHttpsClient1ResultInfoFileErrorWhileReading", ex, null, httpsClient1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageHttpsClient1ResultInfoFileErrorWhileReading", ex, null, httpsClient1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageHttpsClient1ResultInfoFileErrorWhileReading", ex, null, httpsClient1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageHttpsClient1CallWasntSuccessful", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
        }

        if (responseFileJson.exists() != true)
        {
            throw constructTermination("messageHttpsClient1ResponseFileDoesntExist", null, null, responseFileJson.getAbsolutePath());
        }


        File jsonToXml1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_json_to_xml_1.xml");
        File jsonToXml1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_json_to_xml_1.xml");
        File responseFileXml = new File(tempDirectory.getAbsolutePath() + File.separator + "response.xml");

        if (jsonToXml1JobFile.exists() == true)
        {
            if (jsonToXml1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = jsonToXml1JobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (jsonToXml1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageJsonToXml1JobFileExistsButIsntWritable", null, null, jsonToXml1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageJsonToXml1JobFileExistsButIsntAFile", null, null, jsonToXml1JobFile.getAbsolutePath());
            }
        }

        if (jsonToXml1ResultInfoFile.exists() == true)
        {
            if (jsonToXml1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = jsonToXml1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (jsonToXml1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageJsonToXml1ResultInfoFileExistsButIsntWritable", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageJsonToXml1ResultInfoPathExistsButIsntAFile", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
            }
        }

        if (responseFileXml.exists() == true)
        {
            if (responseFileXml.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = responseFileXml.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (responseFileXml.canWrite() != true)
                    {
                        throw constructTermination("messageJsonToXml1ResultFileExistsButIsntWritable", null, null, responseFileXml.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageJsonToXml1ResultPathExistsButIsntAFile", null, null, responseFileXml.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(jsonToXml1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by twitch_channel_id_obtainer_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
            writer.write("<json-to-xml-1-jobfile>\n");
            writer.write("  <json-input-file path=\"" + responseFileJson.getAbsolutePath() + "\"/>\n");
            writer.write("  <xml-output-file path=\"" + responseFileXml.getAbsolutePath() + "\"/>\n");
            writer.write("  <root-element name=\"user-request-response\"/>\n");
            writer.write("</json-to-xml-1-jobfile>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageJsonToXml1JobFileWritingError", ex, null, jsonToXml1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageJsonToXml1JobFileWritingError", ex, null, jsonToXml1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJsonToXml1JobFileWritingError", ex, null, jsonToXml1JobFile.getAbsolutePath());
        }

        builder = new ProcessBuilder("java", "json_to_xml_1", jsonToXml1JobFile.getAbsolutePath(), jsonToXml1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "digital_publishing_workflow_tools" + File.separator + "json_to_xml" + File.separator + "json_to_xml_1"));
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
            throw constructTermination("messageJsonToXml1ErrorWhileReadingOutput", ex, null, jsonToXml1JobFile.getAbsolutePath());
        }

        if (jsonToXml1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageJsonToXml1ResultInfoFileDoesntExistButShould", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
        }

        if (jsonToXml1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageJsonToXml1ResultInfoPathExistsButIsntAFile", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
        }

        if (jsonToXml1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageJsonToXml1ResultInfoFileIsntReadable", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
        }

        wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(jsonToXml1ResultInfoFile);
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
                        break;
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageJsonToXml1ResultInfoFileErrorWhileReading", ex, null, jsonToXml1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageJsonToXml1ResultInfoFileErrorWhileReading", ex, null, jsonToXml1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJsonToXml1ResultInfoFileErrorWhileReading", ex, null, jsonToXml1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageJsonToXml1CallWasntSuccessful", null, null, jsonToXml1JobFile.getAbsolutePath());
        }

        if (responseFileXml.exists() != true)
        {
            throw constructTermination("messageJsonToXml1ResultFileDoesntExist", null, null, responseFileXml.getAbsolutePath());
        }

        if (responseFileXml.isFile() != true)
        {
            throw constructTermination("messageJsonToXml1ResultPathExistsButIsntAFile", null, null, responseFileXml.getAbsolutePath());
        }

        if (responseFileXml.canRead() != true)
        {
            throw constructTermination("messageJsonToXml1ResultFileIsntReadable", null, null, responseFileXml.getAbsolutePath());
        }

        Stack<String> structureStack = new Stack<String>();

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(responseFileXml);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

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

                    structureStack.push(fullElementName);
                }
                else if (event.isEndElement() == true)
                {
                    QName elementName = event.asEndElement().getName();
                    String fullElementName = elementName.getLocalPart();

                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }

                    if (structureStack.peek().equals(fullElementName) != true)
                    {
                    
                    }

                    structureStack.pop();
                }
                else if (event.isCharacters() == true)
                {
                    String path = "/";

                    for (String element : structureStack)
                    {
                        path += element + "/";
                    }

                    if (path.equals("/user-request-response/users/_id/") == true)
                    {
                        if (this.channelId == null)
                        {
                            this.channelId = new String();
                        }

                        this.channelId += event.asCharacters();
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageResponseFileErrorWhileReading", ex, null, responseFileXml.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageResponseFileErrorWhileReading", ex, null, responseFileXml.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageResponseFileErrorWhileReading", ex, null, responseFileXml.getAbsolutePath());
        }

        if (this.channelId == null)
        {
            throw constructTermination("messageResponseFileElementIsntConfigured", null, null, responseFileXml.getAbsolutePath(), "/user-request-response/users/_id/");
        }

        return 0;
    }

    protected String escapeURL(String url)
    {
        String escaped = url;
        // Percent needs to be the first, otherwise it would double-escape
        // other escape sequences.
        escaped = escaped.replaceAll("%", "%25");
        escaped = escaped.replaceAll(" ", "%20");
        escaped = escaped.replaceAll("\"", "%22");
        escaped = escaped.replaceAll("&", "%26");
        escaped = escaped.replaceAll("'", "%27");
        escaped = escaped.replaceAll(",", "%2C");
        escaped = escaped.replaceAll("<", "%3C");
        escaped = escaped.replaceAll(">", "%3E");

        return escaped;
    }

    public String getChannelId()
    {
        return this.channelId;
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
                message = "twitch_channel_id_obtainer_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "twitch_channel_id_obtainer_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "twitch_channel_id_obtainer_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "twitch_channel_id_obtainer_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (twitch_channel_id_obtainer_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(twitch_channel_id_obtainer_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by twitch_channel_id_obtainer_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<twitch-channel-id-obtainer-1-workflow-result-information>\n");

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

                writer.write("</twitch-channel-id-obtainer-1-workflow-result-information>\n");
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

        twitch_channel_id_obtainer_1.resultInfoFile = null;

        System.exit(-1);
        return -1;
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

    protected String channelId = null;

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nTwitchChannelIdObtainer1WorkflowConsole";
    private ResourceBundle l10nConsole;
}
