package ca.future.home.it.secure.home.automation;

import androidx.test.filters.MediumTest;

import org.junit.Assert;
import org.junit.Test;

import java.util.Scanner;


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

        Assert.assertEquals(sum(7,9),16);
        Assert.assertNotEquals(sum(8,4),4);
    }

    @Test
    public void testE() {

        Assert.assertEquals(sum(7,9),16);
        Assert.assertNotEquals(sum(8,4),4);
    }
}