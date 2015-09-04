/**
 * 
 */
package edu.wisc.my.restproxy;

/**
 * Bean to represent a request body.
 * 
 * @author Nicholas Blair
 */
public class RequestBody {

  private byte[] body;
  private String contentType;
  /**
   * @return the body
   */
  public byte[] getBody() {
    return body;
  }
  /**
   * @param body the body to set
   */
  public RequestBody setBody(byte[] body) {
    this.body = body;
    return this;
  }
  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }
  /**
   * @param contentType the contentType to set
   */
  public RequestBody setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "RequestBody [body=" + body + ", contentType=" + contentType + "]";
  }
}
