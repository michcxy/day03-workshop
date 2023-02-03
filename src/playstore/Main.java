package playstore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class Main{

    public static void main(String[] args) throws IOException {

        Map<String, AppStats> stats = new HashMap<>();
        AppStats st;
        int discard = 0;

        //open the file
        Path p = Paths.get(args[0]);
        File f = p.toFile();

        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        //discard the header line (first column)
        br.readLine();

        //loop through the file and process some data
        String line;
        while((line = br.readLine()) != null){ //dont understand this
            String[] cols = line.split(",");
            String appName = cols[0];
            String category = cols[1];
            String col3 = cols[2];
            float rating = 0f;

            try{
                //for string comparison we cant use = have to use .equals(" ")
                if(col3.toLowerCase().equals("nan")){
                    discard++;
                    continue;
                }
                rating = Float.parseFloat(col3);

            }
            catch (NumberFormatException ex){
                discard++;
                //stop processing this line, read the nuxt line
                continue;

            }

            //if there's a category, get the category
            if(stats.containsKey(category)){
                st = stats.get(category);
                st.evaluate(appName, rating);
            }
            else {
                st = new AppStats(category);
                stats.put(category, st);
            }

            st.evaluate(appName, rating);
        }

        for(String cat: stats.keySet()){
            st = stats.get(cat);
            System.out.printf("Catgeory: %s\n", cat);
            System.out.printf("\tAverage rating: %f\n", (st.getTotalRating()/st.getCount()));
            System.out.printf("\tHighest rating: %s, %f\n", st.getHighestApp(), st.getHighestAppRating());
            System.out.printf("\tLowest rating: %s, %f\n", st.getLowestApp(), st.getLowestAppRating());
        }

        System.out.printf("Discarded samples: %d\n", discard);

        br.close();
        fr.close();
        
    }
    
}