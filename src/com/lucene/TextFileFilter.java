package com.lucene;

import java.io.File;
import java.io.FileFilter;

/**
 * ����Ϊ .txt �ļ�������
 *
 */
public class TextFileFilter implements FileFilter {

   @Override
   public boolean accept(File pathname) {
	   return true;
      //return pathname.getName().toLowerCase().endsWith(".txt");
   }
}