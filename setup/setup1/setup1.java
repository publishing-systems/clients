/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of setup1.
 *
 * setup1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * setup1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with setup1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/setup1.java
 * @brief Sets up the various tools that get called by the clients.
 * @author Stephan Kreutzer
 * @since 2015-05-01
 */



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



public class setup1
{
    public static void main(String args[])
    {
        System.out.print("setup1  Copyright (C) 2015  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public\n" +
                         "License 3 or any later version for details. Also, see the source code\n" +
                         "repository https://github.com/publishing-systems/automated_digital_publishing/\n" +
                         "or the project website http://www.publishing-systems.org.\n\n");
    
        String programPath = setup1.class.getProtectionDomain().getCodeSource().getLocation().getFile();


        CopyEntitiesXHTML_1_0_Transitional(programPath, ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "xsltransformator" + File.separator + "xsltransformator1" + File.separator + "entities");
    
        return;
    }

    public static int CopyEntitiesXHTML_1_0_Transitional(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true)
        {
            programPath += "/";
        }
        
        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        
        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true)
        {
            to += "/";
        }


        if (setup1.CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.w3c" + File.separator + "xhtml1-transitional.dtd"), 
                                  new File(programPath + to + "xhtml1-transitional.dtd")) != 0)
        {
            System.exit(-1);
        }

        return 0;
    }

    public static int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("setup1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' doesn't exist.");
            return -1;
        }
        
        if (from.isFile() != true)
        {
            System.out.println("setup1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't a file.");
            return -2;
        }
        
        if (from.canRead() != true)
        {
            System.out.println("setup1: Can't copy '" + from.getAbsolutePath() + "' to '" + to.getAbsolutePath() + "' because '" + from.getAbsolutePath() + "' isn't readable.");
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
