package com.library.managementprojectjava.payload.messages;

public class ErrorMessages {

  private ErrorMessages() {
  }


  //user
  public static final String NOT_FOUND_USER_MESSAGE = "Error: User not found with id %s";
  public static final String NOT_FOUND_USER_MESSAGE_EMAIL = "Error: User not found with email %s";
  public static final String NOT_PERMITTED_METHOD_MESSAGE = "You do not have any permission to do this operation";
  public static final String USER_HAS_LOANS = "User cannot be deleted because there are existing loan records.";


  //unique properties
  public static final String ALREADY_REGISTER_MESSAGE_PHONE_NUMBER = "Error: User with phone number %s is already registered";
  public static final String ALREADY_REGISTER_MESSAGE_EMAIL = "Error: User with email %s is already registered";

  //user roles
  public static final String ROLE_NOT_FOUND = "There is no role like that, check the database";

  //book
  public static final String BOOK_NOT_FOUND = "There is no book like that, check the database";
  public static final String ALREADY_BOOK_FOUND = "A book with the same ISBN already exists in the system.";
  public static final String BOOK_NOT_AVAILABLE_FOR_LOAN = "This book is not available for loan.";
  public static final String USER_HAS_OVERDUE_BOOKS = "You have overdue books. You cannot borrow another book.";
  public static final String USER_REACHED_LOAN_LIMIT = "You have reached your loan limit based on your score.";
  public static final String BOOK_CAN_NOT_DELETED = "This book is currently on loan and cannot be deleted.";



  //author
  public static final String AUTHOR_NOT_FOUND = "There is no author like that, check the database";

  //category
  public static final String CATEGORY_NOT_FOUND = "There is no category like that, check the database";
  public static final String CATEGORY_SEQUENCE_ALREADY_EXISTS_MESSAGE = "Error: Category Sequence is already used";
  public static final String CATEGORY_ALREADY_MESSAGE_NAME = "Error: Category name is already used";
  public static final String CATEGORY_NOT_PERMITTED_METHOD_MESSAGE = "You cannot update a built-in category. This category is protected by the system.";


  //publisher
  public static final String PUBLISHER_NOT_FOUND = "There is no publisher like that, check the database";


  //loan
  public static final String LOAN_NOT_FOUND = "There is no loan like that, check the database";
  public static final String LOAN_ALREADY_RETURNED = "This loan was already returned before.";


}
