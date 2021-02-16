package com.mfi.customvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NrcValidator implements 
ConstraintValidator<NRC, String> {

  @Override
  public void initialize(NRC nrc) {
  }

  @Override
  public boolean isValid(String nrc,
    ConstraintValidatorContext cxt) {
//	  String firstDigit=nrc.substring(0,1);
//	  String first2Digit=nrc.substring(0,2);
//	  Integer IntFirst2Digit=Integer.valueOf(first2Digit);
//	  String last6Digit=nrc.substring(nrc.length()-6);
//	  StringBuffer township= new StringBuffer(nrc.split("(N)")[0].split("/")[1]);  
//	  StringBuffer finalTownship=township.deleteCharAt(township.length()-1); 
//	  
//	  char chrOne=nrc.charAt(1);
//	  String sOne=Character.toString(chrOne);
//	  char chrTwo=nrc.charAt(2);
//	  String sTwo=Character.toString(chrTwo);
	  
	  
	  
      return nrc != null && (nrc.length() >= 17) && (nrc.length() <= 21)&& 
    		  		Integer.valueOf(nrc.substring(0,2))<=15 && (nrc.substring(0,1).matches("[0-9]+")||nrc.substring(0,2).matches("[0-9]+")) &&
    		  		(Character.toString(nrc.charAt(1)).equals("/")||Character.toString(nrc.charAt(2)).equals("/"))&&
    		  		new StringBuffer(nrc.split("(N)")[0].split("/")[1]).deleteCharAt(new StringBuffer(nrc.split("(N)")[0].split("/")[1]).length()-1).toString().matches("[a-zA-Z]+")&&
    		  		nrc.contains("(")&&nrc.contains("N")&&nrc.contains(")")&&
    		  		nrc.substring(nrc.length()-6).matches("[0-9]+") ;
  }

}
