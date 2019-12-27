import java.io.*;
import java.util.*;

public class QueryIndex {

	public static String commonString(String first, String second){
	     String result=new String();
	    //split the second string into words
	    List<String> wordsOfSecond = Arrays.asList(second.split(" "));

	    //split and compare each word of the first string           
	    for (String word : first.split(" ")) {
		if(wordsOfSecond.contains(word))
		    result=result+" "+word;
	    }
	    return result;
	}
	

	public static void main(String[] args) throws IOException 
	{
		long startTime = System.currentTimeMillis();
		//args[0]: input query file
		//args[1]: inverted index
		//args[2]: outputfile.

		FileReader fpResult = new FileReader(args[1]);
		BufferedReader fpBufferedResult = new BufferedReader(fpResult);



	
		/*FileSystem fs = FileSystem.get(new Configuration());
		Path path = new Path("/user/hduser/indexoutput/part-00000");
		BufferedReader fpBufferedResult = new BufferedReader(new InputStreamReader(fs.open(path)));*/	
				
		String lineResult;
		String[] splitLine;
		Hashtable<String, String> hashDoc = new Hashtable<String, String>();



		//Create a HashTable with word as key and the rest as value
		while ((lineResult = fpBufferedResult.readLine()) != null) {
			splitLine = lineResult.split("\\s+:\\s*");
			hashDoc.put(splitLine[0], splitLine[1]);
		}

		
		PrintWriter fpOut = new PrintWriter(args[2]);

		
		FileReader fpQuery = new FileReader(args[0]);
		BufferedReader fpBufferedQuery = new BufferedReader(fpQuery);
		String lineQuery;
		int ctr=0;
		while ((lineQuery = fpBufferedQuery.readLine()) != null) {
			lineQuery=lineQuery.replaceAll("[^a-zA-Z ]","").trim();
			
			if(lineQuery.isEmpty())continue;
			ctr++;
			if(ctr%10==0){
				for(int i=0 ; i<ctr/10 ; i++){System.out.print("=");}
				System.out.print(ctr/10+ "%\r");
			}
			
			
			String common=new String();
			StringTokenizer st=new StringTokenizer(lineQuery);
			String str=st.nextToken();
			String value=hashDoc.get(str);	
			//System.out.println(str);//+" "+value);
			common=value;
				
				//boolean flag=true; 
				while(st.hasMoreTokens()){
					str=st.nextToken();
					//System.out.println(str);				
					String newvalue=hashDoc.get(str);
					//newvalue.replaceAll("[^a-zA-Z]","").trim();
					common=commonString(newvalue,common);
					
					
				}	
				
				fpOut.println(lineQuery+"\t====>"+common+"\n\n");
				
			
			
		}
		System.out.println();
		long endTime   = System.currentTimeMillis();
		long duration	 = endTime - startTime;
		System.out.println("Execution time for "+ctr+" queries "+duration/1000.0+" secs"); 
		fpOut.close();	
	}
}
