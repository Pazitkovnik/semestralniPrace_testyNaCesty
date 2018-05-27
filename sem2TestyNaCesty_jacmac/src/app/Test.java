
package app;

import com.itextpdf.text.DocumentException;
import com.sun.media.sound.InvalidDataException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import utils.FileHandler;

/**
 * Basic test class used for manipulating questions
 * @author Jachym
 */
public class Test implements Comparable<Test>
{
    private String name;
    private String type;
    private ArrayList<Question> questions;
    private ArrayList<Question> questionsBuffer;    //buffer of random questions
    private int questionQuant;
    private int difficulty;
    
    private boolean isRunning = false;
    private boolean isFinished = false;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int currentQuestionNum = 0; //how many questions have been already answered
    private int currentPoints = 0;

    /**
     * 
     * @param name of the test
     * @param type of the test
     * @param questions
     * @param questionsQuant how many questions are going to be used in one run
     * -1 for all of them
     * @param difficulty determined by the author starting from 1
     * negative number or 0 for undefined
     * @throws IllegalArgumentException if questionQuant is smaller than -1,
     * is 0 or is bigger than questions size 
     */
    public Test(String name, String type, ArrayList<Question> questions, int questionsQuant, int difficulty)
    {
        //Question q = new Question("asdf", "dsfasd", new ArrayList<String>(), 1);
        this.name = name;
        this.type = type;
        this.questions = questions;
        
        if(questionsQuant < -1 || questionsQuant == 0 || questionsQuant >= questions.size())
            throw new IllegalArgumentException("spatne zadane mnozstvi zpracovavanych otazek");
        this.questionQuant = questionsQuant;
        
        this.difficulty = (difficulty < 1)? -1 : difficulty;
    }
    
    /**
     * sets the question buffer with random questions from questions list
     */
    private void setQuestionsBuffer()
    {
        //copy the content of questions list
        ArrayList<Question> temp = new ArrayList(questions);
        ArrayList<Question> out = new ArrayList();
        
        //set the amount of questions
        int max = this.questionQuant;
        if(this.questionQuant == -1)
           this.questionQuant = max = this.questions.size();
        
        for(int i = 0; i < max; i++)
        {
            //generate random number based by the temp list size
            int index = (int) (Math.random() * (temp.size()));
            out.add(temp.get(index));
            temp.remove(index);
        }
        questionsBuffer = out;
    }
    
    public void runTest()
    {
        setQuestionsBuffer();
        //this.currentPoints = 0;
        //this.currentQuestionNum = 0;
        isRunning = true;
        startTime = LocalDateTime.now();
    }
    
    /**
     * return question from the question buffer
     * @return current question if the test is running, return null if the test is finished
     * @throws UnsupportedOperationException if the test is not running
     */
    public Question getCurrentQuestion()
    {
        if(isFinished)
            return null;
        if(!isRunning)
            throw new UnsupportedOperationException("nelze dostat otazku,"
                    + " protoze test zatim jeste neni spusteny");       
        return questionsBuffer.get(currentQuestionNum);
    }
    
    /**
     * answers current question and increment currentQuestionNum,
     * if the answer is correct return true, otherwise return false, 
     * @param i answer starting from 1
     * @return true if the answer is correct, fase if it's incorrect
     * @throws IllegalArgumentException is answer is out of range
     */
    public boolean answerCurrentQuestion(int i)
    {
        boolean out = false;
        if(getCurrentQuestion().setChoosenAnswer(i) == true)
        {
            out = true;
            this.currentPoints += getCurrentQuestion().getPoints();
        }
        currentQuestionNum++;
        if(currentQuestionNum >= questionQuant)
        {
            isRunning = false;
            isFinished = true;
            endTime = LocalDateTime.now();
        }
        return out;
    }
    
    /**
     * how much time did it take to complete this test
     * @return time if test is finished, null otherwise
     */
    public Duration getTestTime()
    {
        if(isFinished)
            return Duration.between(startTime, endTime);
        else
            return null;
   }
    
