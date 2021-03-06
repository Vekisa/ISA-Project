package com.isap.ISAProject.controller.airline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.controller.user.HATEOASImplementorUsers;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.service.airline.TicketService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/tickets")
public class TicketController {

	@Autowired
	private TicketService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća karte.", notes = "Povratna vrednost servisa je lista karata koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Ticket>>> getAllTickets(Pageable pageable) {
		return new ResponseEntity<List<Resource<Ticket>>>(HATEOASImplementorAirline.createTicketsList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća kartu sa ID.", notes = "Povratna vrednost servisa je karta koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Ticket.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Karta sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Ticket>> getTicketWithId(@PathVariable("id") Long ticketId) {
		return new ResponseEntity<Resource<Ticket>>(HATEOASImplementorAirline.createTicket(service.findById(ticketId)), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše kartu.", notes = "Povratna vrednost servisa je sačuvana karta.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Ticket.class)
	})
	@PreAuthorize("hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> createNewTicket() {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(service.saveTicket(new Ticket())), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše kartu.", notes = "Briše kartu sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Karta sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('REGULAR_USER')")
	public ResponseEntity<?> deleteTicketWithId(@PathVariable("id") Long ticketId) {
		service.deleteTicket(ticketId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}/seat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Dodaje sedište na kraj reda traženog leta.", notes = "Dodaje jedno sedište na kraj reda koji se šalje kao parametar za let čiji se ID prosleđuje.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = FlightSeat.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Karta sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("(hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToSeat(#id)) OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<FlightSeat>> addSeatToTicketWithId(@PathVariable("id") Long ticketId, @RequestParam("seatId") Long seatId) {
		return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementorAirline.createFlightSeat(service.addSeatToTicket(seatId, ticketId)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/seats", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Ticket>> addMultipleSeatsToTicketWithId(@PathVariable("id") Long ticketId, @RequestBody List<Long> seats) {
		return new ResponseEntity<Resource<Ticket>>(HATEOASImplementorAirline.createTicket(service.addMultipleSeatsToTicket(ticketId, seats)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sedišta za datu kartu.", notes = "Povratna vrednost servisa je lista resursa sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje sedišta za datu kartu."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSeat>>> getSeatsForTicketWithId(@PathVariable("id") Long ticketId) {
		return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementorAirline.createFlightSeatsList(service.getSeatsOfTicket(ticketId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/flight", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća let za datu kartu.", notes = "Povratna vrednost servisa je resurs leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji let za datu kartu."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> getFlightForTicketWithId(@PathVariable("id") Long ticketId) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementorAirline.createFlight(service.getFlightOfTicket(ticketId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/destination", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Destinacija leta.", notes = "Povratna vrednost servisa loakacija.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Location.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<Resource<Location>> getDestinationForTicketWithId(@PathVariable("id") Long ticketId) {
		return new ResponseEntity<Resource<Location>>(new Resource<Location>(service.getDestinationOfTicket(ticketId)), HttpStatus.OK);
	}
}
