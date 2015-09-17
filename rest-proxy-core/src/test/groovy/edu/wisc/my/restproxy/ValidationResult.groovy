/**
 *
 */
package edu.wisc.my.restproxy;

/**
 * Just an example of an object that could be returned by rest api.
 * @author Collin Cudd
 */
public class ValidationResult {

  boolean success;
  String message;
  public ValidationResult(){}
  /**
   * @param success
   * @param message
   */
  public ValidationResult(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

}
