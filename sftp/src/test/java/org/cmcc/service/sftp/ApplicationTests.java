package org.cmcc.service.sftp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.cmcc.service.bean.JfscLackFiles;
import org.cmcc.service.sftp.mapper.JfScLackFilesMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class ApplicationTests {
    @Autowired
    private JfScLackFilesMapper jfScLackFilesMapper;

    @Test
    public void test() {
        List<JfscLackFiles> jfScLackFIles = jfScLackFilesMapper.selectList(new QueryWrapper<JfscLackFiles>());
        log.info(jfScLackFIles.toString());
    }

}
