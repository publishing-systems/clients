/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of mobileread_wiki_ebook_list_validator1.
 *
 * mobileread_wiki_ebook_list_validator1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * mobileread_wiki_ebook_list_validator1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; replacementout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
    *
 * You should have received a copy of the GNU Affero General Public License 3
 * along replacement mobileread_wiki_ebook_list_validator1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/mobileread_wiki_ebook_list_validator1.java
 * @author Stephan Kreutzer
 * @since 2015-04-21
 */



import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.namespace.QName;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import java.lang.InterruptedException;
import java.util.Iterator;
import javax.xml.stream.events.Attribute;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;



public class mobileread_wiki_ebook_list_validator1
{
    public static void main(String args[])
    {
        System.out.print("mobileread_wiki_ebook_list_validator1  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/publishing-systems/automated_digital_publishing/\n" +
                         "and the project website http://www.publishing-systems.org.\n\n");

        String programPath = mobileread_wiki_ebook_list_validator1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        if (args.length < 2)
        {
            System.out.print("Usage:\n" +
                             "\tmobileread_wiki_ebook_list_validator1 mobileread-wiki-ebook-list-url out-directory\n\n");
            System.exit(1);
        }

        File outDirectory = new File(args[1]);

        if (outDirectory.exists() != true)
        {
            if (outDirectory.mkdirs() != true)
            {
                System.out.print("mobileread_wiki_ebook_list_validator1: Can't create output directory '" + outDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (outDirectory.isFile() == true)
            {
                System.out.print("mobileread_wiki_ebook_list_validator1: Output path '" + outDirectory.getAbsolutePath() + "' already exists, but is a file instead of a directory.\n");
                System.exit(-1);
            }
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() == true)
        {
            if (mobileread_wiki_ebook_list_validator1.DeleteFileRecursively(tempDirectory) != 0)
            {
                System.out.println("mobileread_wiki_ebook_list_validator1: Can't clean '" + tempDirectory.getAbsolutePath() + "'.");
                System.exit(-1);
            }
        }

        if (tempDirectory.mkdir() != true)
        {
            System.out.print("mobileread_wiki_ebook_list_validator1: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
            System.exit(-1);
        }

        ProcessBuilder builder = new ProcessBuilder("java", "downloader1", args[0], tempDirectory.getAbsolutePath() + File.separator + "list.xhtml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "downloader" + File.separator + "downloader1"));
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
            ex.printStackTrace();
            System.exit(-1);
        }

        File wikiEbookList = new File(tempDirectory.getAbsolutePath() + File.separator + "list.xhtml");

        if (wikiEbookList.exists() != true)
        {
            System.out.print("mobileread_wiki_ebook_list_validator1: '" + wikiEbookList.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (wikiEbookList.isFile() != true)
        {
            System.out.print("mobileread_wiki_ebook_list_validator1: '" + wikiEbookList.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (wikiEbookList.canRead() != true)
        {
            System.out.print("mobileread_wiki_ebook_list_validator1: '" + wikiEbookList.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        if (mobileread_wiki_ebook_list_validator1.CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1" + File.separator + "entities" + File.separator + "config_empty.xml"),
                                                                  new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1" + File.separator + "entities" + File.separator + "config.xml")) != 0)
        {
            System.exit(-1);
        }

        builder = new ProcessBuilder("java", "xsltransformator1", wikiEbookList.getAbsolutePath(), programPath + "mobileread_wiki_ebook_list.xsl", tempDirectory.getAbsolutePath() + File.separator + "list.xml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1"));
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
            ex.printStackTrace();
            System.exit(-1);
        }

        wikiEbookList = new File(tempDirectory.getAbsolutePath() + File.separator + "list.xml");
        List<String> threadLinks = new ArrayList<String>();


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(new File(tempDirectory.getAbsolutePath() + File.separator + "list.xml"));
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF8");

            XMLEvent event = null;
            int linkEntryCount = 0;


            while (eventReader.hasNext() == true)
            {
                event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    QName elementName = event.asStartElement().getName();
                    String elementNameString = elementName.getLocalPart();

                    if (elementNameString.equalsIgnoreCase("link") == true)
                    {
                        linkEntryCount++;
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            threadLinks.add(event.asCharacters().getData());
                        }
                        else
                        {
                            System.out.print("mobileread_wiki_ebook_list_validator1: Link entry #" + linkEntryCount + " in '" + wikiEbookList.getAbsolutePath() + "' doesn't contain characters.\n");
                            System.exit(-1);
                        }
                    }
                }
            }
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

        if (threadLinks.isEmpty() == true)
        {
            System.out.print("mobileread_wiki_ebook_list_validator1: No link entries extracted into '" + wikiEbookList.getAbsolutePath() + "'.\n");
            System.exit(0);
        }

        for (int i = 0; i < threadLinks.size(); i++)
        {
            builder = new ProcessBuilder("java", "downloader1", threadLinks.get(i), tempDirectory.getAbsolutePath() + File.separator + i + ".xhtml");
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "downloader" + File.separator + "downloader1"));
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
                ex.printStackTrace();
                System.exit(-1);
            }

            File threadPage = new File(tempDirectory.getAbsolutePath() + File.separator + i + ".xhtml");

            if (threadPage.exists() != true)
            {
                System.out.print("mobileread_wiki_ebook_list_validator1: '" + threadPage.getAbsolutePath() + "' as a result of downloading '" + threadLinks.get(i) + "' doesn't exist, but should by now.\n");
                System.exit(-1);
            }

            if (threadPage.isFile() != true)
            {
                System.out.print("mobileread_wiki_ebook_list_validator1: '" + threadPage.getAbsolutePath() + "' isn't a file.\n");
                System.exit(-1);
            }

            if (threadPage.canRead() != true)
            {
                System.out.print("mobileread_wiki_ebook_list_validator1: '" + threadPage.getAbsolutePath() + "' isn't readable.\n");
                System.exit(-1);
            }

            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
        }

        if (mobileread_wiki_ebook_list_validator1.CopyFileBinary(new File(programPath + "xsltransformator1_entities_config_xhtml1-transitional.xml"),
                                                                  new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1" + File.separator + "entities" + File.separator + "config.xml")) != 0)
        {
            System.exit(-1);
        }

        for (int i = 0; i < threadLinks.size(); i++)
        {
            File threadPage = new File(tempDirectory.getAbsolutePath() + File.separator + i + ".xhtml");

            builder = new ProcessBuilder("java", "xml_fix_special_characters_escaping1", threadPage.getAbsolutePath(), threadPage.getAbsolutePath());
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xml_fix_special_characters_escaping" + File.separator + "xml_fix_special_characters_escaping1"));
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
                ex.printStackTrace();
                System.exit(-1);
            }

            builder = new ProcessBuilder("java", "xsltransformator1", threadPage.getAbsolutePath(), programPath + "mobileread_thread_page.xsl", tempDirectory.getAbsolutePath() + File.separator + i + ".xml");
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1"));
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
                ex.printStackTrace();
                System.exit(-1);
            }
        }

        List<AttachmentInfo> attachmentLinks = new ArrayList<AttachmentInfo>();
        List<String> userNames = new ArrayList<String>();
        List<UserLinks> userLinks = new ArrayList<UserLinks>();
        String user = new String();
        String userBlameLink = new String();
        String userReputationLink = new String();


        for (int i = 0; i < threadLinks.size(); i++)
        {
            user = "";
            userBlameLink = "";
            userReputationLink = "";
            int attachmentLinkCount = 0;

            File attachmentListFile = new File(tempDirectory.getAbsolutePath() + File.separator + i + ".xml");

            if (attachmentListFile.length() <= 0)
            {
                // Transformation failed.
                System.out.println("mobileread_wiki_ebook_list_validator1: Thread page '" + threadLinks.get(i) + "', downloaded to '" + tempDirectory.getAbsolutePath() + File.separator + i + ".xhtml', isn't well-formed.");
                continue;
            }

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(attachmentListFile);
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF8");

                XMLEvent event = null;
                int linkEntryCount = 0;


                while (eventReader.hasNext() == true)
                {
                    event = eventReader.nextEvent();

                    if (event.isStartElement() == true)
                    {
                        QName elementName = event.asStartElement().getName();
                        String elementNameString = elementName.getLocalPart();

                        if (elementNameString.equalsIgnoreCase("user") == true)
                        {
                            String linkAttribute = new String();
                            String postAttribute = new String();

                            // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                            @SuppressWarnings("unchecked")
                            Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();

                            while (attributes.hasNext() == true)
                            {
                                Attribute attribute = attributes.next();
                                String attributeName = attribute.getName().getLocalPart();

                                if (attributeName.equals("post") == true)
                                {
                                    postAttribute = attribute.getValue();
                                }
                                else if (attributeName.equals("link") == true)
                                {
                                    linkAttribute = attribute.getValue();
                                }
                            }

                            if (!linkAttribute.isEmpty())
                            {
                                userBlameLink = "http://www.mobileread.com/forums/" + linkAttribute;

                                int posParameterList = linkAttribute.indexOf('?');

                                if (posParameterList >= 0)
                                {
                                    linkAttribute = linkAttribute.substring(posParameterList + new String("?").length());

                                    String[] parameters = linkAttribute.split("&");

                                    if (parameters.length == 0)
                                    {
                                        parameters = new String[] { linkAttribute };
                                    }

                                    if (parameters.length > 0)
                                    {
                                        for (String parameter : parameters)
                                        {
                                            String[] keyValuePair = parameter.split("=");

                                            if (keyValuePair.length >= 2)
                                            {
                                                if (keyValuePair[0].equalsIgnoreCase("u") == true)
                                                {
                                                    userBlameLink = "http://www.mobileread.com/forums/private.php?do=newpm&u=" + keyValuePair[1];
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (!postAttribute.isEmpty())
                            {
                                String[] keyValuePair = postAttribute.split("_");

                                if (keyValuePair.length >= 2)
                                {
                                    boolean isNumeric = true;

                                    try
                                    {
                                        Integer.parseInt(keyValuePair[1]);
                                    }
                                    catch (NumberFormatException ex)
                                    {
                                        isNumeric = false;
                                    }
                                    catch (NullPointerException ex)
                                    {
                                        isNumeric = false;
                                    }

                                    if (isNumeric == true)
                                    {
                                        userReputationLink = "http://www.mobileread.com/forums/reputation.php?p=" + keyValuePair[1];
                                    }
                                }
                            }

                            while (eventReader.hasNext() == true)
                            {
                                event = eventReader.nextEvent();

                                if (event.isCharacters() == true)
                                {
                                    user += event.asCharacters().getData();
                                }
                                else
                                {
                                    break;
                                }
                            }

                            break;
                        }
                    }
                }
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

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(attachmentListFile);
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF8");

                XMLEvent event = null;
                int linkEntryCount = 0;


                while (eventReader.hasNext() == true)
                {
                    event = eventReader.nextEvent();

                    if (event.isStartElement() == true)
                    {
                        QName elementName = event.asStartElement().getName();
                        String elementNameString = elementName.getLocalPart();

                        if (elementNameString.equalsIgnoreCase("link") == true)
                        {
                            String attachmentLink = new String();
                            String attachmentName = new String();

                            // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                            @SuppressWarnings("unchecked")
                            Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();

                            while (attributes.hasNext() == true)
                            {
                                Attribute attribute = attributes.next();
                                String attributeName = attribute.getName().getLocalPart();

                                if (attributeName.equals("name") == true)
                                {
                                    attachmentName = attribute.getValue();
                                    break;
                                }
                            }

                            if (args.length > 2)
                            {
                                // Don't surrender UTF-8, but the non-UTF-8 encoding of the source.
                                attachmentName = attachmentName.replaceAll("[^\\p{ASCII}]", "_");
                            }

                            while (eventReader.hasNext() == true)
                            {
                                event = eventReader.nextEvent();

                                if (event.isCharacters() == true)
                                {
                                    attachmentLink += event.asCharacters().getData();
                                }
                                else
                                {
                                    break;
                                }
                            }

                            if (attachmentName.isEmpty() == true)
                            {
                                attachmentName = "attachment_" + linkEntryCount;
                            }
                            else
                            {
                                if (!attachmentName.endsWith(".epub"))
                                {
                                    continue;
                                }
                            }

                            if (attachmentLink.isEmpty() == false)
                            {
                                linkEntryCount++;
                                attachmentLinks.add(new AttachmentInfo(attachmentLink, attachmentName, threadLinks.get(i)));
                                attachmentLinkCount++;
                                userNames.add(user);
                                userLinks.add(new UserLinks());
                                userLinks.get(userLinks.size() - 1).SetBlameLink(userBlameLink);
                                userLinks.get(userLinks.size() - 1).SetReputationLink(userReputationLink);
                            }
                            else
                            {
                                System.out.print("mobileread_wiki_ebook_list_validator1: Attachment link entry in '" + attachmentListFile.getAbsolutePath() + "' doesn't contain characters.\n");
                                System.exit(-1);
                            }
                        }
                    }
                }
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

            if (attachmentLinkCount == 0)
            {
                System.out.print("mobileread_wiki_ebook_list_validator1: No attachment links in '" + attachmentListFile.getAbsolutePath() + "' of thread '" + threadLinks.get(i) + "'.\n");
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(new File(outDirectory.getAbsolutePath() + File.separator + "downloads.xml")),
                                    "UTF8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<downloads>\n");

            for (int i = 0; i < attachmentLinks.size(); i++)
            {
                builder = new ProcessBuilder("java", "downloader1", attachmentLinks.get(i).GetAttachmentLink(), outDirectory.getAbsolutePath() + File.separator + attachmentLinks.get(i).GetAttachmentName());
                builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "downloader" + File.separator + "downloader1"));
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
                    ex.printStackTrace();
                    System.exit(-1);
                }

                BufferedWriter writer2 = new BufferedWriter(
                                         new OutputStreamWriter(
                                         new FileOutputStream(new File(tempDirectory.getAbsolutePath() + File.separator + "epubcheck_jobfile_" + i + ".xml")),
                                        "UTF8"));

                String attachmentDisplayName = attachmentLinks.get(i).GetAttachmentName();

                attachmentDisplayName = attachmentDisplayName.replaceAll("&", "&amp;");
                attachmentDisplayName = attachmentDisplayName.replaceAll("\"", "&quot;");
                attachmentDisplayName = attachmentDisplayName.replaceAll("'", "&apos;");
                attachmentDisplayName = attachmentDisplayName.replaceAll("<", "&lt;");
                attachmentDisplayName = attachmentDisplayName.replaceAll(">", "&gt;");

                writer2.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer2.write("<epubcheck1-recursive-checker1-job>\n");
                writer2.write("  <in>\n");
                writer2.write("    <input path=\"" + outDirectory.getAbsolutePath() + File.separator + attachmentDisplayName + "\" recursive=\"false\"/>\n");
                writer2.write("  </in>\n");
                writer2.write("</epubcheck1-recursive-checker1-job>\n");
                writer2.flush();
                writer2.close();

                File epubcheckResultDirectory = new File(outDirectory.getAbsolutePath() + File.separator + "epubcheck_result");

                if (epubcheckResultDirectory.exists() != true)
                {
                    if (epubcheckResultDirectory.mkdirs() != true)
                    {
                        System.out.print("mobileread_wiki_ebook_list_validator1: Can't create epubcheck result directory '" + epubcheckResultDirectory.getAbsolutePath() + "'.\n");
                        System.exit(-1);
                    }
                }
                else
                {
                    if (epubcheckResultDirectory.isFile() == true)
                    {
                        System.out.print("mobileread_wiki_ebook_list_validator1: Epubcheck result path '" + epubcheckResultDirectory.getAbsolutePath() + "' already exists, but is a file instead of a directory.\n");
                        System.exit(-1);
                    }
                }

                DeleteFileRecursively(new File(epubcheckResultDirectory.getAbsolutePath() + File.separator + "result_1.log"));

                builder = new ProcessBuilder("java", "epubcheck1_recursive_checker1", tempDirectory.getAbsolutePath() + File.separator + "epubcheck_jobfile_" + i + ".xml", epubcheckResultDirectory.getAbsolutePath());
                builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "epubcheck" + File.separator + "epubcheck1" + File.separator + "workflows"));
                builder.redirectErrorStream(true);

                boolean valid = false;
                boolean warning = false;

                try
                {
                    Process process = builder.start();
                    Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

                    while (scanner.hasNext() == true)
                    {
                        String line = scanner.next();

                        if (line.contains("Das EPUB enthält keine Fehler oder Warnungen") == true ||
                            line.contains("No errors or warnings detected") == true)
                        {
                            valid = true;
                        }
                        else if (line.contains("EpubCheck mit Warnungen abgeschlossen") == true ||
                                 line.contains("Check finished with warnings") == true)
                        {
                            warning = true;
                        }

                        System.out.println(line);
                    }

                    scanner.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    System.exit(-1);
                }

                String thread = attachmentLinks.get(i).GetThread();
                String userDisplayName = userNames.get(i);
                String userLink = new String();

                thread = thread.replaceAll("&", "&amp;");
                thread = thread.replaceAll("\"", "&quot;");
                thread = thread.replaceAll("'", "&apos;");
                thread = thread.replaceAll("<", "&lt;");
                thread = thread.replaceAll(">", "&gt;");

                userDisplayName = userDisplayName.replaceAll("&", "&amp;");
                userDisplayName = userDisplayName.replaceAll("\"", "&quot;");
                userDisplayName = userDisplayName.replaceAll("'", "&apos;");
                userDisplayName = userDisplayName.replaceAll("<", "&lt;");
                userDisplayName = userDisplayName.replaceAll(">", "&gt;");

                if (valid == true)
                {
                    userLink = userLinks.get(i).GetReputationLink();
                }
                else
                {
                    userLink = userLinks.get(i).GetBlameLink();
                }

                userLink = userLink.replaceAll("&", "&amp;");
                userLink = userLink.replaceAll("\"", "&quot;");
                userLink = userLink.replaceAll("'", "&apos;");
                userLink = userLink.replaceAll("<", "&lt;");
                userLink = userLink.replaceAll(">", "&gt;");

                if (valid == true)
                {
                    writer.write("  <download id=\"" + i + "\" name=\"" + attachmentDisplayName + "\" origin=\"" + thread + "\" user=\"" + userDisplayName + "\"");

                    if (!userLink.isEmpty())
                    {
                        writer.write(" user_link=\"" + userLink + "\"");
                    }

                    writer.write(" result=\"valid\"/>\n");
                }
                else if (warning == true)
                {
                    if (new File(epubcheckResultDirectory.getAbsolutePath() + File.separator + "result_1.log").exists() == true)
                    {
                        writer.write("  <download id=\"" + i + "\" name=\"" + attachmentDisplayName + "\" origin=\"" + thread + "\" user=\"" + userDisplayName + "\"");

                        if (!userLink.isEmpty())
                        {
                            writer.write(" user_link=\"" + userLink + "\"");
                        }

                        writer.write(" result=\"warning\"/>\n");

                        if (mobileread_wiki_ebook_list_validator1.CopyFileBinary(new File(epubcheckResultDirectory.getAbsolutePath() + File.separator + "result_1.log"),
                                                                                new File(outDirectory.getAbsolutePath() + File.separator + "result_" + i + ".log")) != 0)
                        {
                            System.exit(-1);
                        }
                    }
                    else
                    {
                        writer.write("  <download id=\"" + i + "\" name=\"" + attachmentDisplayName + "\" origin=\"" + thread + "\" user=\"" + userDisplayName + "\"");

                        if (!userLink.isEmpty())
                        {
                            writer.write(" user_link=\"" + userLink + "\"");
                        }

                        writer.write(" result=\"no_result\"/>\n");
                    }
                }
                else
                {
                    if (new File(epubcheckResultDirectory.getAbsolutePath() + File.separator + "result_1.log").exists() == true)
                    {
                        writer.write("  <download id=\"" + i + "\" name=\"" + attachmentDisplayName + "\" origin=\"" + thread + "\" user=\"" + userDisplayName + "\"");

                        if (!userLink.isEmpty())
                        {
                            writer.write(" user_link=\"" + userLink + "\"");
                        }

                        writer.write(" result=\"error\"/>\n");

                        if (mobileread_wiki_ebook_list_validator1.CopyFileBinary(new File(epubcheckResultDirectory.getAbsolutePath() + File.separator + "result_1.log"),
                                                                                new File(outDirectory.getAbsolutePath() + File.separator + "result_" + i + ".log")) != 0)
                        {
                            System.exit(-1);
                        }
                    }
                    else
                    {
                        writer.write("  <download id=\"" + i + "\" name=\"" + attachmentDisplayName + "\" origin=\"" + thread + "\" user=\"" + userDisplayName + "\"");

                        if (!userLink.isEmpty())
                        {
                            writer.write(" user_link=\"" + userLink + "\"");
                        }

                        writer.write(" result=\"no_result\"/>\n");
                    }
                }

                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }

            writer.write("</downloads>\n");
            writer.flush();
            writer.close();
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

        if (mobileread_wiki_ebook_list_validator1.CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1" + File.separator + "entities" + File.separator + "config_empty.xml"),
                                                                  new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1" + File.separator + "entities" + File.separator + "config.xml")) != 0)
        {
            System.exit(-1);
        }

        builder = new ProcessBuilder("java", "xsltransformator1", outDirectory.getAbsolutePath() + File.separator + "downloads.xml", programPath + "transform_result.xsl", outDirectory.getAbsolutePath() + File.separator + "check_result.xhtml");
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1"));
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
            ex.printStackTrace();
            System.exit(-1);
        }

        return;
    }

    public static int DeleteFileRecursively(File file)
    {
        if (file.isDirectory() == true)
        {
            for (File child : file.listFiles())
            {
                if (mobileread_wiki_ebook_list_validator1.DeleteFileRecursively(child) != 0)
                {
                    return -1;
                }
            }
        }

        if (file.delete() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_validator1: Can't delete '" + file.getAbsolutePath() + "'.");
            return -1;
        }

        return 0;
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_validator1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }

        if (from.isFile() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_validator1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }

        if (from.canRead() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_validator1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
            return -3;
        }


        byte[] buffer = new byte[1024];

        try
        {
            to.createNewFile();

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

class AttachmentInfo
{
    public AttachmentInfo(String attachmentLink, String attachmentName, String thread)
    {
        this.attachmentLink = attachmentLink;
        this.attachmentName = attachmentName;
        this.thread = thread;
    }

    public String GetAttachmentLink()
    {
        return this.attachmentLink;
    }

    public String GetAttachmentName()
    {
        return this.attachmentName;
    }

    public String GetThread()
    {
        return this.thread;
    }

    private String attachmentLink;
    private String attachmentName;
    private String thread;
}

class UserLinks
{
    public UserLinks()
    {
        this.blameLink = new String();
        this.reputationLink = new String();
    }

    public String GetBlameLink()
    {
        return this.blameLink;
    }

    public void SetBlameLink(String blameLink)
    {
        this.blameLink = blameLink;
    }

    public String GetReputationLink()
    {
        return this.reputationLink;
    }

    public void SetReputationLink(String reputationLink)
    {
        this.reputationLink = reputationLink;
    }

    private String blameLink;
    private String reputationLink;
}

