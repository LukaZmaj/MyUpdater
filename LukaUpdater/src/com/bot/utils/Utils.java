package com.bot.utils;

import java.util.Random;

public class Utils
{
  private static Random random = new Random();
  

  public static int nextInt(int min, int max)
  {
    return random.nextInt(max - min) + min;
  }
  
  
  public static void sleep(int time)
  {
    try
    {
      Thread.sleep(time);
    }
    catch (InterruptedException localInterruptedException)
    {
    }
  }


	public static void sleep(int min, int max)
	{
	  try
	  {
	    Thread.sleep(nextInt(min,max));
	  }
	  catch (InterruptedException localInterruptedException)
	  {
	  }
	}
	}
  


