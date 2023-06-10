package ${package.ServiceImpl};

import com.jason.photography.dao.entity.po.${entity};
import com.jason.photography.dao.mapper.${table.mapperName};
import ${package.Service}.${serviceClassName!}${entity}Service;
import com.jason.common.database.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ${serviceClassName!}${entity}ServiceImpl extends BaseServiceImpl<${table.mapperName}, ${entity}> implements ${serviceClassName!}${entity}Service {


}
