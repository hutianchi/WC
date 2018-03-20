//WC.java
import java.io.*;
import java.util.Vector;
import java.util.ArrayList;

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
	   public static boolean allfile=false;
	   public static boolean e=false;
	   public static String shuchu="";
	   
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
	   
	   
	   private static void getAllFile(ArrayList<String>sourcepathArray, ArrayList<String>sourcenameArray, String rootPath, String keyword)   //获取所有文件("*.c")
	    {
	        File rootFile = new File(rootPath);
	        File[] files = rootFile.listFiles();
	        if (files != null)
	        {
	            for (File f : files)
	            {
	                if (f.isDirectory()) //判断是文件夹
	                    getAllFile(sourcepathArray,sourcenameArray, f.getPath(), keyword);
	                else if (f.getName().indexOf(keyword) == f.getName().length() - keyword.length())
	                    {
	                		sourcepathArray.add(f.getPath());
	                		sourcenameArray.add(f.getName());
	                    }
	            }
	        }
	    }

	   
	   
	 //文件读取之后逐个字符判定，字符数行数和词数
	   public static void wordcountwithstoplist(InputStream f) throws IOException {
	       int cc=0;
		   char c =' ';
	       String temp="";
	       int wdflag=0;   //是否可以算作单词
	       int wddflag=1;	//是否不在停用词表里
	       int zsflag=0;    //是否为单行双杠的注释
	       int aloneflag=0;  //当前行的有效字符数（不算注释和回车空格等）
	       int zflag=0;		//是否在/**/之间的注释
	       int zzflag=0;		//是否在/*之后读到了*/
	       int xingflag=0;		//是否此*是/*的星
	       while ((cc = f.read()) != -1) {
	    	   xingflag=0;
	    	   c=(char)cc;
	    	   ++chars;
	           if (c == '\n') {
	               ++lines;
	               if(aloneflag<=1&&zsflag==0&&zflag==0) ++blackl;	//没有或有一个有效字符的行是空行
	               else if((zsflag==1||zflag==1)&&aloneflag<=2) ++zhushil;	//有效字符为0或1个的单行注释或者在/**/里的行为注释行
	               else ++codel;			//有效字符大于等于两个
	               if(zzflag==1) {zflag=0; zzflag=0;}
	    	       zsflag=0;
	    	       aloneflag=0;
	               wdflag=1;
	               if(!stop.contains(temp)&&!temp.equals("")) wddflag=1;
	               temp="";
	           }//判断当前词是否在停用词表里
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
	        			if(c=='/'&&aloneflag<=2)	zsflag=1;  //双杠注释开始
      					if(c=='*')  {zflag=1;xingflag=1;}		///**/注释开始
	        		}
	        	}
	        	if(c=='*'&&zflag==1&&xingflag==0)
	        		if((cc = f.read()) != -1)
	        		{
	        			c=(char)cc;
	        			if(c=='/') 	zzflag=1;    ///**/注释结束，在此行结束判定
	        		}
	       } //最后一个词在循环内可能不会分析，因此出来判定其是否分析过
	       if(!temp.equals("")&&!stop.contains(temp)) ++words;
           if(aloneflag<=1&&zsflag==0&&zflag==0) ++blackl;
           else if((zsflag==1||zflag==1)&&aloneflag<=2) ++zhushil;
           else ++codel;
	       chars=chars-lines+1;
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
	       chars=chars-lines+1;			//换行时有换行和回车两个字符，应算一个
	   }

	   
	   public static void main(String args[]) throws IOException {
		   FileInputStream f;
		   FileInputStream s;
		   ArrayList<String>sourcePath = new ArrayList<String>();
		   ArrayList<String>sourceName = new ArrayList<String>();
		   String path="./";
		   String houzhui=".c";
		   String ssr="";//输入的文件名
		   String osr="result.txt";//输出的文件名  
		   String stsr="";//停用词表文件名
		   //对输入的指令进行分析
	       for(int i=0;i<args.length;i++){
	    	   if(args[i].equals("-c")) c=true;
	    		   
	    	   else if(args[i].equals("-w")) w=true;
	    	   
	    	   else if(args[i].equals("-l")) l=true;
	    	   
	    	   else if(args[i].equals("-a")) a=true;
	    	   
	    	   else if(args[i].equals("-s")) allfile=true;
	    	   
	    	   else if(args[i].equals("-e")) {e=true; stsr=stsr+args[++i];}
	    	   
	    	   else if(args[i].equals("-o")) osr=args[++i];
	    	  
	    	   else if(args[i].charAt(0)!='-') ssr=args[i];
	        }
	       
	       if(allfile)
	       {
	    	   getAllFile(sourcePath,sourceName, path, houzhui);
	    	   
	    	   for(int i=0;i<sourcePath.size();i++)
	    	   {
	    		   words = 0;
	    		   lines = 1;
	    		   chars = 1;
	    		   codel=0;
	    		   blackl=0;
	    		   zhushil=0;
	    		   String pat=sourcePath.get(i);
	    		   String name=sourceName.get(i);
	    		   if(e)
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
	    				   f = new FileInputStream(pat);
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
	    				   f = new FileInputStream(pat);
	    				   wordcountwithoutstoplist(f);
	    				   f.close();
	    			   	} 
	    			   catch (IOException e) {
	    				   return;
	    			   }
	    		   	}	    		   

			       //由指令判定哪些内容需要输出
			       if(c) shuchu=shuchu+name+","+"字符数："+chars+"\r\n";

			       if(w) shuchu=shuchu+name+","+"单词数："+words+"\r\n";

			       if(l) shuchu=shuchu+name+","+"行数："+lines+"\r\n";

			       if(a) shuchu=shuchu+name+","+"代码行/空行/注释行："+codel+"/"+blackl+"/"+zhushil+"\r\n\r\n";

	    	   }
	    		   try {
			    	   File output= new File(osr);
			    	   FileWriter out = new FileWriter(output);
				       out.write(shuchu);
				       out.flush();
				       out.close();
				       }
				       catch(IOException e) {
				    	   return;
				       }	   
	       }
	       
	       else 
	       {
	    	   if(e)
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
		       if(c) 
		       {
		    	   	String re=ssr+","+"字符数："+chars+"\r\n";
		    	   	out.write(re);
		       }
		       if(w)  
		       {
		    	   	String re=ssr+","+"单词数："+words+"\r\n";
		    	   	out.write(re);
		       }
		       if(l) 
		       {	
		    	   	String re=ssr+","+"行数："+lines+"\r\n";
		    	   	out.write(re);
		       }
		       if(a)
		       {
		    	   String re=ssr+","+"代码行/空行/注释行："+codel+"/"+blackl+"/"+zhushil+"\r\n";
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
}
