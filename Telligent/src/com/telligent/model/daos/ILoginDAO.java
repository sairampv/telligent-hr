package com.telligent.model.daos;

import com.telligent.model.dtos.User;


public interface ILoginDAO{
	public User authenticateUser(String userName);
}
