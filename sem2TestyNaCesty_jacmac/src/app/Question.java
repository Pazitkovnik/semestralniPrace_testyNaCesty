
package app;

import java.util.ArrayList;
import utils.QuestionInterface;

/**
 * Basic question class defining necessary methods
 * @author Jachym
 */
public class Question implements QuestionInterface
{
    private String question;
    private String type;    //e.g "Zemepis", "Biologie"
    private ArrayList<String> choices;
    private int correctAnswer;  //starting from 1
    private int points;     //starting from 1 meaning the easiest
    
    private int choosenAnswer;

    /**
     * 
     * @param question
     * @param type of this question (e.g "Geography", "Sports",...)
     * @param choices possible answers
     * @param correctAnswer number representing correct Answer starting from 1
     * meaning that correctAnswer 1 means first choice
     * @param points determines the difficulty of question strarting from 1
     * @throws IllegalArgumentException if choices size is 0
     * @throws IllegalArgumentException if correct answer is out of the choice range
     * @throws IllegalArgumentException if points value is smaller than 1
     */
    public Question(String question, String type, ArrayList<String> choices,
            int correctAnswer, int points)
    {
        this.question = question;
        this.type = type;
        
        if(choices.size() == 0)
            throw new IllegalArgumentException("pocet odpovedi je nulova");        
        this.choices = choices;
        
        if(correctAnswer > choices.size() || correctAnswer < 1)
            throw new IllegalArgumentException("spravna odpoved neni v rozsahu odpovedi");
        this.correctAnswer = correctAnswer;
        
        if(points < 1)
            throw new IllegalArgumentException("pocet bodu nesmi byt mensi jak jedna");  
        this.points = points;
    }

    @Override
    public String getQuestion()
    {
        return question;
    }
    
    /**
     * returns type of this question (e.g "Geography", "Sports",...)
     * @return type in string
     */
    public String getType()
    {
        return type;
    }

    @Override
    public ArrayList<String> getChoices()
    {
        return choices;
    }
    
    /**
     * returns number representing correct Answer starting from 1 
     * meaning that correctAnswer 1 means first choice
     * @return number representing correct Answer starting from 1 
     */
    @Override
    public int getCorrectAnswer()
    {
        return correctAnswer;
    }

    /**
     * returns aproximate difficulty defined by the author of this question
     * difficulty rises with the number meaning that 1 is the easiest
     * @return aproximate difficulty in points
     */
    @Override
    public int getPoints()
    {
        return points;
    }

    @Override
    public int getChoosenAnswer()
    {
        return choosenAnswer;
    }
    
    /**
     * returns 1 if answer is correct, 0 otherwise
     * @param choosenAnswer starting from 1
     * @return 1 if answer is correct, 0 otherwise
     * @throws IllegalArgumentException if answer is out of range
     */
    @Override
    public boolean setChoosenAnswer(int choosenAnswer)
    {
        if(choosenAnswer > choices.size())
            throw new IllegalArgumentException("zadana odpoved neni v rozsahu");
        return ( (this.choosenAnswer = choosenAnswer) == correctAnswer )? true : false;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%n", getQuestion()));
        for(int i = 0; i < getChoices().size(); i++)
        {
            sb.append( String.format("%4d. %s%n", i+1, getChoices().get(i)) );
        }
        return sb.toString();
    }
    
    
    
    
    
    
}
