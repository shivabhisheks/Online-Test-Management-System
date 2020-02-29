package presentation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bean.Question;
import bean.Test;
import bean.User;
import service.UserService;
import service.ValueNullException;

public class Menu {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public static void main(String[] args) {
		int choice;
		User admin = new User();
		admin.setUserId((long)50);
		admin.setAdmin(true);
		admin.setUserName("Shiv");
		admin.setUserPassword("Abcdef1@");
		UserService.addUser(admin);
		Menu menu = new Menu();
		try {
			while(true) {
				System.out.println("1.Login\n2.Signup");
				System.out.println("Enter 1 or 2");
			choice = Integer.parseInt(br.readLine());
			switch(choice)
			{
			case 1 : menu.login();
				break;
			case 2 : if(menu.signUp()) {
						System.out.println("Do you want to login now?");
						if(br.readLine().equalsIgnoreCase("yes"))
						menu.login();
					}
					 break;
			default : System.out.println("try Again.");
			}
			System.out.println("Do you want to continue ???");
			if(br.readLine().equalsIgnoreCase("yes"))
				continue;
			else
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch(NumberFormatException e1)
		{
			System.out.println("Please enter only in Integer value");
		}
	}
	private boolean signUp() {
		User user = new User();
		Set<User> users = UserService.getUsers();
		Iterator<User> usit = users.iterator();
		Pattern charCapital = Pattern.compile("^[A-Z]+");
		Pattern pwd = Pattern.compile("(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[\\d])(?=.*?[#\\?!@\\$%\\^&\\*-]).{8,}");
		Long userId = null;
		String userName="";
		String userPassword = "";
		System.out.println("Enter User Id");
		boolean flag = false;
		try {
			String Id = br.readLine();
			if(Id.isEmpty()) {
				throw new ValueNullException("Please write something.");
			}
			userId = Long.parseLong(Id);
			while(usit.hasNext())
			{
				User nextUser = usit.next();
				if(nextUser.getUserId()==userId)
				{
					System.out.println("Id already exist.");
					System.exit(0);
				}
			}
			System.out.println("Enter Your Name with first character Capital");
			Matcher m = charCapital.matcher(userName = br.readLine());
			if(m.find()!=true)
			{
				System.out.println("Write first char as capital.Try Again");
				System.exit(0);
			}
			System.out.println("Enter Your Password.It must contain atleast 1 Uppercase char, 1 Lowercase,1 digit and a special character");
			Matcher pm  = pwd.matcher(userPassword = br.readLine());
			if(pm.find()!=true)
			{
				System.out.println("Invalid password.Try Again");
				System.exit(0);
			}
			else {
				user.setUserId(userId);
				user.setUserName(userName);
				user.setUserPassword(userPassword);
				user.setAdmin(false);
				user.setUserTest(null);
				UserService.addUser(user);
				System.out.println("Successfully Signed Up. You Can now Log In");
				flag = true;
			}
		}catch(ValueNullException n)
		{
			n.printStackTrace();
			System.out.println("Please Enter something in Id");
			System.exit(0);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	private void login() {
		boolean userExists = false;
		UserService us = new UserService();
		System.out.print("User Id : ");
		try {
			Long id = Long.parseLong(br.readLine());
			System.out.print("Enter Your Password : ");
			String pwd = br.readLine();
			Set<User> users = UserService.getUsers();
			Iterator<User> it = users.iterator();
			while(it.hasNext())
			{
				User nextUser = it.next();
				if(nextUser.getUserId()==id)
				{
					userExists = true;
					if(nextUser.getUserPassword().equals(pwd))
					{
						System.out.print("Welcome "+nextUser.getUserName().toUpperCase()+" ");
						if(nextUser.isAdmin())
						{
							BigInteger testId=null;
							Question question = null;
							boolean adminFlag = true;
							System.out.println("You are an admin");
							while(adminFlag) {
							System.out.println("1.Add a Test\n2.Update an Existing Test\n3.Delete an Existing Test\n4.Assign a Test to a User\n5.Add Questions to a test\n6.Update a Question of a Test\n7.Delete a Question from a Test");
							int choice = Integer.parseInt(br.readLine());
							switch(choice)
							{
							case 1 : us.addTest(new Test());
								     break;
							case 2 : System.out.println("Enter Test Id you want to update");
									 testId = new BigInteger(br.readLine());
									 System.out.println("Enter new data");
									 Test test = us.addTest(new Test());
									 us.updateTest(testId, new Test());
									 System.out.println("Done Updating");
									 break;
							case 3 : System.out.println("Enter Test Id you want to delete");
							 		 testId = new BigInteger(br.readLine());
							 		 us.deleteTest(testId);
							 		 break;
							case 4 : System.out.println("Enter userId to whome you want to assign the test");
									 Long userId = Long.parseLong(br.readLine());
									 System.out.println("Enter test id of the test you want to assign");
									 BigInteger testId2 = new BigInteger(br.readLine());
									 if(us.assignTest(userId, testId2))
										 System.out.println("Assigned");
									 else
										 System.out.println("Either User or test doesn't exist");
									 break;
							case 5 : System.out.println("Enter the test id you want to add questions");
							         testId = new BigInteger(br.readLine());
								     us.addQuestions(testId, new Question());
								     break;
							case 6 : System.out.println("Enter the test id you want to update question");
					         		 testId = new BigInteger(br.readLine());
					         		 question = us.addQuestions(testId, new Question());
					         		 us.updateQuestions(testId, question);
					         		 break;
							case 7 : System.out.println("Enter the test id you want to delete question");
			         		 		 testId = new BigInteger(br.readLine());
							         us.deleteQuestions(testId,new Question());
							         break;
							default :
									  System.out.println("Invalid Choice");
							}
							System.out.println("Do you want to do something else? Say no to log out.");
							if((br.readLine()).equalsIgnoreCase("yes"))
								continue;
							else
								adminFlag = false;
						}
						}
						else
						{
							Menu menu = new Menu();
							System.out.println("Do you want to take your test?");
							try {
							if(br.readLine().equalsIgnoreCase("yes"))
							menu.takeTest(id);
							System.out.println("Do you want to see your result?");
							if((br.readLine()).equalsIgnoreCase("yes"))
							{
								UserService dao = new UserService();
								System.out.println("Your score : "+dao.getResult(nextUser.getUserTest()));
							}
							}catch(NullPointerException np)
							{
								System.out.println("You haven't been assigned any test yet.");
							}
							
						}
						
					}
					else
						System.out.println("Enter Correct Password;");
				}	
			}
			if(userExists == false)
				System.out.println("This user does not exist");
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 void takeTest(Long id) {
				Set<User> users = UserService.getUsers();
				Iterator<User> usit = users.iterator();
				try {
				while(usit.hasNext())
				{
					User nextUser = usit.next();
					if(nextUser.getUserId() == id)
					{
						Test userTest = nextUser.getUserTest();
						Set<Question> testQuestions = userTest.getTestQuestions();
						Iterator<Question> tqit = testQuestions.iterator();
						
						
							while(tqit.hasNext())
						{
							Question nextQuestion = tqit.next();
							System.out.println("Q "+nextQuestion.getQuestionId()+"."+nextQuestion.getQuestionTitle());
							for(int i=0;i<4;i++)
							{
								System.out.println((i+1)+"."+nextQuestion.getQuestionOptions()[i]);
							}
							System.out.println("Enter Your Answer 1-4");
							
								int ans = Integer.parseInt(br.readLine());
								nextQuestion.setChosenAnswer(ans);
								if(nextQuestion.getChosenAnswer()==nextQuestion.getQuestionAnswer())
								{
									nextQuestion.setMarksScored(nextQuestion.getQuestionMarks());
								}
								else
									nextQuestion.setMarksScored(new BigDecimal(0));
							} 
						}
					}
				}catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}
