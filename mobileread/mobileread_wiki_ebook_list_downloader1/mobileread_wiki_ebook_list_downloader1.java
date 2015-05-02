/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of mobileread_wiki_ebook_list_downloader1.
 *
 * mobileread_wiki_ebook_list_downloader1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * mobileread_wiki_ebook_list_downloader1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; replacementout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along replacement mobileread_wiki_ebook_list_downloader1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/mobileread_wiki_ebook_list_downloader1.java
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



public class mobileread_wiki_ebook_list_downloader1
{
    public static void main(String args[])
    {
        System.out.print("mobileread_wiki_ebook_list_downloader1  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/publishing-systems/automated_digital_publishing/\n" +
                         "and the project website http://www.publishing-systems.org.\n\n");

        String programPath = mobileread_wiki_ebook_list_downloader1.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        if (args.length < 2)
        {
            System.out.print("Usage:\n" +
                             "\tmobileread_wiki_ebook_list_downloader1 mobileread-wiki-ebook-list-url out-directory\n\n");
            System.exit(1);
        }

        File outDirectory = new File(args[1]);

        if (outDirectory.exists() != true)
        {
            if (outDirectory.mkdirs() != true)
            {
                System.out.print("mobileread_wiki_ebook_list_downloader1: Can't create output directory '" + outDirectory.getAbsolutePath() + "'.\n");
                System.exit(-1);
            }
        }
        else
        {
            if (outDirectory.isFile() == true)
            {
                System.out.print("mobileread_wiki_ebook_list_downloader1: Output path '" + outDirectory.getAbsolutePath() + "' already exists, but is a file instead of a directory.\n");
                System.exit(-1);
            }
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() == true)
        {
            if (mobileread_wiki_ebook_list_downloader1.DeleteFileRecursively(tempDirectory) != 0)
            {
                System.out.println("mobileread_wiki_ebook_list_downloader1: Can't clean '" + tempDirectory.getAbsolutePath() + "'.");
                System.exit(-1);
            }
        }

        if (tempDirectory.mkdir() != true)
        {
            System.out.print("mobileread_wiki_ebook_list_downloader1: Can't create temp directory '" + tempDirectory.getAbsolutePath() + "'.\n");
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
            System.out.print("mobileread_wiki_ebook_list_downloader1: '" + wikiEbookList.getAbsolutePath() + "' doesn't exist, but should by now.\n");
            System.exit(-1);
        }

        if (wikiEbookList.isFile() != true)
        {
            System.out.print("mobileread_wiki_ebook_list_downloader1: '" + wikiEbookList.getAbsolutePath() + "' isn't a file.\n");
            System.exit(-1);
        }

        if (wikiEbookList.canRead() != true)
        {
            System.out.print("mobileread_wiki_ebook_list_downloader1: '" + wikiEbookList.getAbsolutePath() + "' isn't readable.\n");
            System.exit(-1);
        }

        if (mobileread_wiki_ebook_list_downloader1.CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1" + File.separator + "entities" + File.separator + "config_empty.xml"),
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
                            System.out.print("mobileread_wiki_ebook_list_downloader1: Link entry #" + linkEntryCount + " in '" + wikiEbookList.getAbsolutePath() + "' doesn't contain characters.\n");
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
            System.out.print("mobileread_wiki_ebook_list_downloader1: No link entries extracted into '" + wikiEbookList.getAbsolutePath() + "'.\n");
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
                System.out.print("mobileread_wiki_ebook_list_downloader1: '" + threadPage.getAbsolutePath() + "' as a result of downloading '" + threadLinks.get(i) + "' doesn't exist, but should by now.\n");
                System.exit(-1);
            }

            if (threadPage.isFile() != true)
            {
                System.out.print("mobileread_wiki_ebook_list_downloader1: '" + threadPage.getAbsolutePath() + "' isn't a file.\n");
                System.exit(-1);
            }

            if (threadPage.canRead() != true)
            {
                System.out.print("mobileread_wiki_ebook_list_downloader1: '" + threadPage.getAbsolutePath() + "' isn't readable.\n");
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

        if (mobileread_wiki_ebook_list_downloader1.CopyFileBinary(new File(programPath + "xsltransformator1_entities_config_xhtml1-transitional.xml"),
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

        for (int i = 0; i < threadLinks.size(); i++)
        {
            File attachmentListFile = new File(tempDirectory.getAbsolutePath() + File.separator + i + ".xml");

            if (attachmentListFile.length() <= 0)
            {
                // Transformation failed.
                System.out.println("mobileread_wiki_ebook_list_downloader1: Thread page '" + threadLinks.get(i) + "', downloaded to '" + tempDirectory.getAbsolutePath() + File.separator + i + ".xhtml', isn't well-formed.");
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
                            
                            if (attachmentLink.isEmpty() == false)
                            {
                                linkEntryCount++;
                                attachmentLinks.add(new AttachmentInfo(attachmentLink, attachmentName));
                            }
                            else
                            {
                                System.out.print("mobileread_wiki_ebook_list_downloader1: Attachment link entry in '" + attachmentListFile.getAbsolutePath() + "' doesn't contain characters.\n");
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
        }

        for (int i = 0; i < attachmentLinks.size(); i++)
        {
            builder = new ProcessBuilder("java", "downloader1", "http://www.mobileread.com/forums/" + attachmentLinks.get(i).GetAttachmentLink(), outDirectory.getAbsolutePath() + File.separator + attachmentLinks.get(i).GetAttachmentName());
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

        return;
    }

    public static int DeleteFileRecursively(File file)
    {
        if (file.isDirectory() == true)
        {
            for (File child : file.listFiles())
            {
                if (mobileread_wiki_ebook_list_downloader1.DeleteFileRecursively(child) != 0)
                {
                    return -1;
                }
            }
        }
        
        if (file.delete() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_downloader1: Can't delete '" + file.getAbsolutePath() + "'.");
            return -1;
        }
    
        return 0;
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_downloader1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_downloader1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_downloader1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
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
    public AttachmentInfo(String attachmentLink, String attachmentName)
    {
        this.attachmentLink = attachmentLink;
        this.attachmentName = attachmentName;
    }
    
    public String GetAttachmentLink()
    {
        return this.attachmentLink;
    }
    
    public String GetAttachmentName()
    {
        return this.attachmentName;
    }
    
    private String attachmentLink;
    private String attachmentName;
}

