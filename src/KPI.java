import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class KPI {
    public String current = null;
    public String past = null;
    public Double change = null;
    public String name = null;

    public KPI(String cur, String pst, String nm){
        current = cur;
        past = pst;
        Double pstD = Double.parseDouble(pst.replaceAll("(,*\\s*)*", ""));
        change = Double.parseDouble(cur.replaceAll("(,*\\s*)*", ""))/pstD - 1;
        name = nm;
    }

    public String realiseSentence(){
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        NLGFactory nlgFactory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);

        // words planning:
        Random r = new Random();
        int num_inc = r.nextInt(Main.vbs_increase.length);
        int num_dec = r.nextInt(Main.vbs_decrease.length);
        int num_vb_mod = r.nextInt(Main.vbs_modifier.length);

        int num;
        String vbs_kpi;
        if (change > 0){
            num = num_inc;
            vbs_kpi = Main.vbs_increase[num];
        }
        else {
            num = num_dec;
            vbs_kpi = Main.vbs_decrease[num];
        }
        String time = "last week";

        //Create Clause p:
        SPhraseSpec p = nlgFactory.createClause();
        NPPhraseSpec n = nlgFactory.createNounPhrase(name);
        VPPhraseSpec vb = nlgFactory.createVerbPhrase(vbs_kpi);
        if (change > 0.5) {
            vb.addPreModifier(Main.vbs_modifier[num_vb_mod]);
        }
        vb.addComplement("by");
        PPPhraseSpec pp_time = nlgFactory.createPrepositionPhrase("during " + time);
        PPPhraseSpec pp_value = nlgFactory.createPrepositionPhrase(" from " + past + " to " + current);
        p.setSubject(n);
        p.setVerb(vb);
        p.setObject(BigDecimal.valueOf(change*100)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue() + " percent");
        p.addComplement(pp_time);
        p.addComplement(pp_value);

        p.setFeature(Feature.TENSE, Tense.PAST);
        p.setFeature(Feature.PERFECT, true);
        return realiser.realiseSentence(p);
    }
}