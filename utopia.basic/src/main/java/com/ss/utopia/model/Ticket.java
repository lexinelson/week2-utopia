package com.ss.utopia.model;

import java.util.List;

public class Ticket {
	
	private Integer id;
	private String seatClass;
	private List<Flight> ticket;
	private String confirmation;
	private boolean isActive;
	
	/**
	 * @return the ticket
	 */
	public List<Flight> getTicket() {
		return ticket;
	}
	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(List<Flight> ticket) {
		this.ticket = ticket;
	}
	/**
	 * @return the confirmation
	 */
	public String getConfirmation() {
		return confirmation;
	}
	/**
	 * @param confirmation the confirmation to set
	 */
	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the seatClass
	 */
	public String getSeatClass() {
		return seatClass;
	}
	/**
	 * @param seatClass the seatClass to set
	 */
	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
