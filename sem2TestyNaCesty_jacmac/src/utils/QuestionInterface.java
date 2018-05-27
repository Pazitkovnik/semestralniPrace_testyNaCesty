/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;

/**
 *
 * @author Jachym
 */
public interface QuestionInterface
{
    public int getCorrectAnswer();
    public ArrayList<String> getChoices();
    public String getQuestion();
    public int getPoints();
    public int getChoosenAnswer();
    public boolean setChoosenAnswer(int choosenAnswer);
    
}
