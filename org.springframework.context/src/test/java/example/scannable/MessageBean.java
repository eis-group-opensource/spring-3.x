/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package example.scannable;

/**
 * @author Mark Fisher
 */
@CustomComponent
public class MessageBean {

	private String message;
	
	public MessageBean() {
		this.message = "DEFAULT MESSAGE";
	}

	public MessageBean(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
