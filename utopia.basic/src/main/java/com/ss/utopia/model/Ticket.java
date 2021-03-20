package com.ss.utopia.model;

public class Ticket {
	
	private SeatSection ticket;
	private Passenger passenger;
	/**
	 * @return the ticket
	 */
	public SeatSection getTicket() {
		return ticket;
	}
	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(SeatSection ticket) {
		this.ticket = ticket;
	}
	/**
	 * @return the passenger
	 */
	public Passenger getPassenger() {
		return passenger;
	}
	/**
	 * @param passenger the passenger to set
	 */
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((passenger == null) ? 0 : passenger.hashCode());
		result = prime * result + ((ticket == null) ? 0 : ticket.hashCode());
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
		if (passenger == null) {
			if (other.passenger != null)
				return false;
		} else if (!passenger.equals(other.passenger))
			return false;
		if (ticket == null) {
			if (other.ticket != null)
				return false;
		} else if (!ticket.equals(other.ticket))
			return false;
		return true;
	}

}
