import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import java.util.Random;
import java.util.Vector;

public class Main {
    static String[] vbs_increase = new String[] {"boost", "increase", "hike up", "rise"};
    static String[] vbs_decrease = new String[] {"decrease", "drop", "decline"};
    static String[] vbs_modifier = new String[] {"drastically", "violently", "fiercely"};

    public static void main(String[] args) {
        int inc_count = 0;
        int dec_count = 0;
        int flat_count = 0;
        // data:
        Vector<KPI> KPIS = new Vector<>();
        KPIS.add(new KPI("324,432,432,329", "323,433,434,433","Sales"));
        KPIS.add(new KPI("120,432,432,329", "110,433,434,433", "Baseline Sales"));
        KPIS.add(new KPI("135,432,432,329", "110,433,434,433", "Incremental Sales"));
        KPIS.add(new KPI("3,158,497,391", "1,581,493,946", "Margin"));
        KPIS.add(new KPI("8,330,099,206", "8,535,437,741", "Units"));
        KPIS.add(new KPI("748,710,476", "788,419,212", "Weight"));
        KPIS.add(new KPI("3.74", "3.62", "Average Item Price"));
        KPIS.add(new KPI("34.6", "34.6", "Brand Label Penetration"));

        for (KPI k: KPIS) {
            if (k.change > 0) {
                inc_count++;
            }
            else if (k.change == 0) {
                flat_count++;
            }
            else {
                dec_count++;
            }
        }

        String overview = "Overall, " + inc_count + " KPI increased, " + dec_count + " KPI decreased, "
                + flat_count + " KPI remained the same.";
        System.out.println(overview);

        String title_detail = "Detailed Analysis:";
        Vector<String> detail_analysis = new Vector<>();
        detail_analysis.add(title_detail);
        for (KPI k : KPIS) {
            String sentence = k.realiseSentence();
            detail_analysis.add(sentence);
        }

        String final_message = "";
        for (String s: detail_analysis) {
            final_message = final_message + s + "\n";
        }
        System.out.println(final_message);
    }
}
