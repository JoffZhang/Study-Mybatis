package com.study;

import java.util.ArrayList;

/**
 * @author zy
 * @date 2020/6/4 17:18
 */
public class innerclass {

	public static void main(String[] args) {
		/*//匿名内部类
		new InterfaceTeset() {
			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getAge() {
				return null;
			}
		};
		//new对象后，后面跟着两个大括号
		//两个大括号的方式初始化(本质上是匿名内部类 + 实例化代码块儿)
		new ArrayList<String>(){{
			add("a");
			add("b");
		}};*/

		Person person = new Person("张三") {
			//匿名内部类 可以在这里实现方法，
			// 就可以在这里构建其他方法 或属性 。由于该类没有定义其他方法或属性，所以外部不能使用，只可以在{} 中调用。
			@Override
			public String getName() {
				return super.getName()+"___"+getAge();
			}
			public String getAge(){
				return age;
			}

			public String age;
			//匿名内部类的  初始化代码块
			{
				name = "123";
				age = "age";
				System.out.println("{{ }}");
			}
		};
		System.out.println(person.getName());


	}
}
class Person{
	String name;
	public Person(String name) {
		this.name = name;
		System.out.println("构造方法执行.....");
	}

	{
		//实例化代码块，先于构造方法执行
		System.out.println("实例化初始块");
	}

	public String getName(){
		return name;
	}
}

interface InterfaceTeset{
	String getName();
	String getAge();
}