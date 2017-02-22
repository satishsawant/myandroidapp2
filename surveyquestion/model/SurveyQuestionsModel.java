package realizer.com.mysurvey.surveyquestion.model;

/**
 * Created by Win on 21/12/2016.
 */
public class SurveyQuestionsModel
{
    public String surveyId;
    public String questionId;
    public String questionText;
    public String queSerialNo;
    public String answerOptionNo;
    public String optionList;
    public String insertedAnswer;

    public String getInsertedAnswer() {
        return insertedAnswer;
    }

    public void setInsertedAnswer(String insertedAnswer) {
        this.insertedAnswer = insertedAnswer;
    }

    public String getOptionList() {
        return optionList;
    }

    public void setOptionList(String optionList) {
        this.optionList = optionList;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQueSerialNo() {
        return queSerialNo;
    }

    public void setQueSerialNo(String queSerialNo) {
        this.queSerialNo = queSerialNo;
    }

    public String getAnswerOptionNo() {
        return answerOptionNo;
    }

    public void setAnswerOptionNo(String answerOptionNo) {
        this.answerOptionNo = answerOptionNo;
    }
}
