package gmdProject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadCSV {

	public static void main(String[] args) {
		ReadCSV obj = new ReadCSV();
		obj.run();
	}

	public void run() {

		String csvFile = "/home/chouder/workspace/GMD/omim/omim_onto.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			int elt=0;
			while ((line = br.readLine()) != null) {		
				if(!line.contains("\"")){
					//System.out.println(field);
					String[] field = line.split(cvsSplitBy);
					elt++;
					System.out.println("Preferred label : "+field[1] +"\n\tSynonyms : "+field[2]+ "\n\tCUI : "+field[5]);
					System.out.println("\n");
				}else{
					
				}				
			}
			System.out.println("Nb de CUI : "+elt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Done");
	}
}