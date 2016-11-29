package com.kiley.proxy.cmds;

import java.io.Serializable;

public class CmdConnect implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4378422954319476591L;

	private String targetIp = "";
	
	public CmdConnect(String newTargetIp) {
		this.targetIp = newTargetIp;
	}
	
	public String getTargetIp() {
		return this.targetIp;
	}
}
