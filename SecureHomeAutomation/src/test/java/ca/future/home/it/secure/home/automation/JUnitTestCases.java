package ca.future.home.it.secure.home.automation;

import androidx.test.filters.MediumTest;

import org.junit.Assert;
import org.junit.Test;


@MediumTest
public class JUnitTestCases {
    int sum;
    String compare;
    public void compare(String str1, String str2){
        if (str1.equals(str2)){
            compare="Same";
        }
        else{
            compare="Not Same";
        }


    }
    public int sum(int num1,int num2){
        sum=num1+num2;
        return sum;
    }
    @Test
    public void testA() {

        compare("Same","Same");
        Assert.assertEquals(compare,"Same");
        compare("Same","Different");
        Assert.assertEquals(compare,"Not Same");
    }

    @Test
    public void testB() {

        Assert.assertEquals(sum(7,9),16);
        Assert.assertNotEquals(sum(8,4),4);
    }

}