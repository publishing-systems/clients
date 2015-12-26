/* Copyright (C) 2015 Stephan Kreutzer
 *
 * This file is part of mobileread_wiki_ebook_list_validation_uploader_statistics1
 *
 * mobileread_wiki_ebook_list_validation_uploader_statistics1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * mobileread_wiki_ebook_list_validation_uploader_statistics1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with mobileread_wiki_ebook_list_validation_uploader_statistics1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/mobileread_wiki_ebook_list_validation_uploader_statistics1.java
 * @author Stephan Kreutzer
 * @since 2015-11-18
 */



import java.io.File;
import java.util.Map;
import java.util.HashMap;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;



public class mobileread_wiki_ebook_list_validation_uploader_statistics1
{
    public static void main(String args[])
    {
        System.out.print("mobileread_wiki_ebook_list_validation_uploader_statistics1\n" +
                         "Copyright (C) 2015 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/clients/ and the project website\n" +
                         "http://www.publishing-systems.org.\n\n");

        if (args.length < 2)
        {
            System.out.println("Usage:\n\tmobileread_wiki_ebook_list_validation_uploader_statistics1 downloads-in-file uploader-statistics-out-file\n");
            System.exit(-1);
        }


        File downloadsFile = new File(args[0]);

        if (downloadsFile.exists() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Downloads file \"" + downloadsFile.getAbsolutePath() + "\" doesn't exist.");
            System.exit(-1);
        }

