/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of twitch_video_uploader_1 workflow of clients for the
 * automated_digital_publishing and digital_publishing_workflow_tools packages.
 *
 * twitch_video_uploader_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * twitch_video_uploader_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with twitch_video_uploader_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/tv.twitch/twitch_video_uploader_1/workflows/twitch_video_uploader/twitch_video_uploader_1/twitch_video_uploader_1.java
 * @brief Will upload a series of videos to twitch.tv.
 * @todo Delete files which contain the access_token!
 * @author Stephan Kreutzer
 * @since 2017-09-09
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



public class twitch_video_uploader_1
{
    public static void main(String args[])
    {
        System.out.print("twitch_video_uploader_1 workflow Copyright (C) 2016-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/clients/ and the project website\n" +
                         "http://www.publishing-systems.org.\n\n");

        twitch_video_uploader_1 uploader = new twitch_video_uploader_1();

        try
        {
            uploader.upload(args);
        }
        catch (ProgramTerminationException ex)
        {
            uploader.handleTermination(ex);
        }

        if (uploader.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(uploader.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by twitch_video_uploader_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<twitch-video-uploader-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (uploader.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = uploader.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = uploader.getInfoMessages().get(i);

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
                writer.write("</twitch-video-uploader-1-workflow-result-information>\n");
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

    public int upload(String args[])
    {
        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\ttwitch_video_uploader_1 " + getI10nString("messageParameterList") + "\n");
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

        twitch_video_uploader_1.resultInfoFile = resultInfoFile;

        String programPath = twitch_video_uploader_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("twitch_video_uploader_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        List<JobFileInstruction> instructions = new ArrayList<JobFileInstruction>();
        String channelId = null;
        /** @todo This is a security problem as Strings are immutable in Java.
          * Recommendation is to use char[] instead. */
        String accessToken = null;

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

                    if (tagName.equals("instruction") == true)
                    {
                        StartElement instructionElement = event.asStartElement();
                        Attribute fileAttribute = instructionElement.getAttributeByName(new QName("file"));
                        Attribute actionAttribute = instructionElement.getAttributeByName(new QName("action"));
                        Attribute titleAttribute = instructionElement.getAttributeByName(new QName("title"));

                        if (fileAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "file");
                        }

                        if (actionAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "action");
                        }

                        if (titleAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "title");
                        }

                        if (actionAttribute.getValue().equals("upload") != true)
                        {
                            throw constructTermination("messageJobFileActionNotSupported", null, null, jobFile.getAbsolutePath(), actionAttribute.getValue());
                        }

                        String title = titleAttribute.getValue();
                        File inputFile = new File(fileAttribute.getValue());

                        if (inputFile.isAbsolute() != true)
                        {
                            inputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + fileAttribute.getValue());
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

                        instructions.add(new JobFileInstruction(inputFile, title));
                    }
                    else if (tagName.equals("channel") == true)
                    {
                        StartElement channelElement = event.asStartElement();
                        Attribute idAttribute = channelElement.getAttributeByName(new QName("id"));

                        if (idAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "id");
                        }

                        if (channelId != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        channelId = idAttribute.getValue();
                    }
                    else if (tagName.equals("access-token") == true)
                    {
                        StartElement accessTokenElement = event.asStartElement();
                        Attribute tokenAttribute = accessTokenElement.getAttributeByName(new QName("token"));

                        if (tokenAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "token");
                        }

                        if (accessToken != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        accessToken = tokenAttribute.getValue();
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

        if (instructions.size() <= 0)
        {
            throw constructTermination("messageJobFileElementIsntConfigured", null, null, jobFile.getAbsolutePath(), "instruction");
        }

        if (channelId == null)
        {
            throw constructTermination("messageJobFileElementIsntConfigured", null, null, jobFile.getAbsolutePath(), "channel");
        }

        if (accessToken == null)
        {
            throw constructTermination("messageJobFileElementIsntConfigured", null, null, jobFile.getAbsolutePath(), "access-token");
        }


        for (int instructionIndex = 0; instructionIndex < instructions.size(); instructionIndex++)
        {
            File httpsClient1JobFile = new File(tempDirectory + File.separator + "jobfile_" + instructionIndex + "_create_video.xml");

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
                            this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileExistsButIsntWritable", true, null, null, httpsClient1JobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobPathExistsButIsntAFile", true, null, null, httpsClient1JobFile.getAbsolutePath()));
                    continue;
                }
            }

            File responseFile = new File(tempDirectory.getAbsolutePath() + File.separator + "response_" + instructionIndex + "_create_video.json");

            if (responseFile.exists() == true)
            {
                if (responseFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = responseFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (responseFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResponseFileExistsButIsntWritable", true, null, null, responseFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResponsePathExistsButIsntAFile", true, null, null, responseFile.getAbsolutePath()));
                    continue;
                }
            }

            String title = instructions.get(instructionIndex).getTitle();
            // Ampersand needs to be the first, otherwise it would double-encode
            // other entities.
            title = title.replaceAll("&", "&amp;");
            title = title.replaceAll("<", "&lt;");
            title = title.replaceAll(">", "&gt;");

