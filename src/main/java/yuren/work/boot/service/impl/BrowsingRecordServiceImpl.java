package yuren.work.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuren.work.boot.mapper.BrowsingRecordMapper;
import yuren.work.boot.pojo.BrowsingRecord;
import yuren.work.boot.service.BrowsingRecordService;

@Service
@Transactional
public class BrowsingRecordServiceImpl implements BrowsingRecordService {
    @Autowired
    BrowsingRecordMapper browsingRecordMapper;
    @Override
    public int insertRecord(BrowsingRecord browsingRecord) {
        return browsingRecordMapper.insert(browsingRecord);
    }
}
