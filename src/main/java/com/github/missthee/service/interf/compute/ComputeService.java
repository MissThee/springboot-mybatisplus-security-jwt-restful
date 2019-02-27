package server.service.interf.compute;

import server.db.primary.model.compute.Compute;

import java.util.List;

public interface ComputeService {
    List<Compute> selectGroupBy();

}
