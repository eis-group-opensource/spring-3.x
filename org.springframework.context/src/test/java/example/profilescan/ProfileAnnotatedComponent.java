/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package example.profilescan;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(ProfileAnnotatedComponent.PROFILE_NAME)
@Component(ProfileAnnotatedComponent.BEAN_NAME)
public class ProfileAnnotatedComponent {

	public static final String BEAN_NAME = "profileAnnotatedComponent";
	public static final String PROFILE_NAME = "test";

}
