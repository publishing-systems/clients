/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of wiktionary_article_to_xml_1, a submodule of the
 * wiktionary_dump_to_xml_1 package.
 *
 * wiktionary_article_to_xml_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * wiktionary_article_to_xml_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with wiktionary_article_to_xml_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/org.wiktionary/wiktionary_dump_to_xml_1/wiktionary_article_to_xml_1/wiktionary_article_to_xml_1.java
 * @brief Converts a wiktionary.org article in Mediawiki wikitext format into XML.
 * @author Stephan Kreutzer
 * @since 2016-12-06
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
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.io.FileWriter;



public class wiktionary_article_to_xml_1
{
    public static void main(String args[])
    {
        System.out.print("wiktionary_article_to_xml_1 Copyright (C) 2016-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/refugee-it/clients/ and the project website\n" +
                         "https://www.refugee-it.de.\n\n");

        wiktionary_article_to_xml_1 converter = new wiktionary_article_to_xml_1();

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
                writer.write("<!-- This file was created by wiktionary_article_to_xml_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                writer.write("<wiktionary-article-to-xml-1-result-information>\n");
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

                writer.write("  </success>\n");
                writer.write("</wiktionary-article-to-xml-1-result-information>\n");
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
        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\twiktionary_article_to_xml_1 " + getI10nString("messageParameterList") + "\n");
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
            if (resultInfoFile.isFile() == true)
            {
                if (resultInfoFile.canWrite() != true)
                {
                    throw constructTermination("messageResultInfoFileIsntWritable", null, null, resultInfoFile.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageResultInfoPathIsntAFile", null, null, resultInfoFile.getAbsolutePath());
            }
        }

        wiktionary_article_to_xml_1.resultInfoFile = resultInfoFile;
/*
        String programPath = wiktionary_article_to_xml_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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
*/

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

        System.out.println("wiktionary_article_to_xml_1: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        File inputFile = null;
        File outputFile = null;

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
                            outputFile != null)
                        {
                            break;
                        }
                    }
                    else if (tagName.equals("output-file") == true)
                    {
                        StartElement outputFileElement = event.asStartElement();
                        Attribute pathAttribute = outputFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (outputFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        outputFile = new File(pathAttribute.getValue());

                        if (outputFile.isAbsolute() != true)
                        {
                            outputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

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

                        if (inputFile != null &&
                            outputFile != null)
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

        if (outputFile == null)
        {
            throw constructTermination("messageJobFileOutputFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "output-file");
        }


        this.tokens = new ArrayList<String>();

        try
        {
            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(
                                    new FileInputStream(inputFile),
                                    "UTF-8"));

            try
            {
                int character = reader.read();
                int lastCharacter = -1;
                String buffer = new String();
                boolean isUTF16 = false;

                while (character >= 0)
                {
                    isUTF16 = false;

                    if (Character.isHighSurrogate((char)character) == true)
                    {
                        int character2 = reader.read();

                        if (character2 == 0)
                        {
                            throw constructTermination("messageTokenizerSurrogateAborted", null, null, (char)character, String.format("0x%X", (int)character));
                        }

                        if (Character.isLowSurrogate((char)character2) != true)
                        {
                            throw constructTermination("messageTokenizerSurrogateIncomplete", null, null, (char)character2, String.format("0x%X", (int)character2));
                        }

                        character = character * 0x10000;
                        character += character2;

                        isUTF16 = true;
                    }

                    if (character == '=' ||
                        character == ' ' ||
                        character == '\n' ||
                        character == '\t' ||
                        character == '{' ||
                        character == '}' ||
                        character == '[' ||
                        character == ']' ||
                        character == '|' ||
                        character == '(' ||
                        character == ')' ||
                        character == '\'' ||
                        character == ':' ||
                        character == '-' ||
                        character == '*' ||
                        character == '_')
                    {
                        if (lastCharacter != character &&
                            buffer.isEmpty() != true)
                        {
                            this.tokens.add(buffer);
                            buffer = "";
                        }

                        if (isUTF16 == false)
                        {
                            buffer += (char)character;
                        }
                        else
                        {
                            byte[] codePoints = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(character).array();
                            buffer += new String(codePoints, "UTF-16");
                        }

                        lastCharacter = character;
                    }
                    else if (isLetter(character))
                    {
                        if (isLetter(lastCharacter) != true &&
                            buffer.isEmpty() != true)
                        {
                            this.tokens.add(buffer);
                            buffer = "";
                        }

                        if (isUTF16 == false)
                        {
                            buffer += (char)character;
                        }
                        else
                        {
                            byte[] codePoints = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(character).array();
                            buffer += new String(codePoints, "UTF-16");
                        }

                        lastCharacter = character;
                    }
                    else if (Character.isDigit(character))
                    {
                        if (Character.isDigit(lastCharacter) != true &&
                            buffer.isEmpty() != true)
                        {
                            this.tokens.add(buffer);
                            buffer = "";
                        }

                        if (isUTF16 == false)
                        {
                            buffer += (char)character;
                        }
                        else
                        {
                            byte[] codePoints = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(character).array();
                            buffer += new String(codePoints, "UTF-16");
                        }

                        lastCharacter = character;
                    }
                    else if (character == ',' ||
                             character == '.' ||
                             character == '·' ||
                             character == '“' ||
                             character == '”' ||
                             character == '„' ||
                             character == '‟' ||
                             character == '‘' ||
                             character == '’' ||
                             character == '…' ||
                             character == '<' ||
                             character == '>' ||
                             character == '–' || // EN DASH
                             character == '—' || // EM DASH
                             character == ';' ||
                             character == '"' ||
                             character == '%' ||
                             character == '!' ||
                             character == '?' ||
                             character == '&' ||
                             character == '+' ||
                             character == '#' ||
                             character == '«' ||
                             character == '»' ||
                             character == 'િ' ||
                             character == 'ે' ||
                             character == '→' ||
                             character == '´' ||
                             character == '§' ||
                             character == '›' ||
                             character == '‹' ||
                             character == '~' ||
                             character == '¡' ||
                             character == '€' ||
                             character == '$' ||
                             character == '\\' ||
                             character == '¹' ||
                             character == '²' ||
                             character == '³' ||
                             character == '†' ||
                             character == '↓' ||
                             character == '½' ||
                             character == '₁' ||
                             character == '₂' ||
                             character == '₃' ||
                             character == 'ⴷ' ||
                             character == '@' ||
                             character == '^' ||
                             character == 0x0060 ||     // GRAVE ACCENT
                             character == 0x0088 ||     // CHARACTER TABULATION SET
                             character == 0x00A0 ||     // NO-BREAK SPACE
                             character == 0x00A2 ||     // CENT SIGN
                             character == 0x00A3 ||     // POUND SIGN
                             character == 0x00A4 ||     // CURRENCY SIGN
                             character == 0x00A5 ||     // YEN SIGN
                             character == 0x00A6 ||     // BROKEN BAR
                             character == 0x00A8 ||     // DIAERESIS
                             character == 0x00A9 ||     // COPYRIGHT SIGN
                             character == 0x00AC ||     // NOT SIGN
                             character == 0x00AD ||     // SOFT HYPHEN
                             character == 0x00AE ||     // REGISTERED SIGN
                             character == 0x00AF ||     // MACRON
                             character == 0x00B0 ||     // DEGREE SIGN
                             character == 0x00B1 ||     // PLUS-MINUS SIGN
                             character == 0x00B6 ||     // PILCROW SIGN
                             character == 0x00B8 ||     // CEDILLA
                             character == 0x00BC ||     // VULGAR FRACTION ONE QUARTER
                             character == 0x00BE ||     // VULGAR FRACTION THREE QUARTERS
                             character == 0x00BF ||     // INVERTED QUESTION MARK
                             character == 0x00D7 ||     // MULTIPLICATION SIGN
                             character == 0x00F7 ||     // DIVISION SIGN
                             character == 0x02D6 ||     // MODIFIER LETTER PLUS SIGN
                             character == 0x02D8 ||     // BREVE
                             character == 0x02D9 ||     // DOT ABOVE
                             character == 0x02DD ||     // DOUBLE ACUTE ACCENT
                             character == 0x02E5 ||     // MODIFIER LETTER EXTRA-HIGH TONE BAR
                             character == 0x02E7 ||     // MODIFIER LETTER MID TONE BAR
                             character == 0x02E8 ||     // MODIFIER LETTER LOW TONE BAR
                             character == 0x0300 ||     // COMBINING GRAVE ACCENT
                             character == 0x0301 ||     // COMBINING ACUTE ACCENT
                             character == 0x0302 ||     // COMBINING CIRCUMFLEX ACCENT
                             character == 0x0303 ||     // COMBINING TILDE
                             character == 0x0304 ||     // COMBINING MACRON
                             character == 0x0305 ||     // COMBINING OVERLINE
                             character == 0x0306 ||     // COMBINING BREVE
                             character == 0x0307 ||     // COMBINING DOT ABOVE
                             character == 0x0308 ||     // COMBINING DIAERESIS
                             character == 0x0309 ||     // COMBINING HOOK ABOVE
                             character == 0x030A ||     // COMBINING RING ABOVE
                             character == 0x030B ||     // COMBINING DOUBLE ACUTE ACCENT
                             character == 0x030C ||     // COMBINING CARON
                             character == 0x030D ||     // COMBINING VERTICAL LINE ABOVE
                             character == 0x030F ||     // COMBINING DOUBLE GRAVE ACCENT
                             character == 0x0310 ||     // COMBINING CANDRABINDU
                             character == 0x0311 ||     // COMBINING INVERTED BREVE
                             character == 0x0312 ||     // COMBINING TURNED COMMA ABOVE
                             character == 0x0313 ||     // COMBINING COMMA ABOVE
                             character == 0x0315 ||     // COMBINING COMMA ABOVE RIGHT
                             character == 0x031A ||     // COMBINING LEFT ANGLE ABOVE
                             character == 0x031C ||     // COMBINING LEFT HALF RING BELOW
                             character == 0x031D ||     // COMBINING UP TACK BELOW
                             character == 0x031E ||     // COMBINING DOWN TACK BELOW
                             character == 0x031F ||     // COMBINING PLUS SIGN BELOW
                             character == 0x0320 ||     // COMBINING MINUS SIGN BELOW
                             character == 0x0321 ||     // COMBINING PALATALIZED HOOK BELOW
                             character == 0x0322 ||     // COMBINING RETROFLEX HOOK BELOW
                             character == 0x0323 ||     // COMBINING DOT BELOW
                             character == 0x0325 ||     // COMBINING RING BELOW
                             character == 0x0326 ||     // COMBINING COMMA BELOW
                             character == 0x0327 ||     // COMBINING CEDILLA
                             character == 0x0328 ||     // COMBINING OGONEK
                             character == 0x0329 ||     // COMBINING VERTICAL LINE BELOW
                             character == 0x032A ||     // COMBINING BRIDGE BELOW
                             character == 0x032C ||     // COMBINING CARON BELOW
                             character == 0x032D ||     // COMBINING CIRCUMFLEX ACCENT BELOW
                             character == 0x032E ||     // COMBINING BREVE BELOW
                             character == 0x032F ||     // COMBINING INVERTED BREVE BELOW
                             character == 0x0330 ||     // COMBINING TILDE BELOW
                             character == 0x0331 ||     // COMBINING MACRON BELOW
                             character == 0x0333 ||     // COMBINING DOUBLE LOW LINE
                             character == 0x0334 ||     // COMBINING TILDE OVERLAY
                             character == 0x0339 ||     // COMBINING RIGHT HALF RING BELOW
                             character == 0x033A ||     // COMBINING INVERTED BRIDGE BELOW
                             character == 0x033B ||     // COMBINING SQUARE BELOW
                             character == 0x033D ||     // COMBINING X ABOVE
                             character == 0x033F ||     // COMBINING DOUBLE OVERLINE
                             character == 0x0342 ||     // COMBINING GREEK PERISPOMENI
                             character == 0x0348 ||     // COMBINING DOUBLE VERTICAL LINE BELOW
                             character == 0x034D ||     // COMBINING LEFT RIGHT ARROW BELOW
                             character == 0x0358 ||     // COMBINING DOT ABOVE RIGHT
                             character == 0x035C ||     // COMBINING DOUBLE BREVE BELOW
                             character == 0x035D ||     // COMBINING DOUBLE BREVE
                             character == 0x035E ||     // COMBINING DOUBLE MACRON
                             character == 0x035F ||     // COMBINING DOUBLE MACRON BELOW
                             character == 0x0361 ||     // COMBINING DOUBLE INVERTED BREVE
                             character == 0x0364 ||     // COMBINING LATIN SMALL LETTER E
                             character == 0x0366 ||     // COMBINING LATIN SMALL LETTER O
                             character == 0x0375 ||     // GREEK LOWER NUMERAL SIGN
                             character == 0x0384 ||     // GREEK TONOS
                             character == 0x04CF ||     // CYRILLIC SMALL LETTER PALOCHKA
                             character == 0x055E ||     // ARMENIAN QUESTION MARK
                             character == 0x0591 ||     // HEBREW ACCENT ETNAHTA
                             character == 0x0596 ||     // HEBREW ACCENT TIPEHA
                             character == 0x05A5 ||     // HEBREW ACCENT MERKHA
                             character == 0x05AB ||     // HEBREW ACCENT OLE
                             character == 0x05B0 ||     // HEBREW POINT SHEVA
                             character == 0x05B1 ||     // HEBREW POINT HATAF SEGOL
                             character == 0x05B2 ||     // HEBREW POINT HATAF PATAH
                             character == 0x05B3 ||     // HEBREW POINT HATAF QAMATS
                             character == 0x05B4 ||     // HEBREW POINT HIRIQ
                             character == 0x05B5 ||     // HEBREW POINT TSERE
                             character == 0x05B6 ||     // HEBREW POINT SEGOL
                             character == 0x05B7 ||     // HEBREW POINT PATAH
                             character == 0x05B8 ||     // HEBREW POINT QAMATS
                             character == 0x05B9 ||     // HEBREW POINT HOLAM
                             character == 0x05BB ||     // HEBREW POINT QUBUTS
                             character == 0x05BC ||     // HEBREW POINT DAGESH OR MAPIQ
                             character == 0x05BD ||     // HEBREW POINT METEG
                             character == 0x05BE ||     // HEBREW PUNCTUATION MAQAF
                             character == 0x05BF ||     // HEBREW POINT RAFE
                             character == 0x05C0 ||     // HEBREW PUNCTUATION PASEQ
                             character == 0x05C1 ||     // HEBREW POINT SHIN DOT
                             character == 0x05C2 ||     // HEBREW POINT SIN DOT
                             character == 0x05C3 ||     // HEBREW PUNCTUATION SOF PASUQ
                             character == 0x05C5 ||     // HEBREW MARK LOWER DOT
                             character == 0x05C7 ||     // HEBREW POINT QAMATS QATAN
                             character == 0x05F3 ||     // HEBREW PUNCTUATION GERESH
                             character == 0x05F4 ||     // HEBREW PUNCTUATION GERSHAYIM
                             character == 0x060C ||     // ARABIC COMMA
                             character == 0x061F ||     // ARABIC QUESTION MARK
                             character == 0x064B ||     // ARABIC FATHATAN
                             character == 0x064C ||     // ARABIC DAMMATAN
                             character == 0x064D ||     // ARABIC KASRATAN
                             character == 0x064E ||     // ARABIC FATHA
                             character == 0x064F ||     // ARABIC DAMMA
                             character == 0x0650 ||     // ARABIC KASRA
                             character == 0x0651 ||     // ARABIC SHADDA
                             character == 0x0652 ||     // ARABIC SUKUN
                             character == 0x066B ||     // ARABIC DECIMAL SEPARATOR
                             character == 0x066C ||     // ARABIC THOUSANDS SEPARATOR
                             character == 0x066D ||     // ARABIC FIVE POINTED STAR
                             character == 0x0670 ||     // ARABIC LETTER SUPERSCRIPT ALEF
                             character == 0x06D4 ||     // ARABIC FULL STOP
                             character == 0x06D8 ||     // ARABIC SMALL HIGH MEEM INITIAL FORM
                             character == 0x06E1 ||     // ARABIC SMALL HIGH DOTLESS HEAD OF KHAH
                             character == 0x0730 ||     // SYRIAC PTHAHA ABOVE
                             character == 0x0733 ||     // SYRIAC ZQAPHA ABOVE
                             character == 0x0735 ||     // SYRIAC ZQAPHA DOTTED
                             character == 0x0736 ||     // SYRIAC RBASA ABOVE
                             character == 0x073A ||     // SYRIAC HBASA ABOVE
                             character == 0x073D ||     // SYRIAC ESASA ABOVE
                             character == 0x0741 ||     // SYRIAC QUSHSHAYA
                             character == 0x0762 ||     // ARABIC LETTER KEHEH WITH DOT ABOVE
                             character == 0x07A6 ||     // THAANA ABAFILI
                             character == 0x07A7 ||     // THAANA AABAAFILI
                             character == 0x07A8 ||     // THAANA IBIFILI
                             character == 0x07A9 ||     // THAANA EEBEEFILI
                             character == 0x07AA ||     // THAANA UBUFILI
                             character == 0x07AB ||     // THAANA OOBOOFILI
                             character == 0x07AC ||     // THAANA EBEFILI
                             character == 0x07AD ||     // THAANA EYBEYFILI
                             character == 0x07AE ||     // THAANA OBOFILI
                             character == 0x07AF ||     // THAANA OABOAFILI
                             character == 0x07B0 ||     // THAANA SUKUN
                             character == 0x0901 ||     // DEVANAGARI SIGN CANDRABINDU
                             character == 0x0902 ||     // DEVANAGARI SIGN ANUSVARA
                             character == 0x0903 ||     // DEVANAGARI SIGN VISARGA
                             character == 0x093C ||     // DEVANAGARI SIGN NUKTA
                             character == 0x093E ||     // DEVANAGARI VOWEL SIGN AA
                             character == 0x093F ||     // DEVANAGARI VOWEL SIGN I
                             character == 0x0940 ||     // DEVANAGARI VOWEL SIGN II
                             character == 0x0941 ||     // DEVANAGARI VOWEL SIGN U
                             character == 0x0942 ||     // DEVANAGARI VOWEL SIGN UU
                             character == 0x0943 ||     // DEVANAGARI VOWEL SIGN VOCALIC R
                             character == 0x0944 ||     // DEVANAGARI VOWEL SIGN VOCALIC RR
                             character == 0x0945 ||     // DEVANAGARI VOWEL SIGN CANDRA E
                             character == 0x0947 ||     // DEVANAGARI VOWEL SIGN E
                             character == 0x0948 ||     // DEVANAGARI VOWEL SIGN AI
                             character == 0x0949 ||     // DEVANAGARI VOWEL SIGN CANDRA O
                             character == 0x094A ||     // DEVANAGARI VOWEL SIGN SHORT O
                             character == 0x094B ||     // DEVANAGARI VOWEL SIGN O
                             character == 0x094C ||     // DEVANAGARI VOWEL SIGN AU
                             character == 0x094D ||     // DEVANAGARI SIGN VIRAMA
                             character == 0x0951 ||     // DEVANAGARI STRESS SIGN UDATTA
                             character == 0x0970 ||     // DEVANAGARI ABBREVIATION SIGN
                             character == 0x0981 ||     // BENGALI SIGN CANDRABINDU
                             character == 0x0982 ||     // BENGALI SIGN ANUSVARA
                             character == 0x09BC ||     // BENGALI SIGN NUKTA
                             character == 0x09BE ||     // BENGALI VOWEL SIGN AA
                             character == 0x09BF ||     // BENGALI VOWEL SIGN I
                             character == 0x09C0 ||     // BENGALI VOWEL SIGN II
                             character == 0x09C1 ||     // BENGALI VOWEL SIGN U
                             character == 0x09C2 ||     // BENGALI VOWEL SIGN UU
                             character == 0x09C3 ||     // BENGALI VOWEL SIGN VOCALIC R
                             character == 0x09C7 ||     // BENGALI VOWEL SIGN E
                             character == 0x09C8 ||     // BENGALI VOWEL SIGN AI
                             character == 0x09CB ||     // BENGALI VOWEL SIGN O
                             character == 0x09CC ||     // BENGALI VOWEL SIGN AU
                             character == 0x09CD ||     // BENGALI SIGN VIRAMA
                             character == 0x09CE ||     // BENGALI LETTER KHANDA TA
                             character == 0x09D7 ||     // BENGALI AU LENGTH MARK
                             character == 0x0A02 ||     // GURMUKHI SIGN BINDI
                             character == 0x0A3C ||     // GURMUKHI SIGN NUKTA
                             character == 0x0A3E ||     // GURMUKHI VOWEL SIGN AA
                             character == 0x0A3F ||     // GURMUKHI VOWEL SIGN I
                             character == 0x0A40 ||     // GURMUKHI VOWEL SIGN II
                             character == 0x0A41 ||     // GURMUKHI VOWEL SIGN U
                             character == 0x0A42 ||     // GURMUKHI VOWEL SIGN UU
                             character == 0x0A47 ||     // GURMUKHI VOWEL SIGN EE
                             character == 0x0A48 ||     // GURMUKHI VOWEL SIGN AI
                             character == 0x0A4B ||     // GURMUKHI VOWEL SIGN OO
                             character == 0x0A4C ||     // GURMUKHI VOWEL SIGN AU
                             character == 0x0A4D ||     // GURMUKHI SIGN VIRAMA
                             character == 0x0A70 ||     // GURMUKHI TIPPI
                             character == 0x0A71 ||     // GURMUKHI ADDAK
                             character == 0x0A81 ||     // GUJARATI SIGN CANDRABINDU
                             character == 0x0A82 ||     // GUJARATI SIGN ANUSVARA
                             character == 0x0ABC ||     // GUJARATI SIGN NUKTA
                             character == 0x0ABE ||     // GUJARATI VOWEL SIGN AA
                             character == 0x0AC0 ||     // GUJARATI VOWEL SIGN II
                             character == 0x0AC1 ||     // GUJARATI VOWEL SIGN U
                             character == 0x0AC2 ||     // GUJARATI VOWEL SIGN UU
                             character == 0x0AC9 ||     // GUJARATI VOWEL SIGN CANDRA O
                             character == 0x0ACB ||     // GUJARATI VOWEL SIGN O
                             character == 0x0ACC ||     // GUJARATI VOWEL SIGN AU
                             character == 0x0ACD ||     // GUJARATI SIGN VIRAMA
                             character == 0x0B3E ||     // ORIYA VOWEL SIGN AA
                             character == 0x0B3F ||     // ORIYA VOWEL SIGN I
                             character == 0x0B40 ||     // ORIYA VOWEL SIGN II
                             character == 0x0B41 ||     // ORIYA VOWEL SIGN U
                             character == 0x0B43 ||     // ORIYA VOWEL SIGN VOCALIC R
                             character == 0x0B47 ||     // ORIYA VOWEL SIGN E
                             character == 0x0B4D ||     // ORIYA SIGN VIRAMA
                             character == 0x0BBE ||     // TAMIL VOWEL SIGN AA
                             character == 0x0BBF ||     // TAMIL VOWEL SIGN I
                             character == 0x0BC0 ||     // TAMIL VOWEL SIGN II
                             character == 0x0BC1 ||     // TAMIL VOWEL SIGN U
                             character == 0x0BC2 ||     // TAMIL VOWEL SIGN UU
                             character == 0x0BC6 ||     // TAMIL VOWEL SIGN E
                             character == 0x0BC7 ||     // TAMIL VOWEL SIGN EE
                             character == 0x0BC8 ||     // TAMIL VOWEL SIGN AI
                             character == 0x0BCA ||     // TAMIL VOWEL SIGN O
                             character == 0x0BCB ||     // TAMIL VOWEL SIGN OO
                             character == 0x0BCC ||     // TAMIL VOWEL SIGN AU
                             character == 0x0BCD ||     // TAMIL SIGN VIRAMA
                             character == 0x0BF0 ||     // TAMIL NUMBER TEN
                             character == 0x0C02 ||     // TELUGU SIGN ANUSVARA
                             character == 0x0C3E ||     // TELUGU VOWEL SIGN AA
                             character == 0x0C3F ||     // TELUGU VOWEL SIGN I
                             character == 0x0C40 ||     // TELUGU VOWEL SIGN II
                             character == 0x0C41 ||     // TELUGU VOWEL SIGN U
                             character == 0x0C42 ||     // TELUGU VOWEL SIGN UU
                             character == 0x0C46 ||     // TELUGU VOWEL SIGN E
                             character == 0x0C47 ||     // TELUGU VOWEL SIGN EE
                             character == 0x0C48 ||     // TELUGU VOWEL SIGN AI
                             character == 0x0C4A ||     // TELUGU VOWEL SIGN O
                             character == 0x0C4B ||     // TELUGU VOWEL SIGN OO
                             character == 0x0C4D ||     // TELUGU SIGN VIRAMA
                             character == 0x0C82 ||     // KANNADA SIGN ANUSVARA
                             character == 0x0CBE ||     // KANNADA VOWEL SIGN AA
                             character == 0x0CBF ||     // KANNADA VOWEL SIGN I
                             character == 0x0CC0 ||     // KANNADA VOWEL SIGN II
                             character == 0x0CC1 ||     // KANNADA VOWEL SIGN U
                             character == 0x0CC2 ||     // KANNADA VOWEL SIGN UU
                             character == 0x0CC3 ||     // KANNADA VOWEL SIGN VOCALIC R
                             character == 0x0CC6 ||     // KANNADA VOWEL SIGN E
                             character == 0x0CC7 ||     // KANNADA VOWEL SIGN EE
                             character == 0x0CC8 ||     // KANNADA VOWEL SIGN AI
                             character == 0x0CCA ||     // KANNADA VOWEL SIGN O
                             character == 0x0CCB ||     // KANNADA VOWEL SIGN OO
                             character == 0x0CCD ||     // KANNADA SIGN VIRAMA
                             character == 0x0D02 ||     // MALAYALAM SIGN ANUSVARA
                             character == 0x0D3E ||     // MALAYALAM VOWEL SIGN AA
                             character == 0x0D3F ||     // MALAYALAM VOWEL SIGN I
                             character == 0x0D40 ||     // MALAYALAM VOWEL SIGN II
                             character == 0x0D41 ||     // MALAYALAM VOWEL SIGN U
                             character == 0x0D42 ||     // MALAYALAM VOWEL SIGN UU
                             character == 0x0D43 ||     // MALAYALAM VOWEL SIGN VOCALIC R
                             character == 0x0D46 ||     // MALAYALAM VOWEL SIGN E
                             character == 0x0D47 ||     // TELUGU VOWEL SIGN EE
                             character == 0x0D48 ||     // MALAYALAM VOWEL SIGN AI
                             character == 0x0D4A ||     // MALAYALAM VOWEL SIGN O
                             character == 0x0D4B ||     // MALAYALAM VOWEL SIGN OO
                             character == 0x0D4D ||     // MALAYALAM SIGN VIRAMA
                             character == 0x0D57 ||     // MALAYALAM AU LENGTH MARK
                             character == 0x0D82 ||     // SINHALA SIGN ANUSVARAYA
                             character == 0x0DCA ||     // SINHALA SIGN AL-LAKUNA
                             character == 0x0DCF ||     // SINHALA VOWEL SIGN AELA-PILLA
                             character == 0x0DD1 ||     // SINHALA VOWEL SIGN DIGA AEDA-PILLA
                             character == 0x0DD3 ||     // SINHALA VOWEL SIGN DIGA IS-PILLA
                             character == 0x0DD4 ||     // SINHALA VOWEL SIGN KETTI PAA-PILLA
                             character == 0x0DD2 ||     // SINHALA VOWEL SIGN KETTI IS-PILLA
                             character == 0x0DD9 ||     // SINHALA VOWEL SIGN KOMBUVA
                             character == 0x0DDA ||     // SINHALA VOWEL SIGN DIGA KOMBUVA
                             character == 0x0DDC ||     // SINHALA VOWEL SIGN KOMBUVA HAA AELA-PILLA
                             character == 0x0E31 ||     // THAI CHARACTER MAI HAN-AKAT
                             character == 0x0E34 ||     // THAI CHARACTER SARA I
                             character == 0x0E35 ||     // THAI CHARACTER SARA II
                             character == 0x0E36 ||     // THAI CHARACTER SARA UE
                             character == 0x0E37 ||     // THAI CHARACTER SARA UEE
                             character == 0x0E38 ||     // THAI CHARACTER SARA U
                             character == 0x0E39 ||     // THAI CHARACTER SARA UU
                             character == 0x0E3A ||     // LAO LETTER LO LING
                             character == 0x0E3F ||     // THAI CURRENCY SYMBOL BAHT
                             character == 0x0E47 ||     // THAI CHARACTER MAITAIKHU
                             character == 0x0E48 ||     // THAI CHARACTER MAI EK
                             character == 0x0E49 ||     // THAI CHARACTER MAI THO
                             character == 0x0E4A ||     // THAI CHARACTER MAI TRI
                             character == 0x0E4B ||     // THAI CHARACTER MAI CHATTAWA
                             character == 0x0E4C ||     // THAI CHARACTER THANTHAKHAT
                             character == 0x0E4D ||     // THAI CHARACTER NIKHAHIT
                             character == 0x0EB1 ||     // LAO VOWEL SIGN MAI KAN
                             character == 0x0EB4 ||     // LAO VOWEL SIGN I
                             character == 0x0EB5 ||     // LAO VOWEL SIGN II
                             character == 0x0EB6 ||     // LAO VOWEL SIGN Y
                             character == 0x0EB7 ||     // LAO VOWEL SIGN YY
                             character == 0x0EB8 ||     // LAO VOWEL SIGN U
                             character == 0x0EB9 ||     // LAO VOWEL SIGN UU
                             character == 0x0EBB ||     // LAO VOWEL SIGN MAI KON
                             character == 0x0EC8 ||     // LAO TONE MAI EK
                             character == 0x0EC9 ||     // LAO TONE MAI THO
                             character == 0x0ECD ||     // LAO NIGGAHITA
                             character == 0x0F0B ||     // TIBETAN MARK INTERSYLLABIC TSHEG
                             character == 0x0F0D ||     // TIBETAN MARK SHAD
                             character == 0x0F71 ||     // TIBETAN VOWEL SIGN AA
                             character == 0x0F72 ||     // TIBETAN VOWEL SIGN I
                             character == 0X0F74 ||     // TIBETAN VOWEL SIGN U
                             character == 0x0F7C ||     // TIBETAN VOWEL SIGN O
                             character == 0x0F7A ||     // TIBETAN VOWEL SIGN E
                             character == 0x0F90 ||     // TIBETAN SUBJOINED LETTER KA
                             character == 0x0F94 ||     // TIBETAN SUBJOINED LETTER NGA
                             character == 0x0F95 ||     // TIBETAN SUBJOINED LETTER CA
                             character == 0x0FA6 ||     // TIBETAN SUBJOINED LETTER BA
                             character == 0x0FAD ||     // TIBETAN SUBJOINED LETTER WA
                             character == 0x0FB1 ||     // TIBETAN SUBJOINED LETTER YA
                             character == 0x0FB2 ||     // TIBETAN SUBJOINED LETTER RA
                             character == 0x0FB3 ||     // TIBETAN SUBJOINED LETTER LA
                             character == 0x102B ||     // MYANMAR VOWEL SIGN TALL AA
                             character == 0x102C ||     // MYANMAR VOWEL SIGN AA
                             character == 0x102D ||     // MYANMAR VOWEL SIGN I
                             character == 0x102E ||     // MYANMAR VOWEL SIGN II
                             character == 0x102F ||     // MYANMAR VOWEL SIGN U
                             character == 0x1030 ||     // MYANMAR VOWEL SIGN UU
                             character == 0x1031 ||     // MYANMAR VOWEL SIGN E
                             character == 0x1032 ||     // MYANMAR VOWEL SIGN AI
                             character == 0x1038 ||     // MYANMAR SIGN VISARGA
                             character == 0x1039 ||     // MYANMAR SIGN VIRAMA
                             character == 0x103A ||     // MYANMAR SIGN ASAT
                             character == 0x103C ||     // MYANMAR CONSONANT SIGN MEDIAL RA
                             character == 0x103D ||     // MYANMAR CONSONANT SIGN MEDIAL WA
                             character == 0x1361 ||     // ETHIOPIC WORDSPACE
                             character == 0x1363 ||     // ETHIOPIC COMMA
                             character == 0x1393 ||     // ETHIOPIC TONAL MARK SHORT RIKRIK
                             character == 0x17B6 ||     // KHMER VOWEL SIGN AA
                             character == 0x17B7 ||     // KHMER VOWEL SIGN I
                             character == 0x17B8 ||     // KHMER VOWEL SIGN II
                             character == 0x17BA ||     // KHMER VOWEL SIGN YY
                             character == 0x17BB ||     // KHMER VOWEL SIGN U
                             character == 0x17BC ||     // KHMER VOWEL SIGN UU
                             character == 0x17BD ||     // KHMER VOWEL SIGN UA
                             character == 0x17C0 ||     // KHMER VOWEL SIGN IE
                             character == 0x17C1 ||     // KHMER VOWEL SIGN E
                             character == 0x17C2 ||     // KHMER VOWEL SIGN AE
                             character == 0x17C6 ||     // KHMER SIGN NIKAHIT
                             character == 0x17C8 ||     // KHMER SIGN YUUKALEAPINTU
                             character == 0x17C9 ||     // KHMER SIGN MUUSIKATOAN
                             character == 0x17CA ||     // KHMER SIGN TRIISAP
                             character == 0x17CB ||     // KHMER SIGN BANTOC
                             character == 0x17CD ||     // KHMER SIGN TOANDAKHIAT
                             character == 0x17D2 ||     // KHMER SIGN COENG
                             character == 0x1D79 ||     // LATIN SMALL LETTER INSULAR G
                             character == 0x1D7B ||     // LATIN SMALL CAPITAL LETTER I WITH STROKE
                             character == 0x1DC4 ||     // COMBINING MACRON-ACUTE
                             character == 0x1E9E ||     // LATIN CAPITAL LETTER SHARP S
                             character == 0x1FBD ||     // GREEK KORONIS
                             character == 0x1FBF ||     // GREEK PSILI
                             character == 0x1FCF ||     // GREEK PSILI AND PERISPOMENI
                             character == 0x1FFE ||     // GREEK DASIA
                             character == 0x2002 ||     // EN SPACE
                             character == 0x2005 ||     // FOUR-PER-EM SPACE
                             character == 0x2008 ||     // PUNCTUATION SPACE
                             character == 0x2009 ||     // THIN SPACE
                             character == 0x200A ||     // HAIR SPACE
                             character == 0x200B ||     // ZERO WIDTH SPACE
                             character == 0x200C ||     // ZERO WIDTH NON-JOINER
                             character == 0x200D ||     // ZERO WIDTH JOINER
                             character == 0x200E ||     // LEFT-TO-RIGHT MARK
                             character == 0x200F ||     // RIGHT-TO-LEFT MARK
                             character == 0x2010 ||     // HYPHEN
                             character == 0x2011 ||     // NON-BREAKING HYPHEN
                             character == 0x2012 ||     // FIGURE DASH
                             character == 0x2015 ||     // HORIZONTAL BAR
                             character == 0x2016 ||     // DOUBLE VERTICAL LINE
                             character == 0x201A ||     // SINGLE LOW-9 QUOTATION MARK
                             character == 0x2021 ||     // DOUBLE DAGGER
                             character == 0x2022 ||     // BULLET
                             character == 0x2027 ||     // HYPHENATION POINT
                             character == 0x2028 ||     // LINE SEPARATOR
                             character == 0x202A ||     // LEFT-TO-RIGHT EMBEDDING
                             character == 0x202C ||     // POP DIRECTIONAL FORMATTING
                             character == 0x202F ||     // NARROW NO-BREAK SPACE
                             character == 0x2030 ||     // PER MILLE SIGN
                             character == 0x2032 ||     // PRIME
                             character == 0x2033 ||     // DOUBLE PRIME
                             character == 0x203D ||     // INTERROBANG
                             character == 0x203F ||     // UNDERTIE
                             character == 0x2040 ||     // CHARACTER TIE
                             character == 0x2042 ||     // ASTERISM
                             character == 0x2044 ||     // FRACTION SLASH
                             character == 0x2052 ||     // COMMERCIAL MINUS SIGN
                             character == 0x205D ||     // TRICOLON
                             character == 0x2060 ||     // WORD JOINER
                             character == 0x2070 ||     // SUPERSCRIPT ZERO
                             character == 0x2074 ||     // SUPERSCRIPT FOUR
                             character == 0x2075 ||     // SUPERSCRIPT FIVE
                             character == 0x2077 ||     // SUPERSCRIPT SEVEN
                             character == 0x207A ||     // SUPERSCRIPT PLUS SIGN
                             character == 0x207D ||     // SUPERSCRIPT LEFT PARENTHESIS
                             character == 0x20A8 ||     // RUPEE SIGN
                             character == 0x20A9 ||     // WON SIGN
                             character == 0x20AA ||     // NEW SHEQEL SIGN
                             character == 0x20AB ||     // DONG SIGN
                             character == 0x20AD ||     // KIP SIGN
                             character == 0x20B0 ||     // GERMAN PENNY SIGN
                             character == 0x20B3 ||     // AUSTRAL SIGN
                             character == 0x2100 ||     // ACCOUNT OF
                             character == 0x2103 ||     // DEGREE CELSIUS
                             character == 0x2114 ||     // L B BAR SYMBOL
                             character == 0x2116 ||     // NUMERO SIGN
                             character == 0x2117 ||     // SOUND RECORDING COPYRIGHT
                             character == 0x2121 ||     // TELEPHONE SIGN
                             character == 0x2122 ||     // TRADE MARK SIGN
                             character == 0x214B ||     // TURNED AMPERSAND
                             character == 0x2153 ||     // TURNED AMPERSAND
                             character == 0x215C ||     // VULGAR FRACTION THREE EIGHTHS
                             character == 0x215E ||     // VULGAR FRACTION SEVEN EIGHTHS
                             character == 0x2160 ||     // ROMAN NUMERAL ONE
                             character == 0x2161 ||     // ROMAN NUMERAL TWO
                             character == 0x2162 ||     // ROMAN NUMERAL THREE
                             character == 0x2163 ||     // ROMAN NUMERAL FOUR
                             character == 0x2164 ||     // ROMAN NUMERAL FIVE
                             character == 0x2165 ||     // ROMAN NUMERAL SIX
                             character == 0x2166 ||     // ROMAN NUMERAL SEVEN
                             character == 0x2167 ||     // ROMAN NUMERAL EIGHT
                             character == 0x2168 ||     // ROMAN NUMERAL NINE
                             character == 0x2169 ||     // ROMAN NUMERAL TEN
                             character == 0x216A ||     // ROMAN NUMERAL ELEVEN
                             character == 0x216B ||     // ROMAN NUMERAL TWELVE
                             character == 0x216C ||     // ROMAN NUMERAL FIFTY
                             character == 0x216D ||     // ROMAN NUMERAL ONE HUNDRED
                             character == 0x216E ||     // ROMAN NUMERAL FIVE HUNDRED
                             character == 0x216F ||     // ROMAN NUMERAL ONE THOUSAND
                             character == 0x2170 ||     // SMALL ROMAN NUMERAL ONE
                             character == 0x2171 ||     // SMALL ROMAN NUMERAL TWO
                             character == 0x2172 ||     // SMALL ROMAN NUMERAL THREE
                             character == 0x2173 ||     // SMALL ROMAN NUMERAL FOUR
                             character == 0x2174 ||     // SMALL ROMAN NUMERAL FIVE
                             character == 0x2175 ||     // SMALL ROMAN NUMERAL SIX
                             character == 0x2176 ||     // SMALL ROMAN NUMERAL SEVEN
                             character == 0x2177 ||     // SMALL ROMAN NUMERAL EIGHT
                             character == 0x2178 ||     // SMALL ROMAN NUMERAL NINE
                             character == 0x2179 ||     // SMALL ROMAN NUMERAL TEN
                             character == 0x217A ||     // SMALL ROMAN NUMERAL ELEVEN
                             character == 0x217B ||     // SMALL ROMAN NUMERAL TWELVE
                             character == 0x2181 ||     // ROMAN NUMERAL FIVE THOUSAND
                             character == 0x2182 ||     // ROMAN NUMERAL TEN THOUSAND
                             character == 0x2190 ||     // LEFTWARDS ARROW
                             character == 0x2191 ||     // UPWARDS ARROW
                             character == 0x2194 ||     // LEFT RIGHT ARROW
                             character == 0x2197 ||     // NORTH EAST ARROW
                             character == 0x21AA ||     // RIGHTWARDS ARROW WITH HOOK
                             character == 0x21D2 ||     // RIGHTWARDS DOUBLE ARROW
                             character == 0x21D4 ||     // LEFT RIGHT DOUBLE ARROW
                             character == 0x2200 ||     // FOR ALL
                             character == 0x2202 ||     // PARTIAL DIFFERENTIAL
                             character == 0x2203 ||     // THERE EXISTS
                             character == 0x2206 ||     // INCREMENT
                             character == 0x2207 ||     // NABLA
                             character == 0x2212 ||     // MINUS SIGN
                             character == 0x2213 ||     // MINUS-OR-PLUS SIGN
                             character == 0x2218 ||     // RING OPERATOR
                             character == 0x2219 ||     // BULLET OPERATOR
                             character == 0x221A ||     // SQUARE ROOT
                             character == 0x221B ||     // CUBE ROOT
                             character == 0x221D ||     // PROPORTIONAL TO
                             character == 0x221E ||     // INFINITY
                             character == 0x2221 ||     // MEASURED ANGLE
                             character == 0x2227 ||     // LOGICAL AND
                             character == 0x2229 ||     // INTERSECTION
                             character == 0x222A ||     // UNION
                             character == 0x222B ||     // INTEGRAL
                             character == 0x222E ||     // CONTOUR INTEGRAL
                             character == 0x2236 ||     // RATIO
                             character == 0x2248 ||     // ALMOST EQUAL TO
                             character == 0x2258 ||     // CORRESPONDS TO
                             character == 0x2259 ||     // ESTIMATES
                             character == 0x2260 ||     // NOT EQUAL TO
                             character == 0x2261 ||     // IDENTICAL TO
                             character == 0x2264 ||     // LESS-THAN OR EQUAL TO
                             character == 0x2265 ||     // GREATER-THAN OR EQUAL TO
                             character == 0x22C0 ||     // N-ARY LOGICAL AND
                             character == 0x22C5 ||     // DOT OPERATOR
                             character == 0x22C6 ||     // STAR OPERATOR
                             character == 0x2300 ||     // DIAMETER SIGN
                             character == 0x2318 ||     // PLACE OF INTEREST SIGN
                             character == 0x232C ||     // BENZENE RING
                             character == 0x23D1 ||     // METRICAL BREVE
                             character == 0x2422 ||     // BLANK SYMBOL
                             character == 0x2460 ||     // CIRCLED DIGIT ONE
                             character == 0x249C ||     // PARENTHESIZED LATIN SMALL LETTER A
                             character == 0x24B6 ||     // CIRCLED LATIN CAPITAL LETTER A
                             character == 0x24D0 ||     // CIRCLED LATIN SMALL LETTER J
                             character == 0x25A1 ||     // WHITE SQUARE
                             character == 0x25AD ||     // WHITE RECTANGLE
                             character == 0x25B3 ||     // WHITE UP-POINTING TRIANGLE
                             character == 0x25BA ||     // BLACK RIGHT-POINTING POINTER
                             character == 0x25BC ||     // BLACK DOWN-POINTING TRIANGLE
                             character == 0x2603 ||     // SNOWMAN
                             character == 0x2605 ||     // BLACK STAR
                             character == 0x2606 ||     // WHITE STAR
                             character == 0x2607 ||     // LIGHTNING
                             character == 0x2609 ||     // SUN
                             character == 0x2613 ||     // SALTIRE
                             character == 0x261E ||     // WHITE RIGHT POINTING INDEX
                             character == 0x2623 ||     // BIOHAZARD SIGN
                             character == 0x2624 ||     // CADUCEUS
                             character == 0x2627 ||     // CHI RHO
                             character == 0x262D ||     // HAMMER AND SICKLE
                             character == 0x263E ||     // LAST QUARTER MOON
                             character == 0x263F ||     // MERCURY
                             character == 0x2640 ||     // FEMALE SIGN
                             character == 0x2641 ||     // EARTH
                             character == 0x2642 ||     // MALE SIGN
                             character == 0x2643 ||     // JUPITER
                             character == 0x2644 ||     // SATURN
                             character == 0x2645 ||     // URANUS
                             character == 0x2646 ||     // NEPTUNE
                             character == 0x2647 ||     // PLUTO
                             character == 0x264B ||     // CANCER
                             character == 0x264F ||     // SCORPIUS
                             character == 0x2650 ||     // SAGITTARIUS
                             character == 0x2658 ||     // WHITE CHESS KNIGHT
                             character == 0x265E ||     // BLACK CHESS KNIGHT
                             character == 0x2663 ||     // BLACK CLUB SUIT
                             character == 0x2665 ||     // BLACK HEART SUIT
                             character == 0x2669 ||     // QUARTER NOTE
                             character == 0x266A ||     // EIGHTH NOTE
                             character == 0x266D ||     // MUSIC FLAT SIGN
                             character == 0x266E ||     // MUSIC NATURAL SIGN
                             character == 0x266F ||     // MUSIC SHARP SIGN
                             character == 0x2694 ||     // CROSSED SWORDS
                             character == 0x2695 ||     // STAFF OF AESCULAPIUS
                             character == 0x26AD ||     // MARRIAGE SYMBOL
                             character == 0x2713 ||     // CHECK MARK
                             character == 0x271D ||     // LATIN CROSS
                             character == 0x271E ||     // SHADOWED WHITE LATIN CROSS
                             character == 0x2721 ||     // STAR OF DAVID
                             character == 0x2729 ||     // STRESS OUTLINED WHITE STAR
                             character == 0x272A ||     // CIRCLED WHITE STAR
                             character == 0x272B ||     // OPEN CENTRE BLACK STAR
                             character == 0x272C ||     // BLACK CENTRE WHITE STAR
                             character == 0x272D ||     // OUTLINED BLACK STAR
                             character == 0x272E ||     // HEAVY OUTLINED BLACK STAR
                             character == 0x272F ||     // PINWHEEL STAR
                             character == 0x2730 ||     // SHADOWED WHITE STAR
                             character == 0x2731 ||     // HEAVY ASTERISK
                             character == 0x2732 ||     // OPEN CENTRE ASTERISK
                             character == 0x2733 ||     // EIGHT SPOKED ASTERISK
                             character == 0x2734 ||     // EIGHT POINTED BLACK STAR
                             character == 0x2735 ||     // EIGHT POINTED PINWHEEL STAR
                             character == 0x2736 ||     // SIX POINTED BLACK STAR
                             character == 0x2737 ||     // EIGHT POINTED RECTILINEAR BLACK STAR
                             character == 0x2738 ||     // HEAVY EIGHT POINTED RECTILINEAR BLACK STAR
                             character == 0x2739 ||     // TWELVE POINTED BLACK STAR
                             character == 0x273A ||     // SIXTEEN POINTED ASTERISK
                             character == 0x273B ||     // TEARDROP-SPOKED ASTERISK
                             character == 0x273C ||     // OPEN CENTRE TEARDROP-SPOKED ASTERISK
                             character == 0x273D ||     // HEAVY TEARDROP-SPOKED ASTERISK
                             character == 0x2742 ||     // CIRCLED OPEN CENTRE EIGHT POINTED STAR
                             character == 0x2743 ||     // HEAVY TEARDROP-SPOKED PINWHEEL ASTERISK
                             character == 0x2749 ||     // BALLOON-SPOKED ASTERISK
                             character == 0x274A ||     // EIGHT TEARDROP-SPOKED PROPELLER ASTERISK
                             character == 0x274B ||     // HEAVY EIGHT TEARDROP-SPOKED PROPELLER ASTERISK
                             character == 0x279D ||     // TRIANGLE-HEADED RIGHTWARDS ARROW
                             character == 0x27E8 ||     // MATHEMATICAL LEFT ANGLE BRACKET
                             character == 0x27E9 ||     // MATHEMATICAL RIGHT ANGLE BRACKET
                             character == 0x2C65 ||     // LATIN SMALL LETTER A WITH STROKE
                             character == 0x2E17 ||     // DOUBLE OBLIQUE HYPHEN
                             character == 0x3000 ||     // IDEOGRAPHIC SPACE
                             character == 0x3001 ||     // IDEOGRAPHIC COMMA
                             character == 0x3002 ||     // IDEOGRAPHIC FULL STOP
                             character == 0x3003 ||     // DITTO MARK
                             character == 0x3007 ||     // IDEOGRAPHIC NUMBER ZERO
                             character == 0x3008 ||     // LEFT ANGLE BRACKET
                             character == 0x3009 ||     // RIGHT ANGLE BRACKET
                             character == 0x300C ||     // LEFT CORNER BRACKET
                             character == 0x3021 ||     // HANGZHOU NUMERAL ONE
                             character == 0x3022 ||     // HANGZHOU NUMERAL TWO
                             character == 0x3023 ||     // HANGZHOU NUMERAL THREE
                             character == 0x3029 ||     // HANGZHOU NUMERAL NINE
                             character == 0x309B ||     // KATAKANA-HIRAGANA VOICED SOUND MARK
                             character == 0x30FB ||     // KATAKANA MIDDLE DOT
                             character == 0x3332 ||     // SQUARE HUARADDO
                             character == 0xA651 ||     // CYRILLIC SMALL LETTER YERU WITH BACK YER
                             character == 0xA657 ||     // CYRILLIC SMALL LETTER IOTIFIED A
                             character == 0xA722 ||     // LATIN CAPITAL LETTER EGYPTOLOGICAL ALEF
                             character == 0xA75B ||     // LATIN SMALL LETTER R ROTUNDA
                             character == 0xA997 ||     // JAVANESE LETTER JA
                             character == 0xAA22 ||     // CHAM LETTER YA
                             character == 0xFE20 ||     // COMBINING LIGATURE LEFT HALF
                             character == 0xFE62 ||     // SMALL PLUS SIGN
                             character == 0xFE6A ||     // SMALL PERCENT SIGN
                             character == 0xFEFF ||     // ZERO WIDTH NO-BREAK SPACE
                             character == 0xFF01 ||     // FULLWIDTH EXCLAMATION MARK
                             character == 0xFF06 ||     // FULLWIDTH AMPERSAND
                             character == 0xFF08 ||     // FULLWIDTH LEFT PARENTHESIS
                             character == 0xFF09 ||     // FULLWIDTH RIGHT PARENTHESIS
                             character == 0xFF0C ||     // FULLWIDTH COMMA
                             character == 0xFF0F ||     // FULLWIDTH SOLIDUS
                             character == 0xFF1A ||     // FULLWIDTH COLON
                             character == 0xFF1B ||     // FULLWIDTH SEMICOLON
                             character == 0xFF1D ||     // FULLWIDTH EQUALS SIGN
                             character == 0xFF1F ||     // FULLWIDTH QUESTION MARK
                             character == 0xFF61 ||     // HALFWIDTH IDEOGRAPHIC FULL STOP
                             character == 0xFF65 ||     // HALFWIDTH KATAKANA MIDDLE DOT
                             // UTF-16 codepoints:
                             character == 0xD808DC00 || // CUNEIFORM SIGN A
                             character == 0xD808DC09 || // CUNEIFORM SIGN A2
                             character == 0xD808DC0A || // CUNEIFORM SIGN AB
                             character == 0xD808DC14 || // CUNEIFORM SIGN AB TIMES U PLUS U PLUS U
                             character == 0xD808DC1C || // CUNEIFORM SIGN AD
                             character == 0xD808DC1D || // CUNEIFORM SIGN AK
                             character == 0xD808DC2D || // CUNEIFORM SIGN AN
                             character == 0xD808DC33 || // CUNEIFORM SIGN APIN
                             character == 0xD808DC34 || // CUNEIFORM SIGN ARAD
                             character == 0xD808DC38 || // CUNEIFORM SIGN ASH
                             character == 0xD808DC3E || // CUNEIFORM SIGN ASH2
                             character == 0xD808DC40 || // CUNEIFORM SIGN BA
                             character == 0xD808DC49 || // CUNEIFORM SIGN BI
                             character == 0xD808DC4D || // CUNEIFORM SIGN BU
                             character == 0xD808DC53 || // CUNEIFORM SIGN BUR
                             character == 0xD808DC55 || // CUNEIFORM SIGN DA
                             character == 0xD808DC6E || // CUNEIFORM SIGN DAM
                             character == 0xD808DC72 || // CUNEIFORM SIGN DI
                             character == 0xD808DC74 || // CUNEIFORM SIGN DIM
                             character == 0xD808DC75 || // CUNEIFORM SIGN DIM TIMES SHE
                             character == 0xD808DC76 || // CUNEIFORM SIGN DIM2
                             character == 0xD808DC77 || // CUNEIFORM SIGN DIN
                             character == 0xD808DC79 || // CUNEIFORM SIGN DISH
                             character == 0xD808DC7A || // CUNEIFORM SIGN DU
                             character == 0xD808DC7B || // CUNEIFORM SIGN DU OVER DU
                             character == 0xD808DC7E || // CUNEIFORM SIGN DUB
                             character == 0xD808DC81 || // CUNEIFORM SIGN DUG
                             character == 0xD808DC84 || // CUNEIFORM SIGN DUN
                             character == 0xD808DC85 || // CUNEIFORM SIGN DUN3
                             character == 0xD808DC86 || // CUNEIFORM SIGN DUN3 GUNU
                             character == 0xD808DC8A || // CUNEIFORM SIGN E
                             character == 0xD808DC8D || // CUNEIFORM SIGN E2
                             character == 0xD808DC97 || // CUNEIFORM SIGN EN
                             character == 0xD808DCA0 || // CUNEIFORM SIGN ESH2
                             character == 0xD808DCA6 || // CUNEIFORM SIGN EZEN TIMES BAD
                             character == 0xD808DCB5 || // CUNEIFORM SIGN GA
                             character == 0xD808DCB7 || // CUNEIFORM SIGN GA2
                             character == 0xD808DCBC || // CUNEIFORM SIGN GA2 TIMES AN
                             character == 0xD808DCF2 || // CUNEIFORM SIGN GAL
                             character == 0xD808DCF7 || // CUNEIFORM SIGN GAN2
                             character == 0xD808DCFB || // CUNEIFORM SIGN GAR
                             character == 0xD808DCFC || // CUNEIFORM SIGN GAR3
                             character == 0xD808DD00 || // CUNEIFORM SIGN GI
                             character == 0xD808DD04 || // CUNEIFORM SIGN GI4
                             character == 0xD808DD08 || // CUNEIFORM SIGN GIR2
                             character == 0xD808DD0A || // CUNEIFORM SIGN GIR3
                             character == 0xD808DD11 || // CUNEIFORM SIGN GISH
                             character == 0xD808DD16 || // CUNEIFORM SIGN GU
                             character == 0xD808DD1E || // CUNEIFORM SIGN GUD
                             character == 0xD808DD20 || // CUNEIFORM SIGN GUD TIMES KUR
                             character == 0xD808DD23 || // CUNEIFORM SIGN GUM
                             character == 0xD808DD24 || // CUNEIFORM SIGN GUM TIMES SHE
                             character == 0xD808DD25 || // CUNEIFORM SIGN GUR
                             character == 0xD808DD29 || // CUNEIFORM SIGN HA
                             character == 0xD808DD2D || // CUNEIFORM SIGN HI
                             character == 0xD808DD2F || // CUNEIFORM SIGN HI TIMES ASH2
                             character == 0xD808DD3F || // CUNEIFORM SIGN I
                             character == 0xD808DD45 || // CUNEIFORM SIGN IG
                             character == 0xD808DD46 || // CUNEIFORM SIGN IGI
                             character == 0xD808DD47 || // CUNEIFORM SIGN IGI DIB
                             character == 0xD808DD4E || // CUNEIFORM SIGN IM
                             character == 0xD808DD56 || // CUNEIFORM SIGN ISH
                             character == 0xD808DD57 || // CUNEIFORM SIGN KA
                             character == 0xD808DD74 || // CUNEIFORM SIGN KA TIMES ME
                             character == 0xD808DD8D || // CUNEIFORM SIGN KA2
                             character == 0xD808DD95 || // CUNEIFORM SIGN KAK
                             character == 0xD808DD97 || // CUNEIFORM SIGN KAL
                             character == 0xD808DD9C || // CUNEIFORM SIGN KASKAL
                             character == 0xD808DDA0 || // CUNEIFORM SIGN KI
                             character == 0xD808DDAC || // CUNEIFORM SIGN KU3
                             character == 0xD808DDB0 || // CUNEIFORM SIGN KUL
                             character == 0xD808DDB3 || // CUNEIFORM SIGN KUR
                             character == 0xD808DDB7 || // CUNEIFORM SIGN LA
                             character == 0xD808DDC9 || // CUNEIFORM SIGN LAGAB TIMES HAL
                             character == 0xD808DDF2 || // CUNEIFORM SIGN LAL
                             character == 0xD808DDF7 || // CUNEIFORM SIGN LI
                             character == 0xD808DDFB || // CUNEIFORM SIGN LU
                             character == 0xD808DDFD || // CUNEIFORM SIGN LU2
                             character == 0xD808DE02 || // CUNEIFORM SIGN LU2 TIMES GAN2 TENU
                             character == 0xD808DE17 || // CUNEIFORM SIGN LUGAL
                             character == 0xD808DE20 || // CUNEIFORM SIGN MA
                             character == 0xD808DE23 || // CUNEIFORM SIGN MA2
                             character == 0xD808DE25 || // CUNEIFORM SIGN MAR
                             character == 0xD808DE28 || // CUNEIFORM SIGN ME
                             character == 0xD808DE2C || // CUNEIFORM SIGN MU
                             character == 0xD808DE32 || // CUNEIFORM SIGN MUSH
                             character == 0xD808DE3E || // CUNEIFORM SIGN NA
                             character == 0xD808DE3F || // CUNEIFORM SIGN NA2
                             character == 0xD808DE40 || // CUNEIFORM SIGN NAGA
                             character == 0xD808DE46 || // CUNEIFORM SIGN NAM
                             character == 0xD808DE48 || // CUNEIFORM SIGN NE
                             character == 0xD808DE4C || // CUNEIFORM SIGN NI
                             character == 0xD808DE52 || // CUNEIFORM SIGN NINDA2
                             character == 0xD808DE58 || // CUNEIFORM SIGN NINDA2 TIMES NE
                             character == 0xD808DE61 || // CUNEIFORM SIGN NU
                             character == 0xD808DE63 || // CUNEIFORM SIGN NUN
                             character == 0xD808DE7A || // CUNEIFORM SIGN PA
                             character == 0xD808DE8F || // CUNEIFORM SIGN RA
                             character == 0xD808DE92 || // CUNEIFORM SIGN RU
                             character == 0xD808DE93 || // CUNEIFORM SIGN SA
                             character == 0xD808DE95 || // CUNEIFORM SIGN SAG
                             character == 0xD808DEA9 || // CUNEIFORM SIGN SAL
                             character == 0xD808DEAD || // CUNEIFORM SIGN SHA
                             character == 0xD808DEAE || // CUNEIFORM SIGN SHA3
                             character == 0xD808DEBA || // CUNEIFORM SIGN SHE
                             character == 0xD808DEC0 || // CUNEIFORM SIGN SHESH
                             character == 0xD808DED7 || // CUNEIFORM SIGN SHU
                             character == 0xD808DEE1 || // CUNEIFORM SIGN SILA3
                             character == 0xD808DEE2 || // CUNEIFORM SIGN SU
                             character == 0xD808DEE7 || // CUNEIFORM SIGN SUM
                             character == 0xD808DEEB || // CUNEIFORM SIGN TA
                             character == 0xD808DEFA || // CUNEIFORM SIGN TAK4
                             character == 0xD808DEFB || // CUNEIFORM SIGN TAR
                             character == 0xD808DEFC || // CUNEIFORM SIGN TE
                             character == 0xD808DEFE || // CUNEIFORM SIGN TI
                             character == 0xD808DF05 || // CUNEIFORM SIGN TU
                             character == 0xD808DF07 || // CUNEIFORM SIGN TUK
                             character == 0xD808DF09 || // CUNEIFORM SIGN TUR
                             character == 0xD808DF0B || // CUNEIFORM SIGN U
                             character == 0xD808DF0C || // CUNEIFORM SIGN U GUD
                             character == 0xD808DF0D || // CUNEIFORM SIGN U U U
                             character == 0xD808DF11 || // CUNEIFORM SIGN U2
                             character == 0xD808DF13 || // CUNEIFORM SIGN UD
                             character == 0xD808DF17 || // CUNEIFORM SIGN UD TIMES U PLUS U PLUS U
                             character == 0xD808DF1D || // CUNEIFORM SIGN UM
                             character == 0xD808DF24 || // CUNEIFORM SIGN UMUM TIMES KASKAL
                             character == 0xD808DF26 || // CUNEIFORM SIGN UN
                             character == 0xD808DF28 || // CUNEIFORM SIGN UR
                             character == 0xD808DF2B || // CUNEIFORM SIGN UR2
                             character == 0xD808DF35 || // CUNEIFORM SIGN URI
                             character == 0xD808DF51 || // CUNEIFORM SIGN USH
                             character == 0xD808DF5D || // CUNEIFORM SIGN ZA
                             character == 0xD808DF63 || // CUNEIFORM SIGN ZI
                             character == 0xD808DF65 || // CUNEIFORM SIGN ZI3
                             character == 0xD808DF6A || // CUNEIFORM SIGN ZU
                             character == 0xD809DC03 || // CUNEIFORM NUMERIC SIGN FIVE ASH
                             character == 0xD809DC0C || // CUNEIFORM NUMERIC SIGN SEVEN DISH
                             character == 0xD809DC10 || // CUNEIFORM NUMERIC SIGN FIVE U
                             character == 0xD834DD2B || // MUSICAL SYMBOL DOUBLE FLAT
                             character == 0xD835DCAB || // MATHEMATICAL SCRIPT CAPITAL P
                             character == 0xD835DD04 || // MATHEMATICAL FRAKTUR CAPITAL A
                             character == 0xD835DD1E || // MATHEMATICAL FRAKTUR SMALL A
                             character == 0xD835DD38 || // MATHEMATICAL DOUBLE-STRUCK CAPITAL A
                             character == 0xD835DD46 || // MATHEMATICAL DOUBLE-STRUCK CAPITAL O
                             character == 0xD835DF41 || // MATHEMATICAL BOLD ITALIC SMALL MU
                             character == 0xD840DC01 || // CJK UNIFIED IDEOGRAPH 'the original form for U+4E03' U+20001
                             character == 0xD840DC0B || // CJK UNIFIED IDEOGRAPH U+2000B
                             character == 0xD840DCCA || // CJK UNIFIED IDEOGRAPH U+200CA
                             character == 0xD840DDA4 || // CJK UNIFIED IDEOGRAPH U+201A4
                             character == 0xD840DE2F || // CJK UNIFIED IDEOGRAPH U+2022F
                             character == 0xD840DF3C || // CJK UNIFIED IDEOGRAPH U+2033C
                             character == 0xD840DF7A || // CJK UNIFIED IDEOGRAPH U+2037A
                             character == 0xD841DC25 || // CJK UNIFIED IDEOGRAPH U+20425
                             character == 0xD841DD3D || // CJK UNIFIED IDEOGRAPH U+2053D
                             //character == 0xD841DD52 || // CJK UNIFIED IDEOGRAPH U+20552 
                             character == 0xD842DC66 || // CJK UNIFIED IDEOGRAPH U+20866
                             character == 0xD842DD2A || // CJK UNIFIED IDEOGRAPH U+2092A
                             character == 0xD842DEB3 || // CJK UNIFIED IDEOGRAPH U+20AB3
                             character == 0xD844DDA0 || // CJK UNIFIED IDEOGRAPH U+211A0
                             character == 0xD844DDB8 || // CJK UNIFIED IDEOGRAPH U+211B8
                             character == 0xD844DE75 || // CJK UNIFIED IDEOGRAPH U+21275
                             character == 0xD847DD16 || // CJK UNIFIED IDEOGRAPH U+21D16
                             character == 0xD849DEED || // CJK UNIFIED IDEOGRAPH U+226ED
                             character == 0xD84ADC43 || // CJK UNIFIED IDEOGRAPH U+22843
                             character == 0xD84CDD8A || // CJK UNIFIED IDEOGRAPH U+2318A
                             //character == 0xD855DF62 || // CJK UNIFIED IDEOGRAPH U+25762
                             character == 0xD85CDDD4 || // CJK UNIFIED IDEOGRAPH U+271D4
                             //character == 0xD85CDE30 || // CJK UNIFIED IDEOGRAPH U+27230
                             character == 0xD862DDC0 || // CJK UNIFIED IDEOGRAPH U+27230
                             character == 0xD862DE0F || // CJK UNIFIED IDEOGRAPH U+28A0F
                             character == 0xD862DF46)   // CJK UNIFIED IDEOGRAPH U+28B46
                             //character == 0xD86DDF44)   // 

                    {
                        if (lastCharacter != character)
                        {
                            if (buffer.isEmpty() != true)
                            {
                                this.tokens.add(buffer);
                                buffer = "";
                            }
                        }
                        else
                        {
                            /*
                            File repeatFile = new File("repeat.log");

                            try
                            {
                                BufferedWriter out = new BufferedWriter(
                                                     new FileWriter(repeatFile, true));

                                try
                                {
                                    out.append(String.format("0x%X", (int)character) + " in " + inputFile.getAbsolutePath() + "\n");
                                }
                                finally
                                {
                                    out.close();
                                }
                            }
                            catch (FileNotFoundException ex)
                            {
                                throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                            }
                            catch (UnsupportedEncodingException ex)
                            {
                                throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                            }
                            catch (IOException ex)
                            {
                                throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                            }
                            */

                            throw constructTermination("messageTokenizerCharacterIsRepeating", null, null, (char)character);
                        }

                        if (isUTF16 == false)
                        {
                            this.tokens.add(new String() + (char)character);
                        }
                        else
                        {
                            byte[] codePoints = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(character).array();
                            this.tokens.add(new String(codePoints, "UTF-16"));
                        }

                        lastCharacter = character;
                    }
                    else if (character == '/')
                    {
                        if (buffer.isEmpty() != true)
                        {
                            this.tokens.add(buffer);
                            buffer = "";
                        }

                        if (isUTF16 == false)
                        {
                            this.tokens.add(new String() + (char)character);
                        }
                        else
                        {
                            byte[] codePoints = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(character).array();
                            this.tokens.add(new String(codePoints, "UTF-16"));
                        }

                        lastCharacter = character;
                    }
                    else if (character == 0x0084 || // https://de.wikipedia.org/wiki/Unicodeblock_Lateinisch-1,_Erg%C3%A4nzung
                             character == 0x0093 || // SET TRANSMIT STATE
                             character == 0x0094 || // CANCEL CHARACTER
                             character == 0x0096 || // START OF GUARDED AREA
                             character == 0x0097 || // END OF GUARDED AREA
                             character == 0x009D || // OPERATING SYSTEM COMMAND
                             character == 0x009F || // APPLICATION PROGRAM COMMAND
                             character == 0x201B || // SINGLE HIGH-REVERSED-9 QUOTATION MARK: use 0x2018 instead.
                             character == 0xE006 || // Isn't valid Unicode.
                             character == 0xF62A || // Isn't valid Unicode.
                             character == 0xF4C1 || // Isn't valid Unicode.
                             character == 0xFFFD)   // REPLACEMENT CHARACTER
                    {
                        /*
                        File invalidFile = new File("invalid.log");

                        try
                        {
                            BufferedWriter out = new BufferedWriter(
                                                 new FileWriter(invalidFile, true));

                            try
                            {
                                out.append(String.format("0x%X", (int)character) + " in " + inputFile.getAbsolutePath() + "\n");
                            }
                            finally
                            {
                                out.close();
                            }
                        }
                        catch (FileNotFoundException ex)
                        {
                            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                        }
                        catch (UnsupportedEncodingException ex)
                        {
                            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                        }
                        */

                        throw constructTermination("messageTokenizerInvalidCharacter", null, null, (char)character, String.format("0x%X", (int)character));
                    }
                    else
                    {
                        /*
                        File codepointFile = new File("codepoints.log");

                        try
                        {
                            BufferedWriter out = new BufferedWriter(
                                                 new FileWriter(codepointFile, true));

                            try
                            {
                                out.append(String.format("0x%X", (int)character) + " in " + inputFile.getAbsolutePath() + "\n");
                            }
                            finally
                            {
                                out.close();
                            }
                        }
                        catch (FileNotFoundException ex)
                        {
                            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                        }
                        catch (UnsupportedEncodingException ex)
                        {
                            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
                        }
                        */

                        throw constructTermination("messageTokenizerUnknownCharacter", null, null, (char)character, String.format("0x%X", (int)character));
                    }

                    character = reader.read();
                }

                if (buffer.isEmpty() != true)
                {
                    this.tokens.add(buffer);
                }
            }
            finally
            {
                reader.close();
            }
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTokenizerErrorWhileTokenizing", ex, null);
        }

        /*
        for (int i = 0; i < this.tokens.size(); i++)
        {
            System.out.println(i + ": \"" + this.tokens.get(i) + "\".");
        }
        */

        ParserGerman parser = new ParserGerman(this.tokens, getInfoMessages());
        StringBuilder sbOutput = parser.parse();

        if (sbOutput.length() > 0)
        {
            try
            {
                BufferedWriter outputWriter = new BufferedWriter(
                                            new OutputStreamWriter(
                                            new FileOutputStream(outputFile),
                                            "UTF-8"));

                try
                {
                    outputWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    outputWriter.append("<!-- This file was created by wiktionary_article_to_xml_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                    outputWriter.append("<wiktionary-article>");
                    outputWriter.append(sbOutput.toString());
                    outputWriter.append("</wiktionary-article>\n");
                }
                finally
                {
                    outputWriter.close();
                }
            }
            catch (FileNotFoundException ex)
            {
                throw constructTermination("messageParserErrorWhileParsing", ex, null);
            }
            catch (UnsupportedEncodingException ex)
            {
                throw constructTermination("messageParserErrorWhileParsing", ex, null);
            }
            catch (IOException ex)
            {
                throw constructTermination("messageParserErrorWhileParsing", ex, null);
            }
        }
        else
        {
            throw constructTermination("messageParserFailed", null, null, inputFile.getAbsolutePath());
        }

        return 0;
    }

    public boolean isLetter(int character)
    {
        return Character.isLetter(character) ||
               // SAMARITAN LETTER
               (character >= 0x0800 &&
                character <= 0x0815) ||
               // MALAYALAM LETTER
               (character >= 0x0D7A &&
                character <= 0x0D7F) ||
               // COPTIC CAPITAL/SMALL LETTER
               (character >= 0x2C80 &&
                character <= 0x2CB1) ||
               // TIFINAGH LETTER
               (character >= 0x2D30 &&
                character <= 0x2D65) ||
               // LYCIAN LETTER
               (character >= 0xD800DE80 &&
                character <= 0xD800DE9C) ||
               // GOTHIC LETTER
               (character >= 0xD800DF30 &&
                character <= 0xD800DF49) ||
               // UGARITIC LETTER
               (character >= 0xD800DF80 &&
                character <= 0xD800DF9D) ||
               // IMPERIAL ARAMAIC LETTER
               (character >= 0xD802DC40 &&
                character <= 0xD802DC55) ||
               // PHOENICIAN LETTER
               (character >= 0xD802DD00 &&
                character <= 0xD802DD15) ||
               // AVESTAN LETTER
               (character >= 0xD802DF00 &&
                character <= 0xD802DF35);
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
                message = "wiktionary_article_to_xml_1: " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_article_to_xml_1: " + getI10nStringFormatted(id, arguments);
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
                message = "wiktionary_article_to_xml_1: " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_article_to_xml_1: " + getI10nStringFormatted(id, arguments);
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

        if (wiktionary_article_to_xml_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wiktionary_article_to_xml_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wiktionary_article_to_xml_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/refugee-it/clients/ and https://www.refugee-it.de). -->\n");
                writer.write("<wiktionary-article-to-xml-1-result-information>\n");

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

                writer.write("</wiktionary-article-to-xml-1-result-information>\n");
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

        wiktionary_article_to_xml_1.resultInfoFile = null;

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

    protected ArrayList<String> tokens = null;

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nWiktionaryArticleToXml1Console";
    private ResourceBundle l10nConsole;
}
