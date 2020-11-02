package com.teameleven.reverselecture2.server.api;

import com.teameleven.reverselecture2.server.entities.Entry;
import com.teameleven.reverselecture2.server.repository.ServiceInterface;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Controller {
    private final ServiceInterface service;

    /**
     * @param service a InMemoryCache instance
     */
    public Controller(ServiceInterface service) {
        this.service = service;
    }

    @GET
    @Path("{key}")
    @Timed(name = "get-entry")
    public Entry get(@PathParam("key") LongParam key) {
        return service.get(key.get());
    }

    @GET
    @Timed(name = "view-all-entries")
    public List<Entry> getAll() {
        return service.getAll();
    }

    @PUT
    @Path("{key}/{value}")
    @Timed(name = "add-entry")
    public Response put(@PathParam("key") LongParam key,
                        @PathParam("value") String value) {
        Entry entry = new Entry();
        entry.setKey(key.get());
        entry.setValue(value);

        service.save(entry);

        return Response.status(200).build();
    }
}
