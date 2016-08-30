
public class Demo {
	
	static boolean x;
	public static void main(String[] args) {
		x=false;
		String y=check(x);
		System.out.println(y);
		
	}
	
	public static String check(boolean x)
	{String y = null;
		if(x==true){
			y="a";
			method1();
		}
		else 
			{y="b";
			
			}
		if(x!=true)
		{y+="c";
		method2();}
		
	else 
		{y+="d";
		
		}
	//System.out.println(new Integer(80));
		return y;
	}
	
	static void method1()
	{//do something
	}
	
	
	static void method2()
	{//do something
		
	}
}



