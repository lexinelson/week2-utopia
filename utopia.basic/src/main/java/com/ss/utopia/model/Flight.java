package com.ss.utopia.model;

import java.time.LocalDateTime;

public class Flight {

	private Airplane plane;
	private Route route;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private SeatSection first, business, coach;
	/**
	 * @return the plane
	 */
	public Airplane getPlane() {
		return plane;
	}
	/**
	 * @param plane the plane to set
	 */
	public void setPlane(Airplane plane) {
		this.plane = plane;
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
		this.endTime = endTime;
	}
	/**
	 * @return the first
	 */
	public SeatSection getFirst() {
		return first;
	}
	/**
	 * @param first the first to set
	 */
	public void setFirst(SeatSection first) {
		this.first = first;
	}
	/**
	 * @return the business
	 */
	public SeatSection getBusiness() {
		return business;
	}
	/**
	 * @param business the business to set
	 */
	public void setBusiness(SeatSection business) {
		this.business = business;
	}
	/**
	 * @return the coach
	 */
	public SeatSection getCoach() {
		return coach;
	}
	/**
	 * @param coach the coach to set
	 */
	public void setCoach(SeatSection coach) {
		this.coach = coach;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plane == null) ? 0 : plane.hashCode());
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		if (plane == null) {
			if (other.plane != null)
				return false;
		} else if (!plane.equals(other.plane))
			return false;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
	
	

}
