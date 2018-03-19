//WC.java
import java.io.*;
import java.util.Vector;

public class WC {
	   public static Vector<String> stop;
	   public static int words = 0;
	   public static int lines = 1;
	   public static int chars = 1;
	   public static int codel=0;
	   public static int blackl=0;
	   public static int zhushil=0;
	   public static boolean w=false;
	   public static boolean l=false;
	   public static boolean c=false;
	   public static boolean o=false;
	   public static boolean a=false;
	   public static boolean e=false;
	   
	   //将停用词表放入容器
	   public static void stoplist(InputStream s) throws IOException
	   {
		   int cc=0;
		   char c=' ';
		   String temp="";
		   stop=new Vector<String>();
		   while((cc=s.read()) != -1)
		   {
			   c=(char)cc;
			   if(c!=' '&&c!='\t'&&c!='\r'&&c!='\n') 
				   temp=temp+c;
			   else if(!temp.equals("")) 
			   {
				   stop.add(temp);
				   temp="";
			   }   
		   }
		   if(!temp.equals(""))
		   		stop.addElement(temp);   
	   }
	   
	 //文件读取之后逐个字符判定，字符数行数和词数
	   public static void wordcountwithstoplist(InputStream f) throws IOException {
	       int cc=0;
		   char c =' ';
	       String temp="";
	       int wdflag=0;
	       int wddflag=1;
	       int zsflag=0;
	       int aloneflag=0;
	       int zflag=0;
	       int zzflag=0;
	       int xingflag=0;
	       while ((cc = f.read()) != -1) {
	    	   xingflag=0;
	    	   c=(char)cc;
	    	   ++chars;
	           if (c == '\n') {
	               ++lines;
	               if(aloneflag<=1&&zsflag==0&&zflag==0) ++blackl;
	               else if((zsflag==1||zflag==1)&&aloneflag<=2) ++zhushil;
	               else ++codel;
	               if(zzflag==1) {zflag=0; zzflag=0;}
	    	       zsflag=0;
	    	       aloneflag=0;
	               wdflag=1;
	               if(!stop.contains(temp)&&!temp.equals("")) wddflag=1;
	               temp="";
	           }
	           if(c==' '||c==',') {wdflag=1; if(c==' ') zsflag=0;if(!stop.contains(temp)&&!temp.equals("")) wddflag=1;temp="";}
	           if(c!=' '&&c!='\t'&&c!='\r'&&c!='\n') 
	           {  
	        	   if(c!=',')
	        	   {
	        		   temp=temp+c;
	        		   if(wdflag==1)
	        		   {
	        			   if(wddflag==1)
	        				   {++words; wddflag=0;wdflag=0;}	   
	        		   }  
	        	   }
	        	   if(zsflag==0&&(zflag==0||zzflag==1)) ++aloneflag;
	           }
	        	if(c=='/'&&zflag==0)  
	        	{
	        		if((cc = f.read()) != -1)
	        		{
	        			c=(char)cc;
	        			if(c=='/'&&aloneflag<=2)	zsflag=1;
      					if(c=='*')  {zflag=1;xingflag=1;}
	        		}
	        	}
	        	if(c=='*'&&zflag==1&&xingflag==0)
	        		if((cc = f.read()) != -1)
	        		{
	        			c=(char)cc;
	        			if(c=='/') 	zzflag=1;
	        		}
	       } 
	       if(!temp.equals("")&&!stop.contains(temp)) ++words;
           if(aloneflag<=1&&zsflag==0&&zflag==0) ++blackl;
           else if((zsflag==1||zflag==1)&&aloneflag<=2) ++zhushil;
           else ++codel;
	       chars=chars-2*lines+1;
	   }

	   public static void wordcountwithoutstoplist(InputStream f) throws IOException {
		   int c =0;
	       int wdflag=0;
	       int zflag=0;
	       int zzflag=0;
	       int zsflag=0;
	       int aloneflag=0;
	       int xingflag=0;
	       while ((c = f.read()) != -1) {
	    	   xingflag=0;
	    	   ++chars;	    	   
	           if (c == '\n') {
	               ++lines;
	               if(aloneflag<=1&&zsflag==0&&zflag==0) ++blackl;
	               else if((zsflag==1||zflag==1)&&aloneflag<=2) ++zhushil;
	               else ++codel;
	               if(zzflag==1) {zflag=0; zzflag=0;}
	    	       zsflag=0;
	    	       aloneflag=0;
	               wdflag=1;
	           }
	           if(c==' '||c==',')  wdflag=1;
	           if(c!=' '&&c!='\t'&&c!='\r'&&c!='\n') 
	           {  
	        	   if(c!=',')
	        	   {
	        		   if(wdflag==1)
	        		   {
	        			   ++words;
	        			   wdflag=0;   
	        		   }  
	        	   }
	        	   if(zsflag==0&&(zflag==0||zzflag==1)) ++aloneflag;
	        	}         	   
	            
	        	if(c=='/'&&zflag==0)  
	        	{
	        		if((c = f.read()) != -1)
	        			if(c=='/'&&aloneflag<=2)	zsflag=1;
       					if(c=='*')  {zflag=1;xingflag=1;}
	        	}
	        	if(c=='*'&&zflag==1&&xingflag==0)
	        		if((c = f.read()) != -1)
	        			if(c=='/') 	zzflag=1;
	       } 
	       if(c!=' '&&c!='\t'&&c!='\r'&&c!='\n') ++words;
           if(aloneflag<=1&&zsflag==0&&zflag==0) ++blackl;
           else if((zsflag==1||zflag==1)&&aloneflag<=2) ++zhushil;
           else ++codel;
	       chars=chars-2*lines+1;
	   }

	   
	   public static void main(String args[]) throws IOException {
		   FileInputStream f;
		   FileInputStream s;
		   String ssr="";//输入的文件名
		   String osr="result.txt";//输出的文件名  
		   String stsr="";//停用词表文件名
		   //对输入的指令进行分析
	       for(int i=0;i<args.length;i++){
	    	   if(args[i].equals("-c")) c=true;
	    		   
	    	   else if(args[i].equals("-w")) w=true;
	    	   
	    	   else if(args[i].equals("-l")) l=true;
	    	   
	    	   else if(args[i].equals("-a")) a=true;
	    	   
	    	   else if(args[i].equals("-e")) {e=true; stsr=args[++i];}
	    	   
	    	   else if(args[i].equals("-o")) osr=args[++i];
	    	  
	    	   else if(args[i].charAt(0)!='-') ssr=args[i];
	        }
	       if(e==true)
	       {
	    	   try {//读入文件 
		           s = new FileInputStream(stsr);
		           stoplist(s); 
		           s.close();
		       } 
		       catch (IOException e) {
		           return;
		       }
	    	   
		       try {//读入文件   
		           f = new FileInputStream(ssr);
		           wordcountwithstoplist(f);
		           f.close();
		       } 
		       catch (IOException e) {
		           return;
		       }
	       }
	       
	       else 
	       {
	    	   try {//读入文件   
		           f = new FileInputStream(ssr);
		           wordcountwithoutstoplist(f);
		           f.close();
		       } 
		       catch (IOException e) {
		           return;
		       }
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
	       if(a==true)
	       {
	    	   String re=ssr+","+"代码行/空行/注释行："+codel+"/"+blackl+"/"+zhushil+"/"+"\r\n";
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
