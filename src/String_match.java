import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class String_match {
	public static void main(String[] args){
		char[] line_to_char;       //用户输入的字符串数组
		String Participle=null;
		final String [] subject={"我","你","他","她","它"};
		int subject_length=subject.length;        //4
		final String [] predicate={"要","想","准备要","去","看","准备去","准备","去","干什么"};
		int predicate_length=subject_length+predicate.length;   //9
		final String [] object={"吃饭","睡觉","电影","洗澡","洗漱"};
		int object_length=predicate_length+object.length;       //12
		final Map<String,Object> map=new HashMap<>();
		final List<String> list_subject=new LinkedList<>();         //主语
		final List<String> list_predicate=new LinkedList<>();       //谓语
		final List<String> list_object=new LinkedList<>();          //宾语
		final Stack<String> stack=new Stack<>();
		
	/*
	* 向map中添加数据,用线程来进行并发加载
	* */
		Thread Thread_map=new Thread(new Runnable(){

			public void run() {
				map.put("subject", "主语");
				map.put("predicate", "谓语");
				map.put("object", "宾语");
				map.put("error", "字符匹配失败");
			}
			
		});
		Thread_map.start();
		
	/*
	* 将元素插入到LinkedList中,并且入栈，用线程来进行并发加载
    * */
		Thread thread_init=new Thread(){

			@Override
			public void run() {
				for(String s :object){
					list_object.add(s);
					stack.push(s);
				}
				for(String s :predicate){
					list_predicate.add(s);
					stack.push(s);
				}
				for(String s :subject){
					list_subject.add(s);
					stack.push(s);
				}
			}
			
		};
		thread_init.start();	
	/*
	 * 用户正式读入数据
	 * */
		Scanner input=new Scanner(System.in);
		System.out.println("主语\t"+list_subject);
		System.out.println("谓语\t"+list_predicate);
		System.out.println("宾语\t"+list_object);
		System.out.println("其余均显示字符串匹配失败");
		while(true){
			
	
		System.out.println("\n请输入您要匹配的中文字符串");
		line_to_char=input.nextLine().toCharArray();
		System.out.println("分词结果为：");
	/*
	 * 对用户的输入进行判断
	 * */
			for(int i=0;i<line_to_char.length;i++){
				int j=i,count=1;
				Participle=""+line_to_char[i];
				int seh=stack.search(Participle);
		/*
		 * 判断分词，如果这个分词Participle的长度大于3，就说明Participle的第一个字没有匹配上
		 * 我们需要做的还原i的值并且输出这个字没有匹配上，从下一个次开始继续进行匹配
		 * */
				while(seh==-1&&count<=2&&i+1<line_to_char.length){
					++i;
					++count;
					Participle=Participle+line_to_char[i];
					seh=stack.search(Participle);
				}
				if(seh==-1){
					i=j;
					System.out.println(line_to_char[i]+"\t"+map.get("error"));
				}else if(seh<=subject_length){
					System.out.println(Participle+"\t"+map.get("subject"));
				}else if(seh>subject_length&&seh<=predicate_length){
					System.out.println(Participle+"\t"+map.get("predicate"));
				}else{
					System.out.println(Participle+"\t"+map.get("object"));
				}	
			}	
		}
	}
}
