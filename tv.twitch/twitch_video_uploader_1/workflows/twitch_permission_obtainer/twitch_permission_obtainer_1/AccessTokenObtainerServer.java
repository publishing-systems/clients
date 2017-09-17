/* Copyright (C) 2017 Stephan Kreutzer
 *
 * This file is part of twitch_permission_obtainer_1 workflow of clients for the
 * automated_digital_publishing and digital_publishing_workflow_tools packages.
 *
 * twitch_permission_obtainer_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * twitch_permission_obtainer_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with twitch_permission_obtainer_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/tv.twitch/twitch_video_uploader_1/workflows/twitch_permission_obtainer/twitch_permission_obtainer_1/AccessTokenObtainerServer.java
 * @brief Catches the access_token from the redirect to localhost.
 * @author Stephan Kreutzer
 * @since 2017-09-16
 */



import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Map;



public class AccessTokenObtainerServer implements Runnable, HttpHandler
{
    public AccessTokenObtainerServer(int port, String endpoint, List<InfoMessage> infoMessages, Map<String, String> result)
    {
        this.port = port;
        this.endpoint = endpoint;
        this.infoMessages = infoMessages;
        this.result = result;
    }

    public void run()
    {
        try
        {
            server = HttpServer.create(new InetSocketAddress(this.port), 0);
            server.createContext(this.endpoint, this);
            server.start();
        }
        catch (IOException ex)
        {
            this.infoMessages.add(constructInfoMessage("messageErrorWhileStartingServer", true, ex, null));
            server.stop(0);
            server = null;
        }
    }

    @Override
    public void handle(HttpExchange exchange)
    {
        try
        {
            {
                /** @todo HttpServer and HttpExchange seem to be unable to retain
                  * URL fragments... */
                String parameters = exchange.getRequestURI().getFragment();

                if (parameters == null)
                {
                    this.infoMessages.add(constructInfoMessage("messageResponseURIWithoutParameters", true, null, null));
                    server.stop(0);
                    server = null;
                    return;
                }

                int ampersandPos = parameters.length();

                do
                {
                    ampersandPos = parameters.indexOf('&');
                    String parameter = null;

                    if (ampersandPos >= 0)
                    {
                        parameter = parameters.substring(0, ampersandPos);
                        parameters = parameters.substring(ampersandPos + 1);
                    }
                    else
                    {
                        parameter = parameters;
                    }

                    int equalsPos = parameter.indexOf('=');

                    if (equalsPos < 0)
                    {
                        this.infoMessages.add(constructInfoMessage("messageParameterWithoutEquals", true, null, null));
                        server.stop(0);
                        server = null;
                        return;
                    }

                    String name = parameter.substring(0, equalsPos);
                    String value = parameter.substring(equalsPos + 1);

                    this.result.put(name, value);

                } while (ampersandPos >= 0);
            }

            StringBuilder sbHtml = new StringBuilder();
            sbHtml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sbHtml.append("<!DOCTYPE html\n");
            sbHtml.append("    PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n");
            sbHtml.append("    \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n");
            sbHtml.append("<html version=\"-//W3C//DTD XHTML 1.1//EN\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd\" xml:lang=\"" + this.getLocale().getISO3Language() + "\" lang=\"" + this.getLocale().getISO3Language() + "\">\n");
            sbHtml.append("  <head>\n");
            sbHtml.append("    <meta http-equiv=\"content-type\" content=\"application/xhtml+xml; charset=UTF-8\"/>\n");
            sbHtml.append("    <title>Twitch Video Uploader</title>\n");
            sbHtml.append("  </head>\n");
            sbHtml.append("  <body>\n");
            sbHtml.append("    <div>\n");
            sbHtml.append("      <h1>Twitch Video Uploader</h1>\n");
            sbHtml.append("      <p>\n");

            String messageSuccess = getI10nString("messageSuccess");
            // Ampersand needs to be the first, otherwise it would double-encode
            // other entities.
            messageSuccess = messageSuccess.replaceAll("&", "&amp;");
            messageSuccess = messageSuccess.replaceAll("<", "&lt;");
            messageSuccess = messageSuccess.replaceAll(">", "&gt;");

            sbHtml.append("        " + messageSuccess + "\n");
            sbHtml.append("      </p>\n");
            sbHtml.append("    </div>\n");
            sbHtml.append("  </body>\n");
            sbHtml.append("</html>\n");

            String html = sbHtml.toString();

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.add("Content-Type", "application/xhtml+xml");
            responseHeaders.add("Connection", "close");
            exchange.sendResponseHeaders(200, html.getBytes("UTF-8").length);

            OutputStream body = exchange.getResponseBody();
            body.write(html.getBytes("UTF-8"));

            body.close();
            server.stop(0);
            server = null;
        }
        catch (UnsupportedEncodingException ex)
        {
            this.infoMessages.add(constructInfoMessage("messageErrorWhileHandlingResponse", true, ex, null));
            server.stop(0);
            server = null;
        }
        catch (IOException ex)
        {
            this.infoMessages.add(constructInfoMessage("messageErrorWhileHandlingResponse", true, ex, null));
            server.stop(0);
            server = null;
        }
    }

    public void kill()
    {
        if (server != null)
        {
            server.stop(0);
            server = null;
        }
    }

    public Locale getLocale()
    {
        return Locale.getDefault();
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
                message = "twitch_permission_obtainer_1 workflow (Server): " + getI10nString(id);
            }
            else
            {
                message = "twitch_permission_obtainer_1 workflow (Server): " + getI10nStringFormatted(id, arguments);
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
        catch (MissingResourceException ex)
        {
            this.infoMessages.add(constructInfoMessage("messageErrorWhileHandlingResponse", true, ex, null));

            // This exception unfortunately hangs the server.
            server.stop(0);
            server = null;
            System.exit(-1);
            return null;
        }
    }

    private String getI10nStringFormatted(String i10nStringName, Object ... arguments)
    {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(this.getLocale());

        formatter.applyPattern(getI10nString(i10nStringName));
        return formatter.format(arguments);
    }


    protected static HttpServer server = null;
    protected int port = 42324;
    protected String endpoint = "/twitch_oauth_authorization";
    protected List<InfoMessage> infoMessages;
    protected Map<String, String> result;

    private static final String L10N_BUNDLE = "l10n.l10nAccessTokenObtainerServerConsole";
    private ResourceBundle l10nConsole;
}

