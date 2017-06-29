package com.lucene;

import java.io.File;
import java.io.FileFilter;

/**
 * 用于为 .txt 文件过滤器
 *
 */
public class TextFileFilter implements FileFilter {

   @Override
   public boolean accept(File pathname) {
	   return true;
      //return pathname.getName().toLowerCase().endsWith(".txt");
   }
}