package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.sysoption.FunStationInfoVideoMapper;
import server.db.primary.model.sysoption.StationInfoVideo;
import server.service.FunStationInfoVideoService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class FunStationInfoVideoImp implements FunStationInfoVideoService {
    @Autowired
    FunStationInfoVideoMapper funStationInfoVideoMapper;

    @Override
    public List<StationInfoVideo> selectVideoIpByStationId(Long stationId) {
        Example example = new Example(StationInfoVideo.class);
        example.selectProperties("id","stationId","vIpaddr");
        example.createCriteria().andEqualTo("stationId", stationId);
        return funStationInfoVideoMapper.selectByExample(example);
    }

    @Override
    public boolean createVideoIp(StationInfoVideo stationInfoVideo) {
        stationInfoVideo.setId(null);
        return funStationInfoVideoMapper.insertSelective(stationInfoVideo) > 0;
    }

    @Override
    public boolean deleteVideoIpByIdList(List<Long> idList) {
        Example example = new Example(StationInfoVideo.class);
        example.createCriteria()
                .andIn("id", idList);
        return funStationInfoVideoMapper.deleteByExample(example) > 0;
    }
}
