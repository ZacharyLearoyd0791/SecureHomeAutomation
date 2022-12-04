package ca.future.home.it.secure.home.automation;

import androidx.test.filters.MediumTest;

import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


@MediumTest
public class JUnitTestCases {

    int sum;
    String input;


    String compare;
    String same="Same",notSame="Not Same";
    public void compare(String str1, String str2){
        if (str1.equals(str2)){
            compare=same;
        }
        else{
            compare=notSame;
        }


    }

    public Boolean email() {

        String inputs = "akash@gmail.com";
        System.out.println("Email: " + inputs);

        if (inputs.contains("@")) {

            int count = 0;
            System.out.println("String @ is found");
            String[] newInput = inputs.split("@");

            for (int i = 0; i < newInput.length; i++) {
                System.out.println(count);
                if(count==1){
                    System.out.println("There is 1 @");
                    input=newInput[i];
                    if(input.contains(".")){

                        System.out.println("String . is found");
                        String[] newinput = input.split("@");
                        for (int j = 0; j < newinput.length; j++) {
                            System.out.println(count);
                            if (count==1){
                                System.out.println("There is 1 '.'");
                                return true;
                            }
                            return false;
                        }
                        return false;
                    }
                }
                else if(count>1){
                    System.out.println("More than 1");
                    return false;
                }
                count++;
            }
        }
        return false;
    }

    public int sum(int num1,int num2){
        sum=num1+num2;
        return sum;
    }

    public boolean caseTest(String in){


        if(!in.equals("")){
            return true;
        }
        else{
            return false;
        }

    }

    @Test
    public void testA() {

        compare("Same","Same");
        Assert.assertEquals(compare,same);
        compare("Same","Different");
        Assert.assertEquals(compare,notSame);
        compare("Same","Different");
        Assert.assertNotEquals(compare,same);
    }

    @Test
    public void testB() {

        Assert.assertEquals(sum(7,9),16);
        Assert.assertNotEquals(sum(8,4),4);
    }

    @Test
    public void testC() {
        input="hello";
        Assert.assertTrue(caseTest(input));
        input="";
        Assert.assertFalse(caseTest(input));

    }

    @Test
    public void testD() {

        Assert.assertTrue(email());


    }

    @Test
    public void testE() {


    }
}