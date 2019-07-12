package com.sunrun.setbacks.rs;

import com.sunrun.setbacks.model.Site;

import javax.inject.Inject;
import javax.vecmath.Point2f;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/setbacks")
public class SetbacksResource {

    @Inject
    SetbacksService service;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<List<Point2f>>> getSetbacks(final SetbackRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }

        // = TODO: Maybe validate the layout plane input.
        final Site site = SiteModelFactory.getInstance().buildSiteModel(request.getLayoutPlaneInput());
        return service.getSetbacks(site, request.getRuleIds(), request.getGlobalOffsetMapping());
    }
}
