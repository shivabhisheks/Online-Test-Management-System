package service;


import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import bean.Question;
import bean.Test;

public class TestService implements TestInterface {

	@Override
	public
	BigDecimal calculateTotalMarks()
	{
		Test test = new Test();
		Set<Question> testQuestions = test.getTestQuestions();
		Iterator<Question> testQuestionIterator = testQuestions.iterator();
		BigDecimal testTotalMarks = new BigDecimal(0);
		while(testQuestionIterator.hasNext())
		{
			Question nextQuestion = testQuestionIterator.next();
			testTotalMarks = testTotalMarks.add(nextQuestion.getMarksScored());
			test.setTestTotalMarks(testTotalMarks);
		}
		return test.getTestTotalMarks();
	}

}
