package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;
import com.example.models.LoginDTO;  // Asegúrese de tener esta clase definida
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONObject;

@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

    @PersistenceContext(unitName = "JPA-persistenceU")
    EntityManager entityManager;
    
    @PostConstruct
    public void init() {
        try {
            entityManager = PersistenceManager.getInstance()
                                              .getEntityManagerFactory()
                                              .createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Query q = entityManager.createQuery("select u from Competitor u order by u.surname ASC");
        List<Competitor> competitors = q.getResultList();
        return Response.status(200)
                       .header("Access-Control-Allow-Origin", "*")
                       .entity(competitors)
                       .build();
    }
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompetitor(CompetitorDTO competitor) {
        JSONObject rta = new JSONObject();
        Competitor competitorTmp = new Competitor();
        competitorTmp.setAddress(competitor.getAddress());
        competitorTmp.setAge(competitor.getAge());
        competitorTmp.setCellphone(competitor.getCellphone());
        competitorTmp.setCity(competitor.getCity());
        competitorTmp.setCountry(competitor.getCountry());
        competitorTmp.setName(competitor.getName());
        competitorTmp.setSurname(competitor.getSurname());
        competitorTmp.setTelephone(competitor.getTelephone());
        // Se asigna la contraseña proveniente del DTO
        competitorTmp.setPassword(competitor.getPassword());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(competitorTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(competitorTmp);
            rta.put("competitor_id", competitorTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            competitorTmp = null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
        return Response.status(200)
                       .header("Access-Control-Allow-Origin", "*")
                       .entity(rta)
                       .build();
    }
    
    // Servicio de log-in: recibe correo (almacenado en address) y contraseña.
    // Si las credenciales son correctas, devuelve el Competitor;
    // de lo contrario, lanza NotAuthorizedException.
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {
        Query query = entityManager.createQuery(
            "SELECT c FROM Competitor c WHERE c.address = :email AND c.password = :password"
        );
        query.setParameter("email", loginDTO.getEmail());
        query.setParameter("password", loginDTO.getPassword());
        
        Competitor competitor;
        try {
            competitor = (Competitor) query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotAuthorizedException("Credenciales inválidas");
        }
        return Response.status(200)
                       .header("Access-Control-Allow-Origin", "*")
                       .entity(competitor)
                       .build();
    }
}
