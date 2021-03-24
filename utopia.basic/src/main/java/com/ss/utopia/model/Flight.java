package com.ss.utopia.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Flight {

	private Integer id;
	private Integer maxCapacity;
	private Route route;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Integer[] seats;
	
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
	 * @return the maxCapacity
	 */
	public Integer getMaxCapacity() {
		return maxCapacity;
	}
	/**
	 * @param maxCapacity the maxCapacity to set
	 */
	public void setMaxCapacity(Integer maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	/**
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}
	/**
	 * @param route the route to set
	 */
	public void setRoute(Route route) {
		this.route = route;
	}
	/**
	 * @return the startTime
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(LocalDateTime startTime) {
		if (endTime != null) {
			if (endTime.isAfter(startTime))
				this.startTime = startTime;
			else {
				this.startTime = endTime;
				endTime = startTime;
			}
			route.setDuration(Duration.between(this.startTime, endTime));
		} else
			this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(LocalDateTime endTime) {
		if (startTime != null) {
			if (endTime.isAfter(startTime))
				this.endTime = endTime;
			else {
				this.endTime = startTime;
				this.startTime = endTime;
			}
			route.setDuration(Duration.between(startTime, this.endTime));
		} else
			this.endTime = endTime;
	}
	/**
	 * @return the seats
	 */
	public Integer[] getSeats() {
		return seats;
	}
	/**
	 * @param seats the seats to set
	 */
	public void setSeats(Integer[] seats) {
		this.seats = seats;
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
		Flight other = (Flight) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
