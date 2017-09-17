/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of twitch_video_uploader_2 workflow of clients for the
 * automated_digital_publishing and digital_publishing_workflow_tools packages.
 *
 * twitch_video_uploader_2 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * twitch_video_uploader_2 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with twitch_video_uploader_2 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/tv.twitch/twitch_video_uploader_1/workflows/twitch_video_uploader/twitch_video_uploader_2/twitch_video_uploader_2.java
 * @brief Will obtain permissions and upload a series of videos to twitch.tv.
 * @todo Delete files which contain the access_token!
 * @author Stephan Kreutzer
 * @since 2017-09-16
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



public class twitch_video_uploader_2
{
    public static void main(String args[])
    {
        System.out.print("twitch_video_uploader_2 workflow Copyright (C) 2016-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/clients/ and the project website\n" +
                         "http://www.publishing-systems.org.\n\n");

        twitch_video_uploader_2 uploader = new twitch_video_uploader_2();

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
                writer.write("<!-- This file was created by twitch_video_uploader_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<twitch-video-uploader-2-workflow-result-information>\n");
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
                writer.write("</twitch-video-uploader-2-workflow-result-information>\n");
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
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\ttwitch_video_uploader_2 " + getI10nString("messageParameterList") + "\n");
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

        twitch_video_uploader_2.resultInfoFile = resultInfoFile;

        String programPath = twitch_video_uploader_2.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("twitch_video_uploader_2 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        List<JobFileInstruction> instructions = new ArrayList<JobFileInstruction>();
        String channelId = null;

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

        File twitchPermissionObtainer1WorkflowJobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_twitch_permission_obtainer_1_workflow.xml");
        File twitchPermissionObtainer1WorkflowResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_twitch_permission_obtainer_1_workflow.xml");

        if (twitchPermissionObtainer1WorkflowJobFile.exists() == true)
        {
            if (twitchPermissionObtainer1WorkflowJobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = twitchPermissionObtainer1WorkflowJobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (twitchPermissionObtainer1WorkflowJobFile.canWrite() != true)
                    {
                        throw constructTermination("messageTwitchPermissionObtainer1WorkflowJobFileExistsButIsntWritable", null, null, twitchPermissionObtainer1WorkflowJobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTwitchPermissionObtainer1WorkflowJobFileExistsButIsntAFile", null, null, twitchPermissionObtainer1WorkflowJobFile.getAbsolutePath());
            }
        }

        if (twitchPermissionObtainer1WorkflowResultInfoFile.exists() == true)
        {
            if (twitchPermissionObtainer1WorkflowResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = twitchPermissionObtainer1WorkflowResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (twitchPermissionObtainer1WorkflowResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileExistsButIsntWritable", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoPathExistsButIsntAFile", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(twitchPermissionObtainer1WorkflowJobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by twitch_video_uploader_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
            writer.write("<twitch-permission-obtainer-1-workflow-job>\n");
            writer.write("  <client id=\"feuq0hqseoxyg38p5inirhw2rstqce\"/>\n");
            writer.write("  <permissions scope=\"channel_editor\"/>\n");
            writer.write("</twitch-permission-obtainer-1-workflow-job>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowJobFileWritingError", ex, null, twitchPermissionObtainer1WorkflowJobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowJobFileWritingError", ex, null, twitchPermissionObtainer1WorkflowJobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowJobFileWritingError", ex, null, twitchPermissionObtainer1WorkflowJobFile.getAbsolutePath());
        }

        ProcessBuilder builder = new ProcessBuilder("java", "twitch_permission_obtainer_1", twitchPermissionObtainer1WorkflowJobFile.getAbsolutePath(), twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator +  "twitch_permission_obtainer" + File.separator + "twitch_permission_obtainer_1"));
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
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowErrorWhileReadingOutput", ex, null, twitchPermissionObtainer1WorkflowJobFile.getAbsolutePath());
        }

        if (twitchPermissionObtainer1WorkflowResultInfoFile.exists() != true)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileDoesntExistButShould", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        }

        if (twitchPermissionObtainer1WorkflowResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoPathExistsButIsntAFile", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        }

        if (twitchPermissionObtainer1WorkflowResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileIsntReadable", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        }

        boolean wasSuccess = false;
        String accessToken = null;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(twitchPermissionObtainer1WorkflowResultInfoFile);
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
                    else if (tagName.equals("access-token") == true)
                    {
                        StartElement accessTokenElement = event.asStartElement();
                        Attribute tokenAttribute = accessTokenElement.getAttributeByName(new QName("token"));

                        if (tokenAttribute == null)
                        {
                            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileEntryIsMissingAnAttribute", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath(), tagName, "token");
                        }

                        if (accessToken != null)
                        {
                            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileElementConfiguredMoreThanOnce", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath(), tagName);
                        }

                        accessToken = tokenAttribute.getValue();
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileErrorWhileReading", ex, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileErrorWhileReading", ex, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileErrorWhileReading", ex, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageTwitchPermissionObtainer1WorkflowCallWasntSuccessful", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
        }

        if (accessToken == null)
        {
             throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileElementIsntConfigured", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath(), "access-token");
        }

        {
            boolean deleteSuccessful = false;

            try
            {
                deleteSuccessful = twitchPermissionObtainer1WorkflowResultInfoFile.delete();
            }
            catch (SecurityException ex)
            {

            }

            if (deleteSuccessful != true)
            {
                throw constructTermination("messageTwitchPermissionObtainer1WorkflowResultInfoFileCantDeleteAPIKeysExposed", null, null, twitchPermissionObtainer1WorkflowResultInfoFile.getAbsolutePath());
            }
        }


        File twitchVideoUploader1WorkflowJobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_twitch_video_uploader_1_workflow.xml");
        File twitchVideoUploader1WorkflowResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_twitch_video_uploader_1_workflow.xml");

        if (twitchVideoUploader1WorkflowJobFile.exists() == true)
        {
            if (twitchVideoUploader1WorkflowJobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = twitchVideoUploader1WorkflowJobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (twitchVideoUploader1WorkflowJobFile.canWrite() != true)
                    {
                        throw constructTermination("messageTwitchVideoUploader1WorkflowJobFileExistsButIsntWritable", null, null, twitchVideoUploader1WorkflowJobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTwitchVideoUploader1WorkflowJobFileExistsButIsntAFile", null, null, twitchVideoUploader1WorkflowJobFile.getAbsolutePath());
            }
        }

        if (twitchVideoUploader1WorkflowResultInfoFile.exists() == true)
        {
            if (twitchVideoUploader1WorkflowResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = twitchVideoUploader1WorkflowResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (twitchVideoUploader1WorkflowResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoFileExistsButIsntWritable", null, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoPathExistsButIsntAFile", null, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(twitchVideoUploader1WorkflowJobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by twitch_video_uploader_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
            writer.write("<twitch-video-uploader-1-workflow-job>\n");
            writer.write("  <instructions>\n");

            for (int i = 0; i < instructions.size(); i++)
            {
                JobFileInstruction instruction = instructions.get(i);

                writer.write("    <instruction action=\"upload\" file=\"" + instruction.getInputFile().getAbsolutePath() + "\" title=\"" + instruction.getTitle() + "\"/>\n");
            }

            writer.write("  </instructions>\n");
            writer.write("  <channel id=\"" + channelId + "\"/>\n");
            writer.write("  <access-token token=\"" + accessToken + "\"/>\n");
            writer.write("</twitch-video-uploader-1-workflow-job>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowJobFileWritingError", ex, null, twitchVideoUploader1WorkflowJobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowJobFileWritingError", ex, null, twitchVideoUploader1WorkflowJobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowJobFileWritingError", ex, null, twitchVideoUploader1WorkflowJobFile.getAbsolutePath());
        }

        builder = new ProcessBuilder("java", "twitch_video_uploader_1", twitchVideoUploader1WorkflowJobFile.getAbsolutePath(), twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + "twitch_video_uploader_1"));
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
            throw constructTermination("messageTwitchVideoUploader1WorkflowErrorWhileReadingOutput", ex, null, twitchVideoUploader1WorkflowJobFile.getAbsolutePath());
        }

        if (twitchVideoUploader1WorkflowResultInfoFile.exists() != true)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoFileDoesntExistButShould", null, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        }

        if (twitchVideoUploader1WorkflowResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoPathExistsButIsntAFile", null, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        }

        if (twitchVideoUploader1WorkflowResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoFileIsntReadable", null, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        }

        wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(twitchVideoUploader1WorkflowResultInfoFile);
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
            throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoFileErrorWhileReading", ex, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoFileErrorWhileReading", ex, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowResultInfoFileErrorWhileReading", ex, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowCallWasntSuccessful", null, null, twitchVideoUploader1WorkflowResultInfoFile.getAbsolutePath());
        }

        boolean deleteSuccessful = false;

        try
        {
            deleteSuccessful = twitchVideoUploader1WorkflowJobFile.delete();
        }
        catch (SecurityException ex)
        {

        }

        if (deleteSuccessful != true)
        {
            throw constructTermination("messageTwitchVideoUploader1WorkflowJobFileCantDeleteAPIKeysExposed", null, null, twitchVideoUploader1WorkflowJobFile.getAbsolutePath());
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
                message = "twitch_video_uploader_2 workflow: " + getI10nString(id);
            }
            else
            {
                message = "twitch_video_uploader_2 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "twitch_video_uploader_2 workflow: " + getI10nString(id);
            }
            else
            {
                message = "twitch_video_uploader_2 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (twitch_video_uploader_2.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(twitch_video_uploader_2.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by twitch_video_uploader_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
                writer.write("<twitch-video-uploader-2-workflow-result-information>\n");

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

                writer.write("</twitch-video-uploader-2-workflow-result-information>\n");
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

        twitch_video_uploader_2.resultInfoFile = null;

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

    private static final String L10N_BUNDLE = "l10n.l10nTwitchVideoUploader2WorkflowConsole";
    private ResourceBundle l10nConsole;
}
