package linux.terminal.simulator;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LinuxTerminalSimulator 
{
    public static void main(String[] args) throws IOException 
    {
        Scanner dummy = new Scanner(System.in);
        String var = dummy.nextLine();
        while(!var.equals("exit"))
        {
            if(var.equals("date"))
            {
                date();
            }
            else if(var.equals("pwd"))
            {
                pwd();
            }
            else if(var.equals("ls"))
            {
                LS();
            }
            else if(var.equals("cd") || var.startsWith("cd "))
            {
                CD(var);
            }
            else if(var.equals("clear"))
            {
                clear();
            }
            else if(var.contains("cat "))
            {
                cat(var);
            }
            else if(var.startsWith("rmdir "))
            {
                rmdir(var);
            }
            else if(var.startsWith("mkdir")) 
            {
                mkdir(var);
            }
            else if(var.startsWith("rm"))
            {
                RM(var);
            }
            else if(var.equals("help"))
            {
                help();
            }
            else if(var.startsWith("mv ")) 
            {
                Scanner get = new Scanner(System.in);
                String secondArg = get.next();
                MV(var.substring(3), secondArg);
            }
            else if(var.startsWith("cp "))
            {
              Scanner get = new Scanner(System.in);
                String secondArg = get.next();
                CP(var.substring(3), secondArg);
            }
            else if(var.startsWith("? "))
            {
                specificHelp(var.substring(2));
            }
             else if(var.contains(" > "))
            {
                ReplaceRedirect(var);
            }
              else if(var.contains(" >> "))
            {
                AppendRedirect(var);
            }
              else if(var.startsWith("more "))
            {
                more(var);
            }
            else
            {
                System.out.println("Wrong entry!");
            }
            Scanner loopDummy =new Scanner(System.in);
            var = loopDummy.nextLine();
        } 
    }
    
    public static void date()
    {
        java.util.Date date = new java.util.Date();
        System.out.println(date.toString());
    }
    
    public static void pwd()
    {
        String pwd = System.getProperty("user.dir");
        System.out.println(pwd);
    }
    
    public static void LS()
    {
        File dir = new File(System.getProperty("user.dir"));
        String filesInDir[] = dir.list();
        for(String files: filesInDir)
        {
            System.out.println(files);
        }
    }
    
    public static void CD(String args)
    {
        if(args.length() == 2)
        {
            System.setProperty("user.dir", "C:\\");
        }
        else if(args.substring(3).equals(".."))
        {
          File dir = new File(System.getProperty("user.dir"));
          String parentPath = dir.getParent();
          if(parentPath == null)
              System.out.println("This directory doesnt have a parent directory!");
         else
              System.setProperty("user.dir", parentPath);
        }
        else
        {
            File dir = new File(args.substring(3));
            if(dir.isDirectory()) 
            {
                System.setProperty("user.dir", dir.getAbsolutePath());
            } 
            else 
            {
                System.out.println(args.substring(3) + " is not a directory.");
            }
        }
    }
    
    public static void cat(String args)
    {
            try 
            {
                java.io.FileReader fileReader = new java.io.FileReader(args.substring(4));
                java.io.BufferedReader in = new java.io.BufferedReader(fileReader);
                String line;
                while((line = in.readLine())!= null)
                {
                    System.out.println(line);
                }
            } 
            catch (java.io.FileNotFoundException ex) 
            {
                System.out.println(args.substring(4) + ", file not found.");
            }
            catch (java.io.IOException ex) 
            {
                System.out.println(args.substring(4) + ", input/output error.");
            }
    }
    
    public static void rmdir(String args)
    {
        File file = new File(args.substring(6));
        if(file.isDirectory())
        {
    		if(file.list().length == 0)
                {
    		   file.delete();
    		   System.out.println("Directory is deleted.");
                }
                else if(file.list().length != 0)
                    System.out.println("Directory is full.");
        }
    }
    
    public static void RM(String args)
    {
        if(args.substring(3).contains(" -r "))
        {
            File file = new File(args.substring(6));
            if(file.isDirectory())
            {
                deleteDirectory(file);
                System.out.println("Directory is deleted. ");
            }
            else
            {
                System.out.println("Not a directory. ");
            }
        }
        else
        {
            File file = new File(args.substring(3));
            file.delete();
            System.out.println("File is deleted. ");
        }
        
    }
    
    public static void deleteDirectory(File path) 
    {
            for(File f : path.listFiles())
            {
                if(f.isDirectory()) 
                {
                    deleteDirectory(f);
                    f.delete();
                }
                else
                {
                    f.delete();
                }
            }
            path.delete();
    }
    
    public static void mkdir(String args)
    {
        File newFolder = new File(args.substring(6));
        if(newFolder.isDirectory())
        {
            System.out.println("Directory already exists. ");
        }
        else
        {
            boolean check = newFolder.mkdir();
            if(check)
            {
                System.out.println("Directory is created. ");
            }
            else
            {
                System.out.println("Error creating the directory. ");
            }
        }
    }
    
    public static void help()
    {
        System.out.println("cd: This command changes the current directory to another one. ");
        System.out.println("ls: lists all the files and directories in the user directory. ");
        System.out.println("pwd: displays current user directory. ");
        System.out.println("date: displays the current date and time. ");
        System.out.println("mkdir: creates a directory with each given name");
        System.out.println("rmdir: removes each given empty directory");
        System.out.println("rm *.txt: to delete a specific file | rm –r /home/mydir: to delete the directory and recursively delete its content. ");
        System.out.println("cat: concatenates files and print on the standard output. ");
        System.out.println("mv: If the last argument names an existing directory, mv moves each given file into a file with the same name in that directory. Otherwise, if only two files are given, it moves the first onto the second.");
        System.out.println("cp: If the last argument names an existing directory, cp copies each given file into a file with the same name in that directory. Otherwise, if only two files are given, it copies the first onto the second.");
        System.out.println("more: displays and scrolls down the output in one direction only. You can scroll page by page or line by line. ");
        System.out.println("clear: clears the screen. ");
        System.out.println("exit: stops the terminal. ");
    }
    
    public static void MV(String arg1, String arg2)
    {        
        File file1 = new File(arg1);
        File file2 = new File(arg2);
     
        if(arg1.endsWith(".txt") && arg2.endsWith(".txt"))
        {
            try
            {
                if(!file2.exists())
                {
                    file2.createNewFile();
                }
            }
            catch(java.io.IOException e)
            {
            }
        }
        else
        {
            if (!file2.exists())
            {
                file2.mkdirs();
            }
        }

        if(file1.isDirectory() && file2.isDirectory())
        {
//            if (file1.exists()/* && file1.isDirectory()*/)
//            {
                File[] listOfFiles = file1.listFiles();
                if (listOfFiles != null)
                {
                    for (File child : listOfFiles)
                    {
                        child.renameTo(new File(file2 + "\\" + child.getName()));
                    }
                    file1.delete();
                }
//            }
//            else
//            {
//                System.out.println(file1 + "  Folder does not exists");
//            }
        }
        
        else if(file1.isFile() && file2.isFile())
        {   
            java.io.InputStream inStream;
            java.io.OutputStream outStream;
            try
            {
                inStream = new java.io.FileInputStream(file1);
                outStream = new java.io.FileOutputStream(file2);
                
                byte[] buffer = new byte[1024];
                int length;
                
                while ((length = inStream.read(buffer)) > 0)
                {
                    outStream.write(buffer, 0, length);
                }
                inStream.close();
                outStream.close();
                file1.delete();
                System.out.println("File is moved successful!");
            }
            catch(java.io.IOException e)
            {
            }
        }

        else
        {
            System.out.println("Wrong entry or the source does not exist.");
        }
    } 
    public static void ReplaceRedirect(String arg) throws FileNotFoundException, IOException
    {
        String []Command = arg.split("\\s+");
        FileWriter FileW = new FileWriter(System.getProperty("user.dir"), true);
        BufferedWriter output = new BufferedWriter(FileW);
        if (arg.startsWith("ls"))
        {
            File dir = new File(System.getProperty("user.dir"));
            String filesInDir[] = dir.list();
            for(String files: filesInDir)
            {
                output.write(files);
                output.newLine();
            }
        }
            else if (arg.startsWith("date"))
        {   
            java.util.Date date = new java.util.Date();
            String [] DATE ={(date.toString())};       
            for(String D: DATE)
            {
                output.write(D);
                output.newLine();
            }         
        }
        else  if (arg.startsWith("pwd"))
        {
            String [] pwd = {System.getProperty("user.dir")};
            for ( String P:pwd )
            {
                output.write(P);
                output.newLine();
            }
        
        }    
        else 
        {
              String[] Cmd={Command[0]};
              for (String C:Cmd)
              { 
                   output.write(C);
                   output.newLine();
              } 
        }
        output.close();
    }
        
    public static void AppendRedirect(String arg) throws IOException
    { 
        String []Command = arg.split("\\s+");        
        FileWriter FileW = new FileWriter(System.getProperty("user.dir"), true);
        java.io.PrintWriter output = new java.io.PrintWriter(FileW, true);
        if (arg.startsWith("ls"))
        {
            File dir = new File(System.getProperty("user.dir"));
            String filesInDir[] = dir.list();
            for(String files: filesInDir)
            {
                output.append(files);
                output.println();
            }
        }
        else if (arg.startsWith("date"))
        {
            java.util.Date date = new java.util.Date();
            String [] DATE ={(date.toString())};       
            for(String D: DATE)
            {
                output.append(D);
                output.println();
            }         
        }
        else  if (arg.startsWith("pwd"))
        {
            String [] pwd ={ System.getProperty("user.dir")};
            for ( String P:pwd )
        
            {
                output.append(P);
                output.println();
            }
        
        }    
        else 
        {
              String[] Cmd={Command[0]};
              for (String C:Cmd)
              { 
                   output.append(C);
                   output.println();
              } 
        }
        output.close();
    }
    
    public static void CP(String arg1, String arg2)
    {
        File file1 = new File(arg1);
        File file2 = new File(arg2);
     
        if(arg1.endsWith(".txt") && arg2.endsWith(".txt"))
        {
            try
            {
                if(!file2.exists())
                {
                    file2.createNewFile();
                    int Length;
                    byte []buffer = new byte [1024];
                    InputStream input = new FileInputStream(file1);
                    OutputStream output = new FileOutputStream(file2);
                    while ((Length = input.read(buffer)) > 0)
                    {  
                        output.write(buffer, 0, Length); 
                    }
                    input.close();
                    output.close();
                }
                else 
                {
                    int Length;
                    byte []buffer = new byte [1024];
                    InputStream input = new FileInputStream(file1);
                    OutputStream output = new FileOutputStream(file2);
                    while ((Length = input.read(buffer))>0)
                    {  
                        output.write(buffer, 0, Length); 
                    }
                    input.close();
                    output.close();      
                }
            }
            catch(java.io.IOException e)
            {
                e.printStackTrace();
            }
        }
        else 
        {
            System.out.println("INVALID Input!");
        }   
    }

    
    //@SuppressWarnings("empty-statement")
    public static void more (String arg)  
      {
        //File file1 = new File (arg) ;
        try
        {
            // in = new FileInputStream(arg);
            InputStream in = null;
            in = new FileInputStream(arg.substring(5));
            String Output;
            byte [] Info = new byte [48];
            while ((in.read(Info)) != -1)
            {
                Output = new String (Info);
                System.out.print(Output);
                String NextLine;
                Scanner sc = new Scanner (System.in);
                NextLine = sc.nextLine();
            }
            in.close();
        }
        catch (IOException e) 
        {
            System.out.print("Invalid Input!");
        }
       }
    
   public static void clear()          //clear command (only works in console)
    {
        try
        {
            if(System.getProperty("cs.name").contains("windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }
        catch(java.io.IOException |InterruptedException ex){}
    }

    public static void specificHelp(String arg)
    {
        switch (arg) {
            case "cd":
                System.out.println("cd: This command changes the current directory to another one. ");
                break;
            case "ls":
                System.out.println("ls: lists all the files and directories in the user directory. ");
                break;
            case "pwd":
                System.out.println("pwd: displays current user directory. ");
                break;
            case "date":
                System.out.println("date: displays the current date and time. ");
                break;
            case "mkdir":
                System.out.println("mkdir: creates a directory with each given name");
                break;
            case "rmdir":
                System.out.println("rmdir: removes each given empty directory");
                break;
            case "rm":
                System.out.println("rm *.txt: to delete a specific file | rm –r /home/mydir: to delete the directory and recursively delete its content. ");
                break;
            case "cat":
                System.out.println("cat: concatenates files and print on the standard output. ");
                break;
            case "mv":
                System.out.println("mv: If the last argument names an existing directory, mv moves each given file into a file with the same name in that directory. Otherwise, if only two files are given, it moves the first onto the second.");
                break;
            case "cp":
                System.out.println("cp: If the last argument names an existing directory, cp copies each given file into a file with the same name in that directory. Otherwise, if only two files are given, it copies the first onto the second.");
                break;
            case "more":
                System.out.println("more: displays and scrolls down the output in one direction only. You can scroll page by page or line by line. ");
                break;
            case "clear":
                System.out.println("clear: clears the screen. ");
                break;
            case "exit":
                System.out.println("exit: stops the terminal. ");
                break;
            default:
                System.out.println("Wrong entry!");
                break;
        }
    }
}
