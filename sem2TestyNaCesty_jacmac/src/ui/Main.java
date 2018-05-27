/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import app.Test;
import app.Question;
import app.TestList;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import utils.FileHandler;
import utils.TextToSpeech;

/**
 * Class Main implements basic UI for the TEST app
 * @author Jachym
 */
public class Main
{
    public static Scanner sc = new Scanner(System.in, "Cp1250");
    
    public static TestList testList = new TestList();
    
    
    //public static TextToSpeech tts = new TextToSpeech();
    
    public static void main(String[] args) throws IOException, FileNotFoundException, DocumentException
    {
        TextToSpeech.speak("TESTY NA CESTY, nejlepsi testovac na svete celem");
        testList.loadTests();

        System.out.print("Zadejte své jméno: ");
        boolean exit = false;
        String name = "";
        while(!exit)
        {
            try
            {
                name = sc.nextLine();
                testList.setUserName(name);
                exit = true;
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
        }
        TextToSpeech.speak("Welcome " + name);
        
        menu();
    }
    
    /**
     * provides basic UI menu with all the needed functions
     */
    public static void menu()
    {
        System.out.println("\nTESTY NA CESTY");
        System.out.println("=================");
        System.out.println("vybrat test               1");
        System.out.println("vytvořit test             2");
        System.out.println("nastavení                 3");
        System.out.println("využít tts                4");
        System.out.println("o programu                5");
        System.out.println("konec programu            0");
        System.out.println("---------------------------");
        System.out.print  ("vaše volba:               ");
        
        char charChoice = sc.next().charAt(0);
        //check if user typed number and parse it
        int choice = Character.isDigit(charChoice)
                ?Integer.parseInt(String.valueOf(charChoice)):-1;
        //clear the buffer
        sc.nextLine();
        try
        {
            switch(choice)
            {
                case 1:
                    chooseTest();
                    break;
                case 2:
                    makeTest();
                    break;
                case 3:
                    settings();
                    break;
                case 4:
                    tts();
                    break;
                case 5:
                    about();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Neplatny vstup!");
                    break;
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        menu();
    }
    
    /**
     * prints basic info about this program
     */
    public static void about()
    {
        System.out.println("");
        System.out.println("Testy na Cesty je testovací program, díky kterému si "
                + "procvičíte svoje mozečkové buňky jako nikdy předtím.");
        System.out.println("Jeho úžasné uživatelsky přívětivé prostředí Vás vtrhne "
                + "do světa hlavolamů a hádanek tak silně, že bude velice těžké "
                + "se od něho odtrhnout!");
        System.out.println("Návod:");
        System.out.println("   - pomocí funkce \"vybrat test\" si vyberete test, ktery budete chtit zpracovat.");
        System.out.println("     po jeho napsani se do slozky results vypise formatovany vystup vysledku ve forme pdf");
        System.out.println("     pro znovu napsani testu vypnete a zapnete tento program");
        System.out.println("   - pomocí funkce \"vytvori test\" vytvorite test,"
                + " ktery se na konci zapise do souboru tests a zaroven pripise do souboru tests/index.txt");
        System.out.println("   - pomocí funkce \"nastaveni\" nastavite ruzne parametry programu");
        System.out.println("   - pomocí funkce \"vyuzit tts\" si muzete vyzkouset TextToSpeech pomocnika");
        System.out.println("");
    }
    
    public static void tts()
    {
        System.out.println();
        System.out.println("  TTS :-)");
        System.out.println("  ---------");
        System.out.println("  zadejte větu (exit pro ukončení):   ");
        String line = sc.nextLine();
        if(line.matches("exit"))
            return;
        TextToSpeech.speak(line);
        tts();
    }
    
    /**
     * provide basic interface to change settings of this program
     */
    public static void settings()
    {
        System.out.println();
        System.out.println("  NASTAVENI");
        System.out.println("  ---------");
        System.out.println("  zmenit trideni testu:   1");
        System.out.println("  vypnout/zapnout hlas:   2");
        System.out.println("  txt/csv vysledky testu: 3");
        System.out.println("  zpet do menu:           0");
        System.out.println("  -------------------------");
        System.out.print  ("  vase volba:             ");
        
        char charChoice = sc.next().charAt(0);
        //check if user typed number and parse it
        int choice = Character.isDigit(charChoice)
                ?Integer.parseInt(String.valueOf(charChoice)):-1;
        
        switch(choice)
        {
            case 1:
                System.out.println("    podle jmena:        1");
                System.out.println("    podle obtiznosti:   2");
                System.out.println("    podle poctu otazek: 3");
                System.out.print  ("    vase volba:         ");
                
                char charChoice2 = sc.next().charAt(0);
                if(!Character.isDigit(charChoice2))
                {
                    System.out.println("spatna volba");
                    break;
                }
                //check if user typed number and parse it
                int choice2 = Character.isDigit(charChoice2)
                        ?Integer.parseInt(String.valueOf(charChoice2)):-1;
                
                if(choice2 > 3 || choice2 < 1)
                {
                    System.out.println("spatna volba");
                    break;
                }
                else if(choice2 == 1)
                    testList.sortOption = "name";
                else if(choice2 == 2)
                    testList.sortOption = "difficulty";
                else if(choice2 == 3)
                    testList.sortOption = "questions";
                
                break;
            case 2:
                testList.voice = !testList.voice;
                break;
            case 3:
                testList.txtOut = !testList.txtOut;
                break;
            case 0:
                return;
            default:
                System.out.println("spatna volba");
                break;
        }
        
    }
    
    /**
     * provide basic interface to choose a test
     * @throws FileNotFoundException
     * @throws DocumentException 
     */
    public static void chooseTest() throws FileNotFoundException, DocumentException
    {
        System.out.println(testList);
        System.out.print("zadejte název testu, který chcete spustit: ");
        Test t;
        t = testList.getTest(sc.nextLine());
        while(t == null)
        {
            System.out.println("Zadany nazev neexistuje. Zkuste to znovu.");
            t = testList.getTest(sc.nextLine());
        }
        runTest(t);
    }
    
    /**
     * runs the test and provides basic interface to answer its questions
     * @param t
     * @throws FileNotFoundException
     * @throws DocumentException 
     */
    public static void runTest(Test t) throws FileNotFoundException, DocumentException
    {
        t.runTest();
        Question q;
        while( (q = t.getCurrentQuestion()) != null)
        {
            if(testList.voice)
                TextToSpeech.speak(q.getQuestion());
            System.out.println(q);
            System.out.print("Zvolte odpoved: ");
            int choice;
            boolean choiceEnd = false;
            while(!choiceEnd)
            {
                try
                {
                    choice = Integer.parseInt( String.valueOf(sc.next().charAt(0)) );
                    t.answerCurrentQuestion(choice);
                    
                    choiceEnd = true;
                }
                catch(IllegalArgumentException e)
                {
                    System.out.println("spatna volba");
                }
            }
        }
        System.out.println(t.getResults());
        if(testList.voice)
            TextToSpeech.speak( Test.getCheer(t.getCurrentPoints(), t.getCurrentMaxPoints()) );
        if(testList.txtOut)
            t.saveResultsToPDF();
        else
            t.saveResultsToCsv();
    }
    
    /**
     * provides basic interface to make a new test
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public static void makeTest() throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        sc.nextLine();
        System.out.print("Zadejte název testu:     ");
        String testName = sc.nextLine();
        System.out.print("Zadejte typ testu:       ");
        String testType = sc.nextLine();        
        
        boolean exit = false;
        ArrayList<Question> questions = new ArrayList();
        while(!exit)
        {
            System.out.print("Chcete zadat novou otazku(ano = a, ne = cokoliv jineho): ");
            if(!sc.nextLine().matches("a"))
                exit = true;
            else
            {
                System.out.print("Zadejte otazku: ");
                String question = sc.nextLine();
                
                System.out.print("Zadejte typ otazky: ");
                String type = sc.nextLine();
                
                System.out.print("Zadejte pocet moznych odpovedi: ");
                int choiceQuant = sc.nextInt();
                sc.nextLine();
                
                ArrayList<String> choices = new ArrayList();
                
                for (int i = 0; i < choiceQuant; i++)
                {
                    System.out.println("Zadejte " + (i + 1) + ". moznou odpoved: ");
                    choices.add(sc.nextLine());
                }
                
                System.out.print("Zadejte spravnou odpoved: ");
                int correctAnswer = sc.nextInt();
                
                System.out.print("Zadejte pocet bodu teto otazky: ");
                int points = sc.nextInt();
                sc.nextLine();
                
                questions.add(new Question(question, type, choices, correctAnswer, points));
            }
        }
        System.out.print("Zadejte pocet max. otazek(-1 pro vsechny): ");
        int questionQuant = sc.nextInt();
        
        System.out.print("Zadejte obtiznost testu(1 = nejsnazsi): ");
        int testDifficulty = sc.nextInt();
        
        Test t = new Test(testName, testType, questions, questionQuant, testDifficulty);
        t.saveTestToFile();
        testList.addTest(t);
        
    }
}
