package com.kiley.proxy.cmds;

import java.io.Serializable;

public class CmdMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1802403226455709474L;
	
	private String msg;

	public CmdMessage(String newMsg) {
		this.msg = newMsg;
	}

	public String getMsg() {
		return this.msg;
	}
}
