package server.service;

import server.db.primary.model.sysoption.StationInfoVideo;

import java.util.List;

public interface FunStationInfoVideoService {

    List<StationInfoVideo> selectVideoIpByStationId(Long stationId);

    boolean createVideoIp(StationInfoVideo stationInfoVideo);

    boolean deleteVideoIpByIdList(List<Long> idList);
}
