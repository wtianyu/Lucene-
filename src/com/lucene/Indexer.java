package com.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;


/**
 * ����������ԭʼ���ݣ��������ǾͿ���ʹ��Lucene�⣬ʹ�������
 *
 */
public class Indexer {

	private IndexWriter writer;

	public Indexer(String indexDirectoryPath) throws IOException{
		//this directory will contain the indexes

		deleteFolder(indexDirectoryPath);
		Directory indexDirectory = NIOFSDirectory.open(FileSystems.getDefault().getPath(indexDirectoryPath));

		//create the indexer
		Analyzer luceneAnalyzer  =   new StandardAnalyzer();  
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(luceneAnalyzer);
		writer  =   new IndexWriter(indexDirectory, indexWriterConfig); 
	}

	private void deleteFolder(String indexDirectoryPath) {
		File dirFile = new File(indexDirectoryPath);
		File[] files = dirFile.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			//ɾ�����ļ�  
			if (files[i].isFile()) {  
				new File(files[i].getAbsolutePath()).delete();
			} //ɾ����Ŀ¼  
			else {  
				deleteFolder(files[i].getAbsolutePath());  
			}  
		}
	}

	public void close() throws CorruptIndexException, IOException{
		writer.close();
	}

	private Document getDocument(File file) throws IOException{
		Document document = new Document();

		//index file contents
		Field contentField = new Field(LuceneConstants.CONTENTS, 
				new FileReader(file),TextField.TYPE_NOT_STORED);
		//index file name
		Field fileNameField = new Field(LuceneConstants.FILE_NAME,
				file.getName(),TextField.TYPE_STORED);
		//index file path
		Field filePathField = new Field(LuceneConstants.FILE_PATH,
				file.getCanonicalPath(),TextField.TYPE_STORED);
		document.add(contentField);
		document.add(fileNameField);
		document.add(filePathField);

		return document;
	}   

	private void indexFile(File file) throws IOException{
		System.out.println("Indexing "+file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
	}

	public int createIndex(String dataDirPath, FileFilter filter) 
			throws IOException{
		//get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if(!file.isDirectory()
					&& !file.isHidden()
					&& file.exists()
					&& file.canRead()
					&& filter.accept(file)
					){
				indexFile(file);
			}
		}
		return writer.numDocs();
	}
}