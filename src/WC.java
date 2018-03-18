//WC.java
import java.util.*;
import java.io.*;
import java.nio.*;

public class WC {
	   public static int words = 1;
	   public static int lines = 1;
	   public static int chars = 1;
	   public static boolean w=false;
	   public static boolean l=false;
	   public static boolean c=false;
	   public static boolean o=false;
	   //文件读取之后逐个字符判定，字符数行数和词数
	   public static void wordcount(InputStream f) throws IOException {
	       int c = 0;
	       int flag=0;
	       while ((c = f.read()) != -1) {
	    	   chars++;
	           if (c == '\n') {
	               lines++;
	               flag=1;
	           }
	           if(c==' '||c==',') flag=1;
	           if(c!=' '&&c!=','&&c!='\t'&&c!='\r'&&c!='\n'&&flag==1) 
	           {
	        	   words++;
	        	   flag=0;
	           }
	       }
	       chars=chars-2*lines+1;
	   }
	 
	   public static void main(String args[]) throws IOException {
	       String str=null;
		   FileInputStream f;
		   String ssr="";//输入的文件名
		   String osr="result.txt";//输出的文件名      
		   //对输入的指令进行分析
	       for(int i=0;i<args.length;i++){
	    	   if(args[i].equals("-c")) c=true;
	    		   
	    	   else if(args[i].equals("-w")) w=true;
	    	   
	    	   else if(args[i].equals("-l")) l=true;
	    	   
	    	   else if(args[i].equals("-o")) osr=args[++i];
	    	  
	    	   else if(args[i].charAt(0)!='-') ssr=args[i];
	        }
	       
	       try {
	           //读入文件
	           f = new FileInputStream(ssr);
	           wordcount(f);
	          
	       } 
	       catch (IOException e) {
	           return;
	       }
	       try {
	    	   File output= new File(osr);
	    	   FileWriter out = new FileWriter(output);
	    	   
	       //由指令判定哪些内容需要输出
	       if(c==true) 
	    	   {
	    	   		String re=ssr+","+"字符数："+chars+"\r\n";
	    	   		out.write(re);
	    	   }
	       if(w==true)  
	    	   {
	    	   		String re=ssr+","+"单词数："+words+"\r\n";
	    	   		out.write(re);
	    	   }
	       if(l==true) 
	    	   {	
	    	   		String re=ssr+","+"行数："+lines+"\r\n";
	    	   		out.write(re);
	    	   }
	       out.flush();
	       out.close();
	       }
	       catch(IOException e) {
	    	   return;
	       }
	   }
}
