package edu.wisc.my.restproxy.dao

import org.apache.commons.lang3.NotImplementedException
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.mock.http.MockHttpOutputMessage
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

import static org.mockito.Matchers.eq
import static org.mockito.Mockito.mock

/**
 * Tests the functionality of {@link AgnosticHttpMessageConverter}
 *
 * @author Craig Knuth
 */
@RunWith(MockitoJUnitRunner.class)
class AgnosticHttpMessageConverterTest {

    AgnosticHttpMessageConverter converter = new AgnosticHttpMessageConverter()

    @Test
    void agnosticConverter_canReadAllContentTypes() {
        assert converter.canRead(Object.class, MediaType.ALL)
    }

    @Test
    void agnosticConverter_cannotWriteAnyContentType() {
        assert !converter.canWrite(Object.class, MediaType.APPLICATION_JSON)
        assert !converter.canWrite(Object.class, MediaType.TEXT_HTML)
        assert !converter.canWrite(Object.class, MediaType.TEXT_PLAIN)
        assert !converter.canWrite(Object.class, MediaType.TEXT_XML)
        assert !converter.canWrite(Object.class, MediaType.ALL)
    }

    @Test
    void agnosticConverter_readsIntoStreamingResponseBody() {
        byte[] data = "just a test".getBytes()
        MockHttpInputMessage input = new MockHttpInputMessage(data)
        Object result = converter.read(Object.class, input)
        assert result instanceof StreamingResponseBody

        OutputStream output = mock(OutputStream)
        ((StreamingResponseBody)result).writeTo(output)
        Mockito.verify(output).write(eq(data))
    }

    @Test(expected = NotImplementedException)
    void agnosticConverter_doesNotWrite() {
        Object o = new Object()
        HttpOutputMessage out = new MockHttpOutputMessage()
        converter.writeInternal(o, out)
    }

}
