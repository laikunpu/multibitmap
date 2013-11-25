package com.lkp.multibitmaptest;

import java.util.List;

public class Bean_common_socketToHttp {
	private boolean isUseSocket;
	private List<Bean_socket_requestProperty> properties;

	public boolean isUseSocket() {
		return isUseSocket;
	}

	public void setUseSocket(boolean isUseSocket) {
		this.isUseSocket = isUseSocket;
	}

	public List<Bean_socket_requestProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<Bean_socket_requestProperty> properties) {
		this.properties = properties;
	}

	public Bean_common_socketToHttp(boolean isUseSocket, List<Bean_socket_requestProperty> properties) {
		this.isUseSocket = isUseSocket;
		this.properties = properties;
	}

	public Bean_common_socketToHttp() {
	}

}
