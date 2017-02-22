package realizer.com.mysurvey.surveyreport.model;

/**
 * Created by Win on 27/12/2016.
 */
public class FilteredReportModel
{
    public String QuestionNo="";
    public String OptionNo="";
    public String OptionText="";
    public String numberofAnswers="";



    public String getQuestionNo() {
        return QuestionNo;
    }

    public void setQuestionNo(String questionNo) {
        QuestionNo = questionNo;
    }

    public String getOptionNo() {
        return OptionNo;
    }

    public void setOptionNo(String optionNo) {
        OptionNo = optionNo;
    }

    public String getOptionText() {
        return OptionText;
    }

    public void setOptionText(String optionText) {
        OptionText = optionText;
    }

    public String getNumberofAnswers() {
        return numberofAnswers;
    }

    public void setNumberofAnswers(String numberofAnswers) {
        this.numberofAnswers = numberofAnswers;
    }
}
