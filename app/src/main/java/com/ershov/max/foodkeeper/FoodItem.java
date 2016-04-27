package com.ershov.max.foodkeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Max on 24.04.2016.
 */
public class FoodItem {
    private Date buyDate;
    private Date expireDate;
    private String name;
	public static String DATE_FORMAT = "dd-MM-yyyy";

    public FoodItem() {
        this.buyDate = new Date();
        this.expireDate = this.buyDate ;
        this.name = "Unknown";
    }
	
	public FoodItem(Date expDate, String name) {
        this.buyDate = new Date();
        this.expireDate = expDate;
        this.name = name;
    }
	
	public FoodItem(String expDate, String name) {
        this.buyDate = new Date();
		this.name = name;
		this.expireDate = stringToDate(expDate,DATE_FORMAT);   
    }

    public FoodItem(Date buyDate, Date expDate, String name) {
        this.buyDate = buyDate;
        this.expireDate = expDate;
        this.name = name;
    }

	public String addItemQuery() {
			String query = "INSERT INTO foodkeeper (name, buyDate, expireDate) VALUES ("
				+ "'" + this.name + "',"
				+ "'" + dateToString(this.buyDate,DATE_FORMAT) + "',"
				+ "'" + dateToString(this.expireDate,DATE_FORMAT) + "'"
				+")";
		return query;
	}

    public boolean isExpired() {
        Date currDate = new Date();
        return (currDate.after(this.expireDate));
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
	
	public static Date stringToDate(String dateString, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateToString(Date date, String dateFormatString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        String datetime = dateFormat.format(date);
        return datetime;
	}
}
