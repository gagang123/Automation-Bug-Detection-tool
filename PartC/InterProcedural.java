import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.math.RoundingMode;
import java.util.*;
public class InterProcedural {
 
	public static void main(String[] args)
	{
		//Declare the hash maps and other variables
		HashMap<String, Integer> hashMapNameID = new HashMap<String, Integer>(); //Assigning ID for each function
		HashMap<Integer,HashSet<Integer>> hashMapIDCheck = new HashMap<Integer,HashSet<Integer>>();//Calculate function dependencies from call graph
		HashMap<Integer, String> hashMapIDName = new HashMap<Integer, String>();//Swapping key value pair to use key count for confidence measure
		HashMap<Integer, Integer> hashMapSupport = new HashMap<Integer, Integer>();//Calculate function count for finding support
		HashMap<HashSet<Integer>, Integer> hashMapPairCount = new HashMap<HashSet<Integer>, Integer>();//Calculate pair count
		String key1=null;
		String key2=null;
		int count=0;
		int hashMapVal=0;
		int confidenceThresh; 
		int supportThresh;
		int flag;
		String chck1,chck2,tmp;
		
		//Assign default support threshold as 3 and confidence threshold as 65 
		if(args.length == 1)
		{
			supportThresh = 3;
			confidenceThresh = 65;
		}
		else //Use threshold passed as function parameter
		{
			 supportThresh = Integer.parseInt(args[1]);
			 confidenceThresh = Integer.parseInt(args[2]);

		}
		
		try 
		{
			BufferedReader read = new BufferedReader(new FileReader(args[0]));
			String line = read.readLine();
			//Check if line is not null
				while (line!=null)
					{//Traverse through the file and get all function names
						if(line.contains("node") && line.contains("function:")) 
							{
								int left=line.indexOf("'");
								int right=line.lastIndexOf("'");
								key1=line.substring(left+1, right);
									if(!(hashMapNameID.containsKey(key1)))
										{
											hashMapNameID.put(key1, hashMapVal++);
										}
									line =read.readLine();
									while((!(line.isEmpty())))
										{
											if(line.contains("function")) 
												{
													left=line.indexOf("'");
													right=line.lastIndexOf("'");
													key2=line.substring(left+1, right);
													if(!(hashMapNameID.containsKey(key2)))
														{
															hashMapNameID.put(key2, hashMapVal++);
														}
													HashSet<Integer> Set=new HashSet<Integer>();
													//Assigning id for each name
													if((hashMapIDCheck.get(hashMapNameID.get(key1)))==null)
														{
															Set.add(hashMapNameID.get(key2));
															hashMapIDCheck.put(hashMapNameID.get(key1), Set);	  	    						
														}
													else 
														{
															Set  =hashMapIDCheck.get(hashMapNameID.get(key1));
															Set.add(hashMapNameID.get(key2));
															hashMapIDCheck.put(hashMapNameID.get(key1), Set);	    						
														}
	    						
												}
											if((line =read.readLine())==null) 
												{
													break;
												}
										}
								}
						line=read.readLine();
					}
			read.close();
		}
	
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
	
		//Reversing the key value from hashMapNameID to get (integer, string) key value relation
		ArrayList<String> listNameID =  new ArrayList<String>(hashMapNameID.keySet());
		for(String key : listNameID)
			{
				hashMapIDName.put(hashMapNameID.get(key), key);
			}
		
		for(Map.Entry<Integer, String>	entry: hashMapIDName.entrySet() )
			{
				if(!hashMapIDCheck.containsKey(entry.getKey()))
					{
						hashMapIDCheck.put(entry.getKey(),null);
					}
			}
		

		hashMapNameID.clear();
		HashMap<Integer,HashSet<Integer>> hashMaptest = new HashMap<Integer,HashSet<Integer>>();
		
		//Expand functions based on inter procedural expansion
		hashMaptest=InterProcedural(hashMaptest, hashMapIDCheck);
		
		
		//Calculating function count for each key
		ArrayList<Integer> listIDName =  new ArrayList<Integer>(hashMapIDName.keySet());
		
		for(Integer key : listIDName)
			{
				HashSet<Integer> a=hashMaptest.get(key);
					if(a!=null)
						{
							ArrayList<Integer> arrayList3 =  new ArrayList<Integer>(hashMaptest.get(key));
							for(Integer myKey : arrayList3)
								{
									if(!hashMapSupport.containsKey(myKey))
										{
											hashMapSupport.put(myKey, new Integer(1));
										}
									else
										{
											Integer support = hashMapSupport.get(myKey);
											hashMapSupport.put(myKey, support.intValue()+1);
										}
								}
							}
			}
		
	//Calculate count for each pair
		ArrayList<Integer> listPair =  new ArrayList<Integer>(hashMapIDName.keySet());
		
		for(Integer key : listPair)
			{
				HashSet<Integer> a=hashMaptest.get(key);
				if(a!=null)
					{
						ArrayList<Integer> listSupport =  new ArrayList<Integer>(hashMaptest.get(key));
							for(int i=0; i<listSupport.size(); i++)
								{
									for(int j=i+1; j<listSupport.size(); j++)
										{
											HashSet<Integer> hashSet = new HashSet<Integer>(0); 
											hashSet.add(listSupport.get(i));
											hashSet.add(listSupport.get(j));
												if(!hashMapPairCount.containsKey(hashSet))
													{
														hashMapPairCount.put(hashSet, new Integer(1));
													}
												else
													{
														Integer support = hashMapPairCount.get(hashSet);
														hashMapPairCount.put(hashSet, support.intValue()+1);
													}
										}
								}
						}
			}
		
	
//Calculate confidence and compare with support and confidence threshold 
//and display those values which are greater than the threshold provided
		
		for(Map.Entry<Integer, Integer> check1: hashMapSupport.entrySet())
		{
			for(Map.Entry<HashSet<Integer>, Integer> check2: hashMapPairCount.entrySet())
			{
				HashSet s= check2.getKey();
			
				if(s.contains(check1.getKey()))
					{
						double thresh= new Integer(0);
						thresh=(((double)check2.getValue()/(double)check1.getValue())*100);
						ArrayList<Integer> arrCheck =  new ArrayList<Integer>();
							if(check2.getValue()>=supportThresh && thresh>=confidenceThresh)
								{
									for(Map.Entry<Integer, HashSet<Integer>>entry: hashMapIDCheck.entrySet())
										{
											HashSet<Integer> a=hashMapIDCheck.get(entry.getKey());
												if(a!=null)
													if(entry.getValue().contains(check1.getKey()))
														{
															arrCheck.add(entry.getKey());
																if(entry.getValue().containsAll(s))
																	{
																		arrCheck.remove(entry.getKey());
																	}
														}
										}
									//Rounding off and displaying in correct format		
									List<Object> list = new ArrayList<Object>(s);
									Object[] objects = list.toArray();
									int i=0;
									NumberFormat numf = NumberFormat.getNumberInstance();
									numf.setMaximumFractionDigits(2);
									numf.setRoundingMode (RoundingMode.HALF_EVEN);
									for(int a:arrCheck)
										{
											count++;
											
											chck1=hashMapIDName.get(objects[i+1]);
											chck2=hashMapIDName.get(objects[i]);
											flag = chck1.compareTo(chck2);
											if(flag>0)
											{
												tmp =chck1;
												chck1=chck2;
												chck2 = tmp;
											}
											
										System.out.format("bug: %s in %s, pair: (%s, %s), support: %d, confidence: %.2f%%\n", hashMapIDName.get(check1.getKey()),hashMapIDName.get(a),chck1,chck2,check2.getValue(),thresh);	
										}i++;
								}	
						}
				}
		}

		System.out.println("Total Bugs: "+count);
	}
	
