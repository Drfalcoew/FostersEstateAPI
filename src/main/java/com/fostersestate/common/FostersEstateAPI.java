package com.fostersestate.common;

import io.quarkus.logging.Log;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class FostersEstateAPI extends Application {
    public FostersEstateAPI() {
        Log.debug("FostersEstateAPI started");
    }
}
