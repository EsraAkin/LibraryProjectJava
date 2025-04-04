package com.library.demo.payload.messages;

public class ErrorMessages {

  private ErrorMessages() {
  }


  //user
  public static final String NOT_HAVE_EXPECTED_ROLE_USER = "Error: User does not have expected role";
  public static final String NOT_FOUND_USER_MESSAGE = "Error: User not found with id %s";
  public static final String NOT_FOUND_USER_MESSAGE_USERNAME = "Error: User not found with username %s";
  public static final String NOT_PERMITTED_METHOD_MESSAGE = "You do not have any permission to do this operation";
  public static final String PASSWORD_SHOULD_NOT_MATCHED = "Your passwords are not matched" ;
  public static final String PASSWORD_SHOULD_NOT_MATCHED_HISTORY = "The new password cannot be the same as any of the last 3 passwords. Please choose a different password.";


  //unique properties
  public static final String ALREADY_REGISTER_MESSAGE_USERNAME = "Error: User with username %s is already registered";
  public static final String ALREADY_REGISTER_MESSAGE_SSN = "Error: User with ssn %s is already registered";
  public static final String ALREADY_REGISTER_MESSAGE_PHONE_NUMBER = "Error: User with phone number %s is already registered";
  public static final String ALREADY_REGISTER_MESSAGE_EMAIL = "Error: User with email %s is already registered";

  //user roles
  public static final String ROLE_NOT_FOUND = "There is no role like that, check the database";
  public static final String NOT_FOUND_USER_USER_ROLE_MESSAGE = "Error: User not found with user-role %s";


  //book
  public static final String BOOK_NOT_FOUND = "There is no book like that, check the database";
  public static final String ALREADY_BOOK_FOUND = "A book with the same ISBN already exists in the system.";
  //author
  public static final String AUTHOR_NOT_FOUND = "There is no author like that, check the database";

  //category
  public static final String CATEGORY_NOT_FOUND = "There is no category like that, check the database";

  //publisher
  public static final String PUBLISHER_NOT_FOUND = "There is no publisher like that, check the database";



}