	 private static HashMap<Integer, HashSet<Integer>> InterProcedural(
				HashMap<Integer, HashSet<Integer>> hashMaptest,
				HashMap<Integer, HashSet<Integer>> hashMapIDCheck)
	
	 {
		 Iterator iterator=null;
		 Iterator iterator1=null;
		 HashSet hashKey=null;

		 for(Map.Entry<Integer, HashSet<Integer>> checktest: hashMapIDCheck.entrySet())
		 {
			Integer getKey= checktest.getKey();
			hashKey=hashMapIDCheck.get(getKey);
			
			if(hashKey!=null)
				{
					iterator= hashKey.iterator(); 
					HashSet<Integer> hashSet =new HashSet<Integer>();
					while(iterator.hasNext()){
						Object val=iterator.next();
						int key= (Integer) val;
						if(hashMapIDCheck.containsKey(val))
							{ 
								HashSet HashSetVal=hashMapIDCheck.get(val);
								if(HashSetVal!=null){
									if(hashMapIDCheck.containsKey(getKey))
									{
										hashSet.addAll(HashSetVal);
									} 
								}
								if(HashSetVal==null)
								{
									hashSet.add(key);
								}
							}
				
					if(!hashMaptest.containsKey(getKey))
					{
						hashMaptest.put(getKey, hashSet);
					}
				
				 }
			}
			
		 }
		
		return hashMaptest;
	}
	 
	
	
	 
	 
	
	
	
	}

	
	