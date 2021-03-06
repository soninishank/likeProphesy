/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package likeprophesy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import static likeprophesy.FBHashGet.totalPosts;

/**
 *
 * @author VarunJain, Ayush Agarwal
 */
public class LikeProphesy {
    
    static HashMap<String,Double> result1 = new HashMap<String,Double>();
    static HashMap<String,Double> result2 = new HashMap<String,Double>();
    static ArrayList<Integer> countlikesFrom1 = new ArrayList<Integer>();
    static int countlikesFrom2 = 0;
    static int countlikesFrom3 = 0;
    static int fianlCount = 0;
    static String postToPredict = "";
    
    public static int prophesize(String newPost, String accessToken, Double sliderVal, int photoOrNot)
    {
        result1.clear();
        result2.clear();
        fianlCount = 0;
        
        HashMap<String,ArrayList<String>> hash = FBHashGet.getCleanHash(accessToken); 
        
        if (photoOrNot == 0)
        {
            countlikesFrom1 = algoMachOneCaller(newPost, hash, sliderVal);
            countlikesFrom2 = algoMachTwo(FBHashGet.userCountList, sliderVal);
            countlikesFrom3 = algoMachThree(0);
        }
        else
        {
            countlikesFrom3 = algoMachThree((int) (totalPosts * Math.abs(100.0 - sliderVal)/100));
        }
                
        System.out.println(result1);
        System.out.println(result2);

        return fianlCount;
    }
    
    public static ArrayList<Integer> algoMachOneCaller(String post, HashMap<String,ArrayList<String>> hash, Double sliderVal)
    {
        ArrayList<Integer> countArr = new ArrayList<Integer>();
        for (String postEach : post.split("\n"))
        {
            if (postEach.length() > 0)
            {
                result1.clear();
                countArr.add(algoMachOne(postEach, hash, sliderVal));
            }
        }
        return countArr;
    }
    
public static int algoMachOne(String post, HashMap<String,ArrayList<String>> hash, Double sliderVal)
{
    int countt = 0, matches = 0;
    for (String name : hash.keySet())
    {
        ArrayList<String> likedWords = hash.get(name);
        ArrayList<String> testWords = FBHashGet.getPost(post, "", "");
        matches = 0;
        for (String x : testWords)
        {
            if (likedWords.contains(x))
            {
                matches++;
            }
        }       
        Double prob = (matches*1.0)/testWords.size();
        if (prob > Math.abs(100.0 - sliderVal)/100)
        {
            result1.put(name, prob);
        }
    }   
    for (String name : result1.keySet())
    {
        if (result1.get(name) >= Math.abs(100.0 - sliderVal)/100)
        {
            countt++;
        }
    }
    return countt;
}
    
    public static int algoMachTwo(HashMap<String,Integer> hash, Double sliderVal)
    {
        int count = 0;
        for (String name : hash.keySet())
        {    
            result2.put(name, (hash.get(name)*1.0)/FBHashGet.totalPosts);
            if (result2.get(name) >= Math.abs(100.0 - sliderVal)/100)
            {
                count++;
            }
        }   
        return count;
    }
    
    public static int algoMachThree(int limit)
    {
        Double temp = 0.0;
        if (limit == 0)
        {
            for (int i = 0; i < FBHashGet.numberLikes.size(); i++)
            {
                temp += FBHashGet.numberLikes.get(i) * FBHashGet.numberLikes.get(i);
            }
            temp /= FBHashGet.numberLikes.size();
        }
        else
        {
            Collections.sort(FBHashGet.numberLikes, Collections.reverseOrder());
            System.out.println(FBHashGet.numberLikes);
            for (int i = 0; i < limit; i++)
            {
                temp += FBHashGet.numberLikes.get(i) * FBHashGet.numberLikes.get(i);
            }
            temp /= limit;
        }

        temp = Math.sqrt(temp);
        return temp.intValue();
    }
}
