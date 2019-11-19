package it.barusu.tutorial.actuator;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;


/**
 * Http logging request wrapper.
 *
 * @see org.springframework.web.util.ContentCachingRequestWrapper
 */
public class HttpLoggingRequest extends HttpServletRequestWrapper {

    private final ByteArrayOutputStream content;

    @Nullable
    private final Integer contentLimit;

    private final boolean limit;

    @Nullable
    private ServletInputStream inputStream;

    @Nullable
    private BufferedReader reader;


    public HttpLoggingRequest(HttpServletRequest request) {
        super(request);
        int contentLength = request.getContentLength();
        this.content = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        this.contentLimit = null;
        this.limit = false;
    }

    public HttpLoggingRequest(HttpServletRequest request, int contentLimit) {
        super(request);
        this.content = new ByteArrayOutputStream(contentLimit);
        this.contentLimit = contentLimit;
        this.limit = true;
    }

    public HttpLoggingRequest(HttpServletRequest request, int contentLimit, boolean limit) {
        super(request);
        this.content = new ByteArrayOutputStream(contentLimit);
        this.contentLimit = contentLimit;
        this.limit = limit;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new HttpLoggingInputStream(getRequest().getInputStream());
        }
        return this.inputStream;
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }

    @Override
    public String getParameter(String name) {
        if (this.content.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.content.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (this.content.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        if (this.content.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterValues(name);
    }


    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) &&
                HttpMethod.POST.matches(getMethod()));
    }

    private void writeRequestParametersToCachedContent() {
        try {
            if (this.content.size() == 0) {
                String requestEncoding = getCharacterEncoding();
                Map<String, String[]> form = super.getParameterMap();
                for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
                    String name = nameIterator.next();
                    List<String> values = Arrays.asList(form.get(name));
                    for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
                        String value = valueIterator.next();
                        this.content.write(URLEncoder.encode(name, requestEncoding).getBytes());
                        if (value != null) {
                            this.content.write('=');
                            this.content.write(URLEncoder.encode(value, requestEncoding).getBytes());
                            if (valueIterator.hasNext()) {
                                this.content.write('&');
                            }
                        }
                    }
                    if (nameIterator.hasNext()) {
                        this.content.write('&');
                    }
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
        }
    }

    public byte[] getContentAsByteArray() {
        return this.content.toByteArray();
    }

    protected void handleContentOverflow(int contentCacheLimit) throws IOException {
        content.write("...".getBytes(getCharacterEncoding()));
    }

    private class HttpLoggingInputStream extends ServletInputStream {

        private final ServletInputStream is;
        private boolean overflow = false;

        HttpLoggingInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            int ch = this.is.read();
            if (ch != -1 && !this.overflow) {
                if (contentLimit != null && content.size() == contentLimit && limit) {
                    this.overflow = true;
                    handleContentOverflow(contentLimit);
                } else {
                    content.write(ch);
                }
            }
            return ch;
        }

        @Override
        public int read(@NonNull byte[] b) throws IOException {
            int count = this.is.read(b);
            writeToCache(b, 0, count);
            return count;
        }

        private void writeToCache(final byte[] b, final int off, int count) throws IOException {
            if (!this.overflow && count > 0) {
                if (contentLimit != null && count + content.size() > contentLimit && limit) {
                    this.overflow = true;
                    content.write(b, off, contentLimit - content.size());
                    handleContentOverflow(contentLimit);
                    return;
                }
                content.write(b, off, count);
            }
        }

        @Override
        public int read(@NonNull final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.read(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        @Override
        public int readLine(final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.readLine(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        @Override
        public boolean isFinished() {
            return this.is.isFinished();
        }

        @Override
        public boolean isReady() {
            return this.is.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            this.is.setReadListener(readListener);
        }

    }

}
