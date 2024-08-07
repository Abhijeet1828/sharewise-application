package com.custom.sharewise.constants;

/**
 * Enum for storing the success HTTP codes and messages that will be used
 * in this application.
 * 
 * @author Abhijeet
 *
 */
public enum SuccessConstants {

	USER_SIGN_UP(2000, "User signed up successfully"),
	USER_LOGIN(2001, "User logged in successfully"),
	UPDATE_USER(2002, "User details updated successfully"),
	UPDATE_PASSWORD(2003, "Password updated successfully"),
	CREATE_GROUP(2004, "Group created successfully"),
	UPDATE_GROUP(2005, "Group details updated successfully"),
	DELETE_GROUP(2006, "Group deleted successfully"),
	ADD_MEMBER_TO_GROUP(2007, "User added to the group successfully"),
	REMOVE_MEMBER_FROM_GROUP(2008, "User removed from the group successfully"),
	ADD_GROUP_EXPENSE(2009, "Group expense added successfully"),
	ADD_USER_TRANSACTION(2010, "User transaction added successfully"),
	UPDATE_GROUP_EXPENSE(2011, "Group expense updated successfully"),
	DELETE_GROUP_EXPENSE(2012, "Group expense deleted successfully"),
	RESTORE_GROUP_EXPENSE(2013, "Group expense restored successfully"),
	UPDATE_USER_TRANSACTION(2014, "User transaction updated successfully"),
	DELETE_USER_TRANSACTION(2015, "User transaction deleted successfully"),
	RESTORE_USER_TRANSACTION(2016, "User transaction restored successfully"),
	NO_DEBTS_TO_SIMPLIFY(2017, "No debts to simplify in the group"),
	SIMPLIFY_PAYMENTS(2018, "Payments simplified successfully"),
	FETCH_GROUP_MEMBERS(2019, "Group members fetched successfully"),
	FETCH_PAYMENT_SUMMARY(2020, "Group payment summary fetched successfully"),
	FETCH_GROUP_EXPENSE_SUMMARY(2021, "Group expense summary fetched successfully"),
	FETCH_TOTAL_GROUP_EXPENSE_SUMMARY(2022, "Total group expense summary fetched successfully");

	private final int successCode;
	private final String successMsg;

	private SuccessConstants(int successCode, String successMsg) {
		this.successCode = successCode;
		this.successMsg = successMsg;
	}

	public int getSuccessCode() {
		return successCode;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	@Override
	public String toString() {
		return Integer.toString(successCode) + "-" + successMsg;
	}

}
