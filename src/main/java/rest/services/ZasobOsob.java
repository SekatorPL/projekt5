package rest.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Osoba;

@Path("people")
@Stateless
public class ZasobOsob
{
	@PersistenceContext
	EntityManager em;
	
	@GET
	@Path("page={page}")
	@Produces(MediaType.APPLICATION_JSON)
	public List <Osoba> wyswietl(@PathParam("page") int strona)
	{
		return em.createNamedQuery("wszystkie_osoby", Osoba.class)
				.setFirstResult((strona - 1) * 3)
				.setMaxResults(3)
				.getResultList();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dodaj(Osoba osoba)
	{
		em.persist(osoba);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response usun(@PathParam("id") int id)
	{	
		Osoba wynik = em.createNamedQuery("osoba_o_id", Osoba.class)
				.setParameter("osobaId", id)
				.getSingleResult();
		
		if(wynik == null) return Response.status(404).build();
		
		em.remove(wynik);
		return Response.ok().build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response aktualizuj(@PathParam("id") int id, Osoba osoba)
	{	
		Osoba wynik = em.createNamedQuery("osoba_o_id", Osoba.class)
				.setParameter("osobaId", id)
				.getSingleResult();
		
		if(wynik == null)
		{	
			return Response.status(404).build();
		}
		
		wynik.setFirstName(osoba.getFirstName());
		wynik.setLastName(osoba.getLastName());
		wynik.setGender(osoba.getGender());
		wynik.setBirthday(osoba.getBirthday());
		wynik.setEmail(osoba.getEmail());
		wynik.setAge(osoba.getAge());
		
		em.persist(wynik);
		return Response.ok().build();
	}
}