            /** @todo Proper URL escaping, so XML escaping won't be necessary. */
            title = title.replaceAll(" ", "%20");

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(httpsClient1JobFile),
                                        "UTF-8"));



                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by twitch_video_uploader_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<https-client-1-jobfile>\n");
                writer.write("  <request url=\"https://api.twitch.tv/kraken/videos?channel_id=" + channelId + "&amp;title=" + title + "\" method=\"POST\">\n");
                writer.write("    <header>\n");
                writer.write("      <field name=\"Accept\" value=\"application/vnd.twitchtv.v5+json\"/>\n");
                writer.write("      <field name=\"Authorization\" value=\"OAuth " + accessToken + "\"/>\n");
                writer.write("      <field name=\"Client-ID\" value=\"feuq0hqseoxyg38p5inirhw2rstqce\"/>\n");
                writer.write("      <field name=\"charset\" value=\"utf-8\"/>\n");
                writer.write("    </header>\n");
                writer.write("  </request>\n");
                writer.write("  <response destination=\"" + responseFile.getAbsolutePath() + "\"/>\n");
                writer.write("</https-client-1-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileWritingError", true, ex, null, httpsClient1JobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileWritingError", true, ex, null, httpsClient1JobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileWritingError", true, ex, null, httpsClient1JobFile.getAbsolutePath()));
                continue;
            }

            File httpsClient1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_" + instructionIndex + "_create_video.xml");

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
                            this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileExistsButIsntWritable", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoPathExistsButIsntAFile", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                    continue;
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
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ErrorWhileReadingOutput", true, ex, null, httpsClient1JobFile));
                continue;
            }

            try
            {
                boolean deleteSuccessful = httpsClient1JobFile.delete();

                if (deleteSuccessful == false)
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileCouldntDeleteSecretsExposed", true, null, null, httpsClient1JobFile.getAbsolutePath()));
                }
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileCouldntDeleteSecretsExposed", true, ex, null, httpsClient1JobFile.getAbsolutePath()));
            }

            if (httpsClient1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileDoesntExistButShould", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (httpsClient1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoPathExistsButIsntAFile", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (httpsClient1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileIsntReadable", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
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
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1CallWasntSuccessful", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (responseFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResponseFileDoesntExist", true, null, null, responseFile.getAbsolutePath()));
                continue;
            }


            File jsonToXml1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_json_to_xml_1_create_video_response_" + instructionIndex + ".xml");
            File jsonToXml1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_json_to_xml_1_create_video_response_" + instructionIndex + ".xml");
            File createVideoResponseFile = new File(tempDirectory.getAbsolutePath() + File.separator + "create_video_response_" + instructionIndex + ".xml");

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
                            this.infoMessages.add(constructInfoMessage("messageJsonToXml1JobFileExistsButIsntWritable", true, null, null, jsonToXml1JobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageJsonToXml1JobFileExistsButIsntAFile", true, null, null, jsonToXml1JobFile.getAbsolutePath()));
                    continue;
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
                            this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoFileExistsButIsntWritable", true, null, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoPathExistsButIsntAFile", true, null, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                    continue;
                }
            }

            if (createVideoResponseFile.exists() == true)
            {
                if (createVideoResponseFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = createVideoResponseFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (createVideoResponseFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultFileExistsButIsntWritable", true, null, null, createVideoResponseFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultFileExistsButIsntAFile", true, null, null, createVideoResponseFile.getAbsolutePath()));
                    continue;
                }
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(jsonToXml1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by twitch_video_uploader_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<json-to-xml-1-jobfile>\n");
                writer.write("  <json-input-file path=\"" + responseFile.getAbsolutePath() + "\"/>\n");
                writer.write("  <xml-output-file path=\"" + createVideoResponseFile.getAbsolutePath() + "\"/>\n");
                writer.write("  <root-element name=\"create-video-response\"/>\n");
                writer.write("</json-to-xml-1-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1JobFileWritingError", true, ex, null, jsonToXml1JobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1JobFileWritingError", true, ex, null, jsonToXml1JobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1JobFileWritingError", true, ex, null, jsonToXml1JobFile.getAbsolutePath()));
                continue;
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
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1ErrorWhileReadingOutput", true, ex, null, jsonToXml1JobFile.getAbsolutePath()));
                continue;
            }

            if (jsonToXml1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoFileDoesntExistButShould", true, null, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (jsonToXml1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoPathExistsButIsntAFile", true, null, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (jsonToXml1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoFileIsntReadable", true, null, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                continue;
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
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoFileErrorWhileReading", true, ex, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoFileErrorWhileReading", true, ex, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1ResultInfoFileErrorWhileReading", true, ex, null, jsonToXml1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJsonToXml1CallWasntSuccessful", true, null, null, jsonToXml1JobFile.getAbsolutePath()));
                continue;
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
                message = "twitch_video_uploader_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "twitch_video_uploader_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "twitch_video_uploader_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "twitch_video_uploader_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (twitch_video_uploader_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(twitch_video_uploader_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by twitch_video_uploader_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<twitch-video-uploader-1-workflow-result-information>\n");

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

                writer.write("</twitch-video-uploader-1-workflow-result-information>\n");
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

        twitch_video_uploader_1.resultInfoFile = null;

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

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nTwitchVideoUploader1WorkflowConsole";
    private ResourceBundle l10nConsole;
}
