package se.yrgo.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_CALL")
public class Call {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private Date timeStamp;

	private String notes;


	public Call(String notes){
		// this defaults to a timestamp of "now"
		this (new java.util.Date(), notes);
	}

	public Call(Date timeStamp, String notes){
		// this defaults to a timestamp of "now"
		this.timeStamp = timeStamp;
		this.notes = notes;
	}


	public String toString()	{
		return this.timeStamp + " : " + this.notes;
	}

	public Date getTimeAndDate() {
		return timeStamp;
	}

	public void setTimeAndDate(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Call() {}
}