    public static String formatTestTime(Duration d)
    {
        return String.format("%02d:%02d:%02d", d.getSeconds() / 3600,
                (d.getSeconds() % 3600) / 60, (d.getSeconds() % 60));
    }
//    public static String formatQuestion(Question q)
//    {
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("%s%n", q.getQuestion()));
//        for(int i = 0; i < q.getChoices().size(); i++)
//        {
//            sb.append( String.format("%4d. %s%n", i+1, q.getChoices().get(i)) );
//        }
//        return sb.toString();
//    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    /**
     * return difficulty determined by the author of this test
     * difficulty rises with the number meaning that 1 is the easiest
     * @return rounded difficulty number, -1 if not defined
     */
    public int getDifficulty()
    {
        return difficulty;
    }
    
    public int getCurrentPoints()
    {
        return currentPoints;
    }
    
    public String getResults()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("HODNOCENI TESTU " + name + "\n");
        sb.append("------------------------------\n");
        sb.append("pocet ziskanych bodu/max. bodu: " + getCurrentPoints() + "/" + getCurrentMaxPoints() + "\n");
        sb.append("datum napsani testu:            " + LocalDate.now() + "\n");
        sb.append("celkova doba psani testu:       " + formatTestTime(getTestTime()) + "\n");
        sb.append("slovni hodnoceni:               " + getCheer(getCurrentPoints(), getCurrentMaxPoints()) );
        
        return sb.toString();
    }
    
    public static String getCheer(int points, int max)
    {
        double div = (double)points / max;
        if(div <= 0.2)
            return "Nedostatečně";
        else if(div <= 0.4)
            return "Dostatečně";
        else if(div <= 0.6)
            return "Dobře";
        else if(div <= 0.8)
            return "Chvalitebně";
        else
            return "Výborně";
    }

    /**
     * how many questions are going to be used in one run
     * @return number of question quantity
     */
    public int getQuestionQuant()
    {
        return (questionQuant == -1)?questions.size():questionQuant;
    }
    
    /**
     * returns already answered questions quantity
     * @return already answered questions quantity
     */
    public int getCurrentQuestionNum()
    {
        return this.currentQuestionNum;
    }
    
    /**
     * returns max points of all the questions
     * @return max points of all the questions
     */
    public int getMaxPoints()
    {
        int max = 0;
        for(Question q : questions)
        {
            max += q.getPoints();
        }
        return max;
    }
    
    /**
     * get current max points from the already randomly picked questions
     * @return current max points of this test
     */
    public int getCurrentMaxPoints()
    {
        int max = 0;
        for(Question q : questionsBuffer)
        {
            max += q.getPoints();
        }
        return max;
    }
    
    public Question getQuestion(int index)
    {
        return questions.get(index);
    }
    
    public void addQuestion(Question q)
    {
        questions.add(q);
    }
    
    /**
     * deletes question based on its index in question list
     * @param i index of the question
     * @throws IllegalArgumentException if index is out of range
     */
    public void removeQuestion(int i)
    {
        if(i < 0 || i >= questions.size())
            throw new IllegalArgumentException("index out of range");
        questions.remove(i);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
            sb.append( String.format("%-17s%-25s\n", "název:", getName()) );
            sb.append( String.format("  %-15s%s\n", "téma:", getType()) );
            sb.append( String.format("  %-15s%d\n", "obtížnost:", getDifficulty()) );
            sb.append( String.format("  %-15s%d\n", "otázky:", getQuestionQuant()) );
            sb.append("\n");
        return sb.toString();
    }
    
    
    
    /**
     * Factory metthod which returns a test based on informations of a file
     * @param fileName
     * @return
     * @throws IOException 
     */
    public static Test getTestFromFile(String fileName) throws IOException
    {
        String s = FileHandler.getFileContent(fileName);
        if(s.length() == 0)
            throw new InvalidDataException("soubor " + fileName + " neobsahuje zadne data.");
        
        //TEST data
        String testName = getAttributeData("testName", s);
        String testType = getAttributeData("testType", s);
        int testQuestionQuant = Integer.parseInt(getAttributeData("testQuestionQuant", s));
        int testDifficulty = Integer.parseInt(getAttributeData("testDifficulty", s));
        
        //Questions data
        ArrayList<Question> questions = getQuestionsData(s);
        
        return new Test(testName, testType, questions, testQuestionQuant, testDifficulty);
        
    }
    
    private static String getAttributeData(String att, String source)
    {
        int beg = source.lastIndexOf(att + "=") + att.length() + 1;
        int end = source.indexOf('\n', beg);
        return source.substring(beg, end).trim();
        
    }
    
    /*private static String getAttributeData(String att, String source)
    {
        int beg2 = source.indexOf(att + "=", beg) + att.length() + 1;
        int end2 = source.indexOf('\n', beg2);
        if(beg2 > end || end2 > end)
            throw new IndexOutOfBoundsException("hledany atribut neni v danem rozsahu");
        return source.substring(beg2, end2).trim();
        
    }*/
    
    private static ArrayList<Question> getQuestionsData(String source)
    {
        ArrayList<Question> questions = new ArrayList();
        int beg = 0;
        while( (beg = source.indexOf('{', beg)) != -1)
        {
            int end = source.indexOf('}', beg);
            
            //get attributes info
            String subSource = source.substring(beg, end);
            String question = getAttributeData("question", subSource);
            String type = getAttributeData("type", subSource);
            int correctAnswer = Integer.parseInt( getAttributeData("correctAnswer", subSource) );
            int points = Integer.parseInt( getAttributeData("points", subSource) );
            
            ArrayList<String> choices = getChoicesData(source.substring(beg, end));
            questions.add(new Question(question, type, choices, correctAnswer, points));
            
            beg = end;
        }
        return questions;
    }
    
    private static ArrayList<String> getChoicesData(String source)
    {
        ArrayList<String> choices = new ArrayList();
        int beg = source.indexOf('[');
        int end = source.indexOf(']', beg);
        
        String subString[] = source.substring(beg, end).split("\n");
        for (int i = 1; i < subString.length; i++)
        {
            choices.add(subString[i]);
        }
        return choices;
    }
    
    /**
     * save test into a newly made file named by the test name, if a file with
     * the same name already exists add _kopie to the end (e.g test1_kopie.txt)
     */
    public void saveTestToFile() throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("testName=" + this.name + "\n");
        sb.append("testType=" + this.type + "\n");
        sb.append("testQuestionQuant=" + this.questionQuant + "\n");
        sb.append("testDifficulty=" + this.difficulty + "\n");
        for(Question q : questions)
        {
            sb.append("{\n");
            sb.append("question=" + q.getQuestion() + "\n");
            sb.append("type=" + q.getType() + "\n");
            sb.append("[" + "\n");
            for(String s : q.getChoices())
            {
                sb.append(s + "\n");
            }
            sb.append("]" + "\n");
            sb.append("correctAnswer=" + q.getCorrectAnswer() + "\n");
            sb.append("points=" + q.getPoints() + "\n");
            sb.append("}" + "\n");
        }
        String fName = FileHandler.makeFileAndPrint(sb.toString(), "tests/" + name + ".txt");
        FileHandler.printIntoFile(fName, "tests/index.txt");
    }
    
    /**
     * saves results to a pdf named: testName + "_vysledky" + "_" + date + "_" + time + ".pdf
     * into the results folder
     * @throws FileNotFoundException
     * @throws DocumentException 
     */
    public void saveResultsToPDF() throws FileNotFoundException, DocumentException
    {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        //need to format because there is no way a file can have ':' chars
        String fTime = String.format( "%02d-%02d-%02d", time.getHour(),
                time.getMinute(), time.getSecond() );
        
        StringBuilder sbQuestions = new StringBuilder("\n");
        for(Question q : questionsBuffer)
        {
            sbQuestions.append("\n" + q + "\n");
            sbQuestions.append(" " + ( ( q.getChoosenAnswer() == q.getCorrectAnswer() )?"SPRAVNE":"SPATNE" ) + "\n");
            sbQuestions.append(" zadana odpoved:  " + q.getChoosenAnswer() + "\n");
            sbQuestions.append(" spravna odpoved: " + q.getCorrectAnswer() + "\n");
            
        }
        
        FileHandler.makePDFAndPrint(getResults() + sbQuestions.toString(), "results/" + name +
                "_vysledky_" + date.toString() + "_" + fTime + ".pdf");
    }
    
    public void saveResultsToCsv()
    {
        FileHandler.makeResultsAndPrintBin(this);
    }
    

    @Override
    public int compareTo(Test o)
    {
        return this.name.compareTo(o.getName());
    }
    
    
    
    
}
