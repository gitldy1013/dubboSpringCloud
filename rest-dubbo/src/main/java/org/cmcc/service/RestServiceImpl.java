package org.cmcc.service;

import org.apache.dubbo.config.annotation.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author cmcc
 * @date 2020/6/26
 */
@Service(protocol = "rest")
@Path("/")
public class RestServiceImpl implements RestService {
    @Override
    @Path("test/{p}")
    @GET
    public String test(@PathParam("p") String param) {
        return "rest service: " + param;
    }
}
