package gmdProject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class IndexFiles {

	private IndexFiles() {}


	public static void main(String[] args) {

		String indexPath = "/home/chouder/workspace/GMD/index";
		String fileToIndex = "/home/chouder/workspace/GMD/omim/omim.txt";

		final File file = new File(fileToIndex);
		if (!file.exists() || !file.canRead()) {
			System.out.println("My file to index '" +file.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");
			Directory dir = FSDirectory.open(new File(indexPath));
			Analyzer analyzer = new StandardAnalyzer(Version.LATEST);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDrugs(writer, file);	
			writer.close();
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
	}


	static void indexDrugs(IndexWriter writer, File file)
			throws IOException {
		int eltCount=0;
		if (file.canRead()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);

				// LIRE le fichier ligne par ligne
				InputStreamReader ipsr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(ipsr);
				String line;				
				Document doc = null;
				String id = "";
				while((line=br.readLine())!=null){

					if(line.startsWith("*RECORD*")){
						doc = new Document();
					}
					if(line.startsWith("*FIELD* TI")){
						line=br.readLine();
						//System.out.println("field ti : "+line);
						doc.add(new TextField("field ti", line, Field.Store.YES));	
					}
					if(line.startsWith("*FIELD* CS")){
						String tmp;
						String cs = "";
						while((tmp = br.readLine()) != null){
							if(tmp.contains("*FIELD*")){
								break;
							}else{
								cs = cs + tmp + " ";
							}
						}
						doc.add(new TextField("field cs", cs, Field.Store.YES));
					}
										
					if(line.startsWith("*FIELD* CN")){
						System.out.println("adding : " + doc.get("field cs"));
						writer.addDocument(doc);
						eltCount++;
					}
				}
				System.out.println("I indexed "+eltCount+" diseases");
				fis.close();
				br.close();
			} catch(FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			}
		}
	}
}

