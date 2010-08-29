package util;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class UtilData {

	
	public static String formatar(Date data){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		String result = format.format(data);
		return result;
		
	}
	
	
}
