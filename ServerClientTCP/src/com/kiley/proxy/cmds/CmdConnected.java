package com.kiley.proxy.cmds;

import java.io.Serializable;

public class CmdConnected implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7081176238637086480L;

	private boolean succeeded;

	public CmdConnected(boolean newSucceeded) {
		this.succeeded = newSucceeded;
	}

	public boolean didSucceed() {
		return this.succeeded;
	}
}
