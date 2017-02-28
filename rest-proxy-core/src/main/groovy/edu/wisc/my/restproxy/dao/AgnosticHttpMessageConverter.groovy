package edu.wisc.my.restproxy.dao

import com.google.common.io.ByteStreams
import org.apache.commons.lang3.NotImplementedException
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractHttpMessageConverter
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

/**
 * This converter supports reading all content types into an {@link Object} by copying the byte stream.
 * It does not write any requests.
 *
 * @author Craig Knuth
 */
class AgnosticHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    private static final Class<Object> SUPPORTED_CLASS = Object.class

    AgnosticHttpMessageConverter() {
        super(MediaType.ALL)
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return SUPPORTED_CLASS == clazz
    }

    @Override
    boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) {
        // input stream will close by the time writeTo() is called, so we must extract the data here
        InputStream input = inputMessage.getBody()
        byte[] data = ByteStreams.toByteArray(input)
        return new StreamingResponseBody() {
            @Override
            void writeTo(OutputStream output) throws IOException {
                output.write(data)
            }
        }
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) {
        throw new NotImplementedException("AgnosticHttpMessageConverter can only read http messages")
    }

}
