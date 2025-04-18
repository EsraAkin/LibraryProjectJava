package com.library.demo.payload.messages;

public class SuccessMessages {

  private SuccessMessages() {
  }


  //user
  public static final String USER_FOUND = "User is Found Successfully";
  public static final String USER_CREATE = "User is Saved";
  public static final String USER_DELETE = "User is deleted successfully";
  public static final String USER_UPDATE = "your information has been updated successfully";
  public static final String USER_UPDATE_MESSAGE = "User is Updated Successfully";
  public static final String PASSWORD_CHANGED_RESPONSE_MESSAGE = "Password Successfully Changed" ;

  //Book
  public static final String BOOK_SAVE = "Book is Saved";
  public static final String BOOK_DELETE = "Book is Deleted Successfully";
  public static final String BOOK_FOUND = "Book is Found Successfully";
  public static final String BOOK_UPDATE = "Book information has been updated successfully";

  //Loan
  public static final String LOAN_CREATE = "Loan has been created successfully";

  //Publisher
  public static final String PUBLISHER_CREATE = "Publisher has been created successfully";
  public static final String PUBLISHER_FOUND = "Publisher is Found Successfully";

}
