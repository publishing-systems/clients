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
 * @file $/org.wiktionary/wiktionary_dump_to_xml_1/wiktionary_article_to_xml_1/ParserGerman.java
 * @brief Parses the tokens of a German Wiktionary article and converts them into XML.
 * @author Stephan Kreutzer
 * @since 2016-12-06
 */



import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;



public class ParserGerman
{
    public ParserGerman(List<String> tokens, List<InfoMessage> infoMessages)
    {
        this.tokens = tokens;
        this.infoMessages = infoMessages;
        this.tokenCursor = 0;
    }

    public StringBuilder parse()
    {
        StringBuilder sbResult = new StringBuilder();

        while (this.tokenCursor < this.tokens.size())
        {
            String token = nextToken();

            if (token.equals("=="))
            {
                StringBuilder sbWord = word();

                if (sbWord != null)
                {
                    sbResult.append(sbWord.toString());
                }
            }
        }

        return sbResult;
    }

    public StringBuilder word()
    {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append("<word>");

        if (match(" ") != true)
        {
            return null;
        }

        StringBuilder sbArticleTitle = articleTitle();

        if (sbArticleTitle != null)
        {
            sbResult.append(sbArticleTitle.toString());
        }
        else
        {
            return null;
        }

        if (match(" ") != true)
        {
            return null;
        }

        StringBuilder sbLanguageMarker = languageMarker();

        if (sbLanguageMarker != null)
        {
            sbResult.append(sbLanguageMarker.toString());
        }
        else
        {
            return null;
        }

        if (match(" ") != true ||
            match("==") != true ||
            match("\n") != true)
        {
            return null;
        }

        StringBuilder sbDefinitions = new StringBuilder();

        while (this.tokenCursor < this.tokens.size())
        {
            String token = nextToken();

            if (token.equals("==="))
            {
                StringBuilder sbDefinition = definition();

                if (sbDefinition != null)
                {
                    sbDefinitions.append(sbDefinition.toString());
                }
            }
            /*
            else if (token.equals("=="))
            {
                this.tokenCursor -= 1;
                break;
            }
            */
        }

        if (sbDefinitions.length() > 0)
        {
            sbResult.append(sbDefinitions.toString());
        }
        else
        {
            return null;
        }

        sbResult.append("</word>");

        return sbResult;
    }

    public StringBuilder articleTitle()
    {
        return new StringBuilder("<title>" + nextToken() + "</title>");
    }

    public StringBuilder languageMarker()
    {
        if (match("(") != true ||
            match("{{") != true ||
            match("Sprache") != true ||
            match("|") != true ||
            match("Deutsch") != true ||
            match("}}") != true ||
            match(")") != true)
        {
            return null;
        }

        return new StringBuilder();
    }

    public StringBuilder definition()
    {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append("<definition>");

        if (match(" ") != true)
        {
            return null;
        }

        String token = nextToken();

        if (token.equals("{{"))
        {
            StringBuilder sbWordinfo = wordinfo();

            if (sbWordinfo != null)
            {
                sbResult.append(sbWordinfo.toString());
            }
            else
            {
                return null;
            }
        }
        /*
        else if (token.equals("Übersetzungen"))
        {
            ++this.tokenCursor;
        }
        */

        if (match(" ") != true ||
            match("===") != true)
        {
            return null;
        }

        sbResult.append("</definition>");

        return sbResult;
    }

    public StringBuilder wordinfo()
    {
        StringBuilder sbResult = new StringBuilder();

        if (match("Wortart") != true ||
            match("|") != true)
        {
            return null;
        }

        StringBuilder sbWordtype = wordtype();

        if (sbWordtype != null)
        {
            sbResult.append(sbWordtype.toString());
        }
        else
        {
            return null;
        }

        if (match("|") != true ||
            match("Deutsch") != true ||
            match("}}") != true ||
            match(",") != true ||
            match(" ") != true ||
            match("{{") != true)
        {
            return null;
        }

        StringBuilder sbGender = gender();

        if (sbGender != null)
        {
            sbResult.append(sbGender.toString());
        }
        else
        {
            return null;
        }

        if (match("}}") != true)
        {
            return null;
        }

        if (this.tokenCursor < this.tokens.size())
        {
            if (this.tokens.get(this.tokenCursor).equals(","))
            {
                sbResult.append("<secondary>");
                match(",");
                match(" ");
                match("{{");
                match("Wortart");
                match("|");

                sbWordtype = wordtype();

                if (sbWordtype != null)
                {
                    sbResult.append(sbWordtype.toString());
                }
                else
                {
                    return null;
                }

                match("|");
                match("Deutsch");
                match("}}");
                sbResult.append("</secondary>");
            }
        }

        return sbResult;
    }

    public StringBuilder wordtype()
    {
        StringBuilder sbResult = new StringBuilder("<wordtype>");

        String token = nextToken();

        if (token.equals("Substantiv") == true)
        {
            sbResult.append(token);
        }
        else if (token.equals("Vorname") == true)
        {
            sbResult.append(token);
        }
        else
        {
            this.infoMessages.add(constructInfoMessage("messageParsingError", true, null, null, "Substantiv|Vorname", token));
            return null;
        }

        sbResult.append("</wordtype>");

        return sbResult;
    }

    public StringBuilder gender()
    {
        String token = nextToken();

        if (token.equals("m"))
        {
            return new StringBuilder("<gender>" + token + "</gender>");
        }
        else if (token.equals("f"))
        {
            return new StringBuilder("<gender>" + token + "</gender>");
        }
        else if (token.equals("n"))
        {
            return new StringBuilder("<gender>" + token + "</gender>");
        }

        this.infoMessages.add(constructInfoMessage("messageParsingError", true, null, null, "m|f|n", token));
        return null;
    }

    public boolean match(String required)
    {
        String token = nextToken();

        if (token.equals(required))
        {
            return true;
        }

        this.infoMessages.add(constructInfoMessage("messageParsingError", true, null, null, required, token));
        // throw constructTermination("messageParsingError", null, null, required, token);

        return false;
    }

    public String nextToken()
    {
        if (this.tokenCursor >= this.tokens.size())
        {
            throw constructTermination("messageParserNoMoreTokens", null, null);
        }

        ++this.tokenCursor;
        return this.tokens.get(this.tokenCursor-1);
    }

    protected List<String> tokens;
    protected int tokenCursor;


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
                message = "wiktionary_article_to_xml_1 (ParserGerman): " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_article_to_xml_1 (ParserGerman): " + getI10nStringFormatted(id, arguments);
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
                message = "wiktionary_article_to_xml_1 (ParserGerman): " + getI10nString(id);
            }
            else
            {
                message = "wiktionary_article_to_xml_1 (ParserGerman): " + getI10nStringFormatted(id, arguments);
            }
        }

        return new ProgramTerminationException(id, cause, message, L10N_BUNDLE, arguments);
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
        if (this.l10nParserGerman == null)
        {
            this.l10nParserGerman = ResourceBundle.getBundle(L10N_BUNDLE, this.getLocale());
        }

        try
        {
            return new String(this.l10nParserGerman.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return this.l10nParserGerman.getString(key);
        }
    }

    private String getI10nStringFormatted(String i10nStringName, Object ... arguments)
    {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(this.getLocale());

        formatter.applyPattern(getI10nString(i10nStringName));
        return formatter.format(arguments);
    }

    protected List<InfoMessage> infoMessages = null;

    private static final String L10N_BUNDLE = "l10n.l10nWiktionaryArticleToXml1ParserGerman";
    private ResourceBundle l10nParserGerman;
}
