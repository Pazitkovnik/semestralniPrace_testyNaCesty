
package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import utils.FileHandler;

/**
 * implements dynamic array for individual tests
 * @author Jachym
 */
public class TestList
{
    private ArrayList<Test> tests;
    private String userName;
    public String sortOption = "name";
    public boolean voice = true;
    public boolean txtOut = true;
    
    public TestList()
    {
        tests = new ArrayList<Test>();
    }
    
    /**
     * load tests from "tests/index.txt" file into tests arrayList
     * @throws IOException if something happens with input and output
     */
    public void loadTests() throws IOException
    {
        String s = FileHandler.getFileContent("tests/index.txt");
        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; i++)
        {
            tests.add( Test.getTestFromFile("tests/" + lines[i].trim()) );
        }
    }
    
    /**
     * 
     * @param t 
     * @throw IllegalArgumentException if a test with the same name already exists
     */
    public void addTest(Test t)
    {
        if(getTest(t.getName()) != null)
            throw new IllegalArgumentException("test with the same name already exists");
        tests.add(t);
    }
    
    /**
     * returns test from the tests ArrayList
     * @param index of the test in tests arrayList
     * @throw IllegalArgumentExcepton is index is out of range
     * @return test
     */
    public Test getTest(int index)
    {
        if(index >= tests.size() || index < 0)
            throw new IllegalArgumentException("index out of range");
        
        return tests.get(index);
    }
    
    /**
     * get test based on its name
     * @param testName
     * @return test with the testName, if none was found return null
     */
    public Test getTest(String testName)
    {
        for(Test t : tests)
        {
            if(t.getName().equals(testName))
                return t;
        }
        return null;
    }

    public String getUserName()
    {
        return userName;
    }

    /**
     * set the username
     * @param userName
     * @throws IllegalArgumentException if the parameter username is not valid
     */
    public void setUserName(String userName)
    {
        if(userName.matches("[A-ZÁČĎÉĚÍŇÓŘŠŤŮÚÝŽ]{1}[a-záčďéěíňóřšťůúýž]{1,}[ ]"
                + "[A-ZÁČĎÉĚÍŇÓŘŠŤŮÚÝŽ]{1}[a-záčďéěíňóřšťůúýž]{1,}"))
            this.userName = userName;
        else
            throw new IllegalArgumentException("zadane jmeno je spatne!");
    }

    


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        ArrayList <Test> temp = new ArrayList();       
        temp.addAll(tests);
        if(sortOption.matches("questions"))
        {
            System.out.println("adsfsadfsadf");
            Collections.sort(temp, sortByQuestionQuant);
        }
        else if(sortOption.matches("difficulty"))
            Collections.sort(temp, sortByDifficulty);
        else
        {
            Collections.sort(temp, sortByName);
        }
        
        for(Test t : temp)
        {
            sb.append(t.toString());
        }
        return sb.toString();
    }
    
    public Comparator<Test> sortByName = new Comparator<Test>()
    {
        @Override
        public int compare(Test o1, Test o2) {
            String name1 = o1.getName();
            String name2 = o2.getName();
            return name1.compareTo(name2);
        }
    };
    
    public Comparator<Test> sortByDifficulty = new Comparator<Test>()
    {
        @Override
        public int compare(Test o1, Test o2) {
            return o1.getDifficulty() - o2.getDifficulty();
        }
    };
    
    public Comparator<Test> sortByQuestionQuant = new Comparator<Test>()
    {
        @Override
        public int compare(Test o1, Test o2) {
            return o1.getQuestionQuant() - o2.getQuestionQuant();
        }
    };
}
