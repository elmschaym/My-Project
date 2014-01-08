/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author POTCHOLO
 */
public class AboutStrings {
    
  public static final  char[] nums={'1','2','3','4','5','6','7','8','9','0'};
  public static final char[] signs={'~','!','@','#','$','%','^','&','*','(',')','_','=','+','[',']','{','}',';',':','?',',','<','>','|','"',};
    
  //int i=Integer.
  public boolean isNumber(char key)
    { int num=0;
      while(num<10){
        if(key==nums[num])
        {
            return true;
            
        }
        else
        {
            num++;
        }
      }
       return false;
    }
  
  public boolean isSign(char key){
      int num=0;
      while(num<signs.length){
        if(key==signs[num])
        {
            return true;
            
        }
        else
        {
            num++;
        }
      }
       return false;
  }
  
   public boolean isvalidDate(String st)
   {
      try{
       if(!isNumber(st.charAt(0))) 
      {
          return false;
      }
      else
          return true;
      }
      catch(StringIndexOutOfBoundsException e)
      {
          return false;
      }
   }
   public String setDate(String d,String mon,String yr){
       String res=d+"/"+mon+"/"+yr;
       return res;
   }
    
}
