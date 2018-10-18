package lt.vu.mif.jate.task01;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class testas {
	
	public static void main(String[] args){


List<String> matchList = new ArrayList<String>();
Pattern regex = Pattern.compile("\\{(.*?)\\}");
Matcher regexMatcher = regex.matcher("Hello This is {Java} Not (.NET)");

while (regexMatcher.find()) {//Finds Matching Pattern in String
   matchList.add(regexMatcher.group(1));//Fetching Group from String
}

for(String str:matchList) {
   System.out.println(str);
}
	}

}

