package it.barusu.tutorial.actuator;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * Http logging response wrapper.
 *
 * @see org.springframework.web.util.ContentCachingResponseWrapper
 */
public class HttpLoggingResponse extends HttpServletResponseWrapper {

    private final FastByteArrayOutputStream content = new FastByteArrayOutputStream(1024);

    @Nullable
    private ServletOutputStream outputStream;

    @Nullable
    private PrintWriter writer;

    @Nullable
    private Integer contentLength;

    public HttpLoggingResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int sc) throws IOException {
        copyBodyToResponse(false);
        try {
            super.sendError(sc);
        } catch (IllegalStateException ex) {
            // Possibly on Tomcat when called too late: fall back to silent setStatus
            super.setStatus(sc);
        }
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        copyBodyToResponse(false);
        try {
            super.sendError(sc, msg);
        } catch (IllegalStateException ex) {
            // Possibly on Tomcat when called too late: fall back to silent setStatus
            super.setStatus(sc);
        }
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        copyBodyToResponse(false);
        super.sendRedirect(location);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.outputStream == null) {
            this.outputStream = new HttpLoggingOutputStream(getResponse().getOutputStream());
        }
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.writer == null) {
            String characterEncoding = getCharacterEncoding();
            this.writer = (characterEncoding != null ? new HttpLoggingPrintWriter(characterEncoding) :
                    new HttpLoggingPrintWriter(WebUtils.DEFAULT_CHARACTER_ENCODING));
        }
        return this.writer;
    }

    @Override
    public void flushBuffer() {
        // do not flush the underlying response as the content as not been copied to it yet
    }

    @Override
    public void setContentLength(int len) {
        if (len > this.content.size()) {
            this.content.resize(len);
        }
        this.contentLength = len;
    }

    // Overrides Servlet 3.1 setContentLengthLong(long) at runtime
    @Override
    public void setContentLengthLong(long len) {
        if (len > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Content-Length exceeds ContentCachingResponseWrapper's maximum (" +
                    Integer.MAX_VALUE + "): " + len);
        }
        int lenInt = (int) len;
        if (lenInt > this.content.size()) {
            this.content.resize(lenInt);
        }
        this.contentLength = lenInt;
    }

    @Override
    public void setBufferSize(int size) {
        if (size > this.content.size()) {
            this.content.resize(size);
        }
    }

    @Override
    public void resetBuffer() {
        this.content.reset();
    }

    @Override
    public void reset() {
        super.reset();
        this.content.reset();
    }

    /**
     * Return the status code as specified on the response.
     *
     * @deprecated as of 5.2 in favor of {@link HttpServletResponse#getStatus()}
     */
    @Deprecated
    public int getStatusCode() {
        return getStatus();
    }

    /**
     * Return the cached response content as a byte array.
     */
    public byte[] getContentAsByteArray() {
        return this.content.toByteArray();
    }

    /**
     * Return an {@link InputStream} to the cached content.
     *
     * @since 4.2
     */
    public InputStream getContentInputStream() {
        return this.content.getInputStream();
    }

    /**
     * Return the current size of the cached content.
     *
     * @since 4.2
     */
    public int getContentSize() {
        return this.content.size();
    }

    /**
     * Copy the complete cached body content to the response.
     *
     * @since 4.2
     */
    public void copyBodyToResponse() throws IOException {
        copyBodyToResponse(true);
    }

    /**
     * Copy the cached body content to the response.
     *
     * @param complete whether to set a corresponding content length
     *                 for the complete cached body content
     * @since 4.2
     */
    protected void copyBodyToResponse(boolean complete) throws IOException {
        if (this.content.size() > 0) {
            HttpServletResponse rawResponse = (HttpServletResponse) getResponse();
            if ((complete || this.contentLength != null) && !rawResponse.isCommitted()) {
                rawResponse.setContentLength(complete ? this.content.size() : this.contentLength);
                this.contentLength = null;
            }
            this.content.writeTo(rawResponse.getOutputStream());
            this.content.reset();
            if (complete) {
                super.flushBuffer();
            }
        }
    }


    private class HttpLoggingOutputStream extends ServletOutputStream {

        private final ServletOutputStream os;

        HttpLoggingOutputStream(ServletOutputStream os) {
            this.os = os;
        }

        @Override
        public void write(int b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(@NonNull byte[] b, int off, int len) throws IOException {
            content.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return this.os.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.os.setWriteListener(writeListener);
        }
    }


    private class HttpLoggingPrintWriter extends PrintWriter {

        HttpLoggingPrintWriter(String characterEncoding) throws UnsupportedEncodingException {
            super(new OutputStreamWriter(content, characterEncoding));
        }

        @Override
        public void write(@NonNull char[] buf, int off, int len) {
            super.write(buf, off, len);
            super.flush();
        }

        @Override
        public void write(@NonNull String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
        }

        @Override
        public void write(int c) {
            super.write(c);
            super.flush();
        }
    }
}