        if (downloadsFile.isFile() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Downloads path \"" + downloadsFile.getAbsolutePath() + "\" isn't a file.");
            System.exit(-1);
        }

        if (downloadsFile.canRead() != true)
        {
            System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Downloads file \"" + downloadsFile.getAbsolutePath() + "\" isn't readable.");
            System.exit(-1);
        }

        File resultFile = new File(args[1]);

        if (resultFile.exists() == true)
        {
            if (resultFile.isDirectory() == true)
            {
                System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Result path \"" + resultFile.getAbsolutePath() + "\" is a directory.");
                System.exit(-1);
            }

            if (resultFile.canWrite() != true)
            {
                System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Result file \"" + resultFile.getAbsolutePath() + "\" isn't writable.");
                System.exit(-1);
            }
        }

        System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Call with downloads file \"" + downloadsFile.getAbsolutePath() + "\" and result file \"" + resultFile.getAbsolutePath() + "\".");


        Map<String, ValidationUploaderStatistics> statistics = new HashMap<String, ValidationUploaderStatistics>();

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(downloadsFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            int downloadEntryNumber = 0;
            
            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("download") == true)
                    {
                        downloadEntryNumber++;

                        StartElement downloadElement = event.asStartElement();

                        Attribute userAttribute = downloadElement.getAttributeByName(new QName("user"));

                        if (userAttribute == null)
                        {
                            System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Entry #" + downloadEntryNumber + " is missing its 'user' attribute.");
                            continue;
                        }

                        Attribute resultAttribute = downloadElement.getAttributeByName(new QName("result"));

                        if (resultAttribute == null)
                        {
                            System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Entry #" + downloadEntryNumber + " is missing its 'result' attribute.");
                            continue;
                        }

                        Attribute userLinkAttribute = downloadElement.getAttributeByName(new QName("user_link"));

                        if (resultAttribute == null)
                        {
                            System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Entry #" + downloadEntryNumber + " is missing its 'user_link' attribute.");
                        }

                        ValidationUploaderStatistics uploaderStatistics = null;
                        
                        if (statistics.containsKey(userAttribute.getValue()) == true)
                        {
                            uploaderStatistics = statistics.get(userAttribute.getValue());
                        }
                        else
                        {
                            uploaderStatistics = new ValidationUploaderStatistics(userAttribute.getValue());
                            statistics.put(userAttribute.getValue(), uploaderStatistics);
                        }

                        String result = resultAttribute.getValue();

                        if (result.equals("error") == true)
                        {
                            uploaderStatistics.incrementErrors();
                            
                            if (uploaderStatistics.getLinkComplain() == null)
                            {
                                uploaderStatistics.setLinkComplain(userLinkAttribute.getValue());
                            }
                            else
                            {
                                if (uploaderStatistics.getLinkComplain().equals(userLinkAttribute.getValue()) != true)
                                {
                                    System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Attribute 'user_link' of entry #" + downloadEntryNumber + " with value '" + userLinkAttribute.getValue() + "' differs from the previously encountered link '" + uploaderStatistics.getLinkComplain() + "' for user '" + uploaderStatistics.getUploader() + "'.");
                                }
                            }
                        }
                        else if (result.equals("warning") == true)
                        {
                            uploaderStatistics.incrementWarnings();

                            if (uploaderStatistics.getLinkComplain() == null)
                            {
                                uploaderStatistics.setLinkComplain(userLinkAttribute.getValue());
                            }
                            else
                            {
                                if (uploaderStatistics.getLinkComplain().equals(userLinkAttribute.getValue()) != true)
                                {
                                    System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Attribute 'user_link' of entry #" + downloadEntryNumber + " with value '" + userLinkAttribute.getValue() + "' differs from the previously encountered link '" + uploaderStatistics.getLinkComplain() + "' for user '" + uploaderStatistics.getUploader() + "'.");
                                }
                            }
                        }
                        else if (result.equals("valid") == true)
                        {
                            uploaderStatistics.incrementValids();
                        }
                        else
                        {
                            System.out.println("mobileread_wiki_ebook_list_validation_uploader_statistics1: Attribute 'result' of entry #" + downloadEntryNumber + " contains the unrecognized value '" + resultAttribute.getValue() + "'.");
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

        List<ValidationUploaderStatistics> sortedStatistics = new ArrayList<ValidationUploaderStatistics>();

        for (Map.Entry<String, ValidationUploaderStatistics> entry : statistics.entrySet())
        {
            sortedStatistics.add(entry.getValue());
        }
        
        Collections.sort(sortedStatistics, new Comparator<ValidationUploaderStatistics>() {
            public int compare(ValidationUploaderStatistics lhs, ValidationUploaderStatistics rhs)
            {
                int lhsErrors = lhs.getErrors() + lhs.getWarnings();
                int rhsErrors = rhs.getErrors() + rhs.getWarnings();
                int lhsValids = lhs.getValids();
                int rhsValids = rhs.getValids();
                
                if (lhsErrors < rhsErrors)
                {
                    return -1;
                }
                else if (lhsErrors > rhsErrors)
                {
                    return 1;
                }
                else
                {
                    if (lhsErrors == 0 && rhsErrors == 0)
                    {
                        if (lhsValids < rhsValids)
                        {
                            return 1;
                        }
                        else if (lhsValids > rhsValids)
                        {
                            return -1;
                        }
                        else
                        {
                            return lhs.getUploader().compareTo(rhs.getUploader());
                        }
                    }
                    else
                    {
                        return lhs.getUploader().compareTo(rhs.getUploader());
                    }
                }
            }
        });

        int errorsTotal = 0;
        int warningsTotal = 0;
        int validsTotal = 0;

        for (ValidationUploaderStatistics uploader : sortedStatistics)
        {
            errorsTotal += uploader.getErrors();
            warningsTotal += uploader.getWarnings();
            validsTotal += uploader.getValids();
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(resultFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by mobileread_wiki_ebook_list_validation_uploader_statistics1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). -->\n");
            writer.write("<validation-uploader-statistics>\n");

            for (ValidationUploaderStatistics uploaderStatistics : sortedStatistics)
            {
                String uploaderName = uploaderStatistics.getUploader();

                // Ampersand needs to be the first, otherwise it would double-encode
                // other entities.
                uploaderName = uploaderName.replaceAll("&", "&amp;");
                uploaderName = uploaderName.replaceAll("\"", "&quot;");
                uploaderName = uploaderName.replaceAll("'", "&apos;");
                uploaderName = uploaderName.replaceAll("<", "&lt;");
                uploaderName = uploaderName.replaceAll(">", "&gt;");

                writer.write("  <uploader name=\"" + uploaderName + "\" errors=\"" + uploaderStatistics.getErrors() + "\" warnings=\"" + uploaderStatistics.getWarnings() + "\" valids=\"" + uploaderStatistics.getValids() + "\"");

                if (uploaderStatistics.getErrors() > 0 ||
                    uploaderStatistics.getWarnings() > 0)
                {
                    String complainLink = uploaderStatistics.getLinkComplain();

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    complainLink = complainLink.replaceAll("&", "&amp;");
                    complainLink = complainLink.replaceAll("\"", "&quot;");
                    complainLink = complainLink.replaceAll("'", "&apos;");
                    complainLink = complainLink.replaceAll("<", "&lt;");
                    complainLink = complainLink.replaceAll(">", "&gt;");

                    writer.write(" user_url=\"" + complainLink + "\"");
                }

                writer.write("/>\n");
            }

            writer.write("  <sum errors=\"" + errorsTotal + "\" warnings=\"" + warningsTotal + "\" valids=\"" + validsTotal + "\"/>\n");

            writer.write("</validation-uploader-statistics>\n");
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

