package com.johndoeo.bootvueserver.web;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 对body消息封装
 * @author:hongyan.du
 */
public class ReadBodyRequestWrapper extends HttpServletRequestWrapper {
	//body流缓冲
	private ServletInputStream is;
	
	public ReadBodyRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		ServletInputStream input = super.getInputStream();
		if(is==null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			byte[] buffer = new byte[1024];  
			int len;  
			while ((len = input.read(buffer)) > -1 ) {  
			    baos.write(buffer, 0, len);  
			}  
			baos.flush();
			ServletInputStream stream1 = new RequestCachingInputStream(new ByteArrayInputStream(baos.toByteArray()));
			ServletInputStream stream2 = new RequestCachingInputStream(new ByteArrayInputStream(baos.toByteArray()));
			this.is = stream2;
			return stream1;
		}
		return this.is;
	}
	
	
	private class RequestCachingInputStream extends ServletInputStream {
		
		private final InputStream is;

		private RequestCachingInputStream(InputStream is) {
			this.is = is;
		}

		@Override
		public int read() throws IOException {
			return is.read();
		}

		@Override
		public boolean isFinished() {
			return true;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {

		}
	}
}
