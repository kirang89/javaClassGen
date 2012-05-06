package controllers;

import play.mvc.Controller;
import play.mvc.results.RenderText;

public class Generator extends Controller 
{	 
	 public static void index() 
	 {
	        render();
	 }
	 
	 public static void create()
	 {
		render(); 
	 }
	 
	 public static void makeClass(String class_name,String fields,String datatypes)
	 {
		 String resultClass = generateClass(class_name,fields,datatypes);
		 
		 response.contentType = "text/java";
		 response.setHeader("Content-Disposition", "attachment;filename=\""
					+ class_name+".java" + "\"");
		 
		 response.writeChunk(resultClass);
	 }
	 
	public static String generateClass(String className,String fields,String datatype)
	{
		String newline = System.getProperty("line.separator");
		StringBuilder structure = new StringBuilder();
		structure.append("public class " + className);
		structure.append(newline);
		structure.append("{");
		structure.append(newline);
		
		String variables = generateMemberVariables(fields,datatype);
		if(variables != null)
			structure.append(variables);
		
		structure.append(newline);
		
		String constructor = generateConstructor(className,fields,datatype);
		if(constructor != null)
			structure.append(constructor);
		
		structure.append(newline);
		structure.append(generateGetFunction(fields,datatype));
		structure.append(newline);
		structure.append(newline);
		structure.append(generateSetFunction(fields,datatype));
		structure.append(newline);
		
		structure.append("}");
		structure.append(newline);
		
		return structure.toString();
	}
	
	public static String generateSetFunction(String fields, String datatype) 
	{
		String newline = System.getProperty("line.separator");
		StringBuilder structure = new StringBuilder();
		
		String[] fieldArray = fields.split(",");
		String[] datatypeArray = datatype.split(",");
		if(fieldArray.length != datatypeArray.length)
		{
			return null;
		}
		structure.append("\t");
		for( int i = 0; i < fieldArray.length ; i++ )
		{
			structure.append("public void set" + capitalize(fieldArray[i]) + "(" + datatypeArray[i] + " " + fieldArray[i] + ")");
			structure.append("{this." + fieldArray[i] + " = " + fieldArray[i]+ ";}");
			structure.append(newline);
			structure.append("\t");
		}

		return structure.toString();
	}
	
	public static String capitalize(String field)
	{
		StringBuilder result = new StringBuilder();
		
		String substr = field.substring(1);
		char temp = field.toUpperCase().charAt(0);
		result.append(temp); 
		result.append(substr);
		
		return result.toString();
	}
	
	public static Object generateGetFunction(String fields,String datatype)
	{
		String newline = System.getProperty("line.separator");
		StringBuilder structure = new StringBuilder();
		String[] fieldArray = fields.split(",");
		String[] datatypeArray = datatype.split(",");
		if(fieldArray.length != datatypeArray.length)
		{
			return null;
		}
		structure.append("\t");
		for( int i = 0; i < fieldArray.length ; i++ )
		{
			if(datatypeArray[i].equalsIgnoreCase("boolean"))
				structure.append("public " + datatypeArray[i] + fieldArray[i] + "()");
			else
				structure.append("public " + datatypeArray[i] + " get" + capitalize(fieldArray[i]) + "()");
			structure.append("{return this." + fieldArray[i] + ";}");
			structure.append(newline);
			structure.append("\t");
		}
		return structure.toString();
	}

	public static String generateConstructor(String className, String fields, String datatype)
	{
		String newline = System.getProperty("line.separator");
		StringBuilder structure = new StringBuilder();
		String[] fieldArray = fields.split(",");
		String[] datatypeArray = datatype.split(",");
		if(fieldArray.length != datatypeArray.length)
		{
			return null;
		}
		structure.append("\t");	
		structure.append("public " + className + "()");
		structure.append(newline);
		structure.append("\t");
		structure.append("{");
		structure.append(newline);
		structure.append("\t");
		for( int i = 0; i < fieldArray.length ; i++)
		{
			structure.append("\t");
			if(datatypeArray[i].equalsIgnoreCase("int"))
				structure.append(fieldArray[i] + " = " + "0" + ";");
			else
				if(datatypeArray[i].equalsIgnoreCase("String"))
					structure.append(fieldArray[i] + " = " + "null" + ";");
				else
					if(datatypeArray[i].equalsIgnoreCase("double") || datatypeArray[i].equalsIgnoreCase("float"))
						structure.append(fieldArray[i] + " = " + "0.0" + ";");
					else
						if(datatypeArray[i].equalsIgnoreCase("boolean"))
							structure.append(fieldArray[i] + " = " + "false" + ";");
			structure.append(newline);
			structure.append("\t");
		}
		structure.append("}");
		return structure.toString();
	}
	
	public static String generateMemberVariables(String fields, String datatype)
	{
		StringBuilder result = new StringBuilder();
		String[] fieldArray = fields.split(",");
		String[] datatypeArray = datatype.split(",");
		
		if(fieldArray.length != datatypeArray.length)
		{
			return null;
		}
		String newline = System.getProperty("line.separator");
		for( int i = 0; i < fieldArray.length ; i++)
		{
			result.append("\t");
			result.append("private ");
			result.append(datatypeArray[i] + " " + fieldArray[i] + ";");
			result.append(newline);
		}
		return result.toString();
	}
}